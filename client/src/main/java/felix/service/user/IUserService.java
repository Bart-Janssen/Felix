package felix.service.user;

import felix.exceptions.AlreadyLoggedInException;
import felix.exceptions.NotAuthorizedException;
import felix.models.User;
import java.io.IOException;
import java.net.URISyntaxException;

public interface IUserService
{
    void login(User user) throws NotAuthorizedException, AlreadyLoggedInException;
    void register(User user) throws NotAuthorizedException, AlreadyLoggedInException;
    void logout() throws IOException, URISyntaxException;
    String enable2Fa() throws IOException, URISyntaxException;
    void disable2Fa();
    void rest();
}