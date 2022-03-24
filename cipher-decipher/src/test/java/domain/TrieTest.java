
package domain;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TrieTest {
    
    ArrayList<String> testwords;
    Trie trie;
    
    public TrieTest() {
        
    }
    
    @Before
    public void setUp() {
        this.testwords = new ArrayList<String>();
        this.testwords.add("she");
        this.testwords.add("sea");
        this.testwords.add("shell");
        
        this.trie = new Trie();
        this.trie.createTrie(this.testwords);
    }
    
    @Test
    public void allInsertedWordsAreFound() {
        assertTrue(this.trie.findWord("she"));
        assertTrue(this.trie.findWord("sea"));
        assertTrue(this.trie.findWord("shell"));
    }
    
    @Test
    public void findWordDoesNotReturnSubstrings() {
        assertFalse(this.trie.findWord("se"));
    }
    
    @Test
    public void findSubstringFindsSubstrings() {
        assertTrue(this.trie.findSubstring("se"));
    }
    
    @Test
    public void findWordDoesNotFindNonWords() {
        assertFalse(this.trie.findWord("koira"));
    }
    
    @Test
    public void findSubstringDoesNotFindSubstringsThatAreNotPartOfAnyWord() {
        assertFalse(this.trie.findSubstring("xjg"));
    }
}
