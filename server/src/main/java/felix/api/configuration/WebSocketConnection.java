package felix.api.configuration;

import com.google.gson.Gson;
import felix.api.controller.WebSocket;
import felix.api.models.WebSocketMessage;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@ServerEndpoint(value = "/server/{" + WebSocket.KEY + "}")
public class WebSocketConnection extends WebSocket
{
    @Override
    public void onWebSocketConnect(Session session) throws IOException
    {
        /*
        String token = super.parseToken(session.getPathParameters());
        if (token == null || !super.validateToken(token))
        {
            session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, " Token is not valid"));
            return;
        }
        if (!super.setSession(session, token))
        {
            super.removeSession(session.getId());
            session.close(new CloseReason(CloseReason.CloseCodes.TRY_AGAIN_LATER, " Session is already logged in"));
            return;
        }*/

        String clientPublicKey = session.getPathParameters().get(WebSocket.KEY).replace("--dash--", "/");
        UUID pendingUUID = super.putPendingSession(session, clientPublicKey);
        session.getAsyncRemote().sendText(RsaEncryptionManager.getPubKey());
        try
        {
            String chipper = RsaEncryptionManager.encrypt(this.stringToKey(clientPublicKey), "UUID:" + pendingUUID.toString());
            session.getAsyncRemote().sendText(chipper);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        System.out.println("[Connected] SessionID: " + session.getId());
    }

    private PublicKey stringToKey(String serverPublicKey)
    {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(serverPublicKey));
        KeyFactory keyFactory;
        try
        {
            keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(x509EncodedKeySpec);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onPong(PongMessage pong) {System.out.println("Heartbeat!");}

    @Override
    public void onText(String message, Session session)
    {
        System.out.println("Validate on text: " + super.validateToken(new Gson().fromJson(message, WebSocketMessage.class).getJwtToken().getToken()));
        System.out.println("[on msg odin!] " + new Gson().fromJson(message, WebSocketMessage.class));
        session.getAsyncRemote().sendText("Yay response from server");
    }

    @Override
    public void onClose(CloseReason reason, Session session)
    {
        super.removeSession(session.getId());
        System.out.println("[Session ID] : " + session.getId() + " [Socket Closed]: " + reason);
    }

    @Override
    public void onError(Throwable cause, Session session)
    {
        System.out.println("[Session ID] : " + session.getId() + "[ERROR]: ");
        cause.printStackTrace(System.err);
    }
}