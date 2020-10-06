package felix.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item<T>
{
    private AesEncryptedMessage aesEncryptedMessage;
    private JwtToken token;
    private T t;
}