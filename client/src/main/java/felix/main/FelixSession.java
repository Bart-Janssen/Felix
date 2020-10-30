package felix.main;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;
import felix.controllers.EventClientSocket;
import felix.controllers.HeartBeatThread;
import felix.fxml.messageBox.CustomOkMessage;
import felix.fxml.messageBox.FXML_MessageBoxStatus;
import felix.models.WebSocketMessage;
import felix.service.EncryptionManager;
import felix.service.system.RsaEncryptionManager;
import felix.models.InitWebSocketMessage;
import felix.models.JwtToken;
import felix.models.User;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

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
        return "127.0.0.1";
    }

    public Boolean isInitialized()
    {
        return super.isInitialized();
    }

    public void setToken(JwtToken token)
    {
        FelixSession.token = token;
    }

    static void connectToServer(Stage stage)
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
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Error while connecting.").show();
        }
    }

    public void stopHeartBeat()
    {
        heartBeatThread.stop();
    }

    public void sendMessage(String message, String to) throws GeneralSecurityException
    {
        if (token == null)
        {
            this.disconnect();
            return;
        }
        try
        {
            websocket.sendText(new Gson().toJson(super.aesEncrypt(new WebSocketMessage(message, to, token))));
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
}