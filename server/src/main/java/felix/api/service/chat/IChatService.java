package felix.api.service.chat;

import felix.api.models.Chat;
import java.io.IOException;
import java.net.URISyntaxException;

public interface IChatService
{
    void addNew(Chat chat) throws URISyntaxException, IOException;
}