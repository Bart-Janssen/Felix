package felix.api.repository;

import felix.api.models.TOTP;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

interface TOTPRepository extends CrudRepository<TOTP, UUID> {}