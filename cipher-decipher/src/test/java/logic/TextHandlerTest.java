
package logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TextHandlerTest {
    private TextHandler texthandler;
    
    public TextHandlerTest() {
        this.texthandler = new TextHandler();
    }
    
    @Test
    public void cleanTextRemovesLinebreaks() {
        String text = "linebreaks\nhere";
        String result = this.texthandler.cleanText(text);
        assertEquals("linebreaks here", result);
    }
    
    @Test
    public void cleanTextRemovesNumbers() {
        String text = "testt3xt";
        String result = this.texthandler.cleanText(text);
        assertEquals("testtxt", result);
    }
    
    @Test
    public void cleanTextRemovesNonLetters() {
        String text = "F'rom´ *this* stri%ng #all (special) ch&aracters/ AND capital. letters. [should] =be @removed+?";
        String result = this.texthandler.cleanText(text);
        String expected = "from this string all special characters and capital letters should be removed";
        assertEquals(expected, result);
    }
    
    @Test
    public void findCharacterFrequenciesSavesCharacterFrequenciesInGivenArray() {
        String text = "this is a simple test text";
        double[] frequencies = new double[128];
        this.texthandler.findCharacterFrequencies(text, frequencies);
        // testing approximate values for simplicity
        assertTrue((int) frequencies['t'] == 23);
        assertTrue((int) frequencies['h'] == 4);
        assertTrue((int) frequencies['i'] == 14);
        assertTrue((int) frequencies['s'] == 19);
    }
    
    @Test
    public void getAllUsedCharactersReturnsAStringOfAllUniqueCharactersInText() {
        String text = "this is a simple test text";
        String result = this.texthandler.getAllUsedCharacters(text);
        assertEquals("thisamplex", result);
    }
    
    @Test
    public void getWordListStringReturnsAnOrderedListOfWords() {
        String text = "this is a simple test text";
        String[] list = this.texthandler.getWordListString(text, 2000);
        assertEquals("simple", list[0]);
        assertEquals("test", list[1]);
        assertEquals("text", list[2]);
        assertEquals("this", list[3]);
        assertEquals("is", list[4]);
        assertEquals("a", list[5]);
    }
    
    @Test
    public void getWordListStringTotalLengthOfWordsInListIsNotOverLimit() {
        String text = "this is a simple test text";
        String[] list = this.texthandler.getWordListString(text, 10);
        assertTrue(list.length == 2);
    }
    
    @Test
    public void copyWordListToStringBuilderReturnsIdenticalStringBuilderArray() {
        String[] wordlist = new String[]{"simple", "test", "text", "this", "is", "a"};
        StringBuilder[] resultList = this.texthandler.copyWordListToStringBuilder(wordlist);
        assertEquals(wordlist[0], resultList[0].toString());
        assertEquals(wordlist[1], resultList[1].toString());
        assertEquals(wordlist[2], resultList[2].toString());
        assertEquals(wordlist[3], resultList[3].toString());
        assertEquals(wordlist[4], resultList[4].toString());
        assertEquals(wordlist[5], resultList[5].toString());
    }
    
    @Test
    public void formResultReplacesOriginalCharactersWithFoundKeys() {
        String cipher = "Wnpfhg pgwwxig.";
        String expected = "Simple message.";
        char[] substitutions = new char[128];
        substitutions['w'] = 's';
        substitutions['n'] = 'i';
        substitutions['p'] = 'm';
        substitutions['f'] = 'p';
        substitutions['h'] = 'l';
        substitutions['g'] = 'e';
        substitutions['x'] = 'a';
        substitutions['i'] = 'g';
        String result = this.texthandler.formResult(cipher, substitutions);
        assertEquals(expected, result);
    }
}
