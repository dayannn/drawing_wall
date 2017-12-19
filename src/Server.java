import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.*;
import java.io.*;
import java.util.HashSet;
import java.util.Iterator;

public class Server extends JFrame implements Runnable
{
    private ServerThread clients[] = new ServerThread[50];
    private ServerSocket server = null;
    private Thread       thread = null;
    private int clientCount = 0;
    ServerPaper paper = null;
    private JPanel mainpanel;

    public Server(int port)
    {
       // createUIComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(mainpanel);
        setSize(640, 480);
        setVisible(true);

        try
        {
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            start();
        }
        catch(IOException ioe)
        {
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
        }
    }


    public void run()
    {  while (thread != null)
        {
            try
            {
                System.out.println("Waiting for a client ...");
                addThread(server.accept());
            }
            catch(IOException ioe)
            {
                System.out.println("Server accept error: " + ioe);
                stop();
            }
        }
    }


    public void start()
    {
        if (thread == null)
        {
            thread = new Thread(this);
            thread.start();
        }
    }


    public void stop()
    {
        if (thread != null)
        {
            thread.stop();
            thread = null;
        }
    }


    private int findClient(int ID)
    {
        for (int i = 0; i < clientCount; i++)
            if (clients[i].getID() == ID)
                return i;
        return -1;
    }


    public synchronized void handle(int ID, Object obj)
    {
       /* if (input.equals(".bye"))
        {
            clients[findClient(ID)].send(".bye");
            remove(ID);
        }
        else {*/
            DrawInfo info = (DrawInfo) obj;
            System.out.println("x= " + String.valueOf(info.get_x()) +
                               " y= " + String.valueOf(info.get_y()) +
                               " clr= " + String.valueOf(info.get_clr()));
            paper.addPoint(info);
            for (int i = 0; i < clientCount; i++)
                clients[i].send(obj);
       // }
    }


    public synchronized void remove(int ID)
    {
        int pos = findClient(ID);
        if (pos >= 0)
        {
            ServerThread toTerminate = clients[pos];
            System.out.println("Removing client thread " + ID + " at " + pos);

            if (pos < clientCount-1)
                for (int i = pos+1; i < clientCount; i++)
                    clients[i-1] = clients[i];
            clientCount--;

            try
            {
                toTerminate.close();
            }
            catch(IOException ioe)
            {
                System.out.println("Error closing thread: " + ioe);
            }
            toTerminate.stop(); }
    }


    private void addThread(Socket socket)
    {
        if (clientCount < clients.length)
        {
            System.out.println("Client accepted: " + socket);
            clients[clientCount] = new ServerThread(this, socket);
            try
            {
                clients[clientCount].open();
                clients[clientCount].start();
                clientCount++;
            }
            catch(IOException ioe)
            {
                System.out.println("Error opening thread: " + ioe);
            }
        }
        else
            System.out.println("Client refused: maximum " + clients.length + " reached.");
        }


    public static void main(String args[])
    {
        Server server = new Server(1234);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        paper = new ServerPaper(this);
    }
}


