package felix.api.controller;

import com.google.gson.Gson;
import felix.api.configuration.JwtTokenGenerator;
import felix.api.models.*;
import felix.api.service.EncryptionManager;
import javax.websocket.*;
import java.io.IOException;
import java.security.GeneralSecurityException;

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
        String sessionId = user.getSessionId();
        PendingSession pendingSession = sessions.getPending(user.getSessionId());
        if (pendingSession == null) return false;
        sessions.addSession(user.getDisplayName(), new UserSession(token, user, pendingSession.getSession(), pendingSession.getClientPublicKey(), pendingSession.getAesKey()));
        sessions.removePendingSession(sessionId);
        return true;
    }

    static void logout(String jwtToken)
    {
        sessions.logout(jwtToken);
    }

    protected void killSession(String sessionId)
    {
        sessions.killSession(sessionId);
    }

    protected void sendMessage(Session session, String message, String from, String to) throws GeneralSecurityException
    {
        session.getAsyncRemote().sendText(new Gson().toJson(aesEncrypt(GetterType.SESSION_ID, session.getId(), new WebSocketMessage(WebSocketMessageType.MESSAGE, message, from, to, null))));
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

    protected void putPendingSession(Session session, String clientPublicKey, String aesKey)
    {
        sessions.putPendingSession(session, clientPublicKey, aesKey);
    }

    public static UserSession getSession(GetterType type, String key)
    {
        return sessions.get(type, key);
    }

    public static UserSession updateJwtToken(GetterType type, String key)
    {
        return sessions.updateJwtToken(type, key);
    }

    static void set2Fa(GetterType type, String key, boolean enable)
    {
        sessions.set2Fa(type, key, enable);
    }

    protected boolean checkRemovePendingSession(String sessionId)
    {
        return sessions.checkRemovePendingSession(sessionId);
    }
}