package felix.api.server.service.chat;

import felix.api.server.models.Chat;
import java.io.IOException;
import java.net.URISyntaxException;

public interface IChatService
{
    void addNew(Chat chat) throws URISyntaxException, IOException;
}