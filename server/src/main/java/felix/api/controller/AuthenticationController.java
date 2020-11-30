package felix.api.controller;

import com.google.gson.Gson;
import felix.api.exceptions.BadRequestException;
import felix.api.models.*;
import felix.api.service.EncryptionManager;
import felix.api.service.licence.ILicenceService;
import felix.api.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import felix.api.configuration.JwtTokenGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController extends EncryptionManager
{
    private final ILicenceService licenceService;
    private final IUserService userService;

    @PostMapping("/login/")
    public ResponseEntity<AesEncryptedMessage> login(@RequestBody AuthenticationUser authenticationUser) throws Exception
    {
        Map<String, String> decryptedUserInfo = super.decryptRsaUser(authenticationUser);
        User authenticatedUser = userService.login(User.builder().name(decryptedUserInfo.get("name")).password(decryptedUserInfo.get("password")).build());
        if (authenticatedUser != null)
        {
            if (WebSocket.getSession(GetterType.DISPLAY_NAME, authenticatedUser.getDisplayName()) != null) return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
            authenticatedUser.setSessionId(decryptedUserInfo.get("sessionId"));
            JwtToken token = new JwtTokenGenerator().createJWT(authenticatedUser);
            if (!WebSocket.addSession(authenticatedUser, token)) return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
            UserSession userSession = WebSocket.getSession(GetterType.DISPLAY_NAME, authenticatedUser.getDisplayName());
            for (User friend : userSession.getUser().getFriends())
            {
                UserSession friendSession = WebSocket.getSession(GetterType.DISPLAY_NAME, friend.getDisplayName());
                if (friendSession != null) friendSession.getSession().getAsyncRemote().sendText(new Gson().toJson(aesEncrypt(GetterType.SESSION_ID, friendSession.getSession().getId(), new WebSocketMessage(WebSocketMessageType.LOGIN, "", authenticatedUser.getDisplayName(), friend.getDisplayName(), false, null))));
            }
            return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, token.getToken(), null));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<AesEncryptedMessage> register(@RequestBody AuthenticationUser authenticationUser) throws Exception
    {
        Map<String, String> decryptedUserInfo = super.decryptRsaUser(authenticationUser);

        if (authenticationUser.getPassword().length() < 8) throw new BadRequestException();
        User registeredUser = userService.register(User.builder().displayName(decryptedUserInfo.get("disp")).password(decryptedUserInfo.get("password")).name(decryptedUserInfo.get("name")).build());
        if (registeredUser != null)
        {
            registeredUser.setSessionId(decryptedUserInfo.get("sessionId"));
            JwtToken token = new JwtTokenGenerator().createJWT(registeredUser);
            if (!WebSocket.addSession(registeredUser, token)) return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
            return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, token.getToken(), null));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        UserSession userSession = WebSocket.getSession(GetterType.DISPLAY_NAME, user.getDisplayName());
        for (User friend : userSession.getUser().getFriends())
        {
            UserSession friendSession = WebSocket.getSession(GetterType.DISPLAY_NAME, friend.getDisplayName());
            if (friendSession != null) friendSession.getSession().getAsyncRemote().sendText(new Gson().toJson(aesEncrypt(GetterType.SESSION_ID, friendSession.getSession().getId(), new WebSocketMessage(WebSocketMessageType.LOGOUT, "", user.getDisplayName(), friend.getDisplayName(), false, null))));
        }
        WebSocket.logout(userSession.getToken().getToken());
        userService.logout(userSession.getUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/2fa/enable")
    public ResponseEntity<AesEncryptedMessage> enable2FA(@RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        String qrCode = userService.enable2FA(user.getId(), user.getName());
        WebSocket.set2Fa(GetterType.DISPLAY_NAME, user.getDisplayName(), true);
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, qrCode));
    }

    @DeleteMapping("/2fa/disable")
    public ResponseEntity<AesEncryptedMessage> disable2FA(@RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        userService.disable2FA(user.getId());
        WebSocket.set2Fa(GetterType.DISPLAY_NAME, user.getDisplayName(), false);
        return ResponseEntity.ok(aesEncrypt(GetterType.TOKEN, jwt, null));
    }

    @DeleteMapping("/delete/")
    public ResponseEntity<Void> deleteAccount(@RequestHeader("Authorization") String jwt) throws Exception
    {
        UserSession userSession = WebSocket.getSession(GetterType.TOKEN, jwt);
        for (User friend : userSession.getUser().getFriends())
        {
            UserSession friendSession = WebSocket.getSession(GetterType.DISPLAY_NAME, friend.getDisplayName());
            if (friendSession != null) friendSession.getSession().getAsyncRemote().sendText(new Gson().toJson(aesEncrypt(GetterType.SESSION_ID, friendSession.getSession().getId(), new WebSocketMessage(WebSocketMessageType.LOGOUT, "", userSession.getUser().getDisplayName(), friend.getDisplayName(), false, null))));
        }
        WebSocket.logout(userSession.getToken().getToken());
        this.userService.deleteAccount(userSession.getUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/activation/check/")
    public ResponseEntity<String> checkActivation(@RequestBody Licence licence) throws Exception
    {
        Licence decryptRsaLicence = super.decryptRsaLicence(licence);
        System.out.println(new Gson().toJson(decryptRsaLicence));

        if (this.licenceService.check(decryptRsaLicence))
        {
            System.out.println("its all good");
            return ResponseEntity.ok("");
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/activation/")
    public ResponseEntity<String> activate(@RequestBody Licence licence) throws Exception
    {
        Licence decryptRsaLicence = super.decryptRsaLicence(licence);
        if (decryptRsaLicence.getMacs().size() == 0) return ResponseEntity.status(400).build();
        if (this.licenceService.activate(decryptRsaLicence)) return ResponseEntity.ok().build();
        return ResponseEntity.status(400).body("This licence is not valid or already activated.");
    }

    @GetMapping("/activation/generate/")
    public ResponseEntity<Licence> genLicence() throws Exception
    {
        OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\Frb999\\Documents\\licence.lic"));
        outputStream.write(this.licenceService.generate().toByteArray());
        outputStream.close();

        return ResponseEntity.ok(this.licenceService.generate());
    }
}