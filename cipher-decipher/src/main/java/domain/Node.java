package domain;

/**
 * Class that defines the nodes used in trie tree.
 *
 */
public class Node {

    private String value;
    private Node[] children;
    private boolean isFinal;
    private int numberOfChildren;

    public Node(String value) {
        this.value = value;
        this.children = new Node[5];
        this.isFinal = false;
        this.numberOfChildren = 0;
    }

    /**
     * Add a child node. If children list is full, its size is increased.
     *
     * @param node the child node that is added
     */
    public void addChild(Node node) {
        if (checkIfFull()) {
            increaseSize();
        }
        this.children[this.numberOfChildren] = node;
        this.numberOfChildren++;
    }

    /**
     * Check if list containing children is full.
     *
     * @return true if list was full, false if not.
     */
    public boolean checkIfFull() {
        return this.children.length == this.numberOfChildren + 1;
    }

    /**
     * Increase the list size by five.
     */
    public void increaseSize() {
        Node[] newList = new Node[this.children.length + 5];
        for (int i = 0; i < this.children.length; i++) {
            newList[i] = this.children[i];
        }
        this.children = newList;
    }

    /**
     * Find a child node with the specified value.
     *
     * @param value the value that is searched for
     */
    public Node getChild(String value) {
        for (int i = 0; i < this.children.length; i++) {
            if (this.children[i] != null && this.children[i].getValue().equals(value)) {
                return this.children[i];
            }
        }
        return null;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String letter) {
        this.value = letter;
    }

    public void setIsFinal(boolean value) {
        this.isFinal = value;
    }

    public boolean getIsFinal() {
        return this.isFinal;
    }
}
