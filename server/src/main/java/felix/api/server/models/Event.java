package felix.api.server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event
{
    private int id;
    private String message;
    private EventType type;

    public Event(String message, EventType type)
    {
        this.message = message;
        this.type = type;
    }
}