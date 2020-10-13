package felix.models;

import java.util.UUID;

public class User
{
    private UUID id;
    private String name;
    private String password;
    private String sessionId;
    private String displayName;
    private boolean online;
    private boolean twoFAEnabled;

    public User() {}

    public User(String name, String password)
    {
        this.name = name;
        this.password = password;
    }

    public User(String name, String displayName, String password)
    {
        this.name = name;
        this.displayName = displayName;
        this.password = password;
    }

    public User(UUID id, String name, String displayName, boolean twoFAEnabled)
    {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.twoFAEnabled = twoFAEnabled;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public boolean hasTwoFAEnabled()
    {
        return this.twoFAEnabled;
    }

    public UUID getId()
    {
        return this.id;
    }
}