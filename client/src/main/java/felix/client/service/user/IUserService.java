package felix.client.service.user;

import felix.client.exceptions.AlreadyLoggedInException;
import felix.client.exceptions.NotAuthorizedException;
import felix.client.models.JwtToken;
import felix.client.models.User;

import java.util.UUID;

public interface IUserService
{
    JwtToken login(User user, String encryptedUUID) throws NotAuthorizedException, AlreadyLoggedInException;
}