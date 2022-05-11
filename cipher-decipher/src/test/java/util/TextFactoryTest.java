package util;

import java.io.FileNotFoundException;
import org.junit.Test;
import static org.junit.Assert.*;

public class TextFactoryTest {

    private TextFactory textFactory;

    public TextFactoryTest() {
        this.textFactory = new TextFactory("test_words.txt", new Crypter());
    }

    @Test
    public void getTestWordsReadsAllTheWordsFromTheFileInConstructorCallAndExcludesSingleLettersOtherThanAOrI() {
        String[] words = this.textFactory.getWords();
        assertEquals("sea", words[0]);
        assertEquals("she", words[1]);
        assertEquals("shell", words[2]);
        assertEquals("zeiss", words[3]);
        assertEquals("a", words[4]);
        assertEquals("i", words[5]);
    }

    @Test
    public void countWordsReturnsTheRightCount() {
        int count = this.textFactory.countWords("test_words.txt");
        assertTrue(count == 6);
    }

    @Test
    public void formRandomTextReturnTextContainsRightAmountOfSeparateWords() {
        String text = this.textFactory.formRandomText(4, 0);
        String[] words = text.split(" ");
        assertTrue(words.length == 4);
    }

    @Test
    public void formRandomTextAddsErrorsToTheEndOfTheText() {
        String text = this.textFactory.formRandomText(4, 1);
        String[] words = text.split(" ");
        assertNotEquals("sea", words[3]);
        assertNotEquals("she", words[3]);
        assertNotEquals("shell", words[3]);
        assertNotEquals("zeiss", words[3]);
    }

    @Test
    public void addErrorsToTextAddsAnErrorWordAtTheBeginningOfText() {
        String text = "this is a test text";
        String result = this.textFactory.addErrorsToText(text, 1);
        String[] originalWords = text.split(" ");
        String[] testWords = result.split(" ");

        assertEquals("this", originalWords[0]);
        assertNotEquals("this", testWords[0]);
    }

    @Test
    public void addErrorsToTextDoesNotChangeAmountOfWords() {
        String text = "this is a test text";
        String result = this.textFactory.addErrorsToText(text, 1);
        String[] originalWords = text.split(" ");
        String[] testWords = result.split(" ");

        assertTrue(originalWords.length == testWords.length);
    }

    @Test
    public void getRandomErrorWordReturnedErrorWordsHaveAtLeastLengthFive() {
        String word = this.textFactory.getRandomErrorWord();
        assertTrue(word.length() >= 5);
    }
    
    @Test
    public void formCipherReturnsSomethingElseThanOriginalText() {
        String text = "THIS IS A TEST TEXT";
        String result = this.textFactory.formCipher(text);
        assertNotEquals(text, result);
    }
}
