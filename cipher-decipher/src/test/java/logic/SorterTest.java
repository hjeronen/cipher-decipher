
package logic;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SorterTest {
    private Sorter sorter;
    
    public SorterTest() {
        this.sorter = new Sorter();
    }
    
    @Test
    public void sorterSortsWordsByLengthInDescendingOrder() {
        String[] test = new String[]{"long", "reallylong", "verylong", "longer"};
        this.sorter.sortWords(test);
        assertEquals(test[0].toString(), "reallylong");
        assertEquals(test[1].toString(), "verylong");
        assertEquals(test[2].toString(), "longer");
        assertEquals(test[3].toString(), "long");
    }
}
