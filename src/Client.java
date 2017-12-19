import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Client extends JFrame implements Runnable
{
    private Socket socket              = null;
    private Thread thread              = null;
    private DataInputStream  console   = null;
    private DataOutputStream streamOut = null;
    private ClientThread client    = null;
    private static final Object sMonitor = new Object();
    private ArrayList<Point> pList = new ArrayList<>();
    private ClientPaper paper = null;

    public Client(String serverName, int serverPort)
    {
        paper = new ClientPaper(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(paper);
        setSize(640, 480);
        setVisible(true);


        System.out.println("Establishing connection. Please wait ...");
        try
        {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            start();
        }
        catch(UnknownHostException uhe)
        {
            System.out.println("Host unknown: " + uhe.getMessage());
        }
        catch(IOException ioe)
        {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
    }

    public void run()
    {
       /* while (thread != null)
        {
            synchronized (sMonitor) {
                try {
                    sMonitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }*/

        while (thread != null)
        {
            synchronized (sMonitor) {
                try {
                    sMonitor.wait();
                    if (pList.isEmpty())
                        break;
                    Point p = pList.remove(0);
                    streamOut.writeUTF(String.valueOf((int) p.getX()) + " " + String.valueOf((int) p.getY()));
                    streamOut.flush();
                } catch (IOException ioe) {
                    System.out.println("Sending error: " + ioe.getMessage());
                    stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void handle(String msg)
    {
        if (msg.equals(".bye"))
        {
            System.out.println("Good bye. Press RETURN to exit ...");
            stop();
        }
        else {
            System.out.println(msg);
            String[] dbls = msg.split(" ");
            Integer x = Integer.parseInt(dbls[1]);
            Integer y = Integer.parseInt(dbls[2]);
            paper.addPoint(new Point(x, y));
        }
    }

    public void start() throws IOException
    {
        console   = new DataInputStream(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
        if (thread == null)
        {
            client = new ClientThread(this, socket);
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop()
    {
        if (thread != null)
        {  thread.stop();
            thread = null;
        }
        try
        {
            if (console   != null)
                console.close();
            if (streamOut != null)
                streamOut.close();
            if (socket    != null)
                socket.close();
        }
        catch(IOException ioe)
        {
            System.out.println("Error closing ...");
        }

        client.close();
        client.stop();
    }
    public static void main(String args[])
    {
        Client client = new Client("127.0.0.1", 1234);
    }


    public void send(Point pnt){
        pList.add(pnt);
        synchronized (sMonitor) {
            sMonitor.notify();
        }
    }
}

/* Class paper represents the middle area*/
class ClientPaper extends JPanel {
    private HashSet hs = new HashSet();
    private Client sender;


    public ClientPaper(Client sender) {
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
    protected synchronized void addPoint(Point p) {
        hs.add(p);
        repaint();
    }


    /*Listens to when the mouse is pressed*/
    private class L1 extends MouseAdapter {
        public void mousePressed(MouseEvent me) {
            Point p = me.getPoint();
            addPoint(p);
            sender.send(p);
            //  sender.sendAway(p);
        }
    }

    /*Listens to when the mouse is dragged*/
    private class L2 extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent me) {
            Point p = me.getPoint();
            addPoint(p);
            sender.send(p);
            //  sender.sendAway(p);
        }
    }
} // End class Paper