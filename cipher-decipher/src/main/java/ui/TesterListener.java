package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;
import util.Tester;

/**
 * Action listener for the tester programs.
 */
public class TesterListener implements ActionListener {

    private Tester tester;
    private JTextArea output;

    /**
     * Constructor.
     * 
     * @param tester the tester program that is used
     * @param output the text area where results are set
     */
    public TesterListener(Tester tester, JTextArea output) {
        this.tester = tester;
        this.output = output;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        this.tester.run();
        this.output.setText(this.tester.getResults());
    }

}
