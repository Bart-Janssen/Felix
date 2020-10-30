package felix.service.chat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import felix.models.AesEncryptedMessage;
import felix.models.Chat;
import felix.models.JwtToken;
import felix.service.MainService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;

public class ChatService extends MainService implements IChatService
{
    @Override
    public List<Chat> getAll(String displayName) throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage encryptedDisplayName = super.aesEncrypt(displayName);
        encryptedDisplayName.setMessage(encryptedDisplayName.getMessage().replace("/", "--DASH--"));
        AesEncryptedMessage aesEncryptedMessage = super.get("chats/" + encryptedDisplayName.getMessage(), AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
        return new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), new TypeReference<List<Chat>>(){}.getType());
    }

    @Override
    public List<Chat> getAllGroup(UUID groupId) throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage encryptedDisplayName = super.aesEncrypt(groupId.toString());
        encryptedDisplayName.setMessage(encryptedDisplayName.getMessage().replace("/", "--DASH--"));
        AesEncryptedMessage aesEncryptedMessage = super.get("chats/group/" + encryptedDisplayName.getMessage(), AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
        return new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), new TypeReference<List<Chat>>(){}.getType());
    }
}