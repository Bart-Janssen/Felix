package felix.api.repository;

import felix.api.models.Chat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRepository extends CrudRepository<Chat, UUID>
{
    List<Chat> findAllByFromIdAndToId(final UUID fromId, final UUID toId);
    List<Chat> findAllByToIdAndFromId(final UUID toId, final UUID fromId);
    List<Chat> findAllByToId(final UUID toId);
}