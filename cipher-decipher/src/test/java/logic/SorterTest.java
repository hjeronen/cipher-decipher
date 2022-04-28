
package logic;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SorterTest {
    private Sorter sorter;
    
    
    public SorterTest() {
        this.sorter = new Sorter();
    }
    
    @Test
    public void sortByLengthSortsWordsByLengthInDescendingOrder() {
        String[] test = new String[]{"long", "reallylong", "verylong", "longer"};
        this.sorter.sortByLength(test);
        assertEquals(test[0].toString(), "reallylong");
        assertEquals(test[1].toString(), "verylong");
        assertEquals(test[2].toString(), "longer");
        assertEquals(test[3].toString(), "long");
    }
    
    @Test
    public void sortAlphabeticallySortsWordsAlphabetically() {
        String[] test = new String[]{"long", "reallylong", "verylong", "longer"};
        this.sorter.sortAlphabetically(test);
        assertEquals("long", test[0]);
        assertEquals("longer", test[1]);
        assertEquals("reallylong", test[2]);
        assertEquals("verylong", test[3]);
    }
    
    @Test
    public void sortWordsSortsWordsByLengthAndAlphabeticallyInDescendingOrder() {
        String[] test = new String[]{"apple", "banana", "ball", "cafe", "at"};
        this.sorter.sortWords(test);
        assertEquals("banana", test[0]);
        assertEquals("apple", test[1]);
        assertEquals("ball", test[2]);
        assertEquals("cafe", test[3]);
        assertEquals("at", test[4]);
        
    }
    
    @Test
    public void sortWordsSortsSameLengthWordsInAlphabeticalOrder() {
        String[] test = new String[]{"it", "if", "of", "at", "it"};
        this.sorter.sortWords(test);
        assertEquals("at", test[0]);
        assertEquals("if", test[1]);
        assertEquals("it", test[2]);
        assertEquals("it", test[3]);
        assertEquals("of", test[4]);
    }
}
