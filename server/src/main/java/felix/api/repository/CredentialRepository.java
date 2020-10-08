package felix.api.repository;

import com.warrenstrange.googleauth.ICredentialRepository;
import felix.api.models.TOTP;
import felix.api.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CredentialRepository implements ICredentialRepository
{
    private final UserRepository userRepository;
    private final TOTPRepository totpRepository;

    @Override
    public String getSecretKey(String userIdAsString)
    {
        User user = userRepository.findUserById(UUID.fromString(userIdAsString)).orElseThrow(EntityNotFoundException::new);
        return user.getTotp().getSecretKey();
    }

    @Override
    public void saveUserCredentials(String userIdAsString, String secretKey, int validationCode, List<Integer> scratchCodes)
    {
        User user = userRepository.findUserById(UUID.fromString(userIdAsString)).orElseThrow(EntityNotFoundException::new);
        if (user.getTotp() == null)
        {
            user.setTotp(TOTP.builder()
                    .scratchCodes(scratchCodes)
                    .validationCode(validationCode)
                    .secretKey(secretKey)
                    .build());
            userRepository.save(user);
            return;
        }
        user.getTotp().setValidationCode(validationCode);
        user.getTotp().setSecretKey(secretKey);
        user.getTotp().setScratchCodes(scratchCodes);
        userRepository.save(user);
    }

    public void disable2FA(UUID userId)
    {
        User user = userRepository.findUserById(userId).orElseThrow(EntityNotFoundException::new);
        TOTP totp = user.getTotp();
        user.setTotp(null);
        userRepository.save(user);
        totpRepository.delete(totp);
    }
}