package felix.api.service.chat;

import felix.api.service.MicroService;
import org.springframework.stereotype.Component;
import felix.api.models.Chat;
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