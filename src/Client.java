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
    private OutputStream streamOut = null;
    private ClientThread client    = null;
    private static final Object sMonitor = new Object();
    private ArrayList<DrawInfo> pList = new ArrayList<>();
    private ClientPaper paper = null;

    ObjectOutputStream oos = null;

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
                    DrawInfo info = pList.remove(0);
                    //streamOut.writeUTF(String.valueOf((int) p.getX()) + " " + String.valueOf((int) p.getY()));
                    oos.writeObject(info);
                    oos.flush();
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

    public void handle(Object obj)
    {
      /*  if (msg.equals(".bye"))
        {
            System.out.println("Good bye. Press RETURN to exit ...");
            stop();
        }
        else {*/
            DrawInfo info = (DrawInfo) obj;
            System.out.println("x= " + String.valueOf(info.get_x()) +
                               " y= " + String.valueOf(info.get_y()) +
                               " clr= " + String.valueOf(info.get_clr()));

            paper.addPoint(info);
       // }
    }

    public void start() throws IOException
    {
        streamOut = socket.getOutputStream();
        oos = new ObjectOutputStream(streamOut);
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
            if (oos != null)
                oos.close();
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
        DrawInfo info = new DrawInfo(pnt.x, pnt.y, Color.BLUE);
        pList.add(info);
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
            DrawInfo info = (DrawInfo) i.next();
            g.setColor(info.get_clr());
            g.fillOval(info.get_x(), info.get_y(), 3, 3);

        }
    }

    // Adds a pixel to the Hashset and repaints
    protected synchronized void addPoint(DrawInfo info) {
        hs.add(info);
        repaint();
    }


    /*Listens to when the mouse is pressed*/
    private class L1 extends MouseAdapter {
        public void mousePressed(MouseEvent me) {
            Point p = me.getPoint();
            addPoint(new DrawInfo(p.x, p.y, Color.BLACK));
            sender.send(p);
            //  sender.sendAway(p);
        }
    }

    /*Listens to when the mouse is dragged*/
    private class L2 extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent me) {
            Point p = me.getPoint();
            addPoint(new DrawInfo(p.x, p.y, Color.BLACK));
            sender.send(p);
            //  sender.sendAway(p);
        }
    }
} // End class Paper