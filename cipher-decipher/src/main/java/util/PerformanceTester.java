package util;

import logic.Decrypter;
import logic.KeyFinder;
import logic.TextHandler;

/**
 * A tester class for running performance tests.
 *
 */
public class PerformanceTester implements Tester {

    private Decrypter decrypter;
    private KeyFinder keyFinder;
    private TextHandler textHandler;
    private TextFactory textFactory;

    // runtimes for texts without errors
    private long[] runtimes60words;
    private long[] runtimes100words;
    private long[] runtimes500words;
    private long[] runtimes1000words;
    private long[] runtimes5000words;
    private long[] runtimes10000words;

    // texts with errors, with new text randomly formed for each iteration
    // then decrypted multiple times and average time saved into array
    private long[] runtimesRandom0Errors;
    private long[] runtimesRandom5Errors;
    private long[] runtimesRandom10Errors;
    private long[] runtimesRandom15Errors;
    private long[] runtimesRandom20Errors;

    // same text with increasing number of errors
    private long[] runtimes0Errors;
    private long[] runtimes5Errors;
    private long[] runtimes10Errors;
    private long[] runtimes15Errors;
    private long[] runtimes20Errors;

    // texts with different length and 10 % errors
    private long[] runtimesWithErrors60Words;
    private long[] runtimesWithErrors100Words;
    private long[] runtimesWithErrors200Words;
    private long[] runtimesWithErrors500Words;

    public PerformanceTester(Decrypter d, KeyFinder keyFinder, TextHandler textHandler, TextFactory tf) {
        this.decrypter = d;
        this.keyFinder = keyFinder;
        this.textHandler = textHandler;
        this.textFactory = tf;
    }

    /**
     * Initializes the result arrays.
     *
     * @param repeats number of times the test is repeated, therefore the size
     * of the result arrays
     */
    public void setup(int repeats) {
        this.runtimes60words = new long[repeats];
        this.runtimes100words = new long[repeats];
        this.runtimes500words = new long[repeats];
        this.runtimes1000words = new long[repeats];
        this.runtimes5000words = new long[repeats];
        this.runtimes10000words = new long[repeats];

        this.runtimesRandom0Errors = new long[repeats];
        this.runtimesRandom5Errors = new long[repeats];
        this.runtimesRandom10Errors = new long[repeats];
        this.runtimesRandom15Errors = new long[repeats];
        this.runtimesRandom20Errors = new long[repeats];

        this.runtimes0Errors = new long[repeats];
        this.runtimes5Errors = new long[repeats];
        this.runtimes10Errors = new long[repeats];
        this.runtimes15Errors = new long[repeats];
        this.runtimes20Errors = new long[repeats];

        this.runtimesWithErrors60Words = new long[repeats];
        this.runtimesWithErrors100Words = new long[repeats];
        this.runtimesWithErrors200Words = new long[repeats];
        this.runtimesWithErrors500Words = new long[repeats];
    }

    /**
     * Get runtimes for randomly formed texts. A new text is randomly generated
     * for each slot in the times array. The key finding is performed the given
     * amount of iterations for each text (by keyFindingTimesForTheSameText()),
     * and a rough average is saved in the times array.
     *
     * @param times result array
     * @param words number of words in the test text
     * @param errors number of errors in the test text
     * @param iterations the number of times the key finding is performed for
     * single text
     */
    public void getKeyFindingTimesForRandomTexts(long[] times, int words, int errors, int iterations) {
        for (int i = 0; i < times.length; i++) {
            String text = this.textFactory.formRandomText(words, errors);
            String cipher = this.textFactory.formCipher(text);

            long[] timesForOneText = new long[iterations];
            keyFindingTimesForTheSameText(timesForOneText, cipher);

            times[i] = getAverage(timesForOneText);
        }
    }

