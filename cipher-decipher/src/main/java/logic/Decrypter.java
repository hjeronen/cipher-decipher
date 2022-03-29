package logic;

import domain.Trie;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class that does the decryption.
 *
 */
public class Decrypter {

    private Trie dictionary;

    private double[] frequencies;

    private boolean[] taken;
    private boolean[] substituted;
    private char[] substitutions;

    private String result;

    private ArrayList<String> results;

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
                String[] parts = reader.nextLine().split(",");
                words.add(parts[1]);
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
     * @param length the length of the ciphered text
     */
    public void initializeArrays(int length) {
        // form array for checking which letters have been used
        this.taken = new boolean[128];
        this.substitutions = new char[128];
        for (int i = 0; i < this.taken.length; i++) {
            this.taken[i] = false;
            this.substitutions[i] = '*';
        }

        // form array for checking if some letter has been substituted
        this.substituted = new boolean[length];
        for (int i = 0; i < this.substituted.length; i++) {
            this.substituted[i] = false;
        }

        // resultlist for possible words
        this.results = new ArrayList<String>();
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
        text = text.toLowerCase();

        initializeArrays(text.length());

        // find all unique letters in ciphertext and indexes for each letter
        HashMap<String, ArrayList<Integer>> indexes = new HashMap<String, ArrayList<Integer>>();
        String cipherLetters = "";

        for (int i = 0; i < text.length(); i++) {
            String character = "" + text.charAt(i);
            if (character.equals(" ")) {
                continue;
            }
            if (!indexes.containsKey(character)) {
                ArrayList<Integer> list = new ArrayList<>();
                indexes.put(character, list);
                cipherLetters += character;
            }
            indexes.get(character).add(i);
        }

        // find ciphertext's character frequencies
//        double[] freq = new double[128];
//        for (int i = 0; i < cipherLetters.length(); i++) {
//            freq[cipherLetters.charAt(i)] = (double) indexes.get("" + cipherLetters.charAt(i)).size() / text.length() * 100;
//        }
        // form StringBuilder array of all words in ciphertext, excluding non-letters
        StringBuilder[] sbarray = new StringBuilder[text.length()];
        int index = 0;
        String word = "";
        String abc = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < text.length(); i++) {
            if (!abc.contains("" + text.charAt(i))) {
                if (!word.equals("")) {
                    sbarray[index] = new StringBuilder(word);
                    index++;
                    word = "";
                }
                continue;
            }
            word += text.charAt(i);
            // remember to add last word
            if (i == text.length() - 1) {
                sbarray[index] = new StringBuilder(word);
                index++;
                word = "";
            }
        }

        // decrypt each word in sbarray
//        for (int i = 0; i < sbarray.length; i++) {
//            if (sbarray[i] == null) {
//                break;
//            }
//
//            if (decypher(0, sbarray[i], sbarray[i].toString())) {
//                for (int j = 0; j < this.substitutions.length; j++) {
//                    if (this.substitutions[j] != '*') {
//                        sbarray[i].toString().replace((char) j, this.substitutions[j]);
//                    }
//                }
//            }
//        }
        // decrypt whole text
        String modifiedText = text.replaceAll("[^a-zA-Z\\d\\s:]", "");
        StringBuilder decryption = new StringBuilder(modifiedText);
        this.result = "";

        decypher(0, 0, decryption, modifiedText);

        System.out.println("results: ");
        for (String w : this.results) {
            System.out.println(w);
        }
        System.out.println(this.result);

        return this.result;
    }

    /**
     * Deciphering method. Will go through all possible substitutions with
     * backtracking and adds actual words to the result list. Takes forever to
     * compute. TODO: work out how to use frequencies
     *
     * This is still work in progress!
     */
    public boolean decypher(int start, int end, StringBuilder string, String cipher) {

        // check if reached the end of cipher-word
        if (end == cipher.length()) {
            if (findWord(string.toString().substring(start, end))) {
                this.result = string.toString();
                return true;
            }
            return false;
        // check if end of word
        } else if (cipher.charAt(end) == ' ') {
            if (findWord(string.toString().substring(start, end))) {
                return decypher(end + 1, end + 1, string, cipher);
            }
            return false;
        } else {
            // check if cipher char at i has been substituted, else try some char at it
            if (this.substituted[end]) {
                // move to next char
                return decypher(start, end + 1, string, cipher);
            } else {
                // possible letters for substitution
                String abc = "abcdefghijklmnopqrstuvwxyz";
                for (int j = 0; j < abc.length(); j++) {
                    // check if letter has been used
                    if (this.taken[abc.charAt(j)]) {
                        continue;
                    }
                    this.taken[abc.charAt(j)] = true;
                    // find all indexes of ciphered character
                    char character = cipher.charAt(end);
                    ArrayList<Integer> indexes = new ArrayList<Integer>();
                    for (int z = 0; z < cipher.length(); z++) {
                        if (cipher.charAt(z) == character) {
                            indexes.add(z);
                            this.substituted[z] = true;
                        }
                    }
                    // substitute those with char at j from abc
                    substitute(indexes, string, abc.charAt(j));
                    // call string(i + 1, string, cipher)
                    if (decypher(start, end + 1, string, cipher)) {
                        return true;
                    }
                    // undo substitution
                    substitute(indexes, string, character);
                    for (int integer : indexes) {
                        this.substituted[integer] = false;
                    }
                    // free letter and try next one
                    this.taken[abc.charAt(j)] = false;
                }
                return false;
            }
        }
    }

    /**
     * Find the closest frequency. Finds the char that has the same or closest
     * typical frequency as the ciphered char in cipher text. If the char is
     * already used as key another is chosen.
     *
     * @param freq fequency of the cipher char in cipher text
     * @return char with closest typical frequency
     */
    public char getClosestAvailableKey(double freq) {
        double closestFreq = 0;
        char closestChar = ' ';
        for (int i = Character.getNumericValue('a'); i < this.frequencies.length; i++) {
            if (this.taken[i]) {
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
