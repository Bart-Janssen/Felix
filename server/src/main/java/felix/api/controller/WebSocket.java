package felix.api.controller;

import felix.api.configuration.JwtTokenGenerator;
import felix.api.configuration.RsaEncryptionManager;
import felix.api.models.*;
import javax.websocket.*;
import java.io.IOException;
import java.util.UUID;

public abstract class WebSocket
{
    public static final String KEY = "publickey";

    static SessionMap getSessions()
    {
        return sessions;
    }

    @OnOpen
    public abstract void onWebSocketConnect(Session session) throws IOException;

    @OnMessage
    public abstract void onText(String message, Session session);

    @OnMessage
    public abstract void onPong(PongMessage pong);

    @OnClose
    public abstract void onClose(CloseReason reason, Session session);

    @OnError
    public abstract void onError(Throwable cause, Session session);

    private static SessionMap sessions = new SessionMap();

    public static byte[] getKeyFromSession(String token)
    {
        UserSession userSession = sessions.get(SessionMap.T.TOKEN, token);
        return userSession == null ? null : userSession.getToken().getKey();
    }

    static Boolean addSession(User user, JwtToken token) throws Exception
    {
        UUID pendingUUID = UUID.fromString(RsaEncryptionManager.decrypt(user.getEncryptedUUID())); //todo AES decrypt
        PendingSession pendingSession = sessions.getPending(pendingUUID);
        if (pendingSession == null) return false;
        sessions.addSession(user.getDisplayName(), new UserSession(user, pendingSession.getSession(), token, pendingSession.getClientPublicKey()));
        sessions.removePendingSession(pendingUUID);
        return true;
    }

    protected void removeSession(String sessionId)
    {
        sessions.removeSession(sessionId);
    }

    private Boolean userHasToken(String token)
    {
        User decodedUser = new JwtTokenGenerator().decodeJWT(token);
        if (decodedUser == null) return false;
        UserSession sessionUser = sessions.get(SessionMap.T.DISPLAY_NAME, decodedUser.getDisplayName());
        if (sessionUser == null) return false;
        JwtToken sessionToken = sessionUser.getToken();
        if (sessionToken == null) return false;
        if (!sessionToken.getToken().equals(token)) return false;
        return (decodedUser.getId().equals(sessionUser.getUser().getId())) & (decodedUser.getDisplayName().equals(sessionUser.getUser().getDisplayName()))
                & (decodedUser.getName().equals(sessionUser.getUser().getName())) & (decodedUser.isTwoFAEnabled() == sessionUser.getUser().isTwoFAEnabled());
    }

    protected Boolean validateToken(String token)
    {
        return this.userHasToken(token);
    }

    protected UUID putPendingSession(Session session, String clientPublicKey)
    {
        return sessions.putPendingSession(session, clientPublicKey);
    }
}