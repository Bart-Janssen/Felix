package felix.api.controller;

import felix.api.configuration.JwtTokenGenerator;
import felix.api.models.JwtToken;
import felix.api.models.User;
import felix.api.models.UserSession;
import javax.websocket.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class WebSocket
{
    public static final String TOKEN = "token";

    public static Map<String, UserSession> getSessions()
    {
        return sessions;
    }

    @OnOpen
    public abstract void onWebSocketConnect(Session session) throws IOException;

    @OnMessage
    public abstract void onText(String message, Session session);

    @OnClose
    public abstract void onClose(CloseReason reason, Session session);

    @OnError
    public abstract void onError(Throwable cause, Session session);

    private static Map<String, UserSession> sessions = new HashMap<>();

    protected Boolean setSession(Session session, String token)
    {
        User user = new JwtTokenGenerator().decodeJWT(token);
        UserSession userSession = sessions.get(user.getDisplayName());
        System.out.println(userSession.getSession() == null);
        if (userSession.getSession() != null) return false;
        userSession.setSession(session);
        sessions.put(user.getDisplayName(), userSession);
        return true;
    }

    static Boolean addSession(User user, JwtToken token)
    {
        if (sessions.get(user.getDisplayName()) != null) return false;
        WebSocket.sessions.put(user.getDisplayName(), new UserSession(user, token));
        return true;
    }

    protected void removeSession(String token)
    {
        User user = new JwtTokenGenerator().decodeJWT(token);
        sessions.remove(user.getDisplayName());
    }

    private Boolean userHasToken(String token)
    {
        User decodedUser = new JwtTokenGenerator().decodeJWT(token);
        if (decodedUser == null) return false;
        UserSession sessionUser = sessions.get(decodedUser.getDisplayName());
        if (sessionUser == null) return false;
        JwtToken sessionToken = sessionUser.getToken();
        if (sessionToken == null) return false;
        if (!sessionToken.getToken().equals(token)) return false;
        return (decodedUser.getId().equals(sessionUser.getUser().getId())) & (decodedUser.getDisplayName().equals(sessionUser.getUser().getDisplayName()))
                & (decodedUser.getName().equals(sessionUser.getUser().getName())) & (decodedUser.isTwoFAEnabled() == sessionUser.getUser().isTwoFAEnabled());
    }

    protected String parseToken(Map<String, String> parameters)
    {
        String token = parameters.get(TOKEN);
        return ((token == null) | (!this.userHasToken(token))) ? null : token;
    }
}