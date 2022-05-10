package util;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

/**
 * A class that produces test texts for the tester classes.
 *
 */
public class TextFactory {

    private Crypter crypter;
    private String[] words;

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
        try {
            Scanner reader = new Scanner(new File(filename));
            while (reader.hasNext()) {
                String word = reader.nextLine();
                if (word.length() == 1 && !word.equals("i") && !word.equals("a")) {
                    continue;
                }
                this.words[index] = word;
                index++;
            }
        } catch (Exception exception) {
            System.out.println("Dictionary creation failed.");
        }
    }

    private int countWords(String filename) {
        int count = 0;
        try {
            Scanner reader = new Scanner(new File(filename));
            while (reader.hasNext()) {
                String word = reader.nextLine();
                if (word.length() == 1 && !word.equals("i") && !word.equals("a")) {
                    continue;
                }
                count++;
            }
        } catch (Exception exception) {
            System.out.println("Dictionary creation failed.");
        }
        return count;
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
            String errorWord = "";
            int wordLength = 5 + ran.nextInt(10);
            for (int j = 0; j < wordLength; j++) {
                char letter = (char) (97 + ran.nextInt(26));
                errorWord += letter;
            }
            text += errorWord;
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
        Random ran = new Random();
        for (int i = 0; i < errors; i++) {
            String errorWord = "";
            int wordLength = 5 + ran.nextInt(10);
            for (int j = 0; j < wordLength; j++) {
                char letter = (char) (97 + ran.nextInt(26));
                errorWord += letter;
            }
            wordlist[i] = errorWord;
        }
        String resultText = "";
        for (int i = 0; i < wordlist.length; i++) {
            if (wordlist[i].equals(" ")) {
                continue;
            }
            resultText += wordlist[i];
            resultText += " ";
        }
        return resultText;
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
