package ui;

import logic.Decrypter;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.Label;
import javax.swing.BoxLayout;
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
        this.frame.setPreferredSize(new Dimension(500, 600));
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        

        Container container = this.frame.getContentPane();
        this.frame.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        JTextArea input = new JTextArea("input");
        input.setLineWrap(true);
        input.setWrapStyleWord(true);
        JTextArea output = new JTextArea("output");
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        container.add(input);

        Listener listener = new Listener(this.decrypter, input, output);

        JButton decrypt = new JButton("Decrypt");
        decrypt.setAlignmentX(JButton.CENTER_ALIGNMENT);
        decrypt.addActionListener(listener);
        container.add(decrypt);
        container.add(output);

        this.frame.pack();
        this.frame.setVisible(true);
    }

}
