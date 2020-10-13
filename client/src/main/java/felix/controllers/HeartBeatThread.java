package felix.controllers;

import com.neovisionaries.ws.client.WebSocket;

public class HeartBeatThread implements Runnable
{
    private Boolean exit = false;
    private WebSocket webSocket;

   public HeartBeatThread(WebSocket websocket)
    {
        this.webSocket = websocket;
    }

    public void start()
    {
        System.out.println("Started...");
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
                System.out.println("Heartbeat send!");
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
        System.out.println("Stopped...");
        this.exit = true;
    }
}