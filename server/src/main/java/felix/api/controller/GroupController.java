package felix.api.controller;

import felix.api.configuration.JwtTokenGenerator;
import felix.api.exceptions.BadRequestException;
import felix.api.models.AesEncryptedMessage;
import felix.api.models.GetterType;
import felix.api.models.User;
import felix.api.service.EncryptionManager;
import felix.api.service.group.IGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController extends EncryptionManager
{
    private final IGroupService groupService;

    @PostMapping("/")
    public ResponseEntity<AesEncryptedMessage> createGroup(@RequestHeader("Authorization") String jwt, @RequestBody AesEncryptedMessage aesEncryptedMessage) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        String groupName = super.aesDecrypt(GetterType.TOKEN, jwt, aesEncryptedMessage.getMessage(), String.class);
        if (groupName.length() == 0 || groupName.length() > 12) throw new BadRequestException();
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, this.groupService.createGroup(user.getId(), groupName)));
    }

    @GetMapping("/")
    public ResponseEntity<AesEncryptedMessage> getGroups(@RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, this.groupService.getGroups(user.getId())));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<AesEncryptedMessage> leaveGroup(@RequestHeader("Authorization") String jwt, @PathVariable String groupId) throws Exception
    {
        groupId = super.aesDecrypt(GetterType.TOKEN, jwt, groupId.replace(DASH, "/"), String.class);
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        this.groupService.leaveGroup(UUID.fromString(groupId), user.getId());
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, null));
    }

    @PostMapping("/invites/{groupId}")
    public ResponseEntity<AesEncryptedMessage> inviteGroup(@RequestHeader("Authorization") String jwt, @RequestBody AesEncryptedMessage aesEncryptedMessage, @PathVariable String groupId) throws Exception
    {
        String inviteDisplayName = super.aesDecrypt(GetterType.TOKEN, jwt, aesEncryptedMessage.getMessage(), String.class);
        groupId = super.aesDecrypt(GetterType.TOKEN, jwt, groupId.replace(DASH, "/"), String.class);
        User user = this.groupService.invite(UUID.fromString(groupId), inviteDisplayName);
        WebSocket.addGroupsIfAbsent(user);
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, null));
    }
}