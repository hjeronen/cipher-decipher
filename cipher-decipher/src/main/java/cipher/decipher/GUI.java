
package cipher.decipher;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class GUI implements Runnable {
    private JFrame frame;
    private Decrypter decrypter;
    
    public GUI(Decrypter decrypter) {
        this.decrypter = decrypter;
    }
    
    @Override
    public void run() {
        frame = new JFrame("cipher-decipher");
        frame.setPreferredSize(new Dimension(300, 150));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 1));
        
        Container container = frame.getContentPane();
        JTextField input = new JTextField("");
        JTextField output = new JTextField("");
        container.add(input);
        container.add(output);
        
        Listener listener = new Listener(this.decrypter, input, output);
        
        JPanel panel = new JPanel(new GridLayout(1, 1));
        JButton decrypt = new JButton("Find");
        decrypt.addActionListener(listener);
        panel.add(decrypt);
        container.add(panel);
 
        frame.pack();
        frame.setVisible(true);
    }
    
    
}
