package felix.service.system;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaEncryptionManager
{
    private final static String RSA = "RSA";
    private final static int KEY_SIZE = 2048;
    private static KeyPair clientKeyPair;

    private RsaEncryptionManager(){}

    static
    {
        try
        {
            clientKeyPair = buildKeyPair();
        }
        catch (NoSuchAlgorithmException e)
        {
            clientKeyPair = null;
            e.printStackTrace();
        }
    }

    private static PublicKey clientPublicKey = clientKeyPair.getPublic();
    private static PrivateKey clientPrivateKey = clientKeyPair.getPrivate();

    private static PublicKey serverPublicKey;

    private static KeyPair buildKeyPair() throws NoSuchAlgorithmException
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator.genKeyPair();
    }

    public static String encrypt(String message) throws GeneralSecurityException
    {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes()));
    }

    public static String decrypt(String encrypted) throws GeneralSecurityException
    {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, clientPrivateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
    }

    public static String getPubKey()
    {
        return Base64.getEncoder().encodeToString(clientPublicKey.getEncoded());
    }

    public static void setPublicServerKey(String key)
    {
        try
        {
            serverPublicKey = KeyFactory.getInstance(RSA).generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode((key))));
        }
        catch (GeneralSecurityException e)
        {
            serverPublicKey = null;
            e.printStackTrace();
        }
    }
}