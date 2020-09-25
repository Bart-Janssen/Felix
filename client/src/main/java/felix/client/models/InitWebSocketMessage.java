package felix.client.models;

public class InitWebSocketMessage
{
    private String serverPublicKey;
    private String encryptedAesKey;
    private String encryptedUuid;

    public InitWebSocketMessage() {}

    public InitWebSocketMessage(String serverPublicKey, String encryptedAesKey, String encryptedUuid)
    {
        this.serverPublicKey = serverPublicKey;
        this.encryptedAesKey = encryptedAesKey;
        this.encryptedUuid = encryptedUuid;
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

    public String getEncryptedUuid()
    {
        return encryptedUuid;
    }

    public void setEncryptedUuid(String encryptedUuid)
    {
        this.encryptedUuid = encryptedUuid;
    }
}