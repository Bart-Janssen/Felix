package felix.api.models;

import javax.websocket.Session;
import java.util.*;

public class SessionMap
{
    private Map<String, UserSession> userDisplayNameMap = new HashMap<>();
    private Map<String, String> tokenMap = new HashMap<>();
    private Map<String, String> sessionIdMap = new HashMap<>();
    private Map<UUID, PendingSession> pendingSessions = new HashMap<>();

    public UserSession get(GetterType type, String key)
    {
        switch (type)
        {
            case DISPLAY_NAME: return this.userDisplayNameMap.get(key);
            case TOKEN: return this.userDisplayNameMap.get(this.tokenMap.get(key));
            case SESSION_ID: return this.userDisplayNameMap.get(this.sessionIdMap.get(key));
            default: return null;
        }
    }

    public PendingSession getPending(UUID uuid)
    {
        return this.pendingSessions.get(uuid);
    }

    public void addSession(String displayName, UserSession userSession)
    {
        this.userDisplayNameMap.put(displayName, userSession);
        this.tokenMap.put(userSession.getToken().getToken(), displayName);
        this.sessionIdMap.put(userSession.getSession().getId(), displayName);
    }

    public void removeSession(String sessionId)
    {
        UserSession userSession = this.userDisplayNameMap.get(this.sessionIdMap.get(sessionId));
        this.userDisplayNameMap.remove(userSession.getUser().getDisplayName());
        this.tokenMap.remove(userSession.getToken().getToken());
        this.sessionIdMap.remove(sessionId);
    }

    public Collection<UserSession> values()
    {
        return this.userDisplayNameMap.values();
    }

    public UUID putPendingSession(Session session, String clientPublicKey, String aesKey)
    {
        UUID uniqueUUID = this.getUniqueUUID();
        this.pendingSessions.put(uniqueUUID, new PendingSession(clientPublicKey, session, aesKey));
        return uniqueUUID;
    }

    private UUID getUniqueUUID()
    {
        UUID uuid = UUID.randomUUID();
        if (this.pendingSessions.get(uuid) == null) return uuid;
        return this.getUniqueUUID();
    }

    public void removePendingSession(UUID pendingUUID)
    {
        this.pendingSessions.remove(pendingUUID);
    }
}