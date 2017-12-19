import java.net.*;
import java.io.*;

public class ServerThread extends Thread
{
    private Server       server    = null;
    private Socket           socket    = null;
    private int              ID        = -1;
    private InputStream  streamIn  =  null;
    private OutputStream streamOut = null;

    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;

    public ServerThread(Server _server, Socket _socket)
    {
        super();

        server = _server;
        socket = _socket;
        ID     = socket.getPort();
    }

    public void send(Object obj)
    {
        try
        {
            oos.writeObject(obj);
            oos.flush();
            streamOut.flush();
        }
        catch(IOException ioe)
        {
            System.out.println(ID + " ERROR sending: " + ioe.getMessage());
            server.remove(ID);
            stop();
        }
    }

    public int getID()
    {
        return ID;
    }

    public void run()
    {
        System.out.println("Server Thread " + ID + " running.");
        while (true)
        {
            try
            {
                server.handle(ID, ois.readObject());
            }
            catch(IOException ioe)
            {
                System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                server.remove(ID);
                stop();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void open() throws IOException
    {
        streamIn = socket.getInputStream();
        streamOut = socket.getOutputStream();
        ois = new ObjectInputStream(streamIn);
        oos = new ObjectOutputStream(streamOut);
    }

    public void close() throws IOException
    {
        if (socket != null)
            socket.close();
        if (ois != null)
            ois.close();
        if (oos != null)
            oos.close();
        if (streamIn != null)
            streamIn.close();
        if (streamOut != null)
            streamOut.close();
    }
}