package util;

import java.util.Random;

/**
 * A class for encrypting a text.
 *
 */
public class Crypter {

    /**
     * Constructor.
     */
    public Crypter() {

    }

    /**
     * Encrypt the given text. Forms a key that is used for encrypting the given
     * text. Returns an encryption of the text.
     *
     * @param text the text that is to be encrypted
     * @return the encrypted text with all characters in upper case
     */
    public String encrypt(String text) {
        text = text.toLowerCase();
        int[] key = formKey();
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String encryption = "";
        for (int i = 0; i < text.length(); i++) {
            if (!abc.contains("" + text.charAt(i))) {
                encryption += text.charAt(i);
                continue;
            }
            encryption += (char) key[text.charAt(i)];
        }
        return encryption.toUpperCase();
    }

    /**
     * Form key for encryption. Chooses a random letter from letters array for
     * each char from a-z in the key. The letter is then removed from the array.
     *
     * @return the key
     */
    public int[] formKey() {
        char[] letters = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        Random ran = new Random();
        int[] key = new int[123];
        for (int i = 97; i < 123; i++) {
            int x = ran.nextInt(letters.length);
            key[i] = letters[x];
            letters[x] = '*';
            letters = removeMissingChar(letters);
        }
        return key;
    }

    /**
     * Shorten the list by one. When formKey() removes a letter from the list,
     * it is replaced by an asterisk. Then removeMissingChar() will form a new
     * list without the missing letter and the letter list is replaced with
     * that.
     *
     * @param list the list that is shortened
     * @return new list without the asterisk char
     */
    public char[] removeMissingChar(char[] list) {
        char[] result = new char[list.length - 1];
        int index = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] == '*') {
                continue;
            }
            result[index] = list[i];
            index++;
        }
        return result;
    }
}
