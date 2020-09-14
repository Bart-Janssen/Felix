package felix.api.server.models;

import lombok.Data;

@Data
public class WebSocketItem<T>
{
    private T item;
    private Chat message;

    public WebSocketItem(T item, Chat optionalMessage)
    {
        this.item = item;
        this.message = optionalMessage;
    }

    public WebSocketItem(T item)
    {
        this.item = item;
    }
}