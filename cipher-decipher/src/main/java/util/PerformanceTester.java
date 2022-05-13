package util;

import java.util.Arrays;
import logic.KeyFinder;
import logic.TextHandler;

/**
 * A tester class for running performance tests.
 *
 */
public class PerformanceTester implements Tester {

    private KeyFinder keyFinder;
    private TextHandler textHandler;
    private TextFactory textFactory;

    // preprocessing times
    private long[] preprocessingTimes;
    private int[] words;

    // word lookup times
    private long[] wordLookupTimes;
    private String[] testWords;

    // runtimes for texts without errors
    private long[][] runtimesForTextsWithNoErrors;

    // texts with errors, with new text randomly formed for each iteration
    // then decrypted multiple times and average time saved into array
    private long[][] runtimesForRandomTextsWithErrors;
    private int[] errors;

    // same text with increasing number of errors
    private long[] runtimes0Errors;
    private long[] runtimes5Errors;
    private long[] runtimes10Errors;
    private long[] runtimes15Errors;
    private long[] runtimes20Errors;

    // texts with different length and 20 errors
    private long[] runtimesWithErrors60Words;
    private long[] runtimesWithErrors100Words;
    private long[] runtimesWithErrors200Words;
    private long[] runtimesWithErrors500Words;

    public PerformanceTester(KeyFinder keyFinder, TextHandler textHandler, TextFactory textFactory) {
        this.keyFinder = keyFinder;
        this.textHandler = textHandler;
        this.textFactory = textFactory;
    }

    /**
     * Initializes the result arrays.
     *
     * @param repeats number of times the test is repeated, therefore the size
     * of the result arrays
     */
    public void setup(int repeats) {
        this.words = new int[]{60, 100, 500, 1000, 5000, 10000};
        this.preprocessingTimes = new long[this.words.length];

        // test words lengths 1, 4, 10, 19
        this.testWords = new String[]{"a", "word", "cumbersome", "nonmeteorologically"};
        this.wordLookupTimes = new long[testWords.length];

        this.runtimesForTextsWithNoErrors = new long[this.words.length][3];

        this.errors = new int[]{5, 10, 15, 20, 25};
        this.runtimesForRandomTextsWithErrors = new long[this.errors.length][3];

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
     * Get runtimes for all test cases.
     *
     * @param texts how many times the test is repeated with a new text
     * @param iterations the number of times the test is performed for a single
     * text
     */
    public void getAllRuntimes(int texts, int iterations) {
        getAllPreprocessingTimes(iterations);
        getWordLookupTimes(iterations);
        getTimesForTextsWithNoErrors(texts, iterations);
        getTimesForRandomTextsWithErrors(texts, iterations);
        getTimesForSameTextWithIncreasingNumberOfErrors();
        getTimesForTextsWith20Errors(iterations);
    }

    /**
     * Get all the preprocessing times for texts.
     *
     * @param iterations the number of times the test is performed for a single
     * text
     */
    public void getAllPreprocessingTimes(int iterations) {
        System.out.println("getting preprocessing times");
        for (int i = 0; i < this.preprocessingTimes.length; i++) {
            this.preprocessingTimes[i] = getPreprocessingTime(this.words[i], iterations);
        }
        System.out.println(getPreprocessingTimesResults());
    }

    /**
     * Get median preprocessing time for a text.
     *
     * @param numberOfWords the number of words in the text
     * @param iterations the number of times the test is performed for a single
     * text
     * @return the median of the measured preprocessing times
     */
    public long getPreprocessingTime(int numberOfWords, int iterations) {
        String text = this.textFactory.formRandomText(numberOfWords, 0);
        String cipher = this.textFactory.formCipher(text);

        long[] times = new long[iterations];
        for (int i = 0; i < times.length; i++) {
            long start = System.nanoTime();
            String cipherLetters = this.textHandler.getAllUsedCharacters(cipher);
            double[] cipherFrequencies = this.textHandler.findCharacterFrequencies(cipher);
            String[] cipherwords = this.textHandler.getWordListString(cipher, 2000);
            StringBuilder[] substitutionwords = this.textHandler.copyWordListToStringBuilder(cipherwords);
            long stop = System.nanoTime();
            times[i] = stop - start;
        }
        Arrays.sort(times);
        return times[times.length / 2];
    }

    /**
     * Get times for searching words from the dictionary.
     *
     * @param iterations how many times the time is taken for lookgin up the
     * same word
     */
    public void getWordLookupTimes(int iterations) {
        for (int i = 0; i < this.wordLookupTimes.length; i++) {
            long[] times = new long[iterations];
            for (int j = 0; j < times.length; j++) {
                long start = System.nanoTime();
                this.keyFinder.findWord(this.testWords[i]);
                long stop = System.nanoTime();
                times[j] = stop - start;
            }
            Arrays.sort(times);
            this.wordLookupTimes[i] = times[times.length / 2];
        }
        System.out.println(getWordLookupTimesResults());
    }

    /**
     * Get runtimes for when all words are found in the dictionary.
     *
     * @param texts how many times the test is repeated with a new text
     * @param iterations the number of times the test is performed for single
     * text
     */
    public void getTimesForTextsWithNoErrors(int texts, int iterations) {
        System.out.println("getting times without errors");
        long[] medianTimes = new long[texts];
        for (int i = 0; i < this.runtimesForTextsWithNoErrors.length; i++) {
            getKeyFindingTimesForRandomTexts(medianTimes, this.words[i], 0, iterations);
            this.runtimesForTextsWithNoErrors[i][0] = getAverage(medianTimes);
            Arrays.sort(medianTimes);
            this.runtimesForTextsWithNoErrors[i][1] = medianTimes[0];
            this.runtimesForTextsWithNoErrors[i][2] = medianTimes[medianTimes.length - 1];
        }
        System.out.println(getResultsForTextsWithNoErrors());
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

            times[i] = getMedian(timesForOneText);
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
            long start = System.nanoTime();
            char[] key = this.keyFinder.findKey(cipherwords, words, cipherLetters, cipherFrequencies);
            long stop = System.nanoTime();
            times[i] = stop - start;
        }
    }

