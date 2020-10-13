package felix.models;

public class InitWebSocketMessage
{
    private String serverPublicKey;
    private String encryptedAesKey;
    private String encryptedSessionId;

    public InitWebSocketMessage() {}

    public InitWebSocketMessage(String serverPublicKey, String encryptedAesKey, String encryptedSessionId)
    {
        this.serverPublicKey = serverPublicKey;
        this.encryptedAesKey = encryptedAesKey;
        this.encryptedSessionId = encryptedSessionId;
    }

    public String getServerPublicKey()
    {
        return serverPublicKey;
    }

    public void setServerPublicKey(String serverPublicKey)
    {
        this.serverPublicKey = serverPublicKey;
    }

    public String getEncryptedAesKey()
    {
        return encryptedAesKey;
    }

    public void setEncryptedAesKey(String encryptedAesKey)
    {
        this.encryptedAesKey = encryptedAesKey;
    }

    public String getEncryptedSessionId()
    {
        return encryptedSessionId;
    }

    public void setEncryptedSessionId(String encryptedSessionId)
    {
        this.encryptedSessionId = encryptedSessionId;
    }
}