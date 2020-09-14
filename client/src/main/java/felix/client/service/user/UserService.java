package felix.client.service.user;

import felix.client.exceptions.NotAuthorizedException;
import felix.client.models.JwtToken;
import felix.client.models.User;
import felix.client.service.MainService;

public class UserService extends MainService implements IUserService
{
    @Override
    public void login(User user) throws NotAuthorizedException
    {
        JwtToken token;
        try
        {
            token = super.post("authentication/login", user, JwtToken.class);
        }
        catch (Exception e)
        {
            throw new NotAuthorizedException();
        }
        System.out.println(token.getToken());
    }
}