    /**
     * Get runtimes for the same text. As many iterations as the times array's
     * length.
     *
     * @param times result array
     * @param text the text that is decrypted
     */
    public void keyFindingTimesForTheSameText(long[] times, String text) {
        String cipherLetters = this.textHandler.getAllUsedCharacters(text);
        double[] cipherFrequencies = this.textHandler.findCharacterFrequencies(text);
        String[] cipherwords = this.textHandler.getWordListString(text, 2000);
        StringBuilder[] words = this.textHandler.copyWordListToStringBuilder(cipherwords);

        for (int i = 0; i < times.length; i++) {
            long start = System.currentTimeMillis();
            char[] key = this.keyFinder.findKey(cipherwords, words, cipherLetters, cipherFrequencies);
            long stop = System.currentTimeMillis();
            times[i] = stop - start;
        }
    }

    /**
     * Get runtimes for all test cases.
     *
     * @param iterations the number of times the test is performed for single
     * text
     */
    public void getAllRuntimes(int iterations) {
        getTimesForTextsWithNoErrors(iterations);
        getTimesForRandomTextsWithErrors(iterations);
        getTimesForSameTextWithIncreasingNumberOfErrors();
        getTimesFor10PercentErrors(iterations);
    }

    /**
     * Get runtimes for when all words are found in the dictionary.
     * 
     * @param iterations the number of times the test is performed for single
     * text
     */
    public void getTimesForTextsWithNoErrors(int iterations) {
        System.out.println("getting times without errors");
        getKeyFindingTimesForRandomTexts(this.runtimes60words, 60, 0, iterations);
        getKeyFindingTimesForRandomTexts(this.runtimes100words, 100, 0, iterations);
        getKeyFindingTimesForRandomTexts(this.runtimes500words, 500, 0, iterations);
        getKeyFindingTimesForRandomTexts(this.runtimes1000words, 1000, 0, iterations);
        getKeyFindingTimesForRandomTexts(this.runtimes5000words, 5000, 0, iterations);
        getKeyFindingTimesForRandomTexts(this.runtimes10000words, 10000, 0, iterations);
    }

    /**
     * Get runtimes with randomly formed texts and error words.
     * 
     * @param iterations the number of times the test is performed for single
     * text
     */
    public void getTimesForRandomTextsWithErrors(int iterations) {
        System.out.println("getting times for random texts with errors");
        System.out.println("0 errors");
        getKeyFindingTimesForRandomTexts(this.runtimesRandom0Errors, 100, 0, iterations);
        System.out.println("5 errors");
        getKeyFindingTimesForRandomTexts(this.runtimesRandom5Errors, 100, 5, iterations);
        System.out.println("10 errors");
        getKeyFindingTimesForRandomTexts(this.runtimesRandom10Errors, 100, 10, iterations);
        System.out.println("15 errors");
        getKeyFindingTimesForRandomTexts(this.runtimesRandom15Errors, 100, 15, iterations);
        System.out.println("20 errors");
        getKeyFindingTimesForRandomTexts(this.runtimesRandom20Errors, 100, 20, iterations);
    }

    /**
     * Get runtimes for the same text with increasing number of errors.
     */
    public void getTimesForSameTextWithIncreasingNumberOfErrors() {
        System.out.println("getting times for same text with increasin number of errors");
        String text = this.textFactory.formRandomText(200, 0);
        String cipher = this.textFactory.formCipher(text);
        System.out.println("0 errors");
        keyFindingTimesForTheSameText(this.runtimes0Errors, cipher);
        text = this.textFactory.addErrorsToText(text, 5);
        cipher = this.textFactory.formCipher(text);
        System.out.println("5 errors");
        keyFindingTimesForTheSameText(this.runtimes5Errors, cipher);
        text = this.textFactory.addErrorsToText(text, 10);
        cipher = this.textFactory.formCipher(text);
        System.out.println("10 errors");
        keyFindingTimesForTheSameText(this.runtimes10Errors, cipher);
        text = this.textFactory.addErrorsToText(text, 15);
        cipher = this.textFactory.formCipher(text);
        System.out.println("15 errors");
        keyFindingTimesForTheSameText(this.runtimes15Errors, cipher);
        text = this.textFactory.addErrorsToText(text, 20);
        cipher = this.textFactory.formCipher(text);
        System.out.println("20 errors");
        keyFindingTimesForTheSameText(this.runtimes20Errors, cipher);
    }

