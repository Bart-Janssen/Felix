package felix.service.user;

import felix.exceptions.AlreadyLoggedInException;
import felix.exceptions.NotAuthorizedException;
import felix.models.Licence;
import felix.models.User;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

public interface IUserService
{
    void login(User user) throws NotAuthorizedException, AlreadyLoggedInException, IOException, URISyntaxException, GeneralSecurityException;
    void register(User user) throws NotAuthorizedException, AlreadyLoggedInException, IOException, URISyntaxException, GeneralSecurityException;
    void logout() throws IOException, URISyntaxException;
    String enable2Fa() throws IOException, URISyntaxException, GeneralSecurityException;
    void disable2Fa() throws IOException, URISyntaxException, GeneralSecurityException;
    void deleteAccount() throws IOException, URISyntaxException, GeneralSecurityException;
    void checkLicence(Licence licence) throws IOException, URISyntaxException, GeneralSecurityException;
    void activate(Licence licence) throws IOException, URISyntaxException, GeneralSecurityException;
}