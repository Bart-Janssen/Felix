package felix.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.websocket.Session;

@Data
@AllArgsConstructor
public class PendingSession
{
    private String clientPublicKey;
    private Session session;
}