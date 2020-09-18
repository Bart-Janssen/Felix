package felix.api.models;

import java.util.*;

public class SessionMap
{
    private Map<String, UserSession> userDisplayNameMap = new HashMap<>();
    private Map<String, UserSession> tokenMap = new HashMap<>();

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

    public void addSession(String displayName, UserSession userSession)
    {
        this.userDisplayNameMap.put(displayName, userSession);
        this.tokenMap.put(userSession.getToken().getToken(), userSession);
    }

    public void removeSession(String displayName, String token)
    {
        this.userDisplayNameMap.remove(displayName);
        this.tokenMap.remove(token);
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