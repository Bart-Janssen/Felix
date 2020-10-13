package felix.service.friend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import felix.models.AesEncryptedMessage;
import felix.models.JwtToken;
import felix.models.User;
import felix.service.MainService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

public class FriendService extends MainService implements IFriendService
{
    @Override
    public User sendPendingOutgoingInvite(String displayName) throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage aesEncryptedMessage = super.post("friends/invites/", super.aesEncrypt(displayName), AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
        return new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), User.class);
    }

    @Override
    public List<String> getPendingOutgoingInvites() throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage aesEncryptedMessage = super.get("friends/invites/outgoing/", AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
        return new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), new TypeReference<List<String>>(){}.getType());
    }

    @Override
    public List<String> getFriendInvites() throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage aesEncryptedMessage = super.get("friends/invites/incoming/", AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
        return new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), new TypeReference<List<String>>(){}.getType());
    }

    @Override
    public String acceptInvite(String friendDisplayName) throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage aesEncryptedMessage = super.put("friends/invites/incoming/accept/", super.aesEncrypt(friendDisplayName), AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
        return new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), String.class);
    }

    @Override
    public void declineInvite(String friendDisplayName) throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage encryptedDisplayName = super.aesEncrypt(friendDisplayName);
        encryptedDisplayName.setMessage(encryptedDisplayName.getMessage().replace("/", "--DASH--"));
        AesEncryptedMessage aesEncryptedMessage = super.delete("friends/invites/incoming/decline/" + encryptedDisplayName.getMessage(), AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
    }

    @Override
    public void cancelInvite(String friendDisplayName) throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage encryptedDisplayName = super.aesEncrypt(friendDisplayName);
        encryptedDisplayName.setMessage(encryptedDisplayName.getMessage().replace("/", "--DASH--"));
        AesEncryptedMessage aesEncryptedMessage = super.delete("friends/invites/outgoing/cancel/" + encryptedDisplayName.getMessage(), AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
    }

    @Override
    public List<String> getFriends() throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage aesEncryptedMessage = super.get("friends/", AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
        return new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), new TypeReference<List<String>>(){}.getType());
    }

    @Override
    public void remove(String friendDisplayName) throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage encryptedDisplayName = super.aesEncrypt(friendDisplayName);
        encryptedDisplayName.setMessage(encryptedDisplayName.getMessage().replace("/", "--DASH--"));
        AesEncryptedMessage aesEncryptedMessage = super.delete("friends/" + encryptedDisplayName.getMessage(), AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
    }
}