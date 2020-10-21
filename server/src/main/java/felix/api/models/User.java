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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "felix_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User
{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private String password;
    private boolean twoFAEnabled;
    private boolean online;

    @OneToOne(cascade = CascadeType.ALL)
    private TOTP totp;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String displayName;

    @Transient
    private String sessionId;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> friends = new ArrayList<>();
}