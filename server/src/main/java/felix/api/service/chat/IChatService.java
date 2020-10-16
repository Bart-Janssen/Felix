package felix.api.service.chat;

import felix.api.models.Chat;
import java.util.List;
import java.util.UUID;

public interface IChatService
{
    void addNew(Chat chat);
    List<String> getAll(UUID userId, String friendDisplayName);
    void addNewOffline(UUID userId, String toFriendDisplayName, String message);
}