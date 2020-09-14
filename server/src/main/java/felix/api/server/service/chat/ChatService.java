package felix.api.server.service.chat;

import felix.api.server.service.MicroService;
import org.springframework.stereotype.Component;
import felix.api.server.service.MicroServiceType;
import felix.api.server.models.Chat;
import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class ChatService extends MicroService implements IChatService
{
    @Override
    public void addNew(Chat chat) throws URISyntaxException, IOException
    {

    }
}