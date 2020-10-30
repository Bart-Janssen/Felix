package felix.api.repository;

import felix.api.models.Group;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

public interface GroupRepository extends CrudRepository<Group, UUID>
{
}