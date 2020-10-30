package felix.api.configuration;

import com.google.gson.Gson;
import felix.api.controller.WebSocket;
import felix.api.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.security.GeneralSecurityException;

@Slf4j
@Service
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
        catch (Exception e)
        {
            this.closeSession(session, CloseReason.CloseCodes.TLS_HANDSHAKE_FAILURE, " False handshake.");
        }
    }

    @Override
    public void onText(String message, Session session)
    {
        try
        {
            WebSocketMessage webSocketMessage = super.aesDecrypt(GetterType.SESSION_ID, session.getId(), new Gson().fromJson(message, AesEncryptedMessage.class).getMessage(), WebSocketMessage.class);
            if (!super.validateToken(webSocketMessage.getJwtToken().getToken()))
            {
                this.closeSession(session, CloseReason.CloseCodes.CANNOT_ACCEPT, " JWT token invalid!");
                return;
            }
            if (webSocketMessage.getMessage().length() > 255) return;
            if (!webSocketMessage.isGroup()) this.handlePrivateChat(webSocketMessage);
            else this.handleGroupChat(webSocketMessage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.closeSession(session, CloseReason.CloseCodes.CLOSED_ABNORMALLY, " Internal error.");
        }
    }

    private void handleGroupChat(WebSocketMessage webSocketMessage) throws GeneralSecurityException
    {
        System.out.println("to: " + webSocketMessage.getTo());
        System.out.println("msg: " + webSocketMessage.getMessage());

        //todo
    }

    private void handlePrivateChat(WebSocketMessage webSocketMessage) throws GeneralSecurityException
    {
        UserSession from = WebSocket.getSession(GetterType.TOKEN, webSocketMessage.getJwtToken().getToken());
        UserSession friendTo = WebSocket.getSession(GetterType.DISPLAY_NAME, webSocketMessage.getTo());
        if (friendTo == null)
        {
            WebSocket.saveOfflineChat(from, webSocketMessage.getTo(), webSocketMessage.getMessage());
            return;
        }
        super.sendMessage(friendTo.getSession(), webSocketMessage.getMessage(), from, friendTo, false);
    }

    private void closeSession(Session session, CloseReason.CloseCode closeCode, String reason)
    {
        try
        {
            super.killSession(session.getId());
            session.close(new CloseReason(closeCode, reason));
        }
        catch (Exception ignored) {}
    }

    @Override
    public void onClose(CloseReason reason, Session session)
    {
        log.info("[Session ID] : " + session.getId() + " [Socket Closed]: " + reason);
        if (!super.checkRemovePendingSession(session.getId())) super.killSession(session.getId());
    }

    @Override
    public void onError(Throwable cause, Session session)
    {
        log.error("[ERROR] : [Session ID] : " + session.getId());
        this.closeSession(session, CloseReason.CloseCodes.TRY_AGAIN_LATER, " An unexpected error has occurred.");
    }

    @Override
    public void onPong(PongMessage pong) {}
}