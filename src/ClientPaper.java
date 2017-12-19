import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashSet;
import java.util.Iterator;

/* Class paper represents the middle area*/
public class ClientPaper extends JPanel {
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
