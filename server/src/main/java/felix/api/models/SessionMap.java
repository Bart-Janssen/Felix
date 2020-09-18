package felix.api.models;

import javax.websocket.Session;
import java.util.*;

public class SessionMap
{
    private Map<String, UserSession> userDisplayNameMap = new HashMap<>();
    private Map<String, UserSession> tokenMap = new HashMap<>();
    private Map<String, UserSession> sessionIdMap = new HashMap<>();

    public UserSession get(T t, String key)
    {
        switch (t)
        {
            case DISPLAY_NAME: return this.userDisplayNameMap.get(key);
            case TOKEN: return this.tokenMap.get(key);
            default: return null;
        }
    }

    public UserSession put(T t, String key, UserSession value)
    {
        switch (t)
        {
            case DISPLAY_NAME: return this.userDisplayNameMap.put(key, value);
            case TOKEN: return this.tokenMap.put(key, value);
            default: return null;
        }
    }

    public Boolean updateSession(User user, Session session, String token)
    {
        UserSession userSession = this.userDisplayNameMap.get(user.getDisplayName());
        UserSession tokenSession = this.tokenMap.get(token);
        if (!tokenSession.getUser().getDisplayName().equals(userSession.getUser().getDisplayName())) return false;
        if (!tokenSession.getToken().getToken().equals(userSession.getToken().getToken())) return false;
        userSession.setSession(session);
        tokenSession.setSession(session);
        this.sessionIdMap.put(session.getId(), userSession);
        this.userDisplayNameMap.put(userSession.getUser().getDisplayName(), userSession);
        this.tokenMap.put(tokenSession.getToken().getToken(), userSession);
        return true;
    }

    public void addSession(String displayName, UserSession userSession)
    {
        this.userDisplayNameMap.put(displayName, userSession);
        this.tokenMap.put(userSession.getToken().getToken(), userSession);
    }

    public void removeSession(String sessionId)
    {
        UserSession userSession = sessionIdMap.get(sessionId);
        this.userDisplayNameMap.remove(userSession.getUser().getDisplayName());
        this.tokenMap.remove(userSession.getToken().getToken());
        this.sessionIdMap.remove(sessionId);
    }

    public Collection<UserSession> values(T t)
    {
        switch (t)
        {
            case DISPLAY_NAME: return this.userDisplayNameMap.values();
            case TOKEN: return this.tokenMap.values();
            default: return new ArrayList<>();
        }
    }

    public enum T
    {
        DISPLAY_NAME,
        TOKEN
    }
}