package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * A class that generates test texts for the tester classes.
 *
 */
public class TextFactory {

    private Crypter crypter;
    private String[] words;

    /**
     * Constructor for the TextFactory class.
     *
     * @param filename the name of the file that has the words that are used in the dictionary
     * @param crypter crypter that is used to encrypt the text
     */
    public TextFactory(String filename, Crypter crypter) {
        this.crypter = crypter;
        getTestWords(filename);
    }

    /**
     * Save all the words to a list.
     *
     * @param filename the file where the words are read from
     */
    private void getTestWords(String filename) {
        this.words = new String[countWords(filename)];
        int index = 0;
        try ( InputStream in = getClass().getResourceAsStream(filename);  BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String word;
            while ((word = reader.readLine()) != null) {
                if (word.length() == 1 && !word.equals("i") && !word.equals("a")) {
                    continue;
                }
                this.words[index] = word;
                index++;
            }
        } catch (Exception exception) {
            System.out.println("File not found.");
        }
    }

    /**
     * Count words in the word list. Need to know the amount of words to form
     * the right length of list.
     *
     * @param filename the file where the words are read from
     * @return the number of words in the file
     */
    public int countWords(String filename) {
        int count = 0;
        try ( InputStream in = getClass().getResourceAsStream(filename);  BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String word;
            while ((word = reader.readLine()) != null) {
                if (word.length() == 1 && !word.equals("i") && !word.equals("a")) {
                    continue;
                }
                count++;
            }
        } catch (Exception exception) {
            System.out.println("File not found.");
        }
        return count;
    }

    /**
     * Get the word list that is used. Need this for tests.
     *
     * @return word list
     */
    public String[] getWords() {
        return this.words;
    }

    /**
     * Form a text. Picks words from the word list at random and adds them
     * together as a text. The number of error words is deducted from the total
     * number of words. The error words are 5-15 characters long and also formed
     * randomly, and then added at the end of the text. The resulting string is
     * then returned.
     *
     * @param numberOfWords the total number of words in the text
     * @param errors number of error words
     * @return the formed text
     */
    public String formRandomText(int numberOfWords, int errors) {
        numberOfWords -= errors;
        String text = "";
        Random ran = new Random();
        for (int i = 0; i < numberOfWords; i++) {
            int index = ran.nextInt(this.words.length);
            text += this.words[index];
            text += " ";
        }
        for (int i = 0; i < errors; i++) {
            text += getRandomErrorWord();
            text += " ";
        }
        return text;
    }

    /**
     * Add errors to a text. Forms error words randomly and adds them to the
     * given text. The text is first split into an array and then some words are
     * replaced with error words so that the total amount of words in the text
     * will not change.
     *
     * @param text the text where errors are added
     * @param errors number of error words
     * @return the text with some words replaced with error words
     */
    public String addErrorsToText(String text, int errors) {
        String[] wordlist = text.split(" ");
        for (int i = 0; i < errors; i++) {
            wordlist[i] = getRandomErrorWord();
        }
        String resultText = "";
        for (int i = 0; i < wordlist.length; i++) {
            resultText += wordlist[i];
            resultText += " ";
        }
        return resultText;
    }

    /**
     * Get a randomly formed error word. Random picks random integers from
     * between 97 to 122. These are changed into characters and then added to
     * the word. The word length is picked randomly and should be 5-15
     * characters.
     *
     * @return the randomly formed word
     */
    public String getRandomErrorWord() {
        Random ran = new Random();
        String errorWord = "";
        int wordLength = 5 + ran.nextInt(11);
        for (int j = 0; j < wordLength; j++) {
            char letter = (char) (97 + ran.nextInt(26));
            errorWord += letter;
        }
        return errorWord;
    }

    /**
     * Encrypt the given text.
     *
     * @param text text that is to be encrypted
     * @return encrypted text
     */
    public String formCipher(String text) {
        return this.crypter.encrypt(text);
    }
}
