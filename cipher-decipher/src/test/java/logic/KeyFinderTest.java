package logic;

import domain.Trie;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.junit.Test;
import static org.junit.Assert.*;

public class KeyFinderTest {

    private KeyFinder keyFinder;
    private String dictionaryFilename = "/dictionary.txt";

    public KeyFinderTest() {
        Trie dictionary = new Trie();
        try ( InputStream in = getClass().getResourceAsStream(this.dictionaryFilename);  BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String word;
            while ((word = reader.readLine()) != null) {
                if (word.length() == 1 && !word.equals("i") && !word.equals("a")) {
                    continue;
                }
                dictionary.trieInsert(word);
            }
        } catch (Exception exception) {
            System.out.println("Dictionary creation failed.");
        }
        this.keyFinder = new KeyFinder(dictionary);
    }

    @Test
    public void getClosestAvailableKeyReturnsCharWithClosestFrequency() {
        this.keyFinder.initializeArrays();
        char[] listOfUsedChars = new char[1];
        // the frequency of letter d is 4.1 %
        assertTrue(this.keyFinder.getClosestAvailableKey(listOfUsedChars, 4.1) == 'd');
    }

    @Test
    public void findWordReturnsTrueIfWordIsInDictionary() {
        assertTrue(this.keyFinder.findWord("word"));
    }

    @Test
    public void findWordReturnsFalseForSubstrings() {
        assertFalse(this.keyFinder.findWord("wor"));
    }

    @Test
    public void findSubstringReturnsTrueForSubstrings() {
        assertTrue(this.keyFinder.findSubstring("wor"));
    }

    @Test
    public void copyKeyReturnsCopyOfTheGivenArray() {
        char[] test = new char[]{'a', 'b', 'c'};
        char[] copy = this.keyFinder.copyKey(test);
        assertTrue(test[0] == copy[0]);
        assertTrue(test[1] == copy[1]);
        assertTrue(test[2] == copy[2]);
    }

    @Test
    public void checkIfCharInListReturnsTrueForACharThatIsInTheList() {
        char[] test = new char[]{'a', 'b', 'c'};
        assertTrue(this.keyFinder.checkIfCharInList(test, 'b'));
    }

    @Test
    public void checkIfCharInListReturnsFalseForACharThatIsNotInTheList() {
        char[] test = new char[]{'a', 'b', 'c'};
        assertFalse(this.keyFinder.checkIfCharInList(test, 'd'));
    }

    @Test
    public void checkIfIntInListReturnsTrueIfIntIsInList() {
        int[] test = new int[]{1, 2, 3, 4, 5};
        assertTrue(this.keyFinder.checkIfIntInList(test, 3));
    }

    @Test
    public void checkIfIntInListReturnsFalseIfIntIsNotInList() {
        int[] test = new int[]{1, 2, 3, 4, 5};
        assertFalse(this.keyFinder.checkIfIntInList(test, 0));
    }

    @Test
    public void findKeyFindsTheRightKeyWithPreSetFrequencies() {
        this.keyFinder.initializeArrays();
        String[] cipherwords = new String[]{"nvhhztv", "hrnkov"};
        StringBuilder[] words = new StringBuilder[]{new StringBuilder("nvhhztv"), new StringBuilder("hrnkov")};
        String cipherLetters = "hrnkovzt";
        double[] cipherFrequencies = new double[128];
        cipherFrequencies['h'] = 6.1;
        cipherFrequencies['r'] = 6.8;
        cipherFrequencies['n'] = 2.3;
        cipherFrequencies['k'] = 1.8;
        cipherFrequencies['o'] = 3.9;
        cipherFrequencies['v'] = 12.2;
        cipherFrequencies['z'] = 7.9;
        cipherFrequencies['t'] = 1.9;
        char[] key = this.keyFinder.findKey(cipherwords, words, cipherLetters, cipherFrequencies);

        assertTrue(key['h'] == 's');
        assertTrue(key['r'] == 'i');
        assertTrue(key['n'] == 'm');
        assertTrue(key['k'] == 'p');
        assertTrue(key['o'] == 'l');
        assertTrue(key['v'] == 'e');
        assertTrue(key['z'] == 'a');
        assertTrue(key['t'] == 'g');
    }

