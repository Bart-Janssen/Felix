package felix.client.models;

public class AesEncryptedMessage
{
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
}
