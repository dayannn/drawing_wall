import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread
{
    private Socket socket   = null;
    private Client       client   = null;
    private InputStream  streamIn = null;

    ObjectInputStream ois = null;

    public ClientThread(Client _client, Socket _socket)
    {
        client   = _client;
        socket   = _socket;
        open();
        start();
    }

    public void open()
    {
        try
        {
            streamIn  = socket.getInputStream();
            ois = new ObjectInputStream(streamIn);
        }
        catch(IOException ioe)
        {
            System.out.println("Error getting input stream: " + ioe);
            client.stop();
        }
    }

    public void close()
    {
        try
        {
            if (ois != null)
                ois.close();
            if (streamIn != null)
                streamIn.close();
        }
        catch(IOException ioe)
        {
            System.out.println("Error closing input stream: " + ioe);
        }
    }

    public void run()
    {
        while (true)
        {
            try
            {
                Object obj = ois.readObject();
                if (obj != null)
                    client.handle(obj);
            }
            catch(IOException ioe)
            {
                System.out.println("Listening error: " + ioe.getMessage());
                client.stop();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}