package felix.api.service.friend;

import felix.api.models.User;
import java.util.List;
import java.util.UUID;

public interface IFriendService
{
    User sendFriendInvite(String displayName, UUID userId);
    List<User> getFriends(UUID userId);
    List<String> getOutgoingPendingInvites(UUID userId);
    List<User> getIncomingPendingInvites(UUID userId);
    String acceptInvite(String friendDisplayName, UUID userId);
    void declineInvite(String friendDisplayName, UUID userId);
    void removeFriend(String friendDisplayName, UUID userId);
    void cancelInvite(String friendDisplayName, UUID userId);
}