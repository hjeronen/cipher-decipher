
package cipher.decipher;

import java.util.ArrayList;

public class Node {
    private String value;
    private Node parent;
    private ArrayList<Node> children;
    
    public Node(String value) {
        this.value = value;
        this.children = new ArrayList<Node>();
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
    
    
}
