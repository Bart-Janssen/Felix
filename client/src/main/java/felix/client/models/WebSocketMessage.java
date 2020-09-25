package felix.client.models;

public class WebSocketMessage
{
    private String message;
    private JwtToken jwtToken;

    public WebSocketMessage() {}

    public WebSocketMessage(String message, JwtToken jwtToken)
    {
        this.message = message;
        this.jwtToken = jwtToken;
    }

    public String getMessage()
    {
        return message;
    }

    public JwtToken getJwtToken()
    {
        return jwtToken;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setJwtToken(JwtToken jwtToken)
    {
        this.jwtToken = jwtToken;
    }
}