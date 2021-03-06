package felix.api.configuration;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import static java.nio.charset.StandardCharsets.UTF_8;

public class RsaEncryptionManager
{
    private static final String RSA = "RSA";
    private static final int KEY_SIZE = 2048;
    private static KeyPair serverKeyPair;

    private RsaEncryptionManager(){}

    static
    {
        try
        {
            serverKeyPair = buildKeyPair();
        }
        catch (GeneralSecurityException e)
        {
            serverKeyPair = null;
        }
    }

    private static PublicKey serverPublicKey = serverKeyPair != null ? serverKeyPair.getPublic() : null;
    private static PrivateKey serverPrivateKey = serverKeyPair != null ? serverKeyPair.getPrivate() : null;

    static String encrypt(String key, String message) throws GeneralSecurityException
    {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance(RSA).generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(key))));
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(UTF_8)));
    }

    public static String decrypt(String encrypted) throws GeneralSecurityException
    {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, serverPrivateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted.getBytes())));
    }

    private static KeyPair buildKeyPair() throws GeneralSecurityException
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator.genKeyPair();
    }

    static String getPubKey()
    {
        return Base64.getEncoder().encodeToString(serverPublicKey.getEncoded());
    }
}