    /**
     * Get runtimes for increasing number of words and 10 % errors.
     * 
     * @param iterations the number of times the test is performed for single
     * text
     */
    public void getTimesFor10PercentErrors(int iterations) {
        System.out.println("getting times with 10 % errors");
        getKeyFindingTimesForRandomTexts(this.runtimesWithErrors60Words, 60, 6, iterations);
        getKeyFindingTimesForRandomTexts(this.runtimesWithErrors100Words, 100, 10, iterations);
        getKeyFindingTimesForRandomTexts(this.runtimesWithErrors200Words, 200, 20, iterations);
        getKeyFindingTimesForRandomTexts(this.runtimesWithErrors500Words, 500, 50, iterations);
    }

    /**
     * Count average value from a result array.
     *
     * @param times result array
     */
    public long getAverage(long[] times) {
        long sum = 0;
        for (int i = 0; i < times.length; i++) {
            sum += times[i];
        }
        return sum / times.length;
    }

    @Override
    public void run() {
        int arrayLength = 10;
        int numberOfIterations = 10;
        setup(arrayLength);
        getAllRuntimes(numberOfIterations);
    }

    @Override
    public String getResults() {
        String r = "";
        r += "When all words are found in the dictionary: " + "\n";
        r += "60 words average time ms: " + getAverage(this.runtimes60words) + "\n";
        r += "100 words average time ms: " + getAverage(this.runtimes100words) + "\n";
        r += "500 words average time ms: " + getAverage(this.runtimes500words) + "\n";
        r += "1000 words average time ms: " + getAverage(this.runtimes1000words) + "\n";
        r += "5000 words average time ms: " + getAverage(this.runtimes5000words) + "\n";
        r += "10000 words average time ms: " + getAverage(this.runtimes10000words) + "\n";
        r += "\n";
        r += "All random error texts: " + "\n";
        r += "0 errors average time ms: " + getAverage(this.runtimesRandom0Errors) + "\n";
        r += "5 errors average time ms: " + getAverage(this.runtimesRandom5Errors) + "\n";
        r += "10 errors average time ms: " + getAverage(this.runtimesRandom10Errors) + "\n";
        r += "15 errors average time ms: " + getAverage(this.runtimesRandom15Errors) + "\n";
        r += "20 errors average time ms: " + getAverage(this.runtimesRandom20Errors) + "\n";
        r += "\n";
        r += "Run times for the same text with increasing number of errors: " + "\n";
        r += "0 errors average time ms: " + getAverage(this.runtimes0Errors) + "\n";
        r += "5 errors average time ms: " + getAverage(this.runtimes5Errors) + "\n";
        r += "10 errors average time ms: " + getAverage(this.runtimes10Errors) + "\n";
        r += "15 errors average time ms: " + getAverage(this.runtimes15Errors) + "\n";
        r += "20 errors average time ms: " + getAverage(this.runtimes20Errors) + "\n";
        r += "\n";
        r += "Run times for texts with 10 % errors: " + "\n";
        r += "60 words average time ms: " + getAverage(this.runtimesWithErrors60Words) + "\n";
        r += "100 words errors average time ms: " + getAverage(this.runtimesWithErrors100Words) + "\n";
        r += "200 words errors average time ms: " + getAverage(this.runtimesWithErrors200Words) + "\n";
        r += "500 words errors average time ms: " + getAverage(this.runtimesWithErrors500Words);
        return r;
    }
}
