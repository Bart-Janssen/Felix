package felix.client.service;

import com.google.gson.Gson;
import felix.client.main.FelixSession;
import felix.client.models.AesEncryptedMessage;
import felix.client.models.JwtToken;
import felix.client.service.system.AesEncryptionManager;
import java.security.GeneralSecurityException;

public abstract class EncryptionManager
{
    private static AesEncryptionManager aesEncryptionManager;

    public Boolean isInitialized()
    {
        return aesEncryptionManager != null;
    }

    protected <T> AesEncryptedMessage aesEncrypt(T t) throws GeneralSecurityException
    {
        return new AesEncryptedMessage(aesEncryptionManager.encrypt(new Gson().toJson(t)));
    }

    protected String aesDecrypt(String encryptedMessage) throws GeneralSecurityException
    {
        return aesEncryptionManager.decrypt(encryptedMessage);
    }

    protected void init(String decryptedAesKey)
    {
        aesEncryptionManager = new AesEncryptionManager(decryptedAesKey);
    }

    protected void kill()
    {
        aesEncryptionManager = null;
    }

    protected String refreshJwtToken(String message) throws GeneralSecurityException
    {
        AesEncryptedMessage aesEncryptedMessage = new Gson().fromJson(message, AesEncryptedMessage.class);
        FelixSession.getInstance().setToken(new JwtToken(this.aesDecrypt(aesEncryptedMessage.getToken())));
        return this.aesDecrypt(aesEncryptedMessage.getMessage());
    }

    protected void refreshJwtToken(JwtToken token) throws GeneralSecurityException
    {
        FelixSession.getInstance().setToken(new JwtToken(this.aesDecrypt(token.getToken())));
    }
}