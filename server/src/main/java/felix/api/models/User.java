package felix.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
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
    private boolean twoFAEnabled;

    @OneToOne(cascade = CascadeType.ALL)
    private TOTP totp;

    @Column(unique = true)
    private String displayName;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> friends;

    @Transient
    private boolean online;
}