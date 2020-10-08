package felix.api.service.user;

import felix.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import felix.api.models.User;
import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService
{
    private final UserRepository userRepository;

    @Override
    public User login(User user) throws EntityNotFoundException
    {
        return userRepository.findByNameAndPassword(user.getName(), user.getPassword()).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public User register(User user) throws DataIntegrityViolationException
    {
        return userRepository.save(user);
    }

    /*@Override
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
    public void logout(User user) throws IOException, URISyntaxException
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
    }*/
}