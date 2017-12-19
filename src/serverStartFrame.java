import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dayan on 20.12.2017.
 */
public class serverStartFrame extends JFrame{
    private JTextField portTextField;
    private JButton okButton;
    private JPanel mainpanel;

    public serverStartFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(mainpanel);
        setSize(400, 200);
        setVisible(true);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer port = Integer.parseInt(portTextField.getText());

                new Server(port);

                setVisible(false);
            }
        });
    }

    public static void main(String[] args){
        new serverStartFrame();
    }
}
