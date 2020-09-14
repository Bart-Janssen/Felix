package felix.api.server.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import felix.api.server.exceptions.NotAuthorizedException;
import felix.api.server.service.user.IUserService;
import felix.api.server.models.Chat;
import felix.api.server.models.Event;
import felix.api.server.models.User;
import lombok.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import felix.api.server.configuration.JwtTokenGenerator;
import felix.api.server.service.event.IEventService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/player")
public class PlayerController
{
    private final IUserService userService;

    ///////////////////////////TESTS//////////////////////////////////////
    private final IEventService eventService;

    @GetMapping("/test")
    public ResponseEntity test() throws Exception
    {
        URIBuilder builder = new URIBuilder(); //trademarketv2-service-event : 80
        builder.setScheme("http").setHost("trademarketv2-service-event").setPath("/events/").setPort(80);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(builder.build());
        httpGet.addHeader("content-type", "application/json");
        String jsonResponse = EntityUtils.toString(httpClient.execute(httpGet).getEntity());
        List<Event> events = new Gson().fromJson(jsonResponse, new TypeToken<List<Event>>(){}.getType());
        httpClient.close();
        return ResponseEntity.ok(new Gson().toJson(events));
    }

    @GetMapping("/post")
    public ResponseEntity post() throws Exception
    {
        throw new NotAuthorizedException();
//        return ResponseEntity.ok("post");
    }
    ///////////////////////////TESTS//////////////////////////////////////

    @DeleteMapping("/")
    public ResponseEntity deleteAccount(@RequestHeader("Authorization") String jwt)
    {
        userService.deleteAccount(new JwtTokenGenerator().decodeJWT(jwt).getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<List<Chat>> getPlayerInfo(@RequestHeader("Authorization") String jwt) throws IOException, URISyntaxException
    {
        User user = new JwtTokenGenerator().decodeJWT(jwt);
        List<Chat> messages = new ArrayList<>();
        messages.add(Chat.builder().guiMessage("Welcome to the game.").color("blue").build());
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/friends/all")
    public ResponseEntity<List<User>> getFriendsByUserId(@RequestHeader("Authorization") String jwt) throws IOException, URISyntaxException
    {
        List<User> friends = userService.getFriendsByUserId(new JwtTokenGenerator().decodeJWT(jwt).getId());
        return ResponseEntity.ok(friends);
    }

    @DeleteMapping("/friends/{friendDisplayName}")
    public ResponseEntity removeFriend(@RequestHeader("Authorization") String jwt, @PathVariable String friendDisplayName) throws IOException, URISyntaxException
    {
        userService.removeFriend(friendDisplayName, new JwtTokenGenerator().decodeJWT(jwt).getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/friends/invite")
    public ResponseEntity sendFriendRequest(@RequestHeader("Authorization") String jwt, @RequestBody String displayName) throws IOException, URISyntaxException
    {
        userService.sendFriendInvite(displayName, new JwtTokenGenerator().decodeJWT(jwt).getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/friends/invites/outgoing")
    public ResponseEntity<List<String>> getOutgoingPendingInvites(@RequestHeader("Authorization") String jwt) throws IOException, URISyntaxException
    {
        return ResponseEntity.ok(userService.getOutgoingPendingInvites(new JwtTokenGenerator().decodeJWT(jwt).getId()));
    }

    @GetMapping("/friends/invites/incoming")
    public ResponseEntity<List<String>> getIncomingPendingInvites(@RequestHeader("Authorization") String jwt) throws IOException, URISyntaxException
    {
        return ResponseEntity.ok(userService.getIncomingPendingInvites(new JwtTokenGenerator().decodeJWT(jwt).getId()));
    }

    @DeleteMapping("/friends/invites/outgoing/cancel/{invite}")
    public ResponseEntity<List<String>> cancelInvite(@RequestHeader("Authorization") String jwt, @PathVariable String invite) throws IOException, URISyntaxException
    {
        userService.cancelInvite(invite, new JwtTokenGenerator().decodeJWT(jwt).getId());
        return ResponseEntity.ok(userService.getOutgoingPendingInvites(new JwtTokenGenerator().decodeJWT(jwt).getId()));
    }

    @PutMapping("/friends/invites/incoming/accept")
    public ResponseEntity<List<String>> acceptInvite(@RequestHeader("Authorization") String jwt, @RequestBody String invite) throws IOException, URISyntaxException
    {
        userService.acceptInvite(invite, new JwtTokenGenerator().decodeJWT(jwt).getId());
        return ResponseEntity.ok(userService.getIncomingPendingInvites(new JwtTokenGenerator().decodeJWT(jwt).getId()));
    }

    @DeleteMapping("/friends/invites/incoming/decline/{invite}")
    public ResponseEntity<List<String>> declineInvite(@RequestHeader("Authorization") String jwt, @PathVariable String invite) throws IOException, URISyntaxException
    {
        userService.declineInvite(invite, new JwtTokenGenerator().decodeJWT(jwt).getId());
        return ResponseEntity.ok(userService.getIncomingPendingInvites(new JwtTokenGenerator().decodeJWT(jwt).getId()));
    }
}