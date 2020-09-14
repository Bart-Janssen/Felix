package felix.api.server.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat
{
    private UUID id;
    private String message;
    private String from;
    private String to;
    private String guiMessage;
    private String color;

    public Chat(String guiMessage, String color)
    {
        this.guiMessage = guiMessage;
        this.color = color;
    }
}