package felix.api.models;

import javax.websocket.Session;

public class UserSession
{
    private JwtToken token;
    private User user;
    private Session session;

    public UserSession(User user, JwtToken token)
    {
        this.user = user;
        this.token = token;
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