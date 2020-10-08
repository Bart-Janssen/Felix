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
                p = super.aesEncrypt(GetterType.SESSION_ID, s.getSession().getId(), WebSocketMessage.builder().message("Hello, msg from server: " + s.getUser().getDisplayName()).build());
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
        System.out.println(new Gson().toJson(user));
        return ResponseEntity.ok(super.aesEncrypt(GetterType.TOKEN, jwt, user));
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
            return ResponseEntity.ok(super.aesEncrypt(GetterType.TOKEN, token.getToken(), null));
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
            return ResponseEntity.ok(super.aesEncrypt(GetterType.TOKEN, token.getToken(), null));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        WebSocket.logout(jwt);
//        userService.logout(user); //todo set online status
        return ResponseEntity.ok().build();
    }

    /*@PostMapping("/2fa/enable")
    public ResponseEntity<String> enable2FA(@RequestHeader("Authorization") String jwt) throws IOException, URISyntaxException
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        return ResponseEntity.ok(new String(userService.enable2FA(user.getId(), user.getName())));
    }

    @DeleteMapping("/2fa/disable")
    public ResponseEntity disable2FA(@RequestHeader("Authorization") String jwt) throws IOException, URISyntaxException
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        userService.disable2FA(user.getId());
        return ResponseEntity.ok().build();
    }*/
}