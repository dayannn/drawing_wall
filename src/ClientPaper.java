import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientPaper extends JPanel {
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

    protected synchronized void addPoint(DrawInfo info) {
        hs.add(info);
        repaint();
    }

    private class L1 extends MouseAdapter {
        public void mousePressed(MouseEvent me) {
            Point p = me.getPoint();
            sender.send(p, p);
            lastPressed = p;
        }
    }

    private class L2 extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent me) {
            Point p = me.getPoint();
            sender.send(lastPressed, p);
            lastPressed = p;
        }
    }
}
