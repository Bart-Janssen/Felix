package felix.api.service.event;

import felix.api.models.Event;

import java.io.IOException;
import java.net.URISyntaxException;

public interface IEventService
{
    void addNew(Event event) throws URISyntaxException, IOException;
}