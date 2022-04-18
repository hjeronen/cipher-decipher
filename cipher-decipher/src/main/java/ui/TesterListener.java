
package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;
import util.PerformanceTester;

public class TesterListener implements ActionListener {
    private PerformanceTester tester;
    private JTextArea output;
    
    public TesterListener(PerformanceTester tester, JTextArea output) {
        this.tester = tester;
        this.output = output;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        this.tester.run();
        this.output.setText(this.tester.toString());
    }
    
}
