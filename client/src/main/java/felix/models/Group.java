package felix.models;

import java.util.List;
import java.util.UUID;

public class Group
{
    private UUID id;
    private String groupName;
    private List<User> groupMembers;

    public Group() {}

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public UUID getId()
    {
        return id;
    }

    public List<User> getGroupMembers()
    {
        return groupMembers;
    }
}