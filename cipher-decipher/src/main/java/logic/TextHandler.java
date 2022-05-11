package logic;

/**
 * A class for text handling. From original input text it will remove line
 * breaks and non-letter characters and change characters to lower case. The
 * modified text is broken into words and saved into word arrays. Will also find
 * character frequencies for the ciphered text, find all unique letters and form
 * the result text by using a decryption key. Sorter is used for sorting the
 * words.
 */
public class TextHandler {

    private Sorter sorter;

    public TextHandler() {
        this.sorter = new Sorter();
    }

    /**
     * Clean text. Removes all line breaks, numbers and special characters from
     * the text and changes letters into lower case.
     *
     * @param text the input text from the user
     * @return text in lower case and without special characters
     */
    public String cleanText(String text) {
        String modifiedText = text.replaceAll("\\R+", " ");
        modifiedText = modifiedText.replaceAll("[0-9]", "");
        modifiedText = modifiedText.toLowerCase().replaceAll("[^a-zA-Z\\d\\s:]", "");
        return modifiedText;
    }

    /**
     * Get character frequencies in a text. Saves the frequencies in the
     * frequencies array at the place of the character in question.
     *
     * @param text text in lower case and without special characters
     * @return array of character frequencies
     */
    public double[] findCharacterFrequencies(String text) {
        // find all unique characters in text, their counts and total amount of characters, excluding spaces
        int[] counts = new int[128];
        String characters = "";
        int total = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                continue;
            }
            total++;
            if (!characters.contains("" + text.charAt(i))) {
                characters += text.charAt(i);
            }
            counts[text.charAt(i)] += 1;
        }

        // save ciphertext's character frequencies
        double[] frequencies = new double[128];
        for (int i = 0; i < characters.length(); i++) {
            frequencies[characters.charAt(i)] = (double) counts[characters.charAt(i)] / total * 100;
        }
        return frequencies;
    }

    /**
     * Find all the unique characters that are used in the text.
     *
     * @param text text from which the characters are checked
     * @return all the unique characters as a single String
     */
    public String getAllUsedCharacters(String text) {
        String characters = "";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                continue;
            }
            if (!characters.contains("" + text.charAt(i))) {
                characters += text.charAt(i);
            }
        }
        return characters;
    }

    /**
     * Break text into words and save it into an array. The words are ordered
     * alphabetically and by length (from longest to shortest). Empty spaces and
     * duplicate words are cleaned out.
     *
     * @param text the text that is broken into words
     * @param maxTextLength the maximum amount of characters that should not be
     * exceeded
     * @return an array of words from the text
     */
    public String[] getWordListString(String text, int maxTextLength) {
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
            if (length + temp[i].length() <= maxTextLength) {
                length += temp[i].length();
                count++;
            } else {
                break;
            }
        }
        // the returned wordlist
        String[] words = new String[count];
        int index = 0;
        for (int i = 0; i < temp.length; i++) {
            if (index >= count) {
                break;
            }
            if (i > 0 && temp[i].equals(temp[i - 1])) {
                continue;
            }
            words[index] = temp[i];
            index++;
        }
        return words;
    }

    /**
     * Copy words from a list to a StringBuilder array.
     *
     * @param list the list of words that is copied
     * @return a StringBuilder array of words from the list
     */
    public StringBuilder[] copyWordListToStringBuilder(String[] list) {
        // words-list has words where substitutions are tried on
        StringBuilder[] words = new StringBuilder[list.length];
        for (int i = 0; i < list.length; i++) {
            words[i] = new StringBuilder(list[i]);
        }
        return words;
    }

    /**
     * Form result text. Change the original ciphered characters into the found
     * key values. Non-letters are added as they are. For upper vase characters,
     * keys are changed to uppercase as well.
     *
     * @param text original ciphered text
     * @return text with ciphered characters changed to key values
     */
    public String formResult(String text, char[] substitutions) {
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String resultText = "";
        for (int i = 0; i < text.length(); i++) {
            if (!abc.contains("" + text.charAt(i))) {
                resultText += text.charAt(i);
                continue;
            }
            char character = text.charAt(i);
            if (Character.isUpperCase(character)) {
                char c = substitutions[Character.toLowerCase(character)];
                resultText += Character.toUpperCase(c);
                continue;
            }
            resultText += substitutions[character];
        }
        return resultText;
    }
}
