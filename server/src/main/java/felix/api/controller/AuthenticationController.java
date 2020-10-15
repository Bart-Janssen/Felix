package felix.api.controller;

import com.google.gson.Gson;
import felix.api.exceptions.BadRequestException;
import felix.api.models.*;
import felix.api.service.EncryptionManager;
import felix.api.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import felix.api.configuration.JwtTokenGenerator;
import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController extends EncryptionManager
{
    private final IUserService userService;

    @GetMapping("/hai")
    public ResponseEntity logins()
    {
        int i = 0;
        for (UserSession s : WebSocket.getSessions().values())
        {
            AesEncryptedMessage p;
            try
            {
                i++;
                p = aesEncrypt(GetterType.SESSION_ID, s.getSession().getId(), WebSocketMessage.builder().message("Hello, msg from server: " + s.getUser().getDisplayName()).build());
                s.getSession().getAsyncRemote().sendText(new Gson().toJson(p));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok("yo " + i);
    }

    @PutMapping("/test/")
    public ResponseEntity<AesEncryptedMessage> test(@RequestHeader("Authorization") String jwt, @RequestBody AesEncryptedMessage aesEncryptedMessage) throws Exception
    {
        User user = super.aesDecrypt(GetterType.TOKEN, jwt, aesEncryptedMessage.getMessage(), User.class);
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, user));
    }

    @PostMapping("/login/")
    public ResponseEntity<AesEncryptedMessage> login(@RequestBody User user) throws Exception
    {
        Map<String, String> decryptedUserInfo = super.decryptRsaUser(user);
        user.setName(decryptedUserInfo.get("name"));
        user.setPassword(decryptedUserInfo.get("password"));
        user.setSessionId(decryptedUserInfo.get("sessionId"));
        User authenticatedUser = userService.login(user);
        if (authenticatedUser != null)
        {
            authenticatedUser.setSessionId(user.getSessionId());
            JwtToken token = new JwtTokenGenerator().createJWT(authenticatedUser);
            if (!WebSocket.addSession(authenticatedUser, token)) return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
            UserSession userSession = WebSocket.getSession(GetterType.DISPLAY_NAME, authenticatedUser.getDisplayName());
            for (User friend : userSession.getUser().getFriends())
            {
                UserSession friendSession = WebSocket.getSession(GetterType.DISPLAY_NAME, friend.getDisplayName());
                if (friendSession != null) friendSession.getSession().getAsyncRemote().sendText(new Gson().toJson(aesEncrypt(GetterType.SESSION_ID, friendSession.getSession().getId(), new WebSocketMessage(WebSocketMessageType.LOGIN, "", authenticatedUser.getDisplayName(), friend.getDisplayName(), null))));
            }
            return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, token.getToken(), null));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<AesEncryptedMessage> register(@RequestBody User user) throws Exception
    {
        Map<String, String> decryptedUserInfo = super.decryptRsaUser(user);
        user.setName(decryptedUserInfo.get("name"));
        user.setDisplayName(decryptedUserInfo.get("disp"));
        user.setPassword(decryptedUserInfo.get("password"));
        user.setSessionId(decryptedUserInfo.get("sessionId"));
        if (user.getPassword().length() < 8) throw new BadRequestException();
        User registeredUser = userService.register(user);
        if (registeredUser != null)
        {
            registeredUser.setSessionId(user.getSessionId());
            JwtToken token = new JwtTokenGenerator().createJWT(registeredUser);
            if (!WebSocket.addSession(registeredUser, token)) return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
            return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, token.getToken(), null));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        UserSession userSession = WebSocket.getSession(GetterType.DISPLAY_NAME, user.getDisplayName());
        for (User friend : userSession.getUser().getFriends())
        {
            UserSession friendSession = WebSocket.getSession(GetterType.DISPLAY_NAME, friend.getDisplayName());
            if (friendSession != null) friendSession.getSession().getAsyncRemote().sendText(new Gson().toJson(aesEncrypt(GetterType.SESSION_ID, friendSession.getSession().getId(), new WebSocketMessage(WebSocketMessageType.LOGOUT, "", user.getDisplayName(), friend.getDisplayName(), null))));
        }
        WebSocket.logout(userSession.getToken().getToken());
        userService.logout(userSession.getUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/2fa/enable")
    public ResponseEntity<AesEncryptedMessage> enable2FA(@RequestHeader("Authorization") String jwt) throws Exception
    {
        try
        {
            User user = new JwtTokenGenerator().decodeJWT(jwt);
            String qrCode = userService.enable2FA(user.getId(), user.getName());
            WebSocket.set2Fa(GetterType.DISPLAY_NAME, user.getDisplayName(), true);
            return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, qrCode));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    @DeleteMapping("/2fa/disable")
    public ResponseEntity<AesEncryptedMessage> disable2FA(@RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        userService.disable2FA(user.getId());
        WebSocket.set2Fa(GetterType.DISPLAY_NAME, user.getDisplayName(), false);
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, null));
    }
}