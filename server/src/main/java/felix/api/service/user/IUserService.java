package felix.api.service.user;

import com.google.zxing.WriterException;
import felix.api.models.User;
import org.springframework.dao.DataIntegrityViolationException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

public interface IUserService
{
    User login(User user) throws EntityNotFoundException;
    User register(User user) throws DataIntegrityViolationException;
    String enable2FA(UUID userId, String username) throws IOException, WriterException;
    void disable2FA(UUID userId) throws IOException, URISyntaxException;
    void logout(User user) throws EntityNotFoundException;
}