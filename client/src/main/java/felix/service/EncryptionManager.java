package felix.service;

import com.google.gson.Gson;
import felix.main.FelixSession;
import felix.models.AesEncryptedMessage;
import felix.models.JwtToken;
import felix.service.system.AesEncryptionManager;

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

    protected String refreshJwtTokenAndDecrypt(String message) throws GeneralSecurityException
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