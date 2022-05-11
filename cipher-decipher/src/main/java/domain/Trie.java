package domain;

/**
 * Trie datastructure for saving a wordlist.
 *
 */
public class Trie {

    private Node root;

    public Trie() {
        this.root = new Node("");
    }

    /**
     * Inserts a word into the trie. Loops through the word's characters and 
     * finds the nodes that contain the character as a value. If no nodes with 
     * that value are found, a new node is created, with the character as its 
     * value. The last node is set as the final node of the word (marking where 
     * the word ends).
     * 
     * This function is done according to the pseudocode in wikipedia: https://en.wikipedia.org/wiki/Trie#Insertion
     * 
     * @param   key the word that is inserted to the trie
     */
    public void trieInsert(String key) {
        Node x = this.root;
        for (int i = 0; i < key.length(); i++) {
            if (x.getChild("" + key.charAt(i)) == null) {
                x.addChild(new Node("" + key.charAt(i)));
            }
            x = x.getChild("" + key.charAt(i));
        }
        x.setIsFinal(true);
    }

    /**
     * Find a substring from the trie.
     * 
     * This function is done according to the pseudocode in wikipedia: https://en.wikipedia.org/wiki/Trie#Searching
     * 
     * @param   key the substring that is searched for.
     * @return true if the key was found, false if not
     */
    public Boolean findSubstring(String key) {
        Node node = this.root;
        for (int i = 0; i < key.length(); i++) {
            if (node.getChild("" + key.charAt(i)) == null) {
                return false;
            }
            node = node.getChild("" + key.charAt(i));
        }
        return true;
    }

    /**
     * Find a word from the trie.
     * 
     * This function is done according to the pseudocode in wikipedia: https://en.wikipedia.org/wiki/Trie#Searching
     * 
     * @param   key the word that is searched for
     * @return true if the key was found, false if not
     */
    public Boolean findWord(String key) {
        Node node = this.root;
        for (int i = 0; i < key.length(); i++) {
            if (node.getChild("" + key.charAt(i)) == null) {
                return false;
            }
            node = node.getChild("" + key.charAt(i));
        }
        return node.getIsFinal();
    }
}
