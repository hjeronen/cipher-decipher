package ui;

import logic.Decrypter;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import util.AccuracyTester;
import util.PerformanceTester;

/**
 * The graphical userinterface.
 *
 */
public class GUI implements Runnable {

    private JFrame frame;
    private Decrypter decrypter;
    private PerformanceTester ptester;
    private AccuracyTester atester;

    public GUI(Decrypter decrypter, PerformanceTester ptester, AccuracyTester atester) {
        this.decrypter = decrypter;
        this.ptester = ptester;
        this.atester = atester;
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
        
        TesterListener performanceListener = new TesterListener(this.ptester, output);
        
        JButton performanceTester = new JButton("Performance tests");
        performanceTester.setAlignmentX(JButton.CENTER_ALIGNMENT);
        performanceTester.addActionListener(performanceListener);
        //container.add(performanceTester);
        
        TesterListener accuracyListener = new TesterListener(this.atester, output);
        
        JButton accuracyTester = new JButton("Accuracy tests");
        accuracyTester.setAlignmentX(JButton.CENTER_ALIGNMENT);
        accuracyTester.addActionListener(accuracyListener);
        //container.add(accuracyTester);
        
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(performanceTester);
        panel.add(accuracyTester);
        container.add(panel);

        this.frame.pack();
        this.frame.setVisible(true);
    }

}
