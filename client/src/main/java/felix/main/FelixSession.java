package felix.main;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;
import felix.controllers.EventClientSocket;
import felix.controllers.HeartBeatThread;
import felix.fxml.messageBox.CustomOkMessage;
import felix.models.*;
import felix.service.EncryptionManager;
import felix.service.system.RsaEncryptionManager;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.*;

public class FelixSession extends EncryptionManager
{
    private static FelixSession instance = null;
    private static WebSocket websocket;
    private static HeartBeatThread heartBeatThread = null;
    private static JwtToken token = null;
    private static String sessionId = null;

    public static FelixSession getInstance()
    {
        return instance == null ? new FelixSession() : instance;
    }

    boolean isConnected()
    {
        return websocket != null && websocket.isOpen();
    }

    public void initialize(InitWebSocketMessage initWebSocketMessage)
    {
        try
        {
            RsaEncryptionManager.setPublicServerKey(initWebSocketMessage.getServerPublicKey());
            String decryptedAesKey = RsaEncryptionManager.decrypt(initWebSocketMessage.getEncryptedAesKey());
            super.init(decryptedAesKey);
            sessionId = super.aesDecrypt(initWebSocketMessage.getEncryptedSessionId());
        }
        catch (GeneralSecurityException | NullPointerException e)
        {
            this.disconnect();
            new CustomOkMessage(null, "Encryption failure, disconnecting...").show();
        }
    }

    public static String getIp()
    {
        return "10.10.2.125";  //10.10.2.125
    }

    public Boolean isInitialized()
    {
        return super.isInitialized();
    }

    public void setToken(JwtToken token)
    {
        FelixSession.token = token;
    }

    static boolean connectToServer()
    {
        try
        {
            heartBeatThread = new HeartBeatThread();
            websocket = new WebSocketFactory()
                    .createSocket(URI.create("ws://" + getIp() + ":6666/server/" + RsaEncryptionManager.getPubKey().replace("/", "--dash--")))
                    .addListener(new EventClientSocket())
                    .connect();
            if (websocket.isOpen())
            {
                heartBeatThread.setWebSocket(websocket);
                heartBeatThread.start();
            }
            return websocket.isOpen();
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void stopHeartBeat()
    {
        heartBeatThread.stop();
    }

    public void sendMessage(String message, boolean isGroup, String to) throws GeneralSecurityException
    {
        if (token == null)
        {
            this.disconnect();
            return;
        }
        try
        {
            websocket.sendText(new Gson().toJson(super.aesEncrypt(new WebSocketMessage(message, to, isGroup, token))));
        }
        catch (GeneralSecurityException e)
        {
            this.disconnect();
            throw e;
        }
    }

    public Map<String, String> getEncryptedUserInfo(User user) throws GeneralSecurityException, NullPointerException
    {
        Map<String, String> encryptedUserInfo = new HashMap<>();
        encryptedUserInfo.put("name", RsaEncryptionManager.encrypt(user.getName()));
        if (user.getDisplayName() != null) encryptedUserInfo.put("disp", RsaEncryptionManager.encrypt(user.getDisplayName()));
        encryptedUserInfo.put("password", RsaEncryptionManager.encrypt(user.getPassword()));
        encryptedUserInfo.put("sessionId", RsaEncryptionManager.encrypt(sessionId));
        return encryptedUserInfo;
    }

    public void disconnect()
    {
        this.stopHeartBeat();
        this.closeSession("Client logged off");
    }

    private void closeSession(String reason)
    {
        try
        {
            if (websocket.isOpen()) websocket.sendClose(1001, reason);
            this.clearSession();
        }
        catch (Exception e)
        {
            this.clearSession();
        }
    }

    private void clearSession()
    {
        token = null;
        super.kill();
        instance = null;
        heartBeatThread = null;
        websocket = null;
        sessionId = null;
    }

    public String getToken()
    {
        return token == null ? null : token.getToken();
    }

    public Licence rsaEncryptLicence(Licence licence) throws GeneralSecurityException
    {
        licence.setEncryptedToken(RsaEncryptionManager.encrypt(licence.getToken().toString()));
        List<String> encryptedMacs = new ArrayList<>();
        for (String mac : licence.getMacs())
        {
            encryptedMacs.add(RsaEncryptionManager.encrypt(mac));
        }
        licence.setMacs(encryptedMacs);
        return licence;
    }
}