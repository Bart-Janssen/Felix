package felix.api.controller;

import felix.api.configuration.JwtTokenGenerator;
import felix.api.exceptions.BadRequestException;
import felix.api.models.AesEncryptedMessage;
import felix.api.models.GetterType;
import felix.api.models.User;
import felix.api.service.EncryptionManager;
import felix.api.service.friend.IFriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController extends EncryptionManager
{
    private final IFriendService friendService;

    @GetMapping("/")
    public ResponseEntity<AesEncryptedMessage> getFriendsByUserId(@RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, this.friendService.getFriends(user.getId())));
    }

    @DeleteMapping("/{friendDisplayName}")
    public ResponseEntity removeFriend(@RequestHeader("Authorization") String jwt, @PathVariable String friendDisplayName) throws Exception
    {
        friendDisplayName = super.aesDecrypt(GetterType.TOKEN, jwt, friendDisplayName.replace("--DASH--", "/"), String.class);
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        this.friendService.removeFriend(friendDisplayName, user.getId());
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, null));
    }

    @PostMapping("/invites")
    public ResponseEntity<AesEncryptedMessage> sendFriendInvite(@RequestHeader("Authorization") String jwt, @RequestBody AesEncryptedMessage aesEncryptedMessage) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        String displayName = super.aesDecrypt(GetterType.TOKEN, jwt, aesEncryptedMessage.getMessage(), String.class);
        if (user.getDisplayName().equals(displayName)) throw new BadRequestException();
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, this.friendService.sendFriendInvite(displayName, user.getId())));
    }

    @GetMapping("/invites/outgoing")
    public ResponseEntity<AesEncryptedMessage> getOutgoingPendingInvites(@RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        List<String> friendDisplayNames = this.friendService.getOutgoingPendingInvites(user.getId());
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, friendDisplayNames));
    }

    @GetMapping("/invites/incoming")
    public ResponseEntity<AesEncryptedMessage> getIncomingPendingInvites(@RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        List<User> friendInvitess = this.friendService.getIncomingPendingInvites(user.getId());
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, friendInvitess));
    }

    @PutMapping("/invites/incoming/accept")
    public ResponseEntity<AesEncryptedMessage> acceptInvite(@RequestHeader("Authorization") String jwt, @RequestBody AesEncryptedMessage aesEncryptedMessage) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        String friendInviteDisplayName = super.aesDecrypt(GetterType.TOKEN, jwt, aesEncryptedMessage.getMessage(), String.class);
        String friendDisplayName = this.friendService.acceptInvite(friendInviteDisplayName, user.getId());
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, friendDisplayName));
    }

    @DeleteMapping("/invites/incoming/decline/{friendDisplayName}")
    public ResponseEntity<AesEncryptedMessage> declineInvite(@RequestHeader("Authorization") String jwt, @PathVariable String friendDisplayName) throws Exception
    {
        friendDisplayName = super.aesDecrypt(GetterType.TOKEN, jwt, friendDisplayName.replace("--DASH--", "/"), String.class);
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        this.friendService.declineInvite(friendDisplayName, user.getId());
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, null));
    }

    @DeleteMapping("/invites/outgoing/cancel/{friendDisplayName}")
    public ResponseEntity<AesEncryptedMessage> cancelInvite(@RequestHeader("Authorization") String jwt, @PathVariable String friendDisplayName) throws Exception
    {
        friendDisplayName = super.aesDecrypt(GetterType.TOKEN, jwt, friendDisplayName.replace("--DASH--", "/"), String.class);
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        this.friendService.cancelInvite(friendDisplayName, user.getId());
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, null));
    }
}