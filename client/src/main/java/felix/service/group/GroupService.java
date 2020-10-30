package felix.service.group;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import felix.models.AesEncryptedMessage;
import felix.models.Group;
import felix.models.JwtToken;
import felix.models.User;
import felix.service.MainService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;

public class GroupService extends MainService implements IGroupService
{
    @Override
    public Group createGroup(String groupName) throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage aesEncryptedMessage = super.post("groups/", super.aesEncrypt(groupName), AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
        return new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), Group.class);
    }

    @Override
    public List<Group> getGroups() throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage aesEncryptedMessage = super.get("groups/", AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
        return new Gson().fromJson(super.aesDecrypt(aesEncryptedMessage.getMessage()), new TypeReference<List<Group>>(){}.getType());
    }

    @Override
    public void leaveGroup(Group group) throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage encryptedDisplayName = super.aesEncrypt(group.getId().toString());
        encryptedDisplayName.setMessage(encryptedDisplayName.getMessage().replace("/", "--DASH--"));
        AesEncryptedMessage aesEncryptedMessage = super.delete("groups/" + encryptedDisplayName.getMessage(), AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
    }

    @Override
    public void invite(UUID groupId, String inviteDisplayName) throws GeneralSecurityException, IOException, URISyntaxException
    {
        AesEncryptedMessage encryptedDisplayName = super.aesEncrypt(groupId.toString());
        encryptedDisplayName.setMessage(encryptedDisplayName.getMessage().replace("/", "--DASH--"));
        AesEncryptedMessage aesEncryptedMessage = super.post("groups/invites/" + encryptedDisplayName.getMessage(), super.aesEncrypt(inviteDisplayName), AesEncryptedMessage.class);
        super.refreshJwtToken(new JwtToken(aesEncryptedMessage.getToken()));
    }
}