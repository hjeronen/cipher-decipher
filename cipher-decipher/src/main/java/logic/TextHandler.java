package logic;

/**
 * A class for text handling (clean out non-letter characters, form word arrays
 * etc.)
 */
public class TextHandler {

    private Sorter sorter;

    public TextHandler() {
        this.sorter = new Sorter();
    }

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

    public String[] getWordListString(String text, int maxTextLength) {
        String[] temp = text.split(" ");
        // sort words alphabetically and from longest to shortest
        this.sorter.sortWords(temp);
        // cleaning out extra spaces and duplicate words
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
            if (temp[i].equals("")) {
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
     * key values.
     *
     * @param text original ciphered text
     * @return text with ciphered characters changed to key values
     */
    public String formResult(String text, char[] substitutions) {
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
                char c = substitutions[Character.toLowerCase(character)];
                resultText += Character.toUpperCase(c);
                continue;
            }
            // add into result text the key for character
            resultText += substitutions[character];
        }
        return resultText;
    }
}
