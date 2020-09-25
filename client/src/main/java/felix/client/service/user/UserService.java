package felix.client.service.user;

import felix.client.exceptions.AlreadyLoggedInException;
import felix.client.exceptions.NotAuthorizedException;
import felix.client.main.FelixSession;
import felix.client.models.AesEncryptedMessage;
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
            AesEncryptedMessage aesEncryptedMessage = super.post("authentication/login/", user, AesEncryptedMessage.class);
            return FelixSession.getInstance().decrypt(aesEncryptedMessage.getMessage(), JwtToken.class);
        }
        catch (Exception e)
        {
            if (e instanceof AlreadyLoggedInException) throw new AlreadyLoggedInException();
            throw new NotAuthorizedException();
        }
    }
}