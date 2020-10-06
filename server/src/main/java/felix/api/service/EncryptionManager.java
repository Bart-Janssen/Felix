package felix.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import felix.api.configuration.AesEncryptionManager;
import felix.api.configuration.RsaEncryptionManager;
import felix.api.controller.WebSocket;
import felix.api.models.AesEncryptedMessage;
import felix.api.models.GetterType;
import felix.api.models.User;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public abstract class EncryptionManager
{
    protected <T> AesEncryptedMessage aesEncrypt(GetterType type, String key, T object) throws GeneralSecurityException
    {
        return new AesEncryptedMessage(AesEncryptionManager.encrypt(WebSocket.getSession(type, key).getAesKey(), new Gson().toJson(object)));
    }

    protected <T> T aesDecrypt(GetterType getterType, String key, String encryptedMessage, Type type)
    {
        try
        {
            String decryptedMessage = AesEncryptionManager.decrypt(WebSocket.getSession(getterType, key).getAesKey(), encryptedMessage);
            return new ObjectMapper().readValue(decryptedMessage, this.getType(type));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    protected Map<String, String> decryptRsaUser(User user) throws Exception
    {
        Map<String, String> decryptedUserInfo = new HashMap<>();
        decryptedUserInfo.put("name", RsaEncryptionManager.decrypt(user.getName()));
        if (user.getDisplayName() != null) decryptedUserInfo.put("disp", RsaEncryptionManager.decrypt(user.getDisplayName()));
        decryptedUserInfo.put("password", RsaEncryptionManager.decrypt(user.getPassword()));
        decryptedUserInfo.put("uuid", RsaEncryptionManager.decrypt(user.getEncryptedUUID()));
        return decryptedUserInfo;
    }

    private <E> TypeReference<E> getType(Type type)
    {
        return new TypeReference<E>()
        {
            @Override
            public Type getType()
            {
                return type;
            }
        };
    }
}