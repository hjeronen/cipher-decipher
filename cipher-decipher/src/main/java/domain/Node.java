package domain;

import java.util.ArrayList;

/**
 * Class that defines the nodes used in trie tree.
 *
 */
public class Node {

    private String value;
    private ArrayList<Node> children;
    private boolean isFinal;

    public Node(String value) {
        this.value = value;
        this.children = new ArrayList<Node>();
        this.isFinal = false;
    }

    public void addChild(Node node) {
        this.children.add(node);
    }

    public ArrayList<Node> getChildren() {
        return this.children;
    }

    public Node getChild(String value) {
        for (Node node : this.children) {
            if (node.getValue().equals(value)) {
                return node;
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
