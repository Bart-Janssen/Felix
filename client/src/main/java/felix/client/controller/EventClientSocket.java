package felix.client.controller;

import com.google.gson.Gson;
import felix.client.main.FelixSession;
import felix.client.models.*;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

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

    private void initializeFelixSession(String encrypted)
    {
        InitWebSocketMessage initWebSocketMessage = new Gson().fromJson(encrypted, InitWebSocketMessage.class);
        FelixSession.getInstance().initialize(initWebSocketMessage);
    }

    @OnMessage
    public void onWebSocketText(String message) throws Exception
    {
        if (!FelixSession.getInstance().isInitialized())
        {
            this.initializeFelixSession(message);
            return;
        }
        System.out.println("AES Encrypted msg: " + message);
        message = super.refreshJwtToken(message);
        System.out.println("AES Decrypted msg: " + message);
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason)
    {
        System.out.println("[Closed]: " + new Date() + "; " + reason);
        FelixSession.getInstance().stopHeartBeat();
        super.openNewView(View.LOGIN);
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
//        cause.printStackTrace();
        System.out.println("[ERROR]: " + cause);
        System.out.println("Error");
    }
}