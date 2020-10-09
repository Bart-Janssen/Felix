package felix.api.repository;

import felix.api.models.PendingFriendInvite;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PendingFriendInviteRepository extends CrudRepository<PendingFriendInvite, UUID>
{
    Optional<PendingFriendInvite> findByFriendIdAndUserId(final UUID friendId, final UUID userId);
    Optional<List<PendingFriendInvite>> findAllByUserId(final UUID userId);
    Optional<List<PendingFriendInvite>> findAllByFriendId(final UUID friendId);
}