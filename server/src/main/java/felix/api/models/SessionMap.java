package felix.api.models;

import com.google.gson.Gson;
import felix.api.configuration.JwtTokenGenerator;
import felix.api.service.EncryptionManager;

import javax.websocket.Session;
import java.util.*;

public class SessionMap
{
    private Map<String, UserSession> userDisplayNameMap = new HashMap<>();
    private Map<String, String> tokenMap = new HashMap<>();
    private Map<String, String> sessionIdMap = new HashMap<>();
    private Map<String, PendingSession> pendingSessions = new HashMap<>();
    private Map<UUID, Group> groups = new HashMap<>();

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

    public Group get(UUID groupId)
    {
        return groups.get(groupId);
    }

    public PendingSession getPending(String sessionId)
    {
        return this.pendingSessions.get(sessionId);
    }

    public void addSession(String displayName, UserSession userSession)
    {
        this.userDisplayNameMap.put(displayName, userSession);
        this.tokenMap.put(userSession.getToken().getToken(), displayName);
        this.sessionIdMap.put(userSession.getSession().getId(), displayName);
    }

    public void logout(String jwtToken)
    {
        UserSession session = this.get(GetterType.TOKEN, jwtToken);
        this.putPendingSession(session.getSession(), session.getRsaPublicKey(), session.getAesKey());
        this.killSession(session.getSession().getId());
    }

    public void killSession(String sessionId)
    {
        UserSession session = this.get(GetterType.SESSION_ID, sessionId);
        this.userDisplayNameMap.remove(session.getUser().getDisplayName());
        this.tokenMap.remove(session.getToken().getToken());
        this.sessionIdMap.remove(session.getSession().getId());
    }

    public void putPendingSession(Session session, String clientPublicKey, String aesKey)
    {
        this.pendingSessions.put(session.getId(), new PendingSession(clientPublicKey, session, aesKey));
    }

    public void removePendingSession(String sessionId)
    {
        this.pendingSessions.remove(sessionId);
    }

    public UserSession updateJwtToken(GetterType type, String key)
    {
        UserSession session = this.get(type, key);
        this.tokenMap.remove(session.getToken().getToken());
        session.setToken(new JwtTokenGenerator().createJWT(session.getUser()));
        this.tokenMap.put(session.getToken().getToken(), session.getUser().getDisplayName());
        return this.userDisplayNameMap.put(session.getUser().getDisplayName(), session);
    }

    public void set2Fa(GetterType type, String key, boolean enable)
    {
        this.get(type, key).getUser().setTwoFAEnabled(enable);
    }

    public boolean checkRemovePendingSession(String sessionId)
    {
        if (this.pendingSessions.get(sessionId) == null) return false;
        this.pendingSessions.remove(sessionId);
        return true;
    }

    public List<UserSession> addGroupsIfAbsent(User user)
    {
        List<UserSession> memberSessions = new ArrayList<>();
        for (Group group : user.getMemberGroups())
        {
            if (this.groups.get(group.getId()) == null)
            {
                group.setGroupMembers(new ArrayList<>());
                group.addGroupMember(User.builder().online(user.isOnline()).displayName(user.getDisplayName()).build());
                this.groups.putIfAbsent(group.getId(), group);
            }
            else
            {
                Group alreadyOnlineGroup = this.groups.get(group.getId());
                for (User member : alreadyOnlineGroup.getGroupMembers())
                {
                    UserSession memberSession = this.get(GetterType.DISPLAY_NAME, member.getDisplayName());
                    memberSessions.add(memberSession);
                }
                alreadyOnlineGroup.addGroupMember(User.builder().online(user.isOnline()).displayName(user.getDisplayName()).build());
                this.groups.put(alreadyOnlineGroup.getId(), alreadyOnlineGroup);
            }
        }
        return memberSessions;
    }

    public List<UserSession> logoutGroupAndRemoveGroupFromSessionsIfEmpty(String jwtToken)
    {
        List<UserSession> memberSessions = new ArrayList<>();
        User user = this.get(GetterType.TOKEN, jwtToken).getUser();
        for (Group memberGroup : user.getMemberGroups())
        {
            Group group = groups.get(memberGroup.getId());
            group.removeMember(user);
            for (User member : group.getGroupMembers())
            {
                UserSession memberSession = this.get(GetterType.DISPLAY_NAME, member.getDisplayName());
                memberSessions.add(memberSession);
            }
            if (group.getGroupMembers().size() == 0)
            {
                groups.remove(group.getId());
            }
        }
        return memberSessions;
    }
}