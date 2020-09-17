package felix.client.service.user;

import felix.client.exceptions.AlreadyLoggedInException;
import felix.client.exceptions.NotAuthorizedException;
import felix.client.models.JwtToken;
import felix.client.models.User;

public interface IUserService
{
    JwtToken login(User user) throws NotAuthorizedException, AlreadyLoggedInException;
}