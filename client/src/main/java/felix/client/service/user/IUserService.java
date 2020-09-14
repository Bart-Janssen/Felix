package felix.client.service.user;

import felix.client.exceptions.NotAuthorizedException;
import felix.client.models.User;

public interface IUserService
{
    void login(User user) throws NotAuthorizedException;
}