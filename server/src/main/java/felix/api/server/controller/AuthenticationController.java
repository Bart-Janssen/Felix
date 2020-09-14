package felix.api.server.controller;

import felix.api.server.exceptions.BadRequestException;
import felix.api.server.models.*;
import felix.api.server.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import felix.api.server.configuration.JwtTokenGenerator;
import java.io.IOException;
import java.net.URISyntaxException;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController
{
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody User user) throws IOException, URISyntaxException
    {
        User authenticatedUser = userService.login(user);
        if (authenticatedUser != null)
        {
            authenticatedUser.setPassword("");
            WebSocket.addSession(authenticatedUser);
            this.simpMessagingTemplate.convertAndSend("/socket-receiver/" + authenticatedUser.getDisplayName() + "/online-status",
                    new WebSocketItem<>(authenticatedUser, new Chat(authenticatedUser.getDisplayName() + " has logged in.", "Red")));
            return ResponseEntity.ok(new JwtTokenGenerator().createJWT(authenticatedUser));
        }
        return ResponseEntity.status(401).build();
    }

    @PutMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String jwt) throws IOException, URISyntaxException
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        WebSocket.removeSession(user);
        userService.logout(user);
        user.setPassword("");
        this.simpMessagingTemplate.convertAndSend("/socket-receiver/" + user.getDisplayName() + "/online-status",
                new WebSocketItem<>(user, new Chat(user.getDisplayName() + " has logged out.", "Red")));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<JwtToken> register(@RequestBody User user) throws IOException, URISyntaxException
    {
        if (user.getPassword().length() < 8) throw new BadRequestException();
        User registeredUser = userService.register(user);
        WebSocket.addSession(registeredUser);
        return ResponseEntity.ok(new JwtTokenGenerator().createJWT(registeredUser));
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