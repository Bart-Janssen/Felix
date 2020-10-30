package felix.service.group;

import felix.models.Group;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;

public interface IGroupService
{
    Group createGroup(String groupName) throws GeneralSecurityException, IOException, URISyntaxException;
    List<Group> getGroups() throws GeneralSecurityException, IOException, URISyntaxException;
    void leaveGroup(Group group) throws GeneralSecurityException, IOException, URISyntaxException;
    void invite(UUID groupId, String inviteDisplayName) throws GeneralSecurityException, IOException, URISyntaxException;
}