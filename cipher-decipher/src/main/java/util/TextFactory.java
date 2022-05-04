
package util;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TextFactory {
    private Crypter crypter;
    private ArrayList<String> words;
    
    public TextFactory(String filename, Crypter crypter) {
        this.crypter = crypter;
        this.words = new ArrayList<String>();
        getTestWords(filename);
    }
    
    private void getTestWords(String filename) {
        try {
            Scanner reader = new Scanner(new File(filename));
            while (reader.hasNext()) {
                String word = reader.nextLine();
                if (word.length() == 1 && !word.equals("i") && !word.equals("a")) {
                    continue;
                }
                this.words.add(word);
            }
        } catch (Exception exception) {
            System.out.println("Dictionary creation failed.");
        }
    }

    public String formRandomText(int numberOfWords, int errors) {
        numberOfWords -= errors;
        String text = "";
        Random ran = new Random();
        for (int i = 0; i < numberOfWords; i++) {
            int index = ran.nextInt(this.words.size());
            text += this.words.get(index);
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
    
    public String formCipher(String text) {
        return this.crypter.encrypt(text);
    }
}
