package logic;

import domain.Trie;
import java.io.File;
import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;

public class KeyFinderTest {

    private KeyFinder keyFinder;

    public KeyFinderTest() {
        Trie dictionary = new Trie();
        try {
            Scanner reader = new Scanner(new File("dictionary_long.txt"));
            while (reader.hasNext()) {
                String word = reader.nextLine();
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
    public void checkIfCharIsInListReturnsTrueForACharThatIsInTheList() {
        char[] test = new char[]{'a', 'b', 'c'};
        assertTrue(this.keyFinder.checkIfCharInList(test, 'b'));
    }
    
    @Test
    public void checkIfCharIsInListReturnsFalseForACharThatIsNotInTheList() {
        char[] test = new char[]{'a', 'b', 'c'};
        assertFalse(this.keyFinder.checkIfCharInList(test, 'd'));
    }
    
    @Test
    public void findKeyFindsTheRightKey() {
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
}
