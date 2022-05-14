package ui;

import logic.Decrypter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;

/**
 * Action listener for the decrypt-button.
 *
 */
public class Listener implements ActionListener {

    private Decrypter decrypter;
    private JTextArea input;
    private JTextArea output;

    /**
     * Constructor.
     *
     * @param decrypter the decrypter that is used
     * @param input the text area from where the text is fetched
     * @param output the text area where results are set
     */
    public Listener(Decrypter decrypter, JTextArea input, JTextArea output) {
        this.decrypter = decrypter;
        this.input = input;
        this.output = output;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        this.output.setText(this.decrypter.decrypt(this.input.getText()));
    }
}
