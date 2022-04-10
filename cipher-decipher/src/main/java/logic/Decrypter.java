package logic;

import domain.Trie;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Class that does the decryption.
 *
 */
public class Decrypter {

    private Trie dictionary;

    private double[] frequencies;
    private double[] cipherFrequencies;

    // what key values have been used
    private boolean[] taken;

    // what cipherletters have been changed
    private boolean[] substituted;

    // what a certain char has been substituted with
    private char[] substitutions;
    
    // arrays for cipherwords
    // changing
    private StringBuilder[] words;
    // original
    private String[] cipherwords;
    
    // how many errors are allowed
    private int maxErrors;

    /**
     * Decrypter constructor. Also creates the dictionary and an array of the
     * typical frequencies of all letters.
     *
     * @param filename the name of the file that contains the wordlist that
     * should be used
     */
    public Decrypter(String filename) {
        this.dictionary = new Trie();
        createDictionary(filename);
        String abc = "abcdefghijklmnopqrstuvwxyz";
        double[] freq = new double[]{7.9, 1.4, 2.7, 4.1, 12.2, 2.1, 1.9, 5.9, 6.8, 0.2, 0.8, 3.9, 2.3, 6.5, 7.2, 1.8, 0.1, 5.8, 6.1, 8.8, 2.7, 1.0, 2.3, 0.2, 1.9, 1.0};
        this.frequencies = new double[128];
        for (int i = 0; i < this.frequencies.length; i++) {
            this.frequencies[i] = 200;
        }
        for (int i = 0; i < abc.length(); i++) {
            this.frequencies[abc.charAt(i)] = freq[i];
        }
    }

    /**
     * Creates a dictionary. Words in the given file are stored in a trie
     * datastructure.
     *
     * @param filename the name of the file that contains the wordlist that
     * should be used
     */
    private void createDictionary(String filename) {
        ArrayList<String> words = new ArrayList<String>();
        try {
            Scanner reader = new Scanner(new File(filename));
            while (reader.hasNext()) {
                words.add(reader.nextLine());
            }
        } catch (Exception exception) {
            System.out.println("Dictionary creation failed.");
        }
        this.dictionary.createTrie(words);
    }

    // need this for testing
    /**
     * Initializes the required arrays for decryption.
     *
     */
    public void initializeArrays() {
        // for checking which letters have been used
        this.taken = new boolean[128];
        // for checking which characters have been substituted and with what
        this.substituted = new boolean[128];
        this.substitutions = new char[128];
        for (int i = 0; i < this.taken.length; i++) {
            this.taken[i] = false;
            this.substituted[i] = false;
            this.substitutions[i] = '*';
        }
        // for saving the frequencies of the cipher text characters
        this.cipherFrequencies = new double[128];
    }

    /**
     * Decryption function. Takes a ciphered text as parameter and returns it
     * decrypted.
     *
     * This is still work in progress!
     *
     * @param text input from user
     *
     * @return decrypted text
     */
    public String decrypt(String text) {
        long start = System.currentTimeMillis();
        // cleanup text
        String modifiedText = text.toLowerCase().replaceAll("[^a-zA-Z\\d\\s:]", "");

        initializeArrays();

        // find all unique letters in ciphertext, their counts and total amount of characters, excluding nonletters
        int[] counts = new int[128];
        String cipherLetters = "";
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int total = 0; // faster with modifiedText.length()!
        for (int i = 0; i < modifiedText.length(); i++) {
            if (modifiedText.charAt(i) == ' ') {
                continue;
            }
            total++;
            if (!cipherLetters.contains("" + modifiedText.charAt(i))) {
                cipherLetters += modifiedText.charAt(i);
            }
            counts[modifiedText.charAt(i)] += 1;
        }

        // find ciphertext's character frequencies
        for (int i = 0; i < cipherLetters.length(); i++) {
            this.cipherFrequencies[cipherLetters.charAt(i)] = (double) counts[cipherLetters.charAt(i)] / total * 100;
        }

        // form word lists
        // cipherwords-list is for original ciphered words
        this.cipherwords = modifiedText.split(" ");
        // sort words from longest to shortest to speed up decryption
        // this sorting trick from https://stackoverflow.com/questions/35866240/how-to-sort-string-array-by-length-using-arrays-sort
        Arrays.sort(this.cipherwords, (a,b) -> b.length() - a.length());
        // words-list has words where substitutions are tried on
        this.words = new StringBuilder[this.cipherwords.length];
        for (int i = 0; i < this.cipherwords.length; i++) {
            this.words[i] = new StringBuilder(this.cipherwords[i]);
        }
        
        // set the max amount of errors allowed in the text
        this.maxErrors = (int) Math.floor(this.cipherwords.length * 0.10);
        System.out.println("Max allowed errors " + this.maxErrors);
        
        // find decryption by going through word-arrays and changing letters in words with backtracking
        // increase amount of errors if no results - TODO: set some kind of limit here
        while(!findDecryption(0, 0, 0)) {
            this.maxErrors += 1;
        }
        
        // form result text by getting the keys for original ciphered characters
        String resultText = "";
        for (int i = 0; i < text.length(); i++) {
            // non-letters are added as they are
            if (!abc.contains("" + text.charAt(i))) {
                resultText += text.charAt(i);
                continue;
            }
            // ciphered character
            char character = text.charAt(i);
            // for uppercase characters, change key to uppercase too (keys are saved in and for lower case)
            if (Character.isUpperCase(character)) {
                char c = this.substitutions[Character.toLowerCase(character)];
                resultText += Character.toUpperCase(c);
                continue;
            }
            // add into result text the key for character
            resultText += this.substitutions[character];
        }

        long end = System.currentTimeMillis();
        System.out.println("Elapsed Time in milliseconds: " + (end - start));
        return resultText;
    }

