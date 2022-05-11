
package domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {
    
    public NodeTest() {
    }
    
    @Test
    public void aChildCanBeAddedForTheNode() {
        Node node = new Node("");
        Node[] children = node.getChildren();
        assertTrue(children[0] == null);
        node.addChild(new Node("a"));
        children = node.getChildren();
        assertTrue(children[0] != null);
        assertEquals("a", children[0].getValue());
    }
    
    @Test
    public void addingAChildIncreasesTheAmountOfChildren() {
        Node node = new Node("");
        int children = node.getNumberOfChildren();
        assertTrue(children == 0);
        
        node.addChild(new Node("a"));
        children = node.getNumberOfChildren();
        assertTrue(children == 1);
    }
    
    @Test
    public void aChildWithDesiredValueIsReturned() {
        Node node = new Node("");
        node.addChild(new Node("a"));
        node.addChild(new Node("b"));
        node.addChild(new Node("c"));
        Node returned = node.getChild("b");
        
        assertEquals("b", returned.getValue());
    }
    
    @Test
    public void ifChildWithDesiredValueIsNotFoundNullIsReturned() {
        Node node = new Node("");
        node.addChild(new Node("a"));
        node.addChild(new Node("b"));
        Node returned = node.getChild("c");
        
        assertNull(returned);
    }
    
    @Test
    public void listOfChildrenIsIncreasedWhenFull() {
        Node node = new Node("");
        node.addChild(new Node("a"));
        node.addChild(new Node("b"));
        node.addChild(new Node("c"));
        node.addChild(new Node("d"));
        node.addChild(new Node("e"));
        
        Node[] children = node.getChildren();
        assertTrue(children.length == 5);
        
        node.addChild(new Node("e"));
        children = node.getChildren();
        assertTrue(children.length == 10);
    }
    
    @Test
    public void checkIfFullReturnsTrueIfChildrenListIsFull() {
        Node node = new Node("");
        node.addChild(new Node("a"));
        node.addChild(new Node("b"));
        node.addChild(new Node("c"));
        node.addChild(new Node("d"));
        node.addChild(new Node("e"));
        
        assertTrue(node.checkIfFull());
    }
    
    @Test
    public void checkIfFullReturnsFalseIfChildrenListIsNotFull() {
        Node node = new Node("");
        node.addChild(new Node("a"));
        node.addChild(new Node("b"));
        node.addChild(new Node("c"));
        node.addChild(new Node("d"));
        
        assertFalse(node.checkIfFull());
    }
    
    @Test
    public void increaseSizeAddsFiveToChildrenListSize() {
        Node node = new Node("");
        Node[] children = node.getChildren();
        assertTrue(children.length == 5);
        
        
        node.increaseSize();
        children = node.getChildren();
        assertTrue(children.length == 10);
    }
}
