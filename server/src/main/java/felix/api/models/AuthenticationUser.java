package felix.api.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationUser
{
    private String password;
    private String name;
    private String displayName;
    private String sessionId;
}