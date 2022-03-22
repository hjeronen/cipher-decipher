
package cipher.decipher;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.GridLayout;
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
        JTextField input = new JTextField("input");
        JTextField key = new JTextField("key");
        JTextField output = new JTextField("output");
        container.add(input);
        container.add(key);
        container.add(output);
        
        Listener listener = new Listener(this.decrypter, input, key, output);
        
        JPanel panel = new JPanel(new GridLayout(1, 3));
        JButton decrypt = new JButton("Decrypt");
        decrypt.addActionListener(listener);
        panel.add(decrypt);
        container.add(panel);
 
        frame.pack();
        frame.setVisible(true);
    }
    
    
}
