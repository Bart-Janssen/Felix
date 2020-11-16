package felix.api.configuration;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

public class AesEncryptionManager
{
    private static final int KEY_SIZE = 256;
    private static final String AES = "AES";
    private static final int IV_SIZE = 12;
    private static final int GCM_AUTHENTICATION_TAG = 128;
    private static final String CIPHER_INSTANCE = "AES/GCM/NoPadding";

    private AesEncryptionManager(){}

    public static String encrypt(String key, String plainText) throws GeneralSecurityException
    {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_AUTHENTICATION_TAG, iv);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(key), AES), parameterSpec);
        byte[] encryptedText = cipher.doFinal(plainText.getBytes());
        byte[] combinedEncryptedTextWithIv = new byte[IV_SIZE + encryptedText.length];
        System.arraycopy(iv, 0, combinedEncryptedTextWithIv, 0, IV_SIZE);
        System.arraycopy(encryptedText, 0, combinedEncryptedTextWithIv, IV_SIZE, encryptedText.length);
        return Base64.getEncoder().encodeToString(combinedEncryptedTextWithIv);
    }

    public static String decrypt(String key, String combinedEncryptedTextWithIv) throws GeneralSecurityException
    {
        AlgorithmParameterSpec gcmIv = new GCMParameterSpec(GCM_AUTHENTICATION_TAG,
                Base64.getDecoder().decode(combinedEncryptedTextWithIv), 0, IV_SIZE);
        byte[] combinedEncryptedTextWithIvBytes = Base64.getDecoder().decode(combinedEncryptedTextWithIv);
        int encryptedSize = combinedEncryptedTextWithIvBytes.length - IV_SIZE;
        byte[] encryptedText = new byte[encryptedSize];
        System.arraycopy(combinedEncryptedTextWithIvBytes, IV_SIZE, encryptedText, 0, encryptedSize);
        Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(key), AES), gcmIv);
        return new String(cipher.doFinal(encryptedText));
    }

    static String generateKey() throws GeneralSecurityException
    {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(KEY_SIZE);
        return Base64.getEncoder().encodeToString(keyGenerator.generateKey().getEncoded());
    }
}