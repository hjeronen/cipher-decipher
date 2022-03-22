
package cipher.decipher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

public class Listener implements ActionListener {
    private Decrypter decrypter;
    private JTextField input;
    private JTextField key;
    private JTextField output;
    
    public Listener(Decrypter decrypter, JTextField input, JTextField key, JTextField output) {
        this.decrypter = decrypter;
        this.input = input;
        this.key = key;
        this.output = output;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        // this.output.setText(this.decrypter.findWord(this.input.getText()));
        this.output.setText(this.decrypter.decypher(this.input.getText(), this.key.getText()));
    }
}