    /**
     * Get runtimes with randomly formed texts and error words.
     *
     * @param texts how many times the test is repeated with a new text
     * @param iterations the number of times the test is performed for single
     * text
     */
    public void getTimesForRandomTextsWithErrors(int texts, int iterations) {
        System.out.println("getting times for random texts with errors");
        long[] medianTimes = new long[texts];
        for (int i = 0; i < this.runtimesForRandomTextsWithErrors.length; i++) {
            getKeyFindingTimesForRandomTexts(medianTimes, 200, this.errors[i], iterations);
            this.runtimesForRandomTextsWithErrors[i][0] = getAverage(medianTimes);
            Arrays.sort(medianTimes);
            this.runtimesForRandomTextsWithErrors[i][1] = medianTimes[0];
            this.runtimesForRandomTextsWithErrors[i][2] = medianTimes[medianTimes.length - 1];
        }
        System.out.println(getResultsForRandomErrorTexts());
    }

    /**
     * Get runtimes for the same text with increasing number of errors.
     */
    public void getTimesForSameTextWithIncreasingNumberOfErrors() {
        System.out.println("getting times for same text with increasin number of errors");
        String text = this.textFactory.formRandomText(200, 0);
        String cipher = this.textFactory.formCipher(text);
        long[][] allArrays = new long[][]{this.runtimes0Errors, this.runtimes5Errors, this.runtimes10Errors, this.runtimes15Errors, this.runtimes20Errors};
        int[] howManyErrors = new int[]{5, 10, 15, 20};
        for (int i = 0; i < allArrays.length; i++) {
            keyFindingTimesForTheSameText(allArrays[i], cipher);
            text = this.textFactory.addErrorsToText(text, howManyErrors[i]);
            cipher = this.textFactory.formCipher(text);
        }
        System.out.println(getResultsForTheSameTextWithIncreasingNumberOfErrors());
    }

    /**
     * Get runtimes for increasing number of words and 10 % errors.
     *
     * @param iterations the number of times the test is performed for single
     * text
     */
    public void getTimesForTextsWith20Errors(int iterations) {
        System.out.println("getting times for texts with 20 errors");
        getKeyFindingTimesForRandomTexts(this.runtimesWithErrors60Words, 60, 20, iterations);
        getKeyFindingTimesForRandomTexts(this.runtimesWithErrors100Words, 100, 20, iterations);
        getKeyFindingTimesForRandomTexts(this.runtimesWithErrors200Words, 200, 20, iterations);
        getKeyFindingTimesForRandomTexts(this.runtimesWithErrors500Words, 500, 20, iterations);
        System.out.println(getResultsForTextsWithSameNumberOfErrors());
    }

    /**
     * Get the median value from a result array.
     *
     * @param times result array
     * @return the median of times array values
     */
    public long getMedian(long[] times) {
        Arrays.sort(times);
        return times[times.length / 2];
    }

    /**
     * Count average value from a result array.
     *
     * @param times result array
     * @return the average of times array values
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
        int testRepeats = 10;
        int numberOfIterationsForSingleText = 11;
        setup(testRepeats);
        getAllRuntimes(testRepeats, numberOfIterationsForSingleText);
    }

    @Override
    public String getResults() {
        String r = "";
        r += getPreprocessingTimesResults();
        r += "\n";
        r += getWordLookupTimesResults();
        r += "\n";
        r += getResultsForTextsWithNoErrors();
        r += "\n";
        r += getResultsForRandomErrorTexts();
        r += "\n";
        r += getResultsForRandomErrorTexts();
        r += "\n";
        r += getResultsForTextsWithSameNumberOfErrors();
        return r;
    }

    /**
     * Get the preprocessing times as a string.
     *
     * @return the results as a string
     */
    public String getPreprocessingTimesResults() {
        String r = "Preprocessing times: " + "\n";
        for (int i = 0; i < this.preprocessingTimes.length; i++) {
            r += this.words[i] + " words median " + this.preprocessingTimes[i] + " ns" + "\n";
        }
        return r;
    }

