package felix.api.configuration;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
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

    static String encrypt(String key, String message) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, RsaEncryptionManager.stringToKey(key));
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(UTF_8)));
    }

    public static String decrypt(String encrypted) throws Exception
    {
        byte[] encryptedBytes = Base64.getDecoder().decode(encrypted.getBytes());
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, serverPrivateKey);
        return new String(cipher.doFinal(encryptedBytes));
    }

    private static KeyPair buildKeyPair() throws NoSuchAlgorithmException
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.genKeyPair();
    }

    private static PublicKey stringToKey(String serverPublicKey)
    {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(serverPublicKey));
        KeyFactory keyFactory;
        try
        {
            keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(x509EncodedKeySpec);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    static String getPubKey()
    {
        return Base64.getEncoder().encodeToString(serverPublicKey.getEncoded());
    }
}