package felix.api.models;

import felix.api.configuration.JwtTokenGenerator;
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

    public void removeSession(GetterType type, String key)
    {
        UserSession session = this.get(type, key);


       // UserSession userSession = this.userDisplayNameMap.get(this.sessionIdMap.get(sessionId));
        this.userDisplayNameMap.remove(session.getUser().getDisplayName());
        this.tokenMap.remove(session.getToken().getToken());
        this.sessionIdMap.remove(session.getSession().getId());
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

    public UserSession updateJwtToken(GetterType type, String key)
    {
        UserSession session = this.get(type, key);
        this.tokenMap.remove(session.getToken().getToken());
        session.setToken(new JwtTokenGenerator().createJWT(session.getUser()));
        this.tokenMap.put(session.getToken().getToken(), session.getUser().getDisplayName());
        return this.userDisplayNameMap.put(session.getUser().getDisplayName(), session);
    }
}