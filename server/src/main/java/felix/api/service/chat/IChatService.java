package felix.api.service.chat;

import felix.api.models.Chat;
import java.util.List;
import java.util.UUID;

public interface IChatService
{
    void addNew(Chat chat);
    List<Chat> getAll(UUID userId, String friendDisplayName);
    void addNewOffline(UUID userId, String toFriendDisplayName, String message);
}