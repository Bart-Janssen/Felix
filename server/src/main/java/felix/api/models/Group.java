package felix.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "felix_group")
public class Group
{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private UUID ownerId;
    private String groupName;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> groupMembers = new ArrayList<>();

    public void addGroupMember(User user)
    {
        this.groupMembers.add(user);
    }

    void removeMember(User user)
    {
        this.groupMembers.removeIf(member -> member.getDisplayName().equals(user.getDisplayName()));
    }
}