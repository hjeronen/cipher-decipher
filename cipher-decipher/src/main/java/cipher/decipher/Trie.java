package cipher.decipher;

import java.util.ArrayList;

public class Trie {
    private Node root;
    
    public Trie(Node start) {
        this.root = start;
    }
    
    public Node getRoot() {
        return this.root;
    }
    
    public void createTrie(ArrayList<String> words) {
        for (String word : words) {
            trieInsert(this.root, word);
        }
    }

    public void trieInsert(Node x, String key) {
        for (int i = 0; i < key.length(); i++) {
            if (x.getChild("" + key.charAt(i)) == null) {
                x.addChild(new Node("" + key.charAt(i)));
            }
            x = x.getChild("" + key.charAt(i));
        }
    }

    public Boolean find(String key) {
        Node node = this.root;
        for (int i = 0; i < key.length(); i++) {
            if (node.getChild("" + key.charAt(i)) == null) {
                return false;
            }
            node = node.getChild("" + key.charAt(i));
        }
        return true;
    }
    
    /*
    public void getAll(Node node, String string, ArrayList<String> words) {
        for (Node child : node.getChildren()) {
            if (child.getChildren().isEmpty()) {
                words.add(string + child.getValue());
                continue;
            }
            getAll(child, child.getValue(), words);
        }
    }
    */
}
