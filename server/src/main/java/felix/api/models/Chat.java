package felix.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat
{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private UUID fromId;
    private UUID toId;
    private String message;
    private Long date;

    @Transient
    private String displayNameFrom;
    @Transient
    private String displayNameTo;

    public Chat(UUID fromId, UUID toId, String message, Long date)
    {
        this.fromId = fromId;
        this.toId = toId;
        this.message = message;
        this.date = date;
    }
}