
package cipher.decipher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

public class Listener implements ActionListener {
    private Decrypter decrypter;
    private JTextField input;
    private JTextField output;
    
    public Listener(Decrypter decrypter, JTextField input, JTextField output) {
        this.decrypter = decrypter;
        this.input = input;
        this.output = output;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        this.output.setText(this.decrypter.findWord(this.input.getText()));
    }
}
