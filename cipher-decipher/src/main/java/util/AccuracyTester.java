package util;

import logic.Decrypter;

/**
 * A tester class for running accuracy tests.
 *
 */
public class AccuracyTester implements Tester {

    private Decrypter decrypter;
    private TextFactory textFactory;

    private double[] accuracies0Errors;
    private double[] accuracies5Errors;
    private double[] accuracies12Errors;
    private double[] accuracies14Errors;
    private double[] accuracies21Errors;

    /**
     * Constructor.
     *
     * @param decrypter the decrypter program that is used
     * @param tf the TextFactory that generates test texts
     */
    public AccuracyTester(Decrypter decrypter, TextFactory tf) {
        this.decrypter = decrypter;
        this.textFactory = tf;

    }

    /**
     * Initializes the result arrays.
     *
     * @param repeats number of times the test is repeated, therefore the size
     * of the result arrays
     */
    public void setup(int repeats) {
        this.accuracies0Errors = new double[repeats];
        this.accuracies5Errors = new double[repeats];
        this.accuracies12Errors = new double[repeats];
        this.accuracies14Errors = new double[repeats];
        this.accuracies21Errors = new double[repeats];
    }

    /**
     * Decrypt a given text.
     *
     * @param cipher text that is to be decrypted
     * @return the decrypted text
     */
    public String getDecryption(String cipher) {
        return this.decrypter.decrypt(cipher).toLowerCase();
    }

    /**
     * Check accuracy of the original text and the result of the decryption.
     *
     * @param text original text (not encrypted)
     * @param result the result of the decryption
     * @return percentage of matching characters between original and decrypted
     * text
     */
    public double checkAccuracy(String text, String result) {
        double count = 0;
        double total = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                continue;
            }
            total++;
            if (text.charAt(i) == result.charAt(i)) {
                count++;
            }
        }
        return count / total;
    }

    /**
     * Run tests for the given test case.
     *
     * @param results the array where results are saved
     * @param numberOfWords number of words in the test text
     * @param errors number of errors in the test text
     */
    public void runTests(double[] results, int numberOfWords, int errors) {
        for (int i = 0; i < results.length; i++) {
            String text = this.textFactory.formRandomText(numberOfWords, errors);
            String cipher = this.textFactory.formCipher(text);
            String result = getDecryption(cipher);
            results[i] = checkAccuracy(text, result);
        }
    }

    /**
     * Count average value from a result array.
     *
     * @param results result array
     * @return the average of the values in the results array
     */
    public double getAverage(double[] results) {
        double sum = 0;
        for (int i = 0; i < results.length; i++) {
            sum += results[i];
        }
        return sum / (double) results.length;
    }

    @Override
    public void run() {
        int words = 100;
        int repeats = 10;
        setup(repeats);
        System.out.println("0 errors");
        runTests(this.accuracies0Errors, words, 0);
        System.out.println("5 errors");
        runTests(this.accuracies5Errors, words, 5);
        System.out.println("12 errors");
        runTests(this.accuracies12Errors, words, 12);
        System.out.println("14 errors");
        runTests(this.accuracies14Errors, words, 14);
        System.out.println("21 errors");
        runTests(this.accuracies21Errors, words, 21);
    }

    @Override
    public String getResults() {
        String r = "Average accuracies:" + "\n";
        r += "texts with 0 errors: " + getAverage(this.accuracies0Errors) + "\n";
        r += "texts with 5 errors: " + getAverage(this.accuracies5Errors) + "\n";
        r += "texts with 12 errors: " + getAverage(this.accuracies12Errors) + "\n";
        r += "texts with 14 errors: " + getAverage(this.accuracies14Errors) + "\n";
        r += "texts with 21 errors: " + getAverage(this.accuracies21Errors) + "\n";
        return r;
    }

}
