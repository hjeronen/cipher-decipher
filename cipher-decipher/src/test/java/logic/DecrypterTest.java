package logic;

import java.io.File;
import java.io.FileWriter;
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
        writer.write(System.getProperty("line.separator"));
        writer.write("she");
        writer.write(System.getProperty("line.separator"));
        writer.write("shell");
        writer.write(System.getProperty("line.separator"));
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
    public void decrypterReturnsADecryption() {
        assertEquals(this.decrypter.decrypt(this.testtext), "shell");
    }

    @Test
    public void specialChracatersAreIgnored() {
        String test = "t*if(m)&%#m+?!";
        String expected = "s*he(l)&%#l+?!";
        String result = this.decrypter.decrypt(test);

        assertEquals(expected, result);
    }

    @Test
    public void linebreaksAreIgnored() {
        this.decrypter = new Decrypter("dictionary_long.txt");
        String test = "DJ DK C QLXDWI WF SDGDU PCX. XLRLU KQCSLKBDQK, KJXDHDET FXWZ C BDIILE RCKL, BCGL PWE JBLDX FDXKJ GDSJWXO CTCDEKJ JBL LGDU TCUCSJDS LZQDXL.\n"
                + "\n"
                + "IYXDET JBL RCJJUL, XLRLU KQDLK ZCECTLI JW KJLCU KLSXLJ QUCEK JW JBL LZQDXL’K YUJDZCJL PLCQWE, JBL ILCJB KJCX, CE CXZWXLI KQCSL KJCJDWE PDJB LEWYTB QWPLX JW ILKJXWO CE LEJDXL QUCELJ. QYXKYLI RO JBL LZQDXL’K KDEDKJLX CTLEJK, QXDESLKK ULDC XCSLK BWZL CRWCXI BLX KJCXKBDQ, SYKJWIDCE WF JBL KJWULE QUCEK JBCJ SCE KCGL BLX QLWQUL CEI XLKJWXL FXLLIWZ JW JBL TCUCVO…";
        String expected = "IT IS A PERIOD OF CIVIL WAR. REBEL SPACESHIPS, STRIKING FROM A HIDDEN BASE, HAVE WON THEIR FIRST VICTORY AGAINST THE EVIL GALACTIC EMPIRE.\n"
                + "\n"
                + "DURING THE BATTLE, REBEL SPIES MANAGED TO STEAL SECRET PLANS TO THE EMPIRE’S ULTIMATE WEAPON, THE DEATH STAR, AN ARMORED SPACE STATION WITH ENOUGH POWER TO DESTROY AN ENTIRE PLANET. PURSUED BY THE EMPIRE’S SINISTER AGENTS, PRINCESS LEIA RACES HOME ABOARD HER STARSHIP, CUSTODIAN OF THE STOLEN PLANS THAT CAN SAVE HER PEOPLE AND RESTORE FREEDOM TO THE GALAXY…";
        String result = this.decrypter.decrypt(test);
        assertEquals(expected, result);
    }

    @Test
    public void correctDecryptionForSampleText() {
        this.decrypter = new Decrypter("dictionary_long.txt");
        String test = "DJ DK C QLXDWI WF SDGDU PCX. XLRLU KQCSLKBDQK, KJXDHDET FXWZ C BDIILE RCKL, BCGL PWE JBLDX FDXKJ GDSJWXO CTCDEKJ JBL LGDU TCUCSJDS LZQDXL. IYXDET JBL RCJJUL, XLRLU KQDLK ZCECTLI JW KJLCU KLSXLJ QUCEK JW JBL LZQDXL’K YUJDZCJL PLCQWE, JBL ILCJB KJCX, CE CXZWXLI KQCSL KJCJDWE PDJB LEWYTB QWPLX JW ILKJXWO CE LEJDXL QUCELJ. QYXKYLI RO JBL LZQDXL’K KDEDKJLX CTLEJK, QXDESLKK ULDC XCSLK BWZL CRWCXI BLX KJCXKBDQ, SYKJWIDCE WF JBL KJWULE QUCEK JBCJ SCE KCGL BLX QLWQUL CEI XLKJWXL FXLLIWZ JW JBL TCUCVO…";
        String expected = "IT IS A PERIOD OF CIVIL WAR. REBEL SPACESHIPS, STRIKING FROM A HIDDEN BASE, HAVE WON THEIR FIRST VICTORY AGAINST THE EVIL GALACTIC EMPIRE. DURING THE BATTLE, REBEL SPIES MANAGED TO STEAL SECRET PLANS TO THE EMPIRE’S ULTIMATE WEAPON, THE DEATH STAR, AN ARMORED SPACE STATION WITH ENOUGH POWER TO DESTROY AN ENTIRE PLANET. PURSUED BY THE EMPIRE’S SINISTER AGENTS, PRINCESS LEIA RACES HOME ABOARD HER STARSHIP, CUSTODIAN OF THE STOLEN PLANS THAT CAN SAVE HER PEOPLE AND RESTORE FREEDOM TO THE GALAXY…";
        String result = this.decrypter.decrypt(test);

        assertEquals(expected, result);
    }

    @Test
    public void correctDecryptionForSampleTextWithMissingWords() {
        this.decrypter = new Decrypter("dictionary_long.txt");
        String test = "OK OH R WRFD KOIS QPF KNS FSTSJJOPX. RJKNPAGN KNS WSRKN HKRF NRH TSSX WSHKFPCSW, OIBSFORJ KFPPBH NRYS WFOYSX KNS FSTSJ QPFMSH QFPI KNSOF NOWWSX TRHS RXW BAFHASW KNSI RMFPHH KNS GRJREC. SYRWOXG KNS WFSRWSW OIBSFORJ HKRFQJSSK, R GFPAB PQ QFSSWPI QOGNKSFH JSW TC JADS HDCVRJDSF NRH SHKRTJOHNSW R XSV HSMFSK TRHS PX KNS FSIPKS OMS VPFJW PQ NPKN. KNS SYOJ JPFW WRFKN YRWSF, PTHSHHSW VOKN QOXWOXG CPAXG HDCVRJDSF, NRH WOHBRKMNSW KNPAHRXWH PQ FSIPKS BFPTSH OXKP KNS QRF FSRMNSH PQ HBRMS…";
        String expected = "IT IS A DARK TIME FOR THE REBELLION. ALTHOUGH THE DEATH STAR HAS BEEN DESTROYED, IMPERIAL TROOPS HAVE DRIVEN THE REBEL FORCES FROM THEIR HIDDEN BASE AND PURSUED THEM ACROSS THE GALAXY. EVADING THE DREADED IMPERIAL STARFLEET, A GROUP OF FREEDOM FIGHTERS LED BY LUKE SKYWALKER HAS ESTABLISHED A NEW SECRET BASE ON THE REMOTE ICE WORLD OF HOTH. THE EVIL LORD DARTH VADER, OBSESSED WITH FINDING YOUNG SKYWALKER, HAS DISPATCHED THOUSANDS OF REMOTE PROBES INTO THE FAR REACHES OF SPACE…";
        String result = this.decrypter.decrypt(test);

        assertEquals(expected, result);
    }
}
