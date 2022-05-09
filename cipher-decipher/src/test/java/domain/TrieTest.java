
package domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TrieTest {
    
    Trie trie;
    
    public TrieTest() {
        
    }
    
    @Before
    public void setUp() {
        this.trie = new Trie();
        this.trie.trieInsert("she");
        this.trie.trieInsert("sea");
        this.trie.trieInsert("shell");
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
