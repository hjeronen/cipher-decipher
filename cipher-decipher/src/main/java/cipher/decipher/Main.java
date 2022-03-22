package cipher.decipher;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.SwingUtilities;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Decrypter decrypter = new Decrypter("dictionary.txt");
        
        GUI gui = new GUI(decrypter);
        SwingUtilities.invokeLater(gui);

    }
}
