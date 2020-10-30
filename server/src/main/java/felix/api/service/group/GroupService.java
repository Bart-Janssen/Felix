package felix.api.service.group;

import com.google.gson.Gson;
import felix.api.exceptions.NotAuthorizedException;
import felix.api.models.Group;
import felix.api.models.User;
import felix.api.repository.GroupRepository;
import felix.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService implements IGroupService
{
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Override
    public Group createGroup(UUID userId, String groupName)
    {
        User owner = this.userRepository.findUserById(userId).orElseThrow(EntityNotFoundException::new);
        Group group = new Group();
        group.addGroupMember(owner);
        group.setGroupName(groupName);
        group.setOwnerId(owner.getId());
        Group newGroup = this.groupRepository.save(group);
        owner.addGroup(newGroup);
        this.userRepository.save(owner);
        List<User> members = newGroup.getGroupMembers();
        List<User> emptyMembers = new ArrayList<>();
        members.forEach(member -> emptyMembers.add(User.builder().displayName(member.getDisplayName()).build()));
        newGroup.setGroupMembers(emptyMembers);
        return newGroup;
    }

    @Override
    public List<Group> getGroups(UUID userId)
    {
        User user = this.userRepository.findUserById(userId).orElseThrow(EntityNotFoundException::new);
        List<UUID> groupIds = new ArrayList<>();
        user.getMemberGroups().forEach(group -> groupIds.add(group.getId()));
        List<Group> groups = new ArrayList<>();
        this.groupRepository.findAllById(groupIds).forEach(groups::add);
        for (Group group : groups)
        {
            List<User> members = group.getGroupMembers();
            List<User> emptyMembers = new ArrayList<>();
            members.forEach(member -> emptyMembers.add(User.builder().displayName(member.getDisplayName()).online(member.isOnline()).build()));
            group.setGroupMembers(emptyMembers);
        }
        return groups;
    }

    @Override
    public void leaveGroup(UUID groupId, UUID userId)
    {
        Group group = this.groupRepository.findById(groupId).orElseThrow(EntityNotFoundException::new);
        User user = this.userRepository.findUserById(userId).orElseThrow(EntityNotFoundException::new);
        boolean userIsPartOfGroup = false;
        for (User member : group.getGroupMembers())
        {
            if (member.getId().equals(userId))
            {
                userIsPartOfGroup = true;
                group.getGroupMembers().remove(member);
                break;
            }
        }
        if (!userIsPartOfGroup) throw new NotAuthorizedException();
        user.getMemberGroups().removeIf(memberGroup -> memberGroup.getId().equals(group.getId()));
        this.userRepository.save(user);
        if (group.getGroupMembers().size() == 0) this.groupRepository.delete(group);
        else this.groupRepository.save(group);
    }
}