    @Test
    public void findKeyWillRaiseErrorMarginIfInitialTooSmallAndReturnCorrectKey() {
        String testTextWith20PercentErrors = "SGXWGY DYXRGKZWAIGHF KAWGUIOYFGKAW "
                + "VAXUHFGK XYSZX GFBGSYFGUI XYYQVYW RHNHKXAI KHXVHXAKL ZVNHXFY "
                + "XYKHFIXAKIU UIXGVYU HQLYUIOYUGA KHKIGHF IXGKLKWGK BXAJU NXGUIWY "
                + "AWBYXFYL RYSAXGAF UKZWM UZRRA BXGFMGFS FHFYFIXYUUY ULRVAIOHWLUGU "
                + "BYFBXAQHF FHFUIXHVOGK VXHIHKAIYKOZAWBYOLBY IHABL BGRGFGUOYU "
                + "VHWLBGVUGA HDYXBYUKXGNGFS UVASFZHWG KAPAXBGUY BGUAFFZW UYXDGKYJHRYF "
                + "ZFFGKMYWWYB YFSWAFBYXU VXYVAXAIYZX XYAVVYAXGFS OGWWHKML QEGYE "
                + "PXORHBXIZW YJXKTSVGA FYMQA DQISSMDWPNZQ EATJJVUPMKJWX "
                + "ABHRCBBNWDFB BDBXNFIK DKJNTX POMMHVSFF ";
        TextHandler textHandler = new TextHandler();
        testTextWith20PercentErrors = textHandler.cleanText(testTextWith20PercentErrors);
        this.keyFinder.initializeArrays();
        String[] cipherwords = textHandler.getWordListString(testTextWith20PercentErrors, 2000);
        StringBuilder[] words = textHandler.copyWordListToStringBuilder(cipherwords);
        String cipherLetters = textHandler.getAllUsedCharacters(testTextWith20PercentErrors);
        double[] cipherFrequencies = textHandler.findCharacterFrequencies(testTextWith20PercentErrors);
        char[] key = this.keyFinder.findKey(cipherwords, words, cipherLetters, cipherFrequencies);
        assertTrue(key['s'] == 'g');
        assertTrue(key['g'] == 'i');
        assertTrue(key['x'] == 'r');
    }

    @Test
    public void undoErrorCharsResetsTheCharsInErrorWords() {
        String[] wordList = new String[]{"this", "is", "a", "list"};
        int[] errorWords = new int[]{1, 2};
        boolean[] taken = new boolean[128];
        boolean[] substituted = new boolean[128];
        char[] substitutions = new char[128];
        for (String word : wordList) {
            for (int i = 0; i < word.length(); i++) {
                taken['x'] = true;
                substituted[word.charAt(i)] = true;
                substitutions[word.charAt(i)] = 'x';
            }
        }
        assertTrue(taken['x']);
        assertTrue(substituted['i']);
        assertTrue(substitutions['i'] == 'x');

        this.keyFinder.setTaken(taken);
        this.keyFinder.setSubstituted(substituted);
        this.keyFinder.setSubstitutions(substitutions);
        this.keyFinder.setArrays(wordList, new StringBuilder[0], new double[0]);

        this.keyFinder.undoErrorChars(errorWords);
        assertFalse(taken['x']);
        assertFalse(substituted['i']);
        assertTrue(substitutions['i'] == '*');
        assertFalse(substituted['s']);
        assertTrue(substitutions['s'] == '*');
        assertFalse(substituted['a']);
        assertTrue(substitutions['a'] == '*');
    }

    @Test
    public void setFoundKeysSetsFoundKeysFromSubstitutions() {
        boolean[] taken = new boolean[128];
        boolean[] substituted = new boolean[128];
        char[] substitutions = new char[128];
        String cipherLetters = "thisal";
        for (int i = 0; i < cipherLetters.length(); i++) {
            substitutions[cipherLetters.charAt(i)] = '*';
        }

        substitutions['i'] = 'x';
        substitutions['a'] = 'y';

        assertFalse(taken['x']);
        assertFalse(taken['y']);
        assertFalse(substituted['i']);

        this.keyFinder.setTaken(taken);
        this.keyFinder.setSubstituted(substituted);
        this.keyFinder.setSubstitutions(substitutions);

        this.keyFinder.setFoundKeys(cipherLetters);

        assertTrue(taken['x']);
        assertTrue(taken['y']);
        assertTrue(substituted['i']);
        assertTrue(substituted['a']);
        assertTrue(substitutions['i'] == 'x');
        assertTrue(substitutions['a'] == 'y');

        assertFalse(substituted['t']);
        assertTrue(substitutions['t'] == '*');
        assertFalse(substituted['s']);
        assertTrue(substitutions['s'] == '*');
    }

    @Test
    public void checkForUnsubstitutedCharactersWillFindAndSetTheBestFitForUnsubstitutedChar() {
        String[] wordList = new String[]{"this", "is", "a", "list"};
        String cipherLetters = "thisal";
        boolean[] taken = new boolean[128];
        boolean[] substituted = new boolean[128];
        char[] substitutions = new char[128];
        for (String word : wordList) {
            for (int i = 0; i < word.length(); i++) {
                taken['x'] = true;
                substituted[word.charAt(i)] = true;
                substitutions[word.charAt(i)] = 'x';
            }
        }

        substitutions['i'] = '*';
        substitutions['a'] = '*';

        double[] cipherFrequencies = new double[128];

        cipherFrequencies['i'] = 0.2;
        cipherFrequencies['a'] = 0.1;

        this.keyFinder.setTaken(taken);
        this.keyFinder.setSubstituted(substituted);
        this.keyFinder.setSubstitutions(substitutions);
        this.keyFinder.setArrays(wordList, new StringBuilder[0], cipherFrequencies);

        this.keyFinder.checkForUnsubstitutedCharacters(cipherLetters);

        assertTrue(substitutions['s'] == 'x');
        assertTrue(substitutions['i'] == 'j');
        assertTrue(substitutions['a'] == 'q');
    }
}
