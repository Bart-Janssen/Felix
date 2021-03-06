package felix.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import felix.api.configuration.AesEncryptionManager;
import felix.api.configuration.RsaEncryptionManager;
import felix.api.controller.WebSocket;
import felix.api.models.*;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.*;

public abstract class EncryptionManager
{
    protected static final String DASH = "--DASH--";

    protected static <T> AesEncryptedMessage aesEncrypt(GetterType type, String key, T object) throws GeneralSecurityException
    {
        UserSession session = WebSocket.getSession(type, key);
        if (object == null) return new AesEncryptedMessage(AesEncryptionManager.encrypt(session.getAesKey(), WebSocket.updateJwtToken(type, key).getToken().getToken()), null);
        return new AesEncryptedMessage(AesEncryptionManager.encrypt(session.getAesKey(), WebSocket.updateJwtToken(type, key).getToken().getToken()), AesEncryptionManager.encrypt(session.getAesKey(), new Gson().toJson(object)));
    }

    protected <T> T aesDecrypt(GetterType getterType, String key, String encryptedMessage, Type type) throws Exception
    {
        String decryptedMessage = AesEncryptionManager.decrypt(WebSocket.getSession(getterType, key).getAesKey(), encryptedMessage);
        return new ObjectMapper().readValue(decryptedMessage, this.getType(type));
    }

    protected Map<String, String> decryptRsaUser(AuthenticationUser user) throws GeneralSecurityException
    {
        Map<String, String> decryptedUserInfo = new HashMap<>();
        decryptedUserInfo.put("name", RsaEncryptionManager.decrypt(user.getName()));
        if (user.getDisplayName() != null) decryptedUserInfo.put("disp", RsaEncryptionManager.decrypt(user.getDisplayName()));
        decryptedUserInfo.put("password", RsaEncryptionManager.decrypt(user.getPassword()));
        decryptedUserInfo.put("sessionId", RsaEncryptionManager.decrypt(user.getSessionId()));
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

    protected Licence decryptRsaLicence(Licence licence) throws GeneralSecurityException
    {
        List<String> macs = new ArrayList<>();
        for (String encryptedMac : licence.getMacs())
        {
            macs.add(RsaEncryptionManager.decrypt(encryptedMac));
        }
        return Licence.builder()
                .token(UUID.fromString(RsaEncryptionManager.decrypt(licence.getEncryptedToken())))
                .sign(licence.getSign())
                .macs(macs)
                .build();
    }
}