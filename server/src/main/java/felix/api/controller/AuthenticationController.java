package felix.api.controller;

import felix.api.exceptions.BadRequestException;
import felix.api.models.*;
import felix.api.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import felix.api.configuration.JwtTokenGenerator;
import java.io.IOException;
import java.net.URISyntaxException;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController
{
    private final IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody User user) throws IOException, URISyntaxException
    {
        User authenticatedUser = userService.login(user);
        if (authenticatedUser != null)
        {
            JwtToken token = new JwtTokenGenerator().createJWT(authenticatedUser);
            WebSocket.addSession(authenticatedUser, token);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).build();
    }

    @PutMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String jwt) throws IOException, URISyntaxException
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
//        WebSocket.removeSession(jwt);
        userService.logout(user);
        user.setPassword("");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<JwtToken> register(@RequestBody User user) throws IOException, URISyntaxException
    {
        if (user.getPassword().length() < 8) throw new BadRequestException();
        User registeredUser = userService.register(user);
        JwtToken token = new JwtTokenGenerator().createJWT(registeredUser);
        WebSocket.addSession(registeredUser, token);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/2fa/enable")
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
    }
}