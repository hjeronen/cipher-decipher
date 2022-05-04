package util;

import logic.Decrypter;

public class PerformanceTester implements Tester {

    private Decrypter decrypter;
    private TextFactory textFactory;
    
    private long[] runtimes100words;
    private long[] runtimes500words;
    private long[] runtimes1000words;
    private long[] runtimes5000words;
    private long[] runtimes10000words;

    private long[] runtimes0Errors;
    private long[] runtimes5Errors;
    private long[] runtimes10Errors;
    private long[] runtimes15Errors;
    private long[] runtimes20Errors;

    public PerformanceTester(Decrypter d, TextFactory tf) {
        this.decrypter = d;
        this.textFactory = tf;
    }
    
    public void setup(int repeats) {
        this.runtimes100words = new long[repeats];
        this.runtimes500words = new long[repeats];
        this.runtimes1000words = new long[repeats];
        this.runtimes5000words = new long[repeats];
        this.runtimes10000words = new long[repeats];

        this.runtimes0Errors = new long[repeats];
        this.runtimes5Errors = new long[repeats];
        this.runtimes10Errors = new long[repeats];
        this.runtimes15Errors = new long[repeats];
        this.runtimes20Errors = new long[repeats];
    }

    public void getTimes(long[] times, int words, int errors) {
        for (int i = 0; i < times.length; i++) {
            String text = this.textFactory.formRandomText(words, errors);
            String cipher = this.textFactory.formCipher(text);
            long start = System.currentTimeMillis();
            String result = this.decrypter.decrypt(cipher);
            long stop = System.currentTimeMillis();
            times[i] = stop - start;
        }
    }

    public void getAllRuntimes() {
        // runtimes when all words are found in the dictionary
        getTimes(this.runtimes100words, 100, 0);
        getTimes(this.runtimes500words, 500, 0);
        getTimes(this.runtimes1000words, 1000, 0);
        getTimes(this.runtimes5000words, 5000, 0);
        getTimes(this.runtimes10000words, 10000, 0);

        // runtimes with randomly formed error words
        //System.out.println("0 errors");
        getTimes(this.runtimes0Errors, 200, 0);
        //System.out.println("5 errors");
        getTimes(this.runtimes5Errors, 200, 5);
        //System.out.println("10 errors");
        getTimes(this.runtimes10Errors, 200, 10);
        //System.out.println("15 errors");
        getTimes(this.runtimes15Errors, 200, 15);
        //System.out.println("20 errors");
        getTimes(this.runtimes20Errors, 200, 20);
    }

    public long getAverage(long[] times) {
        long sum = 0;
        for (int i = 0; i < times.length; i++) {
            sum += times[i];
        }
        return sum / times.length;
    }

    @Override
    public void run() {
        setup(100);
        getAllRuntimes();
    }

    @Override
    public String getResults() {
        String r = "";
        r += "When all words are found in the dictionary: " + "\n";
        r += "100 words average time ms: " + getAverage(this.runtimes100words) + "\n";
        r += "500 words average time ms: " + getAverage(this.runtimes500words) + "\n";
        r += "1000 words average time ms: " + getAverage(this.runtimes1000words) + "\n";
        r += "5000 words average time ms: " + getAverage(this.runtimes5000words) + "\n";
        r += "10000 words average time ms: " + getAverage(this.runtimes10000words) + "\n";
        r += "\n";
        r += "Run times for texts with errors: " + "\n";
        r += "0 errors average time ms: " + getAverage(this.runtimes0Errors) + "\n";
        r += "5 errors average time ms: " + getAverage(this.runtimes5Errors) + "\n";
        r += "10 errors average time ms: " + getAverage(this.runtimes10Errors) + "\n";
        r += "15 errors average time ms: " + getAverage(this.runtimes15Errors) + "\n";
        r += "20 errors average time ms: " + getAverage(this.runtimes20Errors);
        return r;
    }
}
