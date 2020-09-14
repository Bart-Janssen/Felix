package felix.api.service.event;

import felix.api.service.MicroService;
import felix.api.models.Event;
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