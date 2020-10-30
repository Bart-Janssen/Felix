package felix.api.service.chat;

import felix.api.configuration.AesEncryptionManager;
import felix.api.exceptions.BadRequestException;
import felix.api.exceptions.NotAuthorizedException;
import felix.api.models.Group;
import felix.api.models.User;
import felix.api.repository.ChatRepository;
import felix.api.models.Chat;
import felix.api.repository.GroupRepository;
import felix.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class ChatService implements IChatService
{
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;

    private final static String base64 = "JwS/4Ozf/OMwXEo8eajsXmOgMYFkhoP3JAcH06kSlVk=";

    @Override
    public void addNew(Chat chat) throws GeneralSecurityException
    {
        chat.setMessage(AesEncryptionManager.encrypt(base64, chat.getMessage()));
        this.chatRepository.save(chat);
    }

    @Override
    public List<Chat> getAll(UUID id, String friendDisplayName) throws GeneralSecurityException
    {
        Map<String, User> friends = this.checkIfFriends(id, friendDisplayName);
        List<Chat> chatMessages = new ArrayList<>();
        this.chatRepository.findAllByFromIdAndToId(friends.get("user").getId(), friends.get("friend").getId()).forEach(chat -> chatMessages.add(Chat.builder()
                .message(chat.getMessage())
                .displayNameFrom(friends.get("user").getDisplayName())
                .displayNameTo(friends.get("friend").getDisplayName())
                .date(chat.getDate())
                .build()));
        this.chatRepository.findAllByToIdAndFromId(friends.get("user").getId(), friends.get("friend").getId()).forEach(chat -> chatMessages.add(Chat.builder()
                .message(chat.getMessage())
                .displayNameTo(friends.get("user").getDisplayName())
                .displayNameFrom(friends.get("friend").getDisplayName())
                .date(chat.getDate())
                .build()));
        chatMessages.sort(Comparator.comparing(Chat::getDate));
        for (Chat chat : chatMessages)
        {
            chat.setMessage(AesEncryptionManager.decrypt(base64, chat.getMessage()));
        }
        return chatMessages;
    }

    @Override
    public void addNewOffline(UUID userId, String toFriendDisplayName, String message) throws GeneralSecurityException
    {
        Map<String, User> friends = this.checkIfFriends(userId, toFriendDisplayName);
        this.addNew(new Chat(friends.get("user").getId(), friends.get("friend").getId(), message, new Date().getTime()));
    }

    @Override
    public List<Chat> getAllGroup(UUID userId, UUID groupId) throws GeneralSecurityException
    {
        Group group = this.groupRepository.findById(groupId).orElseThrow(EntityNotFoundException::new);
        boolean userIsPartOfGroup = false;
        for (User member : group.getGroupMembers())
        {
            if (member.getId().equals(userId))
            {
                userIsPartOfGroup = true;
                break;
            }
        }
        if (!userIsPartOfGroup) throw new NotAuthorizedException();
        List<Chat> chatMessages = new ArrayList<>();
        this.chatRepository.findAllByToId(groupId).forEach(chat -> chatMessages.add(Chat.builder()
                        .message(chat.getMessage())
                        .displayNameFrom(this.userRepository.findUserById(chat.getFromId()).orElseThrow(EntityNotFoundException::new).getDisplayName())
                        .date(chat.getDate())
                        .build()));
        for (Chat chat : chatMessages)
        {
            chat.setMessage(AesEncryptionManager.decrypt(base64, chat.getMessage()));
        }
        return chatMessages;
    }

    private Map<String, User> checkIfFriends(UUID userId, String friendDisplayName) throws BadRequestException
    {
        User user = this.userRepository.findUserById(userId).orElseThrow(EntityNotFoundException::new);
        User friend = this.userRepository.findByDisplayName(friendDisplayName).orElseThrow(EntityNotFoundException::new);
        if (user.getFriends().stream().noneMatch(f -> f.getDisplayName().equals(friendDisplayName))) throw new BadRequestException();
        if (friend.getFriends().stream().noneMatch(f -> f.getDisplayName().equals(user.getDisplayName()))) throw new BadRequestException();
        Map<String, User> friends = new HashMap<>();
        friends.put("user", user);
        friends.put("friend", friend);
        return friends;
    }
}