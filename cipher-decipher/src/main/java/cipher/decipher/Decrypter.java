package cipher.decipher;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Decrypter {

    private Trie dictionary;

    public Decrypter(String filename) {
        this.dictionary = new Trie(new Node(""));
        createDictionary(filename);
    }

    private void createDictionary(String filename) {
        ArrayList<String> words = new ArrayList<String>();
        try {
            Scanner reader = new Scanner(new File(filename));
            while (reader.hasNext()) {
                words.add(reader.nextLine());
            }
        } catch (Exception exception) {
            System.out.println("Dictionary creation failed.");
        }
        this.dictionary.createTrie(words);
    }

    public boolean decypher(Node node, String string) {
        // TODO
        return false;
    }

    public String findWord(String word) {
        if (this.dictionary.find(word.toLowerCase())) {
            return "Found";
        }
        return "Not found";
    }
}
