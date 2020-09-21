package felix.api.configuration;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;
import static java.nio.charset.StandardCharsets.UTF_8;

public class RsaEncryptionManager
{
    private static KeyPair serverKeyPair;

    static
    {
        try
        {
            serverKeyPair = buildKeyPair();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }

    private static PublicKey serverPublicKey = serverKeyPair.getPublic();
    private static PrivateKey serverPrivateKey = serverKeyPair.getPrivate();

    private static KeyPair buildKeyPair() throws NoSuchAlgorithmException
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.genKeyPair();
    }

    public static String encrypt(PublicKey key, String message) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(UTF_8)));
    }

    public static String decrypt(String encrypted) throws Exception
    {
        byte[] d = Base64.getDecoder().decode(encrypted.getBytes());
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, serverPrivateKey);
        byte[] data = cipher.doFinal(d);
        return new String(data);
    }

    public static String getPubKey()
    {
        return Base64.getEncoder().encodeToString(serverPublicKey.getEncoded());
    }
}