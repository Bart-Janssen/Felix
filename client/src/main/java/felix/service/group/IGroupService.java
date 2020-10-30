package felix.service.group;

import felix.models.Group;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface IGroupService
{
    Group createGroup(String groupName) throws GeneralSecurityException, IOException, URISyntaxException;
    List<Group> getGroups() throws GeneralSecurityException, IOException, URISyntaxException;
    void leaveGroup(Group group) throws GeneralSecurityException, IOException, URISyntaxException;
}