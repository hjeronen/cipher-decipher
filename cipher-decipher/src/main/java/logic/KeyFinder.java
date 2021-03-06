package logic;

import domain.Trie;

/**
 * A class that finds the key for decrypting an encrypted text.
 */
public class KeyFinder {

    private Trie dictionary;

    private double[] frequencies;
    private double[] cipherFrequencies;

    private boolean[] taken;
    private boolean[] substituted;
    private char[] substitutions;
    private int[] firstAppearances;

    private String[] cipherwords;
    private StringBuilder[] words;

    private int maxErrors;

    private int[] errorWords;

    private boolean workingKeyFound;

    /**
     * KeyFinder constructor. Will set the given trie as a dictionary where the
     * words are checked. Also sets the known frequencies for letters in texts
     * that are written in English.
     *
     * @param dictionary the trie where dictionary words are saved and where
     * formed words are looked up
     */
    public KeyFinder(Trie dictionary) {
        this.dictionary = dictionary;
        setKeyFrequencies();
    }

    public char[] getSubstitutions() {
        return this.substitutions;
    }

    public void setSubstitutions(char[] array) {
        this.substitutions = array;
    }

    public void setTaken(boolean[] array) {
        this.taken = array;
    }

    public boolean[] getTaken() {
        return this.taken;
    }

    public void setSubstituted(boolean[] array) {
        this.substituted = array;
    }

    public boolean[] getSubstituted() {
        return this.substituted;
    }

    /**
     * Sets the typical frequencies that characters have in texts that are
     * written in english.
     *
     * The values are taken from here:
     * https://www.101computing.net/frequency-analysis/
     */
    private void setKeyFrequencies() {
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
    }

    /**
     * Initializes the required arrays for decryption.
     */
    public void initializeArrays() {
        this.taken = new boolean[128];
        this.substituted = new boolean[128];
        this.substitutions = new char[128];
        this.firstAppearances = new int[128];
        for (int i = 0; i < this.taken.length; i++) {
            this.taken[i] = false;
            this.substituted[i] = false;
            this.substitutions[i] = '*';
            this.firstAppearances[i] = 0;
        }
    }

    /**
     * Set the required word arrays for decryption.
     *
     * @param cipherwords the original cipher words
     * @param words the words on which the character substitutions are tried
     * @param cipherFrequencies the frequencies of cipher characters
     */
    public void setArrays(String[] cipherwords, StringBuilder[] words, double[] cipherFrequencies) {
        this.cipherwords = cipherwords;
        this.words = words;
        this.cipherFrequencies = cipherFrequencies;
    }

    /**
     * Find a key for decrypting a ciphered text. The required word arrays,
     * ciphered text's letters and their frequencies are given as parameters.
     * The decryption will start with the error margin of 10%. This means that
     * 10% of words that are decrypted with these character substitutions are
     * allowed not to be found in the dictionary. If this does not produce any
     * working key, the error margin is doubled. Once a high enough error margin
     * is found (some possible key is found with it), the amount of errors is
     * decreased by one and the decryption is attempted again. This is continued
     * until the amount of errors is too small to produce a working key, and the
     * previous found key is returned. This will ensure the best accuracy and
     * performance time for decryption.
     *
     * @param cipherwords all the words from the encrypted text organized by
     * length
     * @param words copy of cipherwords, character substitutions are tried on
     * these
     * @param cipherLetters all the unique characters that appear in the cipher
     * text
     * @param cipherFrequencies the frequencies of characters in cipher text
     * @return the key that can be used to decrypt the text
     */
    public char[] findKey(String[] cipherwords, StringBuilder[] words, String cipherLetters, double[] cipherFrequencies) {
        setArrays(cipherwords, words, cipherFrequencies);
        double errorMargin = 0.1;
        initializeArrays();
        this.maxErrors = (int) Math.floor(this.cipherwords.length * errorMargin);
        this.errorWords = new int[0];
        this.workingKeyFound = false;

        char[] prevKey = copyKey(this.substitutions);
        int keysFound = 0;

        while (true) {
            int[] allErrorWords = new int[this.maxErrors];
            for (int i = 0; i < allErrorWords.length; i++) {
                allErrorWords[i] = -1;
            }
            if (testKeyValues(0, 0, 0, allErrorWords)) {
                prevKey = copyKey(this.substitutions);
                if (this.maxErrors == 0) {
                    break;
                }
                this.maxErrors--;
                this.workingKeyFound = true;
                keysFound++;
            } else {
                if (keysFound > 0) {
                    break;
                }
                errorMargin *= 2;
                this.maxErrors = (int) Math.floor(this.cipherwords.length * errorMargin);
            }
            undoErrorChars(this.errorWords);
        }

        this.substitutions = prevKey;
        setFoundKeys(cipherLetters);
        checkForUnsubstitutedCharacters(cipherLetters);

        return this.substitutions;
    }

    /**
     * Undo substitutions for characters in error words. The ciphered characters
     * that occur in words that were marked as errors are 'reset' and keys used
     * for them freed, so the next decryption attempt will only handle these.
     *
     * @param errorWords indexes of words that were marked as errors
     */
    public void undoErrorChars(int[] errorWords) {
        for (int i = 0; i < errorWords.length; i++) {
            if (errorWords[i] == -1) {
                break;
            }
            String word = this.cipherwords[errorWords[i]];
            for (int j = 0; j < word.length(); j++) {
                char character = word.charAt(j);
                this.substituted[character] = false;
                this.taken[this.substitutions[character]] = false;
                this.substitutions[character] = '*';
            }
        }
    }

    /**
     * Copy a substitution array to a new array.
     *
     * @param key list of substitutions that is copied
     * @return copy of the given list
     */
    public char[] copyKey(char[] key) {
        char[] copy = new char[key.length];
        for (int i = 0; i < key.length; i++) {
            copy[i] = key[i];
        }
        return copy;
    }

    /**
     * Mark decrypted characters and taken keys as true. These values are reset
     * in between decryption attempts. When the key finding loop breaks, the
     * found values are in this.substitutions. For checking if some ciphered
     * letters have not been set a key value, the decrypted characters and used
     * keys need to be marked as true.
     *
     * @param cipherLetters all characters that appear in cipher text
     */
    public void setFoundKeys(String cipherLetters) {
        for (int i = 0; i < cipherLetters.length(); i++) {
            if (this.substitutions[cipherLetters.charAt(i)] == '*') {
                continue;
            }
            this.substituted[cipherLetters.charAt(i)] = true;
            this.taken[this.substitutions[cipherLetters.charAt(i)]] = true;
        }
    }

    /**
     * Test key values for ciphered characters. Goes through words in
     * this.cipherwords array and tries to find key values for ciphered
     * characters that produce sensible words (that are found from the
     * dictionary). Keys for characters are selected by availability and closest
     * frequency.
     *
     * @param j character index
     * @param i word index
     * @param errors amount of words that could not be decrypted
     * @param allErrorWords indexes of words that were marked as errors
     * @return true if suitable decryption was found, false if not
     */
    public boolean testKeyValues(int j, int i, int errors, int[] allErrorWords) {
        if (i == this.cipherwords.length) {
            this.errorWords = allErrorWords;
            return true;
        }
        if (j == this.cipherwords[i].length()) {
            if (findWord(this.words[i].toString())) {
                return testKeyValues(0, i + 1, errors, allErrorWords);
            }
            // if all the characters in the word were changed before this word, mark as error
            // else return false
            for (int z = 0; z < this.cipherwords[i].length(); z++) {
                if (this.firstAppearances[this.cipherwords[i].charAt(z)] == i) {
                    return false;
                }
            }
            if (errors + 1 <= this.maxErrors) {
                // previously successfully translated words should not be marked as errors
                // enhances detecting that the used error margin is too small
                if (this.workingKeyFound) {
                    if (!checkIfIntInList(this.errorWords, i)) {
                        return false;
                    }
                }
                allErrorWords[errors] = i;
                return testKeyValues(0, i + 1, errors + 1, allErrorWords);
            }
            return false;
        }
        StringBuilder word = this.words[i];
        String cipher = this.cipherwords[i];
        char character = cipher.charAt(j);

        if (this.substituted[character]) {
            word.setCharAt(j, this.substitutions[character]);
            return testKeyValues(j + 1, i, errors, allErrorWords);
        }

        this.substituted[character] = true;
        this.firstAppearances[character] = i;

        char[] testedKeys = new char[26];
        int index = 0;

        double freq = this.cipherFrequencies[character];
        char key = getClosestAvailableKey(testedKeys, freq);

        // while there are available key values that have not been tried (empty char means out of keys)
        while (key != ' ') {
            this.taken[key] = true;
            this.substitutions[character] = key;

            testedKeys[index] = key;
            index++;

            word.setCharAt(j, key);

            if (findSubstring(word.substring(0, j + 1))) {
                if (testKeyValues(j + 1, i, errors, allErrorWords)) {
                    return true;
                }
            }

            this.taken[key] = false;
            key = getClosestAvailableKey(testedKeys, freq);
        }

        word.setCharAt(j, character);
        this.substituted[character] = false;
        this.substitutions[character] = '*';

        int earliestChanged = 0;
        for (int z = cipher.length() - 1; z >= 0; z--) {
            if (this.firstAppearances[cipher.charAt(z)] == i) {
                earliestChanged = z;
            }
        }
        if (j == earliestChanged) {
            // if first changed letter of the word, can be concluded that no possible 
            // substitutions were found for any of the unsubstituted characters in the word
            if (errors + 1 <= this.maxErrors) {
                if (this.workingKeyFound) {
                    if (!checkIfIntInList(this.errorWords, i)) {
                        return false;
                    }
                }
                allErrorWords[errors] = i;
                return testKeyValues(0, i + 1, errors + 1, allErrorWords);
            }
        }
        return false;
    }

    /**
     * Find the closest frequency. Finds the key that has the same or closest
     * typical frequency as the ciphered character in cipher text. If the key is
     * already used or has been tried for this character, another is chosen.
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
     * Check if an integer is on the given list.
     *
     * @param list a list of integers
     * @param i integer that is searched for
     * @return true if i was on the list, false if not
     */
    public boolean checkIfIntInList(int[] list, int i) {
        for (int j = 0; j < list.length; j++) {
            if (list[j] == i) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find a word from the dictionary.
     *
     * @param word the word that is searched for
     * @return true if word was found, false if not
     */
    public boolean findWord(String word) {
        return this.dictionary.findWord(word.toLowerCase());
    }

    /**
     * Find a substring from the dictionary.
     *
     * @param string the substring that is searched for
     * @return true if substring was found, false if not
     */
    public boolean findSubstring(String string) {
        return this.dictionary.findSubstring(string.toLowerCase());
    }

    /**
     * Check if some ciphered letters have not been set a key value. If some
     * cipher character has not been set a key value, choose a character with
     * closest frequency from key values that have not been used yet.
     *
     * @param cipherLetters all unique characters that are used in the cipher
     * text
     */
    public void checkForUnsubstitutedCharacters(String cipherLetters) {
        for (int i = 0; i < cipherLetters.length(); i++) {
            if (this.substitutions[cipherLetters.charAt(i)] == '*') {
                double freq = this.cipherFrequencies[cipherLetters.charAt(i)];
                this.substitutions[cipherLetters.charAt(i)] = getClosestAvailableKey(new char[1], freq);
            }
        }
    }
}
