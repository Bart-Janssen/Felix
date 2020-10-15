package felix.api.service.friend;

import felix.api.exceptions.AlreadyFriendsException;
import felix.api.exceptions.AlreadyPendingInviteException;
import felix.api.models.PendingFriendInvite;
import felix.api.models.User;
import felix.api.repository.PendingFriendInviteRepository;
import felix.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FriendService implements IFriendService
{
    private final UserRepository userRepository;
    private final PendingFriendInviteRepository pendingFriendInviteRepository;

    @Override
    public User sendFriendInvite(String displayName, UUID userId)
    {
        User friend = this.userRepository.findByDisplayName(displayName).orElseThrow(EntityNotFoundException::new);
        PendingFriendInvite existingInvite = this.pendingFriendInviteRepository.findByFriendIdAndUserId(userId, friend.getId()).orElse(null);
        if (existingInvite != null) throw new AlreadyPendingInviteException();
        if (friend.getFriends().stream().anyMatch(friendsFriend -> friendsFriend.getId().equals(userId))) throw new AlreadyFriendsException();
        this.pendingFriendInviteRepository.save(PendingFriendInvite.builder()
                .friendId(friend.getId())
                .userId(userId)
                .build());
        return User.builder().displayName(friend.getDisplayName()).build();
    }

    @Override
    public List<User> getFriends(UUID userId)
    {
        User user = this.userRepository.findUserById(userId).orElseThrow(EntityNotFoundException::new);
        List<User> friends = new ArrayList<>();
        for (User friend : user.getFriends())
        {
            friends.add(User.builder().displayName(friend.getDisplayName()).online(friend.isOnline()).build());
        }
        return friends;
    }

    @Override
    public List<String> getOutgoingPendingInvites(UUID userId)
    {
        List<PendingFriendInvite> pendingInvites = this.pendingFriendInviteRepository.findAllByUserId(userId).orElse(null);
        if (pendingInvites == null) return new ArrayList<>();
        List<String> pendingOutgoingInvites = new ArrayList<>();
        for (PendingFriendInvite pendingFriendInvite : pendingInvites)
        {
            pendingOutgoingInvites.add(this.userRepository.findUserById(pendingFriendInvite.getFriendId()).orElseThrow(EntityNotFoundException::new).getDisplayName());
        }
        return pendingOutgoingInvites;
    }

    @Override
    public List<User> getIncomingPendingInvites(UUID userId)
    {
        List<PendingFriendInvite> pendingInvites = this.pendingFriendInviteRepository.findAllByFriendId(userId).orElse(null);
        if (pendingInvites == null) return new ArrayList<>();
        List<User> pendingIncomingInvites = new ArrayList<>();
        for (PendingFriendInvite request : pendingInvites)
        {
            User pendingIncomingInvite = this.userRepository.findUserById(request.getUserId()).orElseThrow(EntityNotFoundException::new);
            pendingIncomingInvites.add(User.builder().displayName(pendingIncomingInvite.getDisplayName()).online(pendingIncomingInvite.isOnline()).build());
        }
        return pendingIncomingInvites;
    }

    @Override
    public String acceptInvite(String friendDisplayName, UUID userId)
    {
        User friend = this.userRepository.findByDisplayName(friendDisplayName).orElseThrow(EntityNotFoundException::new);
        PendingFriendInvite pendingFriendInvite = this.pendingFriendInviteRepository.findByFriendIdAndUserId(userId, friend.getId()).orElseThrow(EntityNotFoundException::new);
        this.pendingFriendInviteRepository.delete(pendingFriendInvite);
        User user = this.userRepository.findUserById(userId).orElseThrow(EntityNotFoundException::new);
        friend.getFriends().add(user);
        user.getFriends().add(friend);
        this.userRepository.save(friend);
        this.userRepository.save(user);
        return friend.getDisplayName();
    }

    @Override
    public void declineInvite(String friendDisplayName, UUID userId)
    {
        User friend = this.userRepository.findByDisplayName(friendDisplayName).orElseThrow(EntityNotFoundException::new);
        PendingFriendInvite pendingFriendInvite = this.pendingFriendInviteRepository.findByFriendIdAndUserId(userId, friend.getId()).orElseThrow(EntityNotFoundException::new);
        this.pendingFriendInviteRepository.delete(pendingFriendInvite);
    }

    @Override
    public void removeFriend(String friendDisplayName, UUID userId)
    {
        User friend = this.userRepository.findByDisplayName(friendDisplayName).orElseThrow(EntityNotFoundException::new);
        User user = this.userRepository.findUserById(userId).orElseThrow(EntityNotFoundException::new);
        friend.getFriends().removeIf(loopFriend -> user.getDisplayName().equals(loopFriend.getDisplayName()));
        user.getFriends().removeIf(loopUser -> friend.getDisplayName().equals(loopUser.getDisplayName()));
        this.userRepository.save(friend);
        this.userRepository.save(user);
    }

    @Override
    public void cancelInvite(String friendDisplayName, UUID userId)
    {
        User friend = userRepository.findByDisplayName(friendDisplayName).orElseThrow(EntityNotFoundException::new);
        PendingFriendInvite pendingFriendInvite = pendingFriendInviteRepository.findByFriendIdAndUserId(friend.getId(), userId).orElseThrow(EntityNotFoundException::new);
        pendingFriendInviteRepository.delete(pendingFriendInvite);
    }
}