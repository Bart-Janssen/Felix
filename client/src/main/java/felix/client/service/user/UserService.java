package felix.client.service.user;

import felix.client.exceptions.AlreadyLoggedInException;
import felix.client.exceptions.NotAuthorizedException;
import felix.client.models.JwtToken;
import felix.client.models.User;
import felix.client.service.MainService;

public class UserService extends MainService implements IUserService
{
    @Override
    public JwtToken login(User user) throws NotAuthorizedException, AlreadyLoggedInException
    {
        try
        {
            return super.post("authentication/login", user, JwtToken.class);
        }
        catch (Exception e)
        {
            if (e instanceof AlreadyLoggedInException) throw new AlreadyLoggedInException();
            throw new NotAuthorizedException();
        }
    }
}