package felix.api.controller;

import felix.api.configuration.JwtTokenGenerator;
import felix.api.models.JwtToken;
import felix.api.models.User;
import javax.websocket.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class WebSocket
{
    public static final String TOKEN = "token";

    @OnOpen
    public abstract void onWebSocketConnect(Session session) throws IOException;

    @OnMessage
    public abstract void onText(String message, Session session);

    @OnClose
    public abstract void onClose(CloseReason reason, Session session);

    @OnError
    public abstract void onError(Throwable cause, Session session);

    private static Map<String, User> sessions = new HashMap<>();

    static void addSession(User user, JwtToken token)
    {
        WebSocket.sessions.put(token.getToken(), user);
    }

    protected void removeSession(String token)
    {
        sessions.remove(token);
    }

    private Boolean userHasSession(String token)
    {
        User decodedUser = new JwtTokenGenerator().decodeJWT(token);
        User sessionUser = sessions.get(token);
        if ((decodedUser == null) || (sessionUser == null)) return false;
        return (decodedUser.getId().equals(sessionUser.getId())) & (decodedUser.getDisplayName().equals(sessionUser.getDisplayName()))
                & (decodedUser.getName().equals(sessionUser.getName())) & (decodedUser.isTwoFAEnabled() == sessionUser.isTwoFAEnabled());
    }

    protected String parseToken(Map<String, String> parameters)
    {
        String token = parameters.get(TOKEN);
        return ((token == null) & (!this.userHasSession(token))) ? null : token;
    }
}