package felix.api.service.user;

import com.google.zxing.WriterException;
import felix.api.models.User;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.UUID;

public interface IUserService
{
    User login(User user) throws GeneralSecurityException;
    User register(User user) throws GeneralSecurityException;
    String enable2FA(UUID userId, String username) throws IOException, WriterException;
    void disable2FA(UUID userId) throws IOException, URISyntaxException;
    void logout(User user);
    void deleteAccount(User user);
}