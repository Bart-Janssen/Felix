package felix.models;

public class WebSocketMessage
{
    private WebSocketMessageType type;
    private String message;
    private String from;
    private String to;
    private boolean group;
    private JwtToken jwtToken;

    public WebSocketMessage() {}

    public WebSocketMessage(String message, String to, boolean group, JwtToken jwtToken)
    {
        this.message = message;
        this.to = to;
        this.group = group;
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

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public WebSocketMessageType getType()
    {
        return type;
    }

    public void setType(WebSocketMessageType type)
    {
        this.type = type;
    }
}