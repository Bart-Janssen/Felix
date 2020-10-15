package felix.api.configuration;

import com.google.gson.Gson;
import felix.api.controller.WebSocket;
import felix.api.models.*;
import felix.api.service.user.UserService;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.security.GeneralSecurityException;

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
            super.putPendingSession(session, clientPublicKey, aesKey);
            String encryptedSessionId = AesEncryptionManager.encrypt(aesKey, session.getId());
            String encryptedAesKey = RsaEncryptionManager.encrypt(clientPublicKey, aesKey);
            session.getAsyncRemote().sendText(new Gson().toJson(new InitWebSocketMessage(RsaEncryptionManager.getPubKey(), encryptedAesKey, encryptedSessionId)));
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
        WebSocketMessage webSocketMessage = super.aesDecrypt(GetterType.SESSION_ID, session.getId(), new Gson().fromJson(message, AesEncryptedMessage.class).getMessage(), WebSocketMessage.class);
        if (!super.validateToken(webSocketMessage.getJwtToken().getToken()))
        {
            this.closeSession(session, CloseReason.CloseCodes.CANNOT_ACCEPT, " JWT token invalid!");
            return;
        }
//        System.out.println("AES Encrypted msg: " + message);
//        System.out.println("AES Decrypted msg: " + webSocketMessage.getMessage());
//        System.out.println("AES Decrypted jwt: " + webSocketMessage.getJwtToken().getToken());
        UserSession from = WebSocket.getSession(GetterType.TOKEN, webSocketMessage.getJwtToken().getToken());
        UserSession friendTo = WebSocket.getSession(GetterType.DISPLAY_NAME, webSocketMessage.getTo());
        if (friendTo == null)
        {
            System.out.println("Friend is not online lol");
            return;
        }
        this.sendMessage(friendTo.getSession(), webSocketMessage.getMessage(), from.getUser().getDisplayName(), friendTo.getUser().getDisplayName());






//        this.sendMessage(session, "Hey from server!", "");
    }

    private void sendMessage(Session session, String message, String from, String to) throws GeneralSecurityException
    {
        session.getAsyncRemote().sendText(new Gson().toJson(super.aesEncrypt(GetterType.SESSION_ID, session.getId(), new WebSocketMessage(message, from, to, null))));
    }

    private void closeSession(Session session, CloseReason.CloseCode closeCode, String reason)
    {
        try
        {
            super.killSession(session.getId());
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

        if (!super.checkRemovePendingSession(session.getId())) super.killSession(session.getId());
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