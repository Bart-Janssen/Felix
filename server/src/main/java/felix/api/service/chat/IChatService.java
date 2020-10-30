package felix.api.service.chat;

import felix.api.models.Chat;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;

public interface IChatService
{
    void addNew(Chat chat) throws GeneralSecurityException;
    List<Chat> getAll(UUID userId, String friendDisplayName) throws GeneralSecurityException;
    void addNewOffline(UUID userId, String toFriendDisplayName, String message) throws GeneralSecurityException;
    List<Chat> getAllGroup(UUID userId, UUID groupId) throws GeneralSecurityException;
}