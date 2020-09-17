package felix.client.service.user;

import felix.client.exceptions.NotAuthorizedException;
import felix.client.models.JwtToken;
import felix.client.models.User;
import felix.client.service.MainService;

public class UserService extends MainService implements IUserService
{
    @Override
    public JwtToken login(User user) throws NotAuthorizedException
    {
        try
        {
            return super.post("authentication/login", user, JwtToken.class);
        }
        catch (Exception e)
        {
            throw new NotAuthorizedException();
        }
    }
}