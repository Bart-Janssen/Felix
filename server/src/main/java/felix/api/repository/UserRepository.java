package felix.api.repository;

import felix.api.models.User;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID>
{

}