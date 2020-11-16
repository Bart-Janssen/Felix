package felix.api.configuration;

import org.mindrot.jbcrypt.BCrypt;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher
{
    private static final String HASH = "SHA-512";
    private static final String BASE64 = "RR0l2cUC8bn3oKoAcTUKWLBuvG3hLMggNkpT1ah7wLs=";

    public String hash(String password) throws GeneralSecurityException
    {
        return AesEncryptionManager.encrypt(BASE64, BCrypt.hashpw(this.shaHash(password), BCrypt.gensalt(12)));
    }

    public boolean verifyHash(String password, String hash) throws GeneralSecurityException
    {
        return BCrypt.checkpw(this.shaHash(password), AesEncryptionManager.decrypt(BASE64, hash));
    }

    private String shaHash(String password)
    {
        try
        {
            return new String(MessageDigest.getInstance(HASH).digest(password.getBytes(StandardCharsets.UTF_8)));
        }
        catch (NoSuchAlgorithmException ignored)
        {
            return password;
        }
    }
}