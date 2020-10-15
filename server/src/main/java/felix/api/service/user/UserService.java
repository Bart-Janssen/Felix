package felix.api.service.user;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import felix.api.configuration.PasswordHasher;
import felix.api.exceptions.NotAuthorizedException;
import felix.api.repository.CredentialRepository;
import felix.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import felix.api.models.User;
import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService
{
    private final UserRepository userRepository;
    private final GoogleAuthenticator googleAuthenticator;
    private final CredentialRepository credentialRepository;
    private static final int TWO_FACTOR_AUTHENTICATION_CODE_LENGTH = 6;

    @Override
    public User login(User user) throws EntityNotFoundException
    {
        User authenticatedUser = this.userRepository.findByName(user.getName()).orElseThrow(EntityNotFoundException::new);
        if (authenticatedUser.getTotp() == null)
        {
            if (new PasswordHasher().verifyHash(user.getPassword(), authenticatedUser.getPassword()))
            {
                return this.finalizeLogin(false, authenticatedUser);
            }
            throw new NotAuthorizedException();
        }
        int code = Integer.parseInt(user.getPassword().substring(user.getPassword().length() - TWO_FACTOR_AUTHENTICATION_CODE_LENGTH));
        if (!new PasswordHasher().verifyHash(user.getPassword().substring(0, user.getPassword().length() - TWO_FACTOR_AUTHENTICATION_CODE_LENGTH), authenticatedUser.getPassword()))
        if (!this.googleAuthenticator.authorizeUser(authenticatedUser.getId().toString(), code))
        {
            throw new NotAuthorizedException();
        }
        return this.finalizeLogin(true, authenticatedUser);
    }

    private User finalizeLogin(boolean twoFa, User user)
    {
        user.setOnline(true);
        userRepository.save(user);
        user.setPassword("");
        user.setTwoFAEnabled(twoFa);
        this.clearFriendInfo(user);
        return user;
    }

    private void clearFriendInfo(User user)
    {
        user.getFriends().forEach(friend ->
        {
            friend.setFriends(new ArrayList<>());
            friend.setPassword("");
            friend.setTwoFAEnabled(false);
            friend.setName("");
            friend.setTotp(null);
            friend.setId(null);
        });
    }

    @Override
    public User register(User user) throws DataIntegrityViolationException
    {
        user.setPassword(new PasswordHasher().hash(user.getPassword()));
        return this.userRepository.save(user);
    }

    @Override
    public String enable2FA(UUID userId, String username) throws IOException, WriterException
    {
        final GoogleAuthenticatorKey key = this.googleAuthenticator.createCredentials(userId.toString());
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String authURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("Felix", username, key);
        BitMatrix bitMatrix = qrCodeWriter.encode(authURL, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);
        return Base64.getEncoder().encodeToString((byte[])stream.toByteArray());
    }

    @Override
    public void disable2FA(UUID userId)
    {
        this.credentialRepository.disable2FA(userId);
    }

    @Override
    public void logout(User user)
    {
        User dbUser = this.userRepository.findById(user.getId()).orElseThrow(EntityNotFoundException::new);
        dbUser.setOnline(false);
        userRepository.save(dbUser);
    }
}