
package util;

import org.junit.Test;
import static org.junit.Assert.*;

public class CrypterTest {
    
    private Crypter crypter;
    
    public CrypterTest() {
        this.crypter = new Crypter();
    }
    
    @Test
    public void formKeyChoosesCharsAsKeyValues() {
        this.crypter.formKey();
        int[] key = this.crypter.getKey();
        for (int i = 97; i < 123; i++) {
            assertTrue(key[i] > 96 || key[i] < 123);
        }
    }
    
    @Test
    public void removeOneStarCopiesTheListExceptForTheStar() {
        char[] test = new char[]{'a', 'b', 'c', '*', 'e'};
        char[] result = this.crypter.removeOneStar(test);
        
        assertTrue(result.length == test.length - 1);
        
        assertTrue('a' == result[0]);
        assertTrue('b' == result[1]);
        assertTrue('c' == result[2]);
        assertTrue('e' == result[3]);
    }
    
    @Test
    public void encryptReturnsSomethingElseThanTheOriginalText() {
        String text = "This is a simple test text.";
        String encryption = this.crypter.encrypt(text);
        assertNotEquals(text.toUpperCase(), encryption);
    }
}
