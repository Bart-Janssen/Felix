package felix.api.configuration;

import com.google.gson.Gson;
import javafx.collections.FXCollections;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@ServerEndpoint(value = "/server/")
public class Connection
{
//    private static final HashMap<Session, WebSocketMessage> sessionsAndUser = new HashMap<>();
//    private static final HashMap<User, User> pendingInvitesUserTo_Sender = new HashMap<>();
//    private static final ArrayList<Group> groups = new ArrayList<>();

    @OnOpen
    public void onConnect(Session session)
    {
        System.out.println("[Connected] SessionID: " + session.getId());
    }

    @OnMessage
    public void onText(String message, Session session)
    {
        System.out.println("[on msg odin!] " + message);
        session.getAsyncRemote().sendText("Yay response from server");
    }

    @OnClose
    public void onClose(CloseReason reason, Session session)
    {
        System.out.println("[Session ID] : " + session.getId() + " [Socket Closed]: " + reason);
    }

    @OnError
    public void onError(Throwable cause, Session session)
    {
        System.out.println("[Session ID] : " + session.getId() + "[ERROR]: ");
        //cause.printStackTrace(System.err);
    }
}