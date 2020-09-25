package felix.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InitWebSocketMessage
{
    private String serverPublicKey;
    private String encryptedAesKey;
    private String encryptedUuid;
}