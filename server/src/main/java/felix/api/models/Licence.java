package felix.api.models;

import felix.api.configuration.LicenceManager;
import felix.api.repository.ListConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.omg.CORBA.Environment;

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
        return "Publisher:" + publisher + System.lineSeparator() +
                "Application:" + application + System.lineSeparator() +
                "Token:" + token + System.lineSeparator() +
                "SignAlgorithm:" + algorithm + System.lineSeparator();
    }

    @Override
    public String toString()
    {
        return "Publisher:" + publisher + System.lineSeparator() +
                "Application:" + application + System.lineSeparator() +
                "Token:" + token + System.lineSeparator() +
                "SignAlgorithm:" + algorithm + System.lineSeparator() +
                "Sign:" + sign;
    }

    public byte[] toByteArray()
    {
        return this.toString().getBytes();
    }
}