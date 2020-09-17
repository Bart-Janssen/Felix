package felix.client.controller;

import felix.client.models.View;

import javax.websocket.*;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

@ClientEndpoint
public class EventClientSocket extends MainController
{
    @Override
    public void initialize(URL location, ResourceBundle resources) {}

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
        System.out.println("[Closed]: " + new Date() + "; " + reason);
        super.stopHeartbeat();
        super.openNewView(View.LOGIN);
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace();
        System.out.println("[ERROR]: " + cause);
        System.out.println("Error");
    }
}