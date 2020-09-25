package felix.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AesEncryptedMessage
{
    private String message;
}