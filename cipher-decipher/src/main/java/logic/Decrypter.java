package logic;

import domain.Trie;
import java.io.File;
import java.util.Scanner;

/**
 * Class that does the decryption.
 *
 */
public class Decrypter {

    private TextHandler texthandler;
    private KeyFinder keyfinder;

    private int maxTextLength;

    /**
     * Decrypter constructor. Also creates the dictionary and an array of the
     * typical frequencies of all letters.
     *
     * @param filename the name of the file that contains the wordlist that
     * should be used as a dictionary
     */
    public Decrypter(String filename) {
        this.texthandler = new TextHandler();
        this.keyfinder = new KeyFinder(createDictionary(filename));
        this.maxTextLength = 2000;
    }

    /**
     * Decryption function. Takes a ciphered text as parameter and returns it
     * decrypted.
     *
     * @param text input from user
     *
     * @return decrypted text
     */
    public String decrypt(String text) {
        String modifiedText = this.texthandler.cleanText(text);
        if (modifiedText.isBlank()) {
            return text;
        }

        String cipherLetters = this.texthandler.getAllUsedCharacters(modifiedText);
        double[] cipherFrequencies = this.texthandler.findCharacterFrequencies(modifiedText);
        this.keyfinder.setCipherFrequencies(cipherFrequencies);

        String[] cipherwords = this.texthandler.getWordListString(modifiedText, this.maxTextLength);
        StringBuilder[] words = this.texthandler.copyWordListToStringBuilder(cipherwords);

        char[] key = this.keyfinder.findKey(cipherwords, words, cipherLetters);

        return this.texthandler.formResult(text, key);
    }

    /**
     * Creates a dictionary. Words in the given file are stored in a trie
     * datastructure.
     *
     * @param filename the name of the file that contains the wordlist that
     * should be used
     */
    private Trie createDictionary(String filename) {
        Trie dictionary = new Trie();
        try {
            Scanner reader = new Scanner(new File(filename));
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
        return dictionary;
    }

}
