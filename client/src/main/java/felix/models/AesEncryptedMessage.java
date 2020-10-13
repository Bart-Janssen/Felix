package felix.models;

public class AesEncryptedMessage
{
    private String token;
    private String message;

    public AesEncryptedMessage() {}

    public AesEncryptedMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }
}