    /**
     * Decryption by word. Goes through words in this.cipherwords array and tries
     * to find key values for ciphered characters. Keys for characters are selected 
     * by availability and closest frequency.
     *
     * @param j character index
     * @param i word index
     * @param errors amount of words that could not be decrypted
     * @return true if suitable decryption was found, false if not
     */
    public boolean findDecryption(int j, int i, int errors) {
        // check if all words handled
        if (i == this.cipherwords.length) {
            return true;
        }
        // check if at end of word
        if (j == this.cipherwords[i].length()) {
            // if word is found, move forward
            if (findWord(this.words[i].toString())) {
                return findDecryption(0, i + 1, errors);
            }
            // else return false - cannot handle errors here, need to try all possible substitutions before marking as error
            return false;
        }
        // save the word where substitutions are made and the original ciphered word
        StringBuilder word = this.words[i];
        String cipher = this.cipherwords[i];
        
        // the ciphered character that is to be changed
        char character = cipher.charAt(j);
        // check if character has already been assigned a key value
        if (this.substituted[character]) {
            // change the cipher character to key value
            word.setCharAt(j, this.substitutions[character]);
            // check if last character of the word
            if (j == this.cipherwords[i].length() - 1) {
                // need to do error check here incase all the characters of cipher word have been assigned a key value before
                if (!findWord(this.words[i].toString())) {
                    // if word was not found, check if error limit breaks
                    if (errors + 1 <= this.maxErrors) {
                        // if not over limit, increase amount of errors and continue to next word
                        return findDecryption(0, i + 1, errors + 1);
                    }
                    // if there are too many errors, continue to next method call,
                    // where full word is checked and since word is not found, return false
                }
            }
            // if not at end of word, or if word was found or there have been too many errors, continue
            return findDecryption(j + 1, i, errors);
        }
        // character has not been assigned a key value, so mark it as substituted now
        this.substituted[character] = true;
        // form list for key values that have been tried
        char[] used = new char[26];
        // mark the free spot in list
        int index = 0;
        // get the frequency of the ciphered character
        double freq = this.cipherFrequencies[character];
        // find a key value that is available and that has the closest frequency
        char key = getClosestAvailableKey(used, freq);
        
        // while there are available key values that have not been tried (empty char means out of keys)
        while (key != ' ') {
            // mark key value as taken
            this.taken[key] = true;
            // remember that character was substituted with this key
            this.substitutions[character] = key;
            // remember which keys have been tried
            used[index] = key;
            // mark next free spot in used keys list
            index++;
            // change the ciphered character in word to key
            word.setCharAt(j, key);
            
            // check if looks reasonable and move to next character
            // if cannot find the beginning of the word, cannot find the whole word either - the whole word check is unnecessary?
            if (findSubstring(word.substring(0, j + 1))) {
                if (findDecryption(j + 1, i, errors)) {
                    return true;
                }
            }
            
            // substring was not found, so possibly not working substitution
            // free key
            this.taken[key] = false;
            // find next closest key
            key = getClosestAvailableKey(used, freq);
        }
        // no possible substitutions found, so return ciphered character to original
        word.setCharAt(j, character);
        // mark character as unsubstituted
        this.substituted[character] = false;
        // no key value found for the character
        this.substitutions[character] = '*';
        
        // check if first letter of the word
        if (j == 0) {
            // if arrived here, means that no possible substitutions were found for any of the unsibstituted characters in the word
            // check how many errors
            if (errors + 1 <= this.maxErrors) {
                // if not too many errors, mark word as error and move on to next word
                return findDecryption(0, i + 1, errors + 1);
            }
        }
        // if not back at the beginning of the word or if too many errors, return back to previous point in recursion
        return false;
    }
    
    /**
     * Find the closest frequency. Finds the char that has the same or closest
     * typical frequency as the ciphered char in cipher text. If the char is
     * already used as key another is chosen.
     *
     * @param used a list of characters that have already been tried
     * @param freq frequency of the cipher char in cipher text
     * @return char with closest typical frequency
     */
    public char getClosestAvailableKey(char[] used, double freq) {
        double closestFreq = 1000;
        char closestChar = ' ';
        for (int i = 'a'; i < 123; i++) {
            if (this.taken[i]) {
                continue;
            }
            if (checkIfCharInList(used, (char) i)) {
                continue;
            }
            if (Math.abs(this.frequencies[i] - freq) < Math.abs(closestFreq - freq)) {
                closestChar = (char) i;
                closestFreq = this.frequencies[i];
            }
        }
        return closestChar;
    }

    /**
     * Check if a character is on the given list.
     *
     * @param list a list of characters
     * @param character character that is searched for
     * @return true if character was on the list, false if not
     */
    public boolean checkIfCharInList(char[] list, char character) {
        for (int j = 0; j < list.length; j++) {
            if (list[j] == character) {
                return true;
            }
        }
        return false;
    }

    /**
     * Substitute chars at given indexes. The chars in the StringBuilder whose
     * indexes are on the list are substituted with given value.
     *
     * @param indexes list of indexes of the characters that are replaced
     * @param sb StringBuilder word that is modified
     * @param value the character that is placed on given indexes of the word
     */
    public void substitute(ArrayList<Integer> indexes, StringBuilder sb, char value) {
        for (int index : indexes) {
            sb.setCharAt(index, value);
        }
    }

    /**
     * Find word from dictionary.
     *
     * @param word the word that is searched
     */
    public boolean findWord(String word) {
        return this.dictionary.findWord(word.toLowerCase());
    }

    /**
     * Find substring from dictionary.
     *
     * @param string the substring that is searched
     */
    public boolean findSubstring(String string) {
        return this.dictionary.findSubstring(string.toLowerCase());
    }
}
