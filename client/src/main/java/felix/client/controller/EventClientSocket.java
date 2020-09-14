package felix.client.controller;

import javax.websocket.*;
import java.util.Date;

@ClientEndpoint
public class EventClientSocket
{
    @OnOpen
    public void onWebSocketConnect()
    {
        System.out.println("[Connected]");
    }

    @OnMessage
    public void onWebSocketText(String message)
    {
        System.out.println(message);
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason)
    {
        System.out.println("[Closed]: " + new Date() + reason);
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace();
        System.out.println("[ERROR]: " + cause);
        System.out.println("Error");
    }
}