package felix.controllers;

import com.neovisionaries.ws.client.WebSocket;

public class HeartBeatThread implements Runnable
{
    private Boolean exit = false;
    private WebSocket webSocket;

    public void setWebSocket(WebSocket websocket)
    {
        this.webSocket = websocket;
    }

    public void start()
    {
        new Thread(this).start();
    }

    @Override
    public void run()
    {
        while (!this.exit)
        {
            try
            {
                Thread.sleep(0x4_45_C0);
                this.webSocket.sendPing("Ping");
            }
            catch (Exception ignored)
            {
                this.exit = true;
            }
        }
    }

    public void stop()
    {
        this.exit = true;
    }
}