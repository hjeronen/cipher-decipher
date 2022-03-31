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
    private double[] cipherFrequencies;

    private boolean[] taken;
    private boolean[] substituted;

    private String result;

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
        // form arrays for checking which letters have been used and for saving the frequencies of cipher text characters
        this.taken = new boolean[128];
        this.cipherFrequencies = new double[128];
        for (int i = 0; i < this.taken.length; i++) {
            this.taken[i] = false;
            this.cipherFrequencies[i] = 0;
        }
        // form array for checking if some letter has been substituted
        this.substituted = new boolean[length];
        for (int i = 0; i < this.substituted.length; i++) {
            this.substituted[i] = false;
        }
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
        // cleanup text
        text = text.toLowerCase();
        String modifiedText = text.replaceAll("[^a-zA-Z\\d\\s:]", "");
        StringBuilder decryption = new StringBuilder(modifiedText);
        this.result = "";

        initializeArrays(text.length());

        // find all unique letters in ciphertext and indexes for each letter for counting frequencies - could do this with a int[]
        HashMap<String, ArrayList<Integer>> indexes = new HashMap<String, ArrayList<Integer>>();
        String cipherLetters = "";
        String abc = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < text.length(); i++) {
            String character = "" + text.charAt(i);
            if (!abc.contains(character)) {
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
        for (int i = 0; i < cipherLetters.length(); i++) {
            this.cipherFrequencies[cipherLetters.charAt(i)] = (double) indexes.get("" + cipherLetters.charAt(i)).size() / text.length() * 100;
        }

        // decrypt whole text
        decypher(0, 0, decryption, modifiedText);

        return this.result;
    }

    /**
     * Deciphering method. Uses character frequencies to pick the most likely
     * substitutions. Will stop at the first possible solution. Keeps track of
     * separate words with start and end indexes.
     *
     * @param start starting index of a word in text
     * @param end end index of a word in text
     * @param string string where the substitutions are made
     * @param cipher original ciphertext
     * @return true if suitable decryption was found, false if not
     */
    public boolean decypher(int start, int end, StringBuilder string, String cipher) {
        // check if reached the end of ciphertext
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
            // check if cipher char at i has been substituted, else try some letter at it
            if (this.substituted[end]) {
                // move to next char
                return decypher(start, end + 1, string, cipher);
            } else {
                // list for letters that have been tried
                char[] used = new char[128];
                int index = 0;
                char character = cipher.charAt(end);

                // find all indexes of the ciphered character
                ArrayList<Integer> indexes = new ArrayList<Integer>();
                for (int z = 0; z < cipher.length(); z++) {
                    if (cipher.charAt(z) == character) {
                        indexes.add(z);
                    }
                }
                
                // get the frequency of ciphered character in cipher text
                double freq = this.cipherFrequencies[character];
                // find letter with closest frequency
                char letter = getClosestAvailableKey(used, freq);

                // loop until out of substitute letters
                while (letter != ' ') {
                    // mark the substitute letter as taken and add it to the list of tried letters
                    this.taken[letter] = true;
                    used[index] = letter;
                    index++;
                    
                    // mark the indexes that are substituted
                    for (int i : indexes) {
                        this.substituted[i] = true;
                    }

                    // substitute characters in string with letter
                    substitute(indexes, string, letter);

                    // if looks reasonable, move forward
                    if (findSubstring(string.substring(start, end + 1))) {
                        // if string(i + 1, string, cipher) returns true, break loop
                        if (decypher(start, end + 1, string, cipher)) {
                            return true;
                        }
                    }

                    // undo substitution
                    substitute(indexes, string, character);
                    for (int i : indexes) {
                        this.substituted[i] = false;
                    }

                    // free letter and try next one
                    this.taken[letter] = false;
                    letter = getClosestAvailableKey(used, freq);
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
     * @param used a list of characters that have already been tried
     * @param freq frequency of the cipher char in cipher text
     * @return char with closest typical frequency
     */
    public char getClosestAvailableKey(char[] used, double freq) {
        double closestFreq = 1000;
        char closestChar = ' ';
        for (int i = Character.getNumericValue('a'); i < this.frequencies.length; i++) {
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
