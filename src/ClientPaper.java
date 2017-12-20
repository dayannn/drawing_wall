import javafx.scene.shape.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/* Class paper represents the middle area*/
public class ClientPaper extends JPanel {
    //private HashSet<DrawInfo> hs = new HashSet<DrawInfo>();
    private ArrayList<DrawInfo> hs = new ArrayList<>();
    private Client sender;

    private Point lastPressed;


    public ClientPaper(Client sender) {
        this.sender = sender;
        setBackground(Color.white);
        addMouseListener(new L1());
        addMouseMotionListener(new L2());
    }

    // Used for painting the pixels
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        Iterator i = hs.iterator();
        while(i.hasNext()) {
            DrawInfo info = (DrawInfo) i.next();
            g.setColor(info.get_clr());
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(info.getWidth()));
            g2.drawLine(info.get_x1(), info.get_y1(), info.get_x2(), info.get_y2());

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
           // addPoint(new DrawInfo(p.x, p.y, Color.BLACK));
            sender.send(p, p);
            lastPressed = p;
        }
    }

    /*Listens to when the mouse is dragged*/
    private class L2 extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent me) {
            Point p = me.getPoint();
          //  addPoint(new DrawInfo(p.x, p.y, Color.BLACK));
            sender.send(lastPressed, p);
            lastPressed = p;


        }
    }
} // End class Paper
