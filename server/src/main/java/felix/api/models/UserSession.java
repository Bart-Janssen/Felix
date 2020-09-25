package felix.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.websocket.Session;

@Data
@AllArgsConstructor
public class UserSession
{
    private JwtToken token;
    private User user;
    private Session session;
    private String aesKey;
}