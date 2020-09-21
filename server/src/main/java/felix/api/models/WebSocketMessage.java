package felix.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebSocketMessage
{
    private String message;
    private JwtToken jwtToken;
}