package felix.api.repository;

import felix.api.models.Licence;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

public interface LicenceRepository extends CrudRepository<Licence, UUID>
{

}