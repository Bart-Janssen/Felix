package felix.api.server.service.event;

import felix.api.server.models.Event;

import java.io.IOException;
import java.net.URISyntaxException;

public interface IEventService
{
    void addNew(Event event) throws URISyntaxException, IOException;
}