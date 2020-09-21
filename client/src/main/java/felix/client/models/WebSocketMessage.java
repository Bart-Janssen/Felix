package felix.client.models;

public class WebSocketMessage
{
    private String message;
    private JwtToken jwtToken;

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
}