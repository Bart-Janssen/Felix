package felix.client.service.user;

import felix.client.exceptions.AlreadyLoggedInException;
import felix.client.exceptions.NotAuthorizedException;
import felix.client.models.JwtToken;
import felix.client.models.User;
import felix.client.service.MainService;

import java.util.UUID;

public class UserService extends MainService implements IUserService
{
    @Override
    public JwtToken login(User user, String encryptedUUID) throws NotAuthorizedException, AlreadyLoggedInException
    {
        try
        {
            user.setEncryptedUUID(encryptedUUID);
            return super.post("authentication/login/", user, JwtToken.class);
        }
        catch (Exception e)
        {
            if (e instanceof AlreadyLoggedInException) throw new AlreadyLoggedInException();
            throw new NotAuthorizedException();
        }
    }
}