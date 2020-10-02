package felix.api.configuration;

import com.google.gson.Gson;
import felix.api.controller.WebSocket;
import felix.api.models.AesEncryptedMessage;
import felix.api.models.GetterType;
import felix.api.models.InitWebSocketMessage;
import felix.api.models.WebSocketMessage;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.security.GeneralSecurityException;
import java.util.UUID;

@ServerEndpoint(value = "/server/{" + WebSocket.KEY + "}")
public class WebSocketConnection extends WebSocket
{
    @Override
    public void onWebSocketConnect(Session session)
    {
        try
        {
            String clientPublicKey = session.getPathParameters().get(WebSocket.KEY).replace("--dash--", "/");
            String aesKey = AesEncryptionManager.generateKey();
            UUID pendingUUID = super.putPendingSession(session, clientPublicKey, aesKey);
            String encryptedUUID = AesEncryptionManager.encrypt(aesKey, pendingUUID.toString());
            String encryptedAesKey = RsaEncryptionManager.encrypt(clientPublicKey, aesKey);
            session.getAsyncRemote().sendText(new Gson().toJson(new InitWebSocketMessage(RsaEncryptionManager.getPubKey(), encryptedAesKey, encryptedUUID)));
        }
        catch (GeneralSecurityException e)
        {
            e.printStackTrace();
            this.closeSession(session, CloseReason.CloseCodes.TLS_HANDSHAKE_FAILURE, " False handshake.");
            return;
        }
        System.out.println("[Connected] SessionID: " + session.getId());
    }

    @Override
    public void onText(String message, Session session) throws GeneralSecurityException
    {
        WebSocketMessage webSocketMessage = super.decrypt(GetterType.SESSION_ID, session.getId(), new Gson().fromJson(message, AesEncryptedMessage.class).getMessage(), WebSocketMessage.class);
        if (!super.validateToken(webSocketMessage.getJwtToken().getToken()))
        {
            this.closeSession(session, CloseReason.CloseCodes.CANNOT_ACCEPT, " JWT token invalid!");
            return;
        }
        System.out.println("AES Encrypted msg: " + message);
        System.out.println("AES Decrypted msg: " + webSocketMessage.getMessage());
        System.out.println("AES Decrypted jwt: " + webSocketMessage.getJwtToken().getToken());
        this.sendMessage(session, "Hey from server!");
    }

    private void sendMessage(Session session, String message) throws GeneralSecurityException
    {
        session.getAsyncRemote().sendText(new Gson().toJson(WebSocket.encrypt(GetterType.SESSION_ID, session.getId(), new WebSocketMessage(message, null))));
    }

    private void closeSession(Session session, CloseReason.CloseCode closeCode, String reason)
    {
        try
        {
            super.removeSession(session.getId());
            session.close(new CloseReason(closeCode, reason));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(CloseReason reason, Session session)
    {
        System.out.println("[Session ID] : " + session.getId() + " [Socket Closed]: " + reason);
        super.removeSession(session.getId());
    }

    @Override
    public void onError(Throwable cause, Session session)
    {
        System.out.println("[Session ID] : " + session.getId() + "[ERROR]: ");
        this.closeSession(session, CloseReason.CloseCodes.TRY_AGAIN_LATER, " An unexpected error has occurred.");
        cause.printStackTrace(System.err);
    }

    @Override
    public void onPong(PongMessage pong) {System.out.println("Heartbeat!");}
}