package logic;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class DecrypterTest {
    
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    File testFile;
    Decrypter decrypter;
    String testtext;
    
    public DecrypterTest() {
    }
    
    @Before
    public void setUp() throws Exception {
        this.testFile = testFolder.newFile("test_words.txt");
        FileWriter writer = new FileWriter("test_words.txt");
        writer.write("sea");
        writer.write(System.getProperty( "line.separator" ));
        writer.write("she");
        writer.write(System.getProperty( "line.separator" ));
        writer.write("shell");
        writer.write(System.getProperty( "line.separator" ));
        writer.write("zeiss");
        writer.close();
        this.decrypter = new Decrypter("test_words.txt");
        this.testtext = "tifmm";
    }
    
    @After
    public void tearDown() {
        this.testFile.delete();
    }

    @Test
    public void getClosestAvailableKeyReturnsCharWithClosestFrequency() {
        this.decrypter.initializeArrays();
        char[] listOfUsedChars = new char[1];
        // the frequency of letter d is 4.1 %
        assertTrue(this.decrypter.getClosestAvailableKey(listOfUsedChars, 4.1) == 'd');
    }
    
    @Test
    public void findWordReturnsTrueIfWordIsInDictionary() {
        assertTrue(this.decrypter.findWord("sea"));
    }
    
    @Test
    public void findWordReturnsFalseForSubstrings() {
        assertFalse(this.decrypter.findWord("se"));
    }
    
    @Test
    public void findSubstringReturnsTrueForSubstrings() {
        assertTrue(this.decrypter.findSubstring("se"));
    }
    
    @Test
    public void decrypterReturnsADecryption() {
        assertEquals(this.decrypter.decrypt(this.testtext), "shell");
    }
}
