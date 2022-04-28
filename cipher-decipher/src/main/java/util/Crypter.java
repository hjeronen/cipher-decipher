package util;

import java.util.Random;

public class Crypter {

    private int[] key;

    public Crypter() {
        this.key = new int[123];
    }

    public String encrypt(String text) {
        text = text.toLowerCase();
        formKey();
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String encryption = "";
        for (int i = 0; i < text.length(); i++) {
            if (!abc.contains("" + text.charAt(i))) {
                encryption += text.charAt(i);
                continue;
            }
            encryption += (char) this.key[text.charAt(i)];
        }
        return encryption.toUpperCase();
    }

    public void formKey() {
        char[] letters = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 
                                    'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 
                                    's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        Random ran = new Random();
        this.key = new int[123];
        for (int i = 97; i < 123; i++) {
            int x = ran.nextInt(letters.length);
            this.key[i] = letters[x];
            letters[x] = '*';
            letters = removeOneStar(letters);
        }
    }
    
    public char[] removeOneStar(char[] list) {
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
    
    public int[] getKey() {
        return this.key;
    }
}