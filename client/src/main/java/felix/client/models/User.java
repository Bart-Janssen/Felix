package felix.client.models;

import java.util.UUID;

public class User
{
    private UUID id;
    private String name;
    private String password;
    private String encryptedUUID;
    private int coins;
    private int level;
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

    public void setEncryptedUUID(String encryptedUUID)
    {
        this.encryptedUUID = encryptedUUID;
    }

    public String getEncryptedUUID()
    {
        return encryptedUUID;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }
}