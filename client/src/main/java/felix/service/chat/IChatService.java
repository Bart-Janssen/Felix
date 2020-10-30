package felix.service.chat;

import felix.models.Chat;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;

public interface IChatService
{
    List<Chat> getAll(String displayName) throws GeneralSecurityException, IOException, URISyntaxException;
    List<Chat> getAllGroup(UUID groupId) throws GeneralSecurityException, IOException, URISyntaxException;
}