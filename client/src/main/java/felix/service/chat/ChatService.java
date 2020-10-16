package felix.service.chat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import felix.models.AesEncryptedMessage;
import felix.models.JwtToken;
import felix.service.MainService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

public class ChatService extends MainService implements IChatService
{
    @Override
    public List<String> getAll(String displayName) throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage encryptedDisplayName = super.aesEncrypt(displayName);
        encryptedDisplayName.setMessage(encryptedDisplayName.getMessage().replace("/", "--DASH--"));
        AesEncryptedMessage aesEncryptedMessage = super.get("chats/" + encryptedDisplayName.getMessage(), AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
        return new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), new TypeReference<List<String>>(){}.getType());
    }
}