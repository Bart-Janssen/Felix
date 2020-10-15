package felix.service.friend;

import felix.models.User;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface IFriendService
{
    User sendPendingOutgoingInvite(String displayName) throws GeneralSecurityException, IOException, URISyntaxException;
    List<String> getPendingOutgoingInvites() throws GeneralSecurityException, IOException, URISyntaxException;
    List<User> getFriendInvites() throws GeneralSecurityException, IOException, URISyntaxException;
    String acceptInvite(String friendDisplayName) throws GeneralSecurityException, IOException, URISyntaxException;
    List<User> getFriends() throws GeneralSecurityException, IOException, URISyntaxException;
    void remove(String friendDisplayName) throws GeneralSecurityException, IOException, URISyntaxException;
    void declineInvite(String friendDisplayName) throws GeneralSecurityException, IOException, URISyntaxException;
    void cancelInvite(String friendDisplayName) throws GeneralSecurityException, IOException, URISyntaxException;
}