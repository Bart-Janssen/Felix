package felix.client.service.user;

import com.google.gson.Gson;
import felix.client.exceptions.AlreadyLoggedInException;
import felix.client.exceptions.NotAuthorizedException;
import felix.client.models.AesEncryptedMessage;
import felix.client.models.JwtToken;
import felix.client.models.User;
import felix.client.service.MainService;

public class UserService extends MainService implements IUserService
{
    @Override
    public JwtToken login(User encryptedUser) throws NotAuthorizedException, AlreadyLoggedInException
    {
        try
        {
            AesEncryptedMessage aesEncryptedMessage = super.post("authentication/login/", encryptedUser, AesEncryptedMessage.class);
            return new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), JwtToken.class);
        }
        catch (Exception e)
        {
            if (e instanceof AlreadyLoggedInException) throw new AlreadyLoggedInException();
            throw new NotAuthorizedException();
        }
    }

    @Override
    public JwtToken register(User encryptedUser) throws NotAuthorizedException, AlreadyLoggedInException
    {
        try
        {
            AesEncryptedMessage aesEncryptedMessage = super.post("authentication/register/", encryptedUser, AesEncryptedMessage.class);
            return new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), JwtToken.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new NotAuthorizedException();
        }
    }

    @Override
    public void rest()
    {
        try
        {
            AesEncryptedMessage aesEncryptedMessage = super.post("authentication/test/", super.aesEncrypt("hey"), void.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}