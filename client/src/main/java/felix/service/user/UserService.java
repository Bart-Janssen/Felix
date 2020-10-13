package felix.service.user;

import com.google.gson.Gson;
import felix.exceptions.AlreadyLoggedInException;
import felix.exceptions.NotAuthorizedException;
import felix.models.AesEncryptedMessage;
import felix.models.JwtToken;
import felix.models.User;
import felix.service.MainService;
import java.io.IOException;
import java.net.URISyntaxException;

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
    public void logout() throws IOException, URISyntaxException
    {
        super.put("authentication/logout", null, void.class);
    }

    @Override
    public String enable2Fa()
    {
        try
        {
            AesEncryptedMessage aesEncryptedMessage = super.post("authentication/2fa/enable/", null, AesEncryptedMessage.class);
            super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
            return new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), String.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void disable2Fa()
    {
        try
        {
            AesEncryptedMessage aesEncryptedMessage = super.delete("authentication/2fa/disable/", AesEncryptedMessage.class);
            super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
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