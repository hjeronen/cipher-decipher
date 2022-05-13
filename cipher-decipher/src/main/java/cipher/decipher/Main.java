package cipher.decipher;

import ui.GUI;
import logic.Decrypter;
import javax.swing.SwingUtilities;
import util.AccuracyTester;
import util.Crypter;
import util.PerformanceTester;
import util.TextFactory;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename = "dictionary_long.txt";
        Decrypter decrypter = new Decrypter(filename);
        Crypter crypter = new Crypter();
        TextFactory textFactory = new TextFactory(filename, crypter);
        PerformanceTester ptester = new PerformanceTester(decrypter.getKeyFinder(), decrypter.getTextHandler(), textFactory);
        AccuracyTester atester = new AccuracyTester(decrypter, textFactory);
        
        GUI gui = new GUI(decrypter, ptester, atester);
        SwingUtilities.invokeLater(gui);

    }
}
