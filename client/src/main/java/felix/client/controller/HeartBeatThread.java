package felix.client.controller;

import javax.websocket.Session;
import java.nio.ByteBuffer;

public class HeartBeatThread implements Runnable
{
    private Boolean exit = false;
    private Session session;

    public void start()
    {
        System.out.println("Started...");
        new Thread(this).start();
    }

    public HeartBeatThread(Session session)
    {
        this.session = session;
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
                this.session.getAsyncRemote().sendPing(ByteBuffer.wrap("Ping".getBytes()));
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