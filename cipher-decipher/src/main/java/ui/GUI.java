package ui;

import logic.Decrypter;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/**
 * The graphical userinterface.
 *
 */
public class GUI implements Runnable {

    private JFrame frame;
    private Decrypter decrypter;

    public GUI(Decrypter decrypter) {
        this.decrypter = decrypter;
    }

    @Override
    public void run() {
        this.frame = new JFrame("cipher-decipher");
        this.frame.setPreferredSize(new Dimension(300, 150));
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setLayout(new GridLayout(3, 1));

        Container container = this.frame.getContentPane();
        JTextArea input = new JTextArea("input");
        JTextArea output = new JTextArea("output");
        container.add(input);

        Listener listener = new Listener(this.decrypter, input, output);

        JButton decrypt = new JButton("Decrypt");
        decrypt.addActionListener(listener);
        container.add(decrypt);
        container.add(output);

        this.frame.pack();
        this.frame.setVisible(true);
    }

}
