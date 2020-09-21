package felix.api.models;

import javax.websocket.Session;
import java.util.*;

public class SessionMap
{
    private Map<String, UserSession> userDisplayNameMap = new HashMap<>();
    private Map<String, String> tokenMap = new HashMap<>();
    private Map<String, String> sessionIdMap = new HashMap<>();

    public UserSession get(T t, String key)
    {
        switch (t)
        {
            case DISPLAY_NAME: return this.userDisplayNameMap.get(key);
            case TOKEN: return this.userDisplayNameMap.get(this.tokenMap.get(key));
            default: return null;
        }
    }

    public Boolean updateSession(User user, Session session, String token)
    {
        UserSession userSession = this.userDisplayNameMap.get(user.getDisplayName());
        UserSession tokenSession = this.userDisplayNameMap.get(this.tokenMap.get(token));
        if (!tokenSession.getUser().getDisplayName().equals(userSession.getUser().getDisplayName())) return false;
        if (!tokenSession.getToken().getToken().equals(userSession.getToken().getToken())) return false;
        userSession.setSession(session);
        this.userDisplayNameMap.put(userSession.getUser().getDisplayName(), userSession);
        this.sessionIdMap.put(session.getId(), userSession.getToken().getToken());
        return true;
    }

    public void addSession(String displayName, UserSession userSession)
    {
        this.userDisplayNameMap.put(displayName, userSession);
        this.tokenMap.put(userSession.getToken().getToken(), displayName);
    }

    public void removeSession(String sessionId)
    {
        UserSession userSession = this.userDisplayNameMap.get(this.tokenMap.get(this.sessionIdMap.get(sessionId)));
        this.userDisplayNameMap.remove(userSession.getUser().getDisplayName());
        this.tokenMap.remove(userSession.getToken().getToken());
        this.sessionIdMap.remove(sessionId);
    }

    public Collection<UserSession> values()
    {
        return this.userDisplayNameMap.values();
    }

    public enum T
    {
        DISPLAY_NAME,
        TOKEN
    }
}