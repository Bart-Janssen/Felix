package felix.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage
{
    private WebSocketMessageType type;
    private String message;
    private String from;
    private String to;
    private boolean group;
    private JwtToken jwtToken;
}