    public String getWordLookupTimesResults() {
        String r = "Searching a word from trie times: " + "\n";
        for (int i = 0; i < this.wordLookupTimes.length; i++) {
            r += "for word of length " + this.testWords[i].length() + " the median time was " + this.wordLookupTimes[i] + " ns" + "\n";
        }
        return r;
    }

    /**
     * Get the results for texts with no errors as a string.
     *
     * @return the results as a string
     */
    public String getResultsForTextsWithNoErrors() {
        String r = "";
        r += "When all words are found in the dictionary: " + "\n";
        for (int i = 0; i < this.runtimesForTextsWithNoErrors.length; i++) {
            r += this.words[i] + " words average time ms: " + this.runtimesForTextsWithNoErrors[i][0] / 1000000 + " min " + this.runtimesForTextsWithNoErrors[i][1] / 1000000 + " max " + this.runtimesForTextsWithNoErrors[i][2] / 1000000 + "\n";
        }
        return r;
    }

    /**
     * Get the results for random error texts as a string.
     *
     * @return the results as a string
     */
    public String getResultsForRandomErrorTexts() {
        String r = "";
        r += "All random error texts: " + "\n";
        for (int i = 0; i < this.runtimesForRandomTextsWithErrors.length; i++) {
            r += this.errors[i] + " errors average time ms: " + this.runtimesForRandomTextsWithErrors[i][0] / 1000000 + " min " + this.runtimesForRandomTextsWithErrors[i][1] / 1000000 + " max " + this.runtimesForRandomTextsWithErrors[i][2] / 1000000 + "\n";
        }
        return r;
    }

    /**
     * Get the results for the same text with increasing number of errors as a
     * string.
     *
     * @return the results as a string
     */
    public String getResultsForTheSameTextWithIncreasingNumberOfErrors() {
        Arrays.sort(this.runtimes0Errors);
        Arrays.sort(this.runtimes5Errors);
        Arrays.sort(this.runtimes10Errors);
        Arrays.sort(this.runtimes15Errors);
        Arrays.sort(this.runtimes20Errors);
        String r = "";
        long[] times = this.runtimes0Errors;
        long min = times[0] / 1000000;
        long max = times[times.length - 1] / 1000000;
        r += "Run times for the same text with increasing number of errors: " + "\n";
        r += "0 errors average time ms: " + getAverage(this.runtimes0Errors) + " min " + min + " max " + max + "\n";
        times = this.runtimes5Errors;
        min = times[0] / 1000000;
        max = times[times.length - 1] / 1000000;
        r += "5 errors average time ms: " + getAverage(this.runtimes5Errors) + " min " + min + " max " + max + "\n";
        times = this.runtimes10Errors;
        min = times[0] / 1000000;
        max = times[times.length - 1] / 1000000;
        r += "10 errors average time ms: " + getAverage(this.runtimes10Errors) + " min " + min + " max " + max + "\n";
        times = this.runtimes15Errors;
        min = times[0] / 1000000;
        max = times[times.length - 1] / 1000000;
        r += "15 errors average time ms: " + getAverage(this.runtimes15Errors) + " min " + min + " max " + max + "\n";
        times = this.runtimes20Errors;
        min = times[0] / 1000000;
        max = times[times.length - 1] / 1000000;
        r += "20 errors average time ms: " + getAverage(this.runtimes20Errors) + " min " + min + " max " + max + "\n";
        return r;
    }

    /**
     * Get the results for texts with same number of errors as a string.
     *
     * @return the results as a string
     */
    public String getResultsForTextsWithSameNumberOfErrors() {
        Arrays.sort(this.runtimesWithErrors60Words);
        Arrays.sort(this.runtimesWithErrors100Words);
        Arrays.sort(this.runtimesWithErrors200Words);
        Arrays.sort(this.runtimesWithErrors500Words);
        String r = "";
        long[] times = this.runtimesWithErrors60Words;
        long min = times[0] / 1000000;
        long max = times[times.length - 1] / 1000000;
        r += "Run times for texts with 20 errors: " + "\n";
        r += "60 words average time ms: " + getAverage(this.runtimesWithErrors60Words) + " min " + min + " max " + max + "\n";
        times = this.runtimesWithErrors100Words;
        min = times[0] / 1000000;
        max = times[times.length - 1] / 1000000;
        r += "100 words errors average time ms: " + getAverage(this.runtimesWithErrors100Words) + " min " + min + " max " + max + "\n";
        times = this.runtimesWithErrors200Words;
        min = times[0] / 1000000;
        max = times[times.length - 1] / 1000000;
        r += "200 words errors average time ms: " + getAverage(this.runtimesWithErrors200Words) + " min " + min + " max " + max + "\n";
        times = this.runtimesWithErrors500Words;
        min = times[0] / 1000000;
        max = times[times.length - 1] / 1000000;
        r += "500 words errors average time ms: " + getAverage(this.runtimesWithErrors500Words) + " min " + min + " max " + max;
        return r;
    }
}
