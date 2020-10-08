package felix.api.configuration;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import felix.api.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GoogleAuthenticatorConfiguration
{
    private final CredentialRepository credentialRepository;

    @Bean
    public GoogleAuthenticator googleAuthenticator()
    {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        googleAuthenticator.setCredentialRepository(credentialRepository);
        return googleAuthenticator;
    }
}