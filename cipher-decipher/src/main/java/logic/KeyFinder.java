package logic;

import domain.Trie;

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

    public KeyFinder(Trie dictionary) {
        this.dictionary = dictionary;
        setKeyFrequencies();
    }

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

    public void setCipherFrequencies(double[] frequencies) {
        this.cipherFrequencies = frequencies;
    }

    /**
     * Initializes the required arrays for decryption.
     *
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
     * Find a key for decrypting a ciphered text. The correct error margin is
     * found through binary search.
     *
     * @param cipherwords all the words from the encrypted text organized by
     * length
     * @param words copy of cipherwords, character substitutions are tried on
     * these
     * @param cipherLetters all the unique characters that appear in the
     * ciphered text
     * @return the key that can be used to decrypt the text
     */
    public char[] findKey(String[] cipherwords, StringBuilder[] words, String cipherLetters) {
        this.cipherwords = cipherwords;
        this.words = words;

        long start = System.currentTimeMillis();

        double percentage = 0;
        double lower = 0;
        double upper = 0.1;
        initializeArrays();
        this.maxErrors = (int) Math.floor(this.cipherwords.length * percentage);
        this.errorWords = new int[0];

        char[] prevKey = copyKey(this.substitutions);
        int prevWorkingNumberOfErrors = 0;
        int prevNumberOfErrors = 0;
        int keysFound = 0;

        while (true) {
            this.maxErrors = (int) Math.floor(this.cipherwords.length * percentage);
            if (keysFound > 0 && (prevNumberOfErrors == this.maxErrors || prevWorkingNumberOfErrors == this.maxErrors)) {
                this.substitutions = prevKey;
                break;
            }
            int[] allErrorWords = new int[this.maxErrors];
            for (int i = 0; i < allErrorWords.length; i++) {
                allErrorWords[i] = -1;
            }
            if (testKeyValues(0, 0, 0, allErrorWords)) {
                upper = percentage;
                prevWorkingNumberOfErrors = this.maxErrors;
                percentage = (percentage + lower) / 2;
                prevKey = copyKey(this.substitutions);
                keysFound++;
            } else {
                if (percentage == 0) {
                    percentage = upper;
                    continue;
                }
                if (keysFound == 0) {
                    lower = percentage;
                    percentage *= 2;
                } else {
                    lower = percentage;
                    percentage = (upper + lower) / 2;
                }
            }
            undoErrorChars(this.errorWords);
            prevNumberOfErrors = this.maxErrors;
        }

        long stop = System.currentTimeMillis();
        System.out.println("time " + (stop - start));
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
     * characters. Keys for characters are selected by availability and closest
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
            // if all the characters were changed before this word, mark as error
            // else return false
            for (int z = 0; z < this.cipherwords[i].length(); z++) {
                if (this.firstAppearances[this.cipherwords[i].charAt(z)] == i) {
                    return false;
                }
            }
            if (errors + 1 <= this.maxErrors) {
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
                allErrorWords[errors] = i;
                return testKeyValues(0, i + 1, errors + 1, allErrorWords);
            }
        }

        return false;
    }

    /**
     * Find the closest frequency. Finds the key that has the same or closest
     * typical frequency as the ciphered character in cipher text. If the key is
     * already used or tried for this character, another is chosen.
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
     * Find a word from the dictionary.
     *
     * @param word the word that is searched
     * @return true if word was found, false if not
     */
    public boolean findWord(String word) {
        return this.dictionary.findWord(word.toLowerCase());
    }

    /**
     * Find a substring from the dictionary.
     *
     * @param string the substring that is searched
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
