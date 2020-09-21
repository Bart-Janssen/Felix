package felix.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User
{
    private UUID id;
    private String name;
    private String password;
    private String encryptedUUID;
    private int coins;
    private int level;
    private String displayName;
    private boolean online;
    private boolean twoFAEnabled;
}