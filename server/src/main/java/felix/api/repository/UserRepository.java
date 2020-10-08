package felix.api.repository;

import felix.api.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID>
{
    Optional<User> findByNameAndPassword(final String name, final String password);
    Optional<User> findByName(final String name);
}