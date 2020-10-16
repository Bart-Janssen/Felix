package felix.api.configuration;

import org.mindrot.jbcrypt.BCrypt;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher
{
    private final static String HASH = "SHA-512";

    public String hash(String password)
    {
        return BCrypt.hashpw(this.shaHash(password), BCrypt.gensalt(12));
    }

    public boolean verifyHash(String password, String hash)
    {
        return BCrypt.checkpw(this.shaHash(password), hash);
    }

    private String shaHash(String password)
    {
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH);
            return new String(messageDigest.digest(password.getBytes(StandardCharsets.UTF_8)));
        }
        catch (NoSuchAlgorithmException ignored)
        {
            return password;
        }
    }
}