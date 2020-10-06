package felix.api.controller;

import felix.api.configuration.JwtTokenGenerator;
import felix.api.models.*;
import felix.api.service.EncryptionManager;
import javax.websocket.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

public abstract class WebSocket extends EncryptionManager
{
    public static final String KEY = "publickey";

    static SessionMap getSessions()
    {
        return sessions;
    }

    @OnOpen
    public abstract void onWebSocketConnect(Session session) throws IOException;

    @OnMessage
    public abstract void onText(String message, Session session) throws GeneralSecurityException;

    @OnMessage
    public abstract void onPong(PongMessage pong);

    @OnClose
    public abstract void onClose(CloseReason reason, Session session);

    @OnError
    public abstract void onError(Throwable cause, Session session);

    private static SessionMap sessions = new SessionMap();

    public static byte[] getKeyFromSession(String token)
    {
        UserSession userSession = sessions.get(GetterType.TOKEN, token);
        return userSession == null ? null : userSession.getToken().getKey();
    }

    static Boolean addSession(User user, JwtToken token)
    {
        UUID pendingUUID = UUID.fromString(user.getEncryptedUUID());
        PendingSession pendingSession = sessions.getPending(pendingUUID);
        if (pendingSession == null) return false;
        sessions.addSession(user.getDisplayName(), new UserSession(token, user, pendingSession.getSession(), pendingSession.getAesKey()));
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
        UserSession sessionUser = sessions.get(GetterType.DISPLAY_NAME, decodedUser.getDisplayName());
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

    protected UUID putPendingSession(Session session, String clientPublicKey, String aesKey)
    {
        return sessions.putPendingSession(session, clientPublicKey, aesKey);
    }

    public static UserSession getSession(GetterType type, String key)
    {
        return sessions.get(type, key);
    }

    public static UserSession updateJwtToken(GetterType type, String key)
    {
        return sessions.updateJwtToken(type, key);
    }
}