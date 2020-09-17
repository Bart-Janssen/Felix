package felix.api.configuration;

import felix.api.controller.WebSocket;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/server/{" + WebSocket.TOKEN + "}")
public class WebSocketConnection extends WebSocket
{
    @Override
    public void onWebSocketConnect(Session session) throws IOException
    {
        String token = super.parseToken(session.getPathParameters());
        if (token == null) session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, " Token is not valid"));
        System.out.println("[Connected] SessionID: " + session.getId());
    }

    @Override
    public void onText(String message, Session session)
    {
        System.out.println("[on msg odin!] " + message);
        session.getAsyncRemote().sendText("Yay response from server");
    }

    @Override
    public void onClose(CloseReason reason, Session session)
    {
        super.removeSession(super.parseToken(session.getPathParameters()));
        System.out.println("[Session ID] : " + session.getId() + " [Socket Closed]: " + reason);
    }

    @Override
    public void onError(Throwable cause, Session session)
    {
        System.out.println("[Session ID] : " + session.getId() + "[ERROR]: ");
        cause.printStackTrace(System.err);
    }
}