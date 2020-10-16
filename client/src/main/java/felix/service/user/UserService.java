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
import java.security.GeneralSecurityException;

public class UserService extends MainService implements IUserService
{
    @Override
    public void login(User encryptedUser) throws NotAuthorizedException, AlreadyLoggedInException, IOException, URISyntaxException, GeneralSecurityException
    {
        AesEncryptedMessage aesEncryptedMessage = super.post("authentication/login/", encryptedUser, AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
    }

    @Override
    public void register(User encryptedUser) throws NotAuthorizedException, AlreadyLoggedInException, IOException, URISyntaxException, GeneralSecurityException
    {
        AesEncryptedMessage aesEncryptedMessage = super.post("authentication/register/", encryptedUser, AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
    }

    @Override
    public void logout() throws IOException, URISyntaxException
    {
        super.put("authentication/logout", null, void.class);
    }

    @Override
    public String enable2Fa() throws IOException, URISyntaxException, GeneralSecurityException
    {
        AesEncryptedMessage aesEncryptedMessage = super.post("authentication/2fa/enable/", null, AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
        return new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), String.class);
    }

    @Override
    public void disable2Fa() throws IOException, URISyntaxException, GeneralSecurityException
    {
        AesEncryptedMessage aesEncryptedMessage = super.delete("authentication/2fa/disable/", AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
    }
}