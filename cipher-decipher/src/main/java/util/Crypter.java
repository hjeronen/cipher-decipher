package util;

import java.util.ArrayList;
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
        ArrayList<Character> letters = new ArrayList<Character>();
        String abc = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < abc.length(); i++) {
            letters.add(abc.charAt(i));
        }
        Random ran = new Random();
        this.key = new int[123];
        for (int i = 97; i < 123; i++) {
            int x = ran.nextInt(letters.size());
            this.key[i] = letters.get(x);
            letters.remove(x);
        }
    }
}