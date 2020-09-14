package felix.api.server.service.event;

import felix.api.server.service.MicroService;
import felix.api.server.service.MicroServiceType;
import felix.api.server.models.Event;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class EventService extends MicroService implements IEventService
{
    @Override
    public void addNew(Event event) throws URISyntaxException, IOException
    {

    }
}