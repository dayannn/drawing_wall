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

    public Server(int port)
    {
        paper = new ServerPaper(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(paper);
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


    public synchronized void handle(int ID, String input)
    {
        if (input.equals(".bye"))
        {
            clients[findClient(ID)].send(".bye");
            remove(ID);
        }
        else {
            System.out.println(input);
            String[] dbls = input.split(" ");
            Integer x = Integer.parseInt(dbls[0]);
            Integer y = Integer.parseInt(dbls[1]);
            paper.addPoint(new Point(x, y));
            for (int i = 0; i < clientCount; i++)
                clients[i].send(ID + ": " + input);
        }
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
}


/* Class paper represents the middle area*/
class ServerPaper extends JPanel {
    private HashSet hs = new HashSet();
    private Server sender;


    public ServerPaper(Server sender) {
        this.sender = sender;
        setBackground(Color.white);
        addMouseListener(new L1());
        addMouseMotionListener(new L2());
    }

    // Used for painting the pixels
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        Iterator i = hs.iterator();
        while(i.hasNext()) {
            Point p = (Point)i.next();
            g.fillOval(p.x, p.y, 3, 3);
        }
    }


    // Adds a pixel to the Hashset and repaints
    protected  synchronized void addPoint(Point p) {
        hs.add(p);
        repaint();
    }


    /*Listens to when the mouse is pressed*/
    private class L1 extends MouseAdapter {
        public void mousePressed(MouseEvent me) {
            Point p = me.getPoint();
            addPoint(p);
          //  sender.sendAway(p);
        }
    }

    /*Listens to when the mouse is dragged*/
    private class L2 extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent me) {
            Point p = me.getPoint();
            addPoint(p);
          //  sender.sendAway(p);
        }
    }
} // End class Paper