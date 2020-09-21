package felix.api.models;

import javax.websocket.Session;

public class UserSession
{
    private JwtToken token;
    private User user;
    private Session session;
    private String publicKey;

    public UserSession(User user, Session session, JwtToken token, String publicKey)
    {
        this.user = user;
        this.session = session;
        this.token = token;
        this.publicKey = publicKey;
    }

    public User getUser()
    {
        return user;
    }

    public JwtToken getToken()
    {
        return token;
    }

    public void setSession(Session session)
    {
        this.session = session;
    }

    public Session getSession()
    {
        return session;
    }
}