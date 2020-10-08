package felix.api.service.user;

import com.google.zxing.WriterException;
import felix.api.models.User;
import org.springframework.dao.DataIntegrityViolationException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

public interface IUserService
{
    User login(User user) throws EntityNotFoundException;
    User register(User user) throws DataIntegrityViolationException;
    String enable2FA(UUID userId, String username) throws IOException, WriterException;
    void disable2FA(UUID userId) throws IOException, URISyntaxException;
    /*List<User> getFriendsByUserId(UUID fromString) throws IOException, URISyntaxException;
    void logout(User user) throws IOException, URISyntaxException;
    void sendFriendInvite(String displayName, UUID userId) throws IOException, URISyntaxException;
    List<String> getOutgoingPendingInvites(UUID userId) throws IOException, URISyntaxException;
    List<String> getIncomingPendingInvites(UUID userId) throws IOException, URISyntaxException;
    void cancelInvite(String invite, UUID userId) throws IOException, URISyntaxException;
    void acceptInvite(String invite, UUID userId) throws IOException, URISyntaxException;
    void declineInvite(String invite, UUID userId) throws IOException, URISyntaxException;
    void removeFriend(String friendDisplayName, UUID userId) throws IOException, URISyntaxException;
    void deleteAccount(UUID userId);*/
}