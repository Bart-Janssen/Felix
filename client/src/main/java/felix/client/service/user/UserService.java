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
    public void login(User encryptedUser) throws NotAuthorizedException, AlreadyLoggedInException
    {
        try
        {
            AesEncryptedMessage aesEncryptedMessage = super.post("authentication/login/", encryptedUser, AesEncryptedMessage.class);
            super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (e instanceof AlreadyLoggedInException) throw new AlreadyLoggedInException();
            throw new NotAuthorizedException();
        }
    }

    @Override
    public void register(User encryptedUser) throws NotAuthorizedException, AlreadyLoggedInException
    {
        try
        {
            AesEncryptedMessage aesEncryptedMessage = super.post("authentication/register/", encryptedUser, AesEncryptedMessage.class);
            super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
        }
        catch (Exception e)
        {
            if (e instanceof AlreadyLoggedInException) throw new AlreadyLoggedInException();
            throw new NotAuthorizedException();
        }
    }

    @Override
    public void rest()
    {
        try
        {
            AesEncryptedMessage aesEncryptedMessage = super.put("authentication/test/", super.aesEncrypt(new User("hai", "pass")), AesEncryptedMessage.class);
            super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
            System.out.println(new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), User.class).getName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}