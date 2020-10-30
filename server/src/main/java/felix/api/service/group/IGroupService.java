package felix.api.service.group;

import felix.api.models.Group;
import felix.api.models.User;
import java.util.List;
import java.util.UUID;

public interface IGroupService
{
    Group createGroup(UUID userId, String groupName);
    List<Group> getGroups(UUID userId);
    void leaveGroup(UUID groupId, UUID userId);
    User invite(UUID groupId, String inviteDisplayName);
}