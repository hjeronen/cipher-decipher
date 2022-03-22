package cipher.decipher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Decrypter {

    private Trie dictionary;
    private StringBuilder s;

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

    public String decypher(String text, String key) {
        String abc = "abcdefghijklmnopqrstuvwxyz";
        text = text.toLowerCase();
        System.out.println(text);
        this.s = new StringBuilder(text);
        key = key.toLowerCase();
        System.out.println(key);
        
        HashMap<String, ArrayList<Integer>> indexes = new HashMap<String, ArrayList<Integer>>();
        for (int i = 0; i < text.length(); i++) {
            if (!indexes.containsKey("" + text.charAt(i))) {
                ArrayList<Integer> list = new ArrayList<>();
                indexes.put("" + text.charAt(i), list);
            }
            indexes.get("" + text.charAt(i)).add(i);
        }
        
        this.s = new StringBuilder(text);
        
        for (int i = 0; i < key.length(); i++) {
            if (indexes.containsKey("" + key.charAt(i))) {
                substitute(indexes.get("" + key.charAt(i)), abc.charAt(i));
            }
        }
        
        return this.s.toString();
    }
    
    public void substitute(ArrayList<Integer> indexes, char value) {
        for (int index : indexes) {
            this.s.setCharAt(index, value);
        }
    }

    public String findWord(String word) {
        if (this.dictionary.find(word.toLowerCase())) {
            return "Found";
        }
        return "Not found";
    }
}
