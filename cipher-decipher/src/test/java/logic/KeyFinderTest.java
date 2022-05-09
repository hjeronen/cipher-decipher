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
}
