package felix.client.controller;

import javax.websocket.Session;
import java.nio.ByteBuffer;

public class HeartBeatThread implements Runnable
{
    private boolean exit = false;
    private Session session;

    void start()
    {
        new Thread(this).start();
    }

    HeartBeatThread(Session session)
    {
        this.session = session;
    }

    @Override
    public void run()
    {
        while (!exit)
        {
            try
            {
                Thread.sleep(0x4_45_C0);
                this.session.getAsyncRemote().sendPing(ByteBuffer.wrap("Ping".getBytes()));
            }
            catch (Exception ignored) {}
        }
    }

    void stop()
    {
        exit = true;
    }
}