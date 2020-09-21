package felix.client.service.system;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaEncryptionManager
{
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
            e.printStackTrace();
        }
    }

    private static PublicKey clientPublicKey = clientKeyPair.getPublic();
    private static PrivateKey clientPrivateKey = clientKeyPair.getPrivate();

    private static PublicKey serverPublicKey;

    private static KeyPair buildKeyPair() throws NoSuchAlgorithmException
    {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    public static String encrypt(String message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes()));
    }

    public static String decrypt(String encrypted) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        byte[] d = Base64.getDecoder().decode(encrypted);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, clientPrivateKey);
        byte[] data = cipher.doFinal(d);
        return new String(data);
    }

    public static String getPubKey()
    {
        return Base64.getEncoder().encodeToString(clientPublicKey.getEncoded());
    }

    public static void setPublicServerKey(String serverPublicKey)
    {
        byte[] data = Base64.getDecoder().decode((serverPublicKey));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact;
        try
        {
            fact = KeyFactory.getInstance("RSA");
            RsaEncryptionManager.serverPublicKey = fact.generatePublic(spec);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e)
        {
            e.printStackTrace();
        }
    }

    public static PublicKey getPublicServerKey()
    {
        return serverPublicKey;
    }
}