package felix.client.service.user;

import felix.client.exceptions.AlreadyLoggedInException;
import felix.client.exceptions.NotAuthorizedException;
import felix.client.models.User;
import java.io.IOException;
import java.net.URISyntaxException;

public interface IUserService
{
    void login(User user) throws NotAuthorizedException, AlreadyLoggedInException;
    void register(User user) throws NotAuthorizedException, AlreadyLoggedInException;
    void logout() throws IOException, URISyntaxException;
    String enable2Fa() throws IOException, URISyntaxException;
    void rest();
}