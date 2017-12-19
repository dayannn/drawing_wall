import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dayan on 20.12.2017.
 */
public class clientStartFrame extends JFrame{
    private JPanel mainpanel;
    private JButton okButton;
    private JTextField addressTextField;
    private JTextField portTextField;

    public String getAddr() {
        return addr;
    }

    public Integer getPort() {
        return port;
    }

    String addr;
    Integer port;


    public clientStartFrame() {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(mainpanel);
        setSize(400, 200);
        setVisible(true);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addr = addressTextField.getText();
                port = Integer.parseInt(portTextField.getText());

                new Client(addr, port);

                setVisible(false);
            }
        });
    }

    public static void main (String[] args){
        new clientStartFrame();
    }
}
