package felix.api.controller;

import felix.api.configuration.JwtTokenGenerator;
import felix.api.models.JwtToken;
import felix.api.models.SessionMap;
import felix.api.models.User;
import felix.api.models.UserSession;
import javax.websocket.*;
import java.io.IOException;
import java.util.Map;

public abstract class WebSocket
{
    public static final String TOKEN = "token";

    public static SessionMap getSessions()
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

    protected Boolean setSession(Session session, String token)
    {
        User user = new JwtTokenGenerator().decodeJWT(token);
        UserSession userSession = sessions.get(SessionMap.T.DISPLAY_NAME, user.getDisplayName());
        if (userSession.getSession() != null) return false;
        userSession.setSession(session);
        sessions.put(SessionMap.T.DISPLAY_NAME, user.getDisplayName(), userSession);
        return true;
    }

    public static byte[] getKeyFromSession(String token)
    {
        UserSession userSession = sessions.get(SessionMap.T.TOKEN, token);
        return userSession == null ? null : userSession.getToken().getKey();
    }

    static Boolean addSession(User user, JwtToken token)
    {
        if (sessions.get(SessionMap.T.DISPLAY_NAME, user.getDisplayName()) != null) return false;
        WebSocket.sessions.addSession(user.getDisplayName(), new UserSession(user, token));
        return true;
    }

    protected void removeSession(String token)
    {
        User user = new JwtTokenGenerator().decodeJWT(token);
        sessions.removeSession(user.getDisplayName(), token);
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

    protected String parseToken(Map<String, String> parameters)
    {
        return parameters.get(TOKEN);
    }

    protected Boolean validateToken(String token)
    {
        return this.userHasToken(token);
    }
}