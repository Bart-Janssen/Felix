package felix.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import felix.api.configuration.AesEncryptionManager;
import felix.api.configuration.JwtTokenGenerator;
import felix.api.configuration.RsaEncryptionManager;
import felix.api.models.*;
import javax.websocket.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class WebSocket
{
    public static final String KEY = "publickey";

    static SessionMap getSessions()
    {
        return sessions;
    }

    @OnOpen
    public abstract void onWebSocketConnect(Session session) throws IOException;

    @OnMessage
    public abstract void onText(String message, Session session) throws GeneralSecurityException;

    @OnMessage
    public abstract void onPong(PongMessage pong);

    @OnClose
    public abstract void onClose(CloseReason reason, Session session);

    @OnError
    public abstract void onError(Throwable cause, Session session);

    private static SessionMap sessions = new SessionMap();

    public static byte[] getKeyFromSession(String token)
    {
        UserSession userSession = sessions.get(GetterType.TOKEN, token);
        return userSession == null ? null : userSession.getToken().getKey();
    }

    static Boolean addSession(User user, JwtToken token)
    {
        UUID pendingUUID = UUID.fromString(user.getEncryptedUUID());
        PendingSession pendingSession = sessions.getPending(pendingUUID);
        if (pendingSession == null) return false;
        sessions.addSession(user.getDisplayName(), new UserSession(token, user, pendingSession.getSession(), pendingSession.getAesKey()));
        sessions.removePendingSession(pendingUUID);
        return true;
    }

    protected void removeSession(String sessionId)
    {
        sessions.removeSession(sessionId);
    }

    private Boolean userHasToken(String token)
    {
        User decodedUser = new JwtTokenGenerator().decodeJWT(token);
        if (decodedUser == null) return false;
        UserSession sessionUser = sessions.get(GetterType.DISPLAY_NAME, decodedUser.getDisplayName());
        if (sessionUser == null) return false;
        JwtToken sessionToken = sessionUser.getToken();
        if (sessionToken == null) return false;
        if (!sessionToken.getToken().equals(token)) return false;
        return (decodedUser.getId().equals(sessionUser.getUser().getId())) & (decodedUser.getDisplayName().equals(sessionUser.getUser().getDisplayName()))
                & (decodedUser.getName().equals(sessionUser.getUser().getName())) & (decodedUser.isTwoFAEnabled() == sessionUser.getUser().isTwoFAEnabled());
    }

    protected Boolean validateToken(String token)
    {
        return this.userHasToken(token);
    }

    protected UUID putPendingSession(Session session, String clientPublicKey, String aesKey)
    {
        return sessions.putPendingSession(session, clientPublicKey, aesKey);
    }

    static Map<String, String> decryptRsaUser(User user) throws Exception
    {
        Map<String, String> decryptedUserInfo = new HashMap<>();
        decryptedUserInfo.put("name", RsaEncryptionManager.decrypt(user.getName()));
        decryptedUserInfo.put("password", RsaEncryptionManager.decrypt(user.getPassword()));
        decryptedUserInfo.put("uuid", RsaEncryptionManager.decrypt(user.getEncryptedUUID()));
        return decryptedUserInfo;
    }

    protected static <T> AesEncryptedMessage encrypt(GetterType type, String key, T object) throws GeneralSecurityException
    {
        return new AesEncryptedMessage(AesEncryptionManager.encrypt(sessions.get(type, key).getAesKey(), new Gson().toJson(object)));
    }

    protected <T> T decrypt(GetterType getterType, String key, String encryptedMessage, Type type)
    {
        try
        {
            UserSession userSession = sessions.get(getterType, key);
            String decryptedMessage = AesEncryptionManager.decrypt(userSession.getAesKey(), encryptedMessage);
            return decryptedMessage == null ? null : new ObjectMapper().readValue(decryptedMessage, this.getType(type));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
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