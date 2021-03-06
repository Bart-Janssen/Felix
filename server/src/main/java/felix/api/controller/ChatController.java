package felix.api.controller;

import felix.api.configuration.JwtTokenGenerator;
import felix.api.models.*;
import felix.api.service.EncryptionManager;
import felix.api.service.chat.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatController extends EncryptionManager
{
    private final IChatService chatService;

    @GetMapping("/{friendDisplayName}")
    public ResponseEntity<AesEncryptedMessage> getAll(@RequestHeader("Authorization") String jwt, @PathVariable("friendDisplayName") String friendDisplayName) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        friendDisplayName = super.aesDecrypt(GetterType.TOKEN, jwt, friendDisplayName.replace(DASH, "/"), String.class);
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, chatService.getAll(user.getId(), friendDisplayName)));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<AesEncryptedMessage> getAllGroup(@RequestHeader("Authorization") String jwt, @PathVariable("groupId") String groupId) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        groupId = super.aesDecrypt(GetterType.TOKEN, jwt, groupId.replace(DASH, "/"), String.class);
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, chatService.getAllGroup(user.getId(), UUID.fromString(groupId))));
    }
}