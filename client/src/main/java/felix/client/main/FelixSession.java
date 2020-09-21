package felix.client.main;

import com.google.gson.Gson;
import felix.client.controller.EventClientSocket;
import felix.client.controller.HeartBeatThread;
import felix.client.models.JwtToken;
import felix.client.models.WebSocketMessage;
import felix.client.service.system.RsaEncryptionManager;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

public class FelixSession
{
    private static FelixSession felixSession = null;
    private static Session session;
    private static HeartBeatThread heartBeatThread;
    private static JwtToken token = null;
    private static UUID pendingUUID = null;

    public static FelixSession getInstance()
    {
        return FelixSession.felixSession == null ? new FelixSession() : FelixSession.felixSession;
    }

    public void setToken(JwtToken token)
    {
        FelixSession.token = token;
    }

    public void setPendingUUID(UUID pendingUUID)
    {
        FelixSession.pendingUUID = pendingUUID;
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

    public void disconnect()
    {
        try
        {
            FelixSession.session.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "Client logged off"));
            this.stopHeartBeat();
        }
        catch (Exception ignored) {}
    }

    public void stopHeartBeat()
    {
        heartBeatThread.stop();
    }

    public void sendMessage(String message)
    {
        if (FelixSession.token == null)
        {
            this.disconnect();
            return;
        }
        FelixSession.session.getAsyncRemote().sendText(new Gson().toJson(new WebSocketMessage(message, FelixSession.token))); //todo aes encrypt
    }

    public UUID getPendingUUID()
    {
        return pendingUUID;
    }
}