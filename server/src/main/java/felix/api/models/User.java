package felix.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "telix_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User
{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private String name;
    private String password;
    private String sessionId;
    private int coins;
    private int level;
    @Column(unique = true)
    private String displayName;
    @Transient
    private boolean online;
    private boolean twoFAEnabled;
}