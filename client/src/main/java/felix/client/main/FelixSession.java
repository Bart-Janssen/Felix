package felix.client.main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import felix.client.controller.EventClientSocket;
import felix.client.controller.HeartBeatThread;
import felix.client.models.*;
import felix.client.service.system.AesEncryptionManager;
import felix.client.service.system.RsaEncryptionManager;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FelixSession
{
    private static AesEncryptionManager aesEncryptionManager = null;
    private static FelixSession felixSession = null;
    private static Session session;
    private static HeartBeatThread heartBeatThread;
    private static JwtToken token = null;
    private static UUID pendingUUID = null;

    public static FelixSession getInstance()
    {
        return FelixSession.felixSession == null ? new FelixSession() : FelixSession.felixSession;
    }

    public void initialize(InitWebSocketMessage initWebSocketMessage)
    {
        try
        {
            RsaEncryptionManager.setPublicServerKey(initWebSocketMessage.getServerPublicKey());
            String decryptedAesKey = RsaEncryptionManager.decrypt(initWebSocketMessage.getEncryptedAesKey());
            aesEncryptionManager = new AesEncryptionManager(decryptedAesKey);
            pendingUUID = UUID.fromString(aesEncryptionManager.decrypt(initWebSocketMessage.getEncryptedUuid()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Boolean isInitialized()
    {
        return aesEncryptionManager != null;
    }

    public void setToken(JwtToken token)
    {
        FelixSession.token = token;
    }

    static void connectToServer()
    {
        try
        {
            FelixSession.session = ContainerProvider.getWebSocketContainer().connectToServer(new EventClientSocket(), URI.create("ws://localhost:6666/server/" + RsaEncryptionManager.getPubKey().replace("/", "--dash--")));
            if (FelixSession.session.isOpen())
            {
                heartBeatThread = new HeartBeatThread(session);
                heartBeatThread.start();
            }
        }
        catch (DeploymentException | IOException e)
        {
            e.printStackTrace();
        }
    }

    public void stopHeartBeat()
    {
        this.clearSession();
        heartBeatThread.stop();
    }

    public void sendMessage(String message)
    {
        if (FelixSession.token == null)
        {
            this.disconnect();
            return;
        }
        FelixSession.session.getAsyncRemote().sendText(new Gson().toJson(this.encrypt(new WebSocketMessage(message, FelixSession.token))));
    }

    public Map<String, String> getEncryptedUserInfo(User user)
    {
        try
        {
            Map<String, String> encryptedUserInfo = new HashMap<>();
            encryptedUserInfo.put("name", RsaEncryptionManager.encrypt(user.getName()));
            encryptedUserInfo.put("password", RsaEncryptionManager.encrypt(user.getPassword()));
            encryptedUserInfo.put("uuid", RsaEncryptionManager.encrypt(pendingUUID.toString()));
            return encryptedUserInfo;
//            return RsaEncryptionManager.encrypt(pendingUUID.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /*public String getEncryptedPendingUUID()
    {
        try
        {
            return RsaEncryptionManager.encrypt(pendingUUID.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }*/

    private <T> AesEncryptedMessage encrypt(T t)
    {
        return new AesEncryptedMessage(aesEncryptionManager.encrypt(new Gson().toJson(t)));
    }

    public <T> T decrypt(String encryptedMessage, Type type)
    {
        try
        {
            String decryptedMessage = aesEncryptionManager.decrypt(encryptedMessage);
            return new ObjectMapper().readValue(decryptedMessage, this.getType(type));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.closeSession(CloseReason.CloseCodes.PROTOCOL_ERROR, "Cannot read object.");
            return null;
        }
    }

    public void disconnect()
    {
        this.closeSession(CloseReason.CloseCodes.GOING_AWAY, "Client logged off");
        this.stopHeartBeat();
    }

    private void closeSession(CloseReason.CloseCode closeCode, String reason)
    {
        try
        {
            this.clearSession();
            session.close(new CloseReason(closeCode, reason));
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
        aesEncryptionManager = null;
        felixSession = null;
    }

    private <E> TypeReference<E> getType(Type type)
    {
        return new TypeReference<E>()
        {
            @Override
            public Type getType()
            {
                return type;
            }
        };
    }
}