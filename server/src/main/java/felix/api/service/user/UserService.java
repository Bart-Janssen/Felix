package felix.api.service.user;

import felix.api.exceptions.NotImplementedException;
import felix.api.service.MicroService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import felix.api.models.User;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService extends MicroService implements IUserService
{
    private Map<String, User> users = new HashMap<String, User>() //TODO db connectie
    {{
        super.put("Henk", User.builder().id(UUID.randomUUID()).displayName("Henk123").name("Henk").twoFAEnabled(false).password("123").build()); //todo db connectie
        super.put("Bart", User.builder().id(UUID.randomUUID()).displayName("Bart123").name("Bart").twoFAEnabled(false).password("123").build()); //todo db connectie
    }};

    @Override
    public User login(User user) throws IOException, URISyntaxException
    {
        User authenticatedUser = this.users.get(user.getName()); //todo db connectie
        if (authenticatedUser == null) return null;
        if (!authenticatedUser.getPassword().equals(user.getPassword())) return null;
        return User.builder().id(authenticatedUser.getId()).twoFAEnabled(authenticatedUser.isTwoFAEnabled()).name(authenticatedUser.getName()).displayName(authenticatedUser.getDisplayName()).build(); //throw new NotImplementedException();
    }

    @Override
    public void logout(User user) throws IOException, URISyntaxException
    {
        throw new NotImplementedException();
    }

    @Override
    public String enable2FA(UUID userId, String username) throws IOException, URISyntaxException
    {
        throw new NotImplementedException();
    }

    @Override
    public void disable2FA(UUID userId) throws IOException, URISyntaxException
    {
        throw new NotImplementedException();
    }

    @Override
    public User register(User user) throws IOException, URISyntaxException
    {
        throw new NotImplementedException();
    }

    @Override
    public List<User> getFriendsByUserId(UUID id) throws IOException, URISyntaxException
    {
        throw new NotImplementedException();
    }

    @Override
    public void sendFriendInvite(String displayName, UUID userId) throws IOException, URISyntaxException
    {
        throw new NotImplementedException();
    }

    @Override
    public List<String> getOutgoingPendingInvites(UUID userId) throws IOException, URISyntaxException
    {
        throw new NotImplementedException();
    }

    @Override
    public List<String> getIncomingPendingInvites(UUID userId) throws IOException, URISyntaxException
    {
        throw new NotImplementedException();
    }

    @Override
    public void cancelInvite(String invite, UUID userId) throws IOException, URISyntaxException
    {
        throw new NotImplementedException();
    }

    @Override
    public void acceptInvite(String invite, UUID userId) throws IOException, URISyntaxException
    {
        throw new NotImplementedException();
    }

    @Override
    public void declineInvite(String invite, UUID userId) throws IOException, URISyntaxException
    {
        throw new NotImplementedException();
    }

    @Override
    public void removeFriend(String friendDisplayName, UUID userId) throws IOException, URISyntaxException
    {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAccount(UUID userId)
    {
        throw new NotImplementedException();
    }
}