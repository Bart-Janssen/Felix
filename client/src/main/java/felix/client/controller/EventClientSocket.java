package felix.client.controller;

import felix.client.main.FelixSession;
import felix.client.models.View;
import felix.client.service.system.RsaEncryptionManager;

import javax.websocket.*;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

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

    private void setPendingUUID(String chipper)
    {
        try
        {
            String message = RsaEncryptionManager.decrypt(chipper);
            if (message.startsWith("UUID:"))
            {
                FelixSession.getInstance().setPendingUUID(UUID.fromString(message.replace("UUID:", "")));
                return;
            }
            //todo: disconnect
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onWebSocketText(String message)
    {
        if (RsaEncryptionManager.getPublicServerKey() == null)
        {
            RsaEncryptionManager.setPublicServerKey(message);
            return;
        }
        if (FelixSession.getInstance().getPendingUUID() == null)
        {
            this.setPendingUUID(message);
            return;
        }
        System.out.println(message); //todo decrypt with aes.
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