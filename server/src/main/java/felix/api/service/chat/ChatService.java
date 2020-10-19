package felix.api.service.chat;

import felix.api.exceptions.BadRequestException;
import felix.api.models.User;
import felix.api.repository.ChatRepository;
import felix.api.models.Chat;
import felix.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class ChatService implements IChatService
{
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void addNew(Chat chat)
    {
        this.chatRepository.save(chat);
    }

    @Override
    public List<Chat> getAll(UUID id, String friendDisplayName)
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
        return chatMessages;
    }

    @Override
    public void addNewOffline(UUID userId, String toFriendDisplayName, String message)
    {
        Map<String, User> friends = this.checkIfFriends(userId, toFriendDisplayName);
        this.addNew(new Chat(friends.get("user").getId(), friends.get("friend").getId(), message, new Date().getTime()));
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