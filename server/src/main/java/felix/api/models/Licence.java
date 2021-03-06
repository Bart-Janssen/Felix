package felix.api.models;

import felix.api.configuration.LicenceManager;
import felix.api.repository.ListConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Licence
{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID token;
    private boolean activated;
    private static final String publisher = "Bart Janssen";
    private static final String application = "Felix";
    private static final String algorithm = LicenceManager.getAlgorithm();
    private String sign;

    @Transient
    private String encryptedToken;

    @Convert(converter = ListConverter.class)
    private List<String> macs;

    public String toSignString()
    {
        return "Publisher:" + publisher + (char)0b00001101 + (char)0b00001010 +
                "Application:" + application + (char)0b00001101 + (char)0b00001010 +
                "Token:" + token + (char)0b00001101 + (char)0b00001010 +
                "SignAlgorithm:" + algorithm + (char)0b00001101 + (char)0b00001010;
    }

    @Override
    public String toString()
    {
        return "Publisher:" + publisher + (char)0b00001101 + (char)0b00001010 +
                "Application:" + application + (char)0b00001101 + (char)0b00001010 +
                "Token:" + token + (char)0b00001101 + (char)0b00001010 +
                "SignAlgorithm:" + algorithm + (char)0b00001101 + (char)0b00001010 +
                "Sign:" + sign;
    }
}