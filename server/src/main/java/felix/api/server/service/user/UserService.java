package felix.api.server.service.user;

import com.fasterxml.jackson.core.type.TypeReference;
import felix.api.server.exceptions.NotImplementedException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import felix.api.server.service.MicroService;
import felix.api.server.service.MicroServiceType;
import felix.api.server.models.User;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService extends MicroService implements IUserService
{
    private final AmqpTemplate rabbitTemplate;

    @Override
    public User login(User user) throws IOException, URISyntaxException
    {
        throw new NotImplementedException();
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