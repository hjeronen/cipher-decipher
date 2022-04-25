package logic;

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
     * Get cipher text's character frequencies. Saves the frequencies in
     * this.cipherFrequencies array at the place of the character in question.
     *
     * @param text ciphered text in lower case and without special characters
     */
    public void findCharacterFrequencies(String text, double[] cipherFrequencies) {
        // find all unique letters in ciphertext, their counts and total amount of characters, excluding nonletters
        int[] counts = new int[128];
        String cipherLetters = "";
        int total = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                continue;
            }
            total++;
            if (!cipherLetters.contains("" + text.charAt(i))) {
                cipherLetters += text.charAt(i);
            }
            counts[text.charAt(i)] += 1;
        }

        // save ciphertext's character frequencies
        for (int i = 0; i < cipherLetters.length(); i++) {
            cipherFrequencies[cipherLetters.charAt(i)] = (double) counts[cipherLetters.charAt(i)] / total * 100;
        }
    }

    public String getAllUsedCharacters(String text) {
        String characters = "";
        for (int i = 0; i < text.length(); i++) {
            if (!characters.contains("" + text.charAt(i))) {
                characters += text.charAt(i);
            }
        }
        return characters;
    }

    public String[] getWordListString(String text) {
        String[] temp = text.split(" ");
        // sort words alphabetically and from longest to shortest
        this.sorter.sortWords(temp);
        // cleaning out extra spaces and duplicate words
        int count = 0;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("")) {
                break;
            }
            if (i > 0 && temp[i].equals(temp[i - 1])) {
                continue;
            }
            count++;
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

    public String[] downsizeText(String text, int maxTextLength) {
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

            if (length + temp[i].length() < maxTextLength) {
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
}
