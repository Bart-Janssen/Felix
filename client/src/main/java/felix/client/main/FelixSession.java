package felix.client.main;

import com.google.gson.Gson;
import felix.client.controller.EventClientSocket;
import felix.client.controller.HeartBeatThread;
import felix.client.models.*;
import felix.client.service.EncryptionManager;
import felix.client.service.system.RsaEncryptionManager;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class FelixSession extends EncryptionManager
{
    private static FelixSession felixSession = null;
    private static Session session = null;
    private static HeartBeatThread heartBeatThread = null;
    private static JwtToken token = null;
    private static String sessionId = null;

    public static FelixSession getInstance()
    {
        return felixSession == null ? new FelixSession() : felixSession;
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
        catch (GeneralSecurityException e)
        {
            e.printStackTrace();
            this.disconnect();
        }
    }

    public Boolean isInitialized()
    {
        return super.isInitialized();
    }

    public void setToken(JwtToken token)
    {
        FelixSession.token = token;
    }

    static void connectToServer()
    {
        try
        {
            session = ContainerProvider.getWebSocketContainer().connectToServer(new EventClientSocket(), URI.create("ws://localhost:6666/server/" + RsaEncryptionManager.getPubKey().replace("/", "--dash--")));
            if (session.isOpen())
            {
                heartBeatThread = new HeartBeatThread(session);
                heartBeatThread.start();
            }
        }
        catch (DeploymentException | IOException e)
        {
            e.printStackTrace();
            FelixSession.getInstance().disconnect();
        }
    }

    public void stopHeartBeat()
    {
        heartBeatThread.stop();
    }

    public void sendMessage(String message)
    {
        if (token == null)
        {
            this.disconnect();
            return;
        }
        try
        {
            session.getAsyncRemote().sendText(new Gson().toJson(super.aesEncrypt(new WebSocketMessage(message, token))));
        }
        catch (GeneralSecurityException e)
        {
            e.printStackTrace();
            this.disconnect();
        }
    }

    public Map<String, String> getEncryptedUserInfo(User user)
    {
        try
        {
            Map<String, String> encryptedUserInfo = new HashMap<>();
            encryptedUserInfo.put("name", RsaEncryptionManager.encrypt(user.getName()));
            if (user.getDisplayName() != null) encryptedUserInfo.put("disp", RsaEncryptionManager.encrypt(user.getDisplayName()));
            encryptedUserInfo.put("password", RsaEncryptionManager.encrypt(user.getPassword()));
            encryptedUserInfo.put("sessionId", RsaEncryptionManager.encrypt(sessionId));
            return encryptedUserInfo;
        }
        catch (GeneralSecurityException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void disconnect()
    {
        this.stopHeartBeat();
        this.closeSession(CloseReason.CloseCodes.GOING_AWAY, "Client logged off");
    }

    private void closeSession(CloseReason.CloseCode closeCode, String reason)
    {
        try
        {
            session.close(new CloseReason(closeCode, reason));
            this.clearSession();
        }
        catch (Exception e)
        {
            this.clearSession();
            e.printStackTrace();
        }
    }

    private void clearSession()
    {
        token = null;
        super.kill();
        felixSession = null;
        heartBeatThread = null;
        session = null;
        sessionId = null;
    }

    public String getToken()
    {
        return token == null ? null : token.getToken();
    }
}