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
    private Sorter sorter;

    private double[] frequencies;
    private double[] cipherFrequencies;
    private String cipherLetters;

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

    private int maxTextLength;
    
    // in which word some character first appears
    private int[] firstAppearances;

    /**
     * Decrypter constructor. Also creates the dictionary and an array of the
     * typical frequencies of all letters.
     *
     * @param filename the name of the file that contains the wordlist that
     * should be used as a dictionary
     */
    public Decrypter(String filename) {
        this.dictionary = new Trie();
        this.sorter = new Sorter();
        createDictionary(filename);
        String abc = "abcdefghijklmnopqrstuvwxyz";
        double[] freq = new double[]{7.9, 1.4, 2.7, 4.1, 12.2, 2.1, 1.9, 5.9,
            6.8, 0.2, 0.8, 3.9, 2.3, 6.5, 7.2, 1.8, 0.1,
            5.8, 6.1, 8.8, 2.7, 1.0, 2.3, 0.2, 1.9, 1.0};
        this.frequencies = new double[128];
        for (int i = 0; i < this.frequencies.length; i++) {
            this.frequencies[i] = 200;
        }
        for (int i = 0; i < abc.length(); i++) {
            this.frequencies[abc.charAt(i)] = freq[i];
        }
        this.maxTextLength = 2000;
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

    /**
     * Decryption function. Takes a ciphered text as parameter and returns it
     * decrypted.
     *
     * @param text input from user
     *
     * @return decrypted text
     */
    public String decrypt(String text) {
        //long start = System.currentTimeMillis();
        // cleanup text
        String modifiedText = text.replaceAll("\\R+", " ");
        modifiedText = modifiedText.replaceAll("[0-9]", "");
        modifiedText = modifiedText.toLowerCase().replaceAll("[^a-zA-Z\\d\\s:]", "");

        if (modifiedText.isBlank()) {
            return text;
        }
        // prepare arrays and get character frequencies for ciphertext
        initializeArrays();
        findCharacterFrequencies(modifiedText);
        formWordLists(modifiedText);

        // set the max amount of errors allowed in the text
        double percentage = 0;
        this.maxErrors = (int) Math.floor(this.cipherwords.length * percentage);
        int multiply = 0;

        // find decryption by going through word-arrays and changing letters in words with backtracking
        // increase amount of errors if no results - TODO: set some kind of limit here
        while (!findDecryption(0, 0, 0)) {
            if (percentage == 0) {
                percentage = 0.05;
            }
            multiply++;
            this.maxErrors = (int) Math.floor(this.cipherwords.length * (percentage * multiply));
        }

        //long stop = System.currentTimeMillis();
        //System.out.println("time " + (stop - start));
        // check if unsubstituted characters
        for (int i = 0; i < this.cipherLetters.length(); i++) {
            if (!this.substituted[this.cipherLetters.charAt(i)]) {
                this.substituted[this.cipherLetters.charAt(i)] = true;
                double freq = this.cipherFrequencies[this.cipherLetters.charAt(i)];
                this.substitutions[this.cipherLetters.charAt(i)] = getClosestAvailableKey(new char[1], freq);
            }
        }
        return formResult(text);
    }

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
        this.firstAppearances = new int[128];
        for (int i = 0; i < this.taken.length; i++) {
            this.taken[i] = false;
            this.substituted[i] = false;
            this.substitutions[i] = '*';
            this.firstAppearances[i] = 0;
        }
        // for saving the frequencies of the cipher text characters
        this.cipherFrequencies = new double[128];
    }

    /**
     * Get cipher text's character frequencies. Saves the frequencies in
     * this.cipherFrequencies array at the place of the character in question.
     *
     * @param modifiedText ciphered text in lower case and without special
     * characters
     */
    public void findCharacterFrequencies(String modifiedText) {
        // find all unique letters in ciphertext, their counts and total amount of characters, excluding nonletters
        int[] counts = new int[128];
        this.cipherLetters = "";
        int total = 0;
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

        // save ciphertext's character frequencies
        for (int i = 0; i < cipherLetters.length(); i++) {
            this.cipherFrequencies[cipherLetters.charAt(i)] = (double) counts[cipherLetters.charAt(i)] / total * 100;
        }
    }

    /**
     * Form word lists. Save the words in the cipher text into an array and sort
     * by length in descending order.
     *
     * @param modifiedText ciphered text in lower case and without special
     * characters
     */
    public void formWordLists(String modifiedText) {
        // check if text too long
        if (modifiedText.length() > this.maxTextLength) {
            this.cipherwords = downsizeText(modifiedText);
        } else {
            // cleaning out extra spaces and duplicate words
            String[] temp = modifiedText.split(" ");
            int count = 0;
            // sort words from longest to shortest
            this.sorter.sortWords(temp);
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].equals("")) {
                    break;
                }
                if (i > 0 && temp[i].equals(temp[i - 1])) {
                    continue;
                }

                count++;
            }
            // cipherwords-list is for original ciphered words
            this.cipherwords = new String[count];
            int index = 0;
            for (int i = 0; i < temp.length; i++) {
                if (index >= count) {
                    break;
                }
                if (temp[i].equals("")) {
                    break;
                }
                if (i > 0 && temp[i].equals(temp[i - 1])) {
                    continue;
                }
                this.cipherwords[index] = temp[i];
                index++;
            }
        }

        // words-list has words where substitutions are tried on
        this.words = new StringBuilder[this.cipherwords.length];
        for (int i = 0; i < this.cipherwords.length; i++) {
            this.words[i] = new StringBuilder(this.cipherwords[i]);
        }
    }

    public String[] downsizeText(String text) {
        String[] temp = text.split(" ");
        this.sorter.sortWords(temp);
        int length = 0;
        int count = 0;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("")) {
                break;
            }
            if (i > 0 && temp[i].equals(temp[i - 1])) {
                continue;
            }

            if (length + temp[i].length() < this.maxTextLength) {
                length += temp[i].length();
                count++;
            } else {
                break;
            }
        }
        String[] result = new String[count];
        int index = 0;
        for (int i = 0; i < temp.length; i++) {
            if (index >= count) {
                break;
            }
            if (temp[i].equals("")) {
                break;
            }
            if (i > 0 && temp[i].equals(temp[i - 1])) {
                continue;
            }
            result[index] = temp[i];
            index++;
        }
        return result;
    }

    /**
     * Form result text. Change the original ciphered characters into the found
     * key values.
     *
     * @param text original ciphered text
     * @return text with ciphered characters changed to key values
     */
    public String formResult(String text) {
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
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
        return resultText;
    }

    /**
     * Decryption by word. Goes through words in this.cipherwords array and
     * tries to find key values for ciphered characters. Keys for characters are
     * selected by availability and closest frequency.
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
            if (errors + 1 <= this.maxErrors) {
                return findDecryption(0, i + 1, errors + 1);
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
            return findDecryption(j + 1, i, errors);
        }

        // character has not been assigned a key value, so mark it as substituted now
        this.substituted[character] = true;
        this.firstAppearances[character] = i;
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

        // check if first unchanged letter of the word
        int earliestChanged = 0;
        for (int z = cipher.length() - 1; z >= 0; z--) {
            if (this.firstAppearances[cipher.charAt(z)] == i) {
                earliestChanged = z;
            }
        }
        if (j == earliestChanged) {
            // if arrived here, means that no possible substitutions were found for any of the unsubstituted characters in the word
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
