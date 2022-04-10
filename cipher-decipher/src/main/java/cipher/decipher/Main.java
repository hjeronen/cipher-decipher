package cipher.decipher;

import ui.GUI;
import logic.Decrypter;
import javax.swing.SwingUtilities;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Decrypter decrypter = new Decrypter("dictionary_old.txt");
        
        GUI gui = new GUI(decrypter);
        SwingUtilities.invokeLater(gui);

    }
}
