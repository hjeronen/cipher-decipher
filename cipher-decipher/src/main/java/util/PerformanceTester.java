package util;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import logic.Decrypter;

public class PerformanceTester {

    private Decrypter decrypter;
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
    
    private ArrayList<String> words;

    public PerformanceTester(Decrypter d) {
        this.decrypter = d;
        
        this.runtimes100words = new long[100];
        this.runtimes500words = new long[100];
        this.runtimes1000words = new long[100];
        this.runtimes5000words = new long[100];
        this.runtimes10000words = new long[100];
        
        this.runtimes0Errors = new long[100];
        this.runtimes5Errors = new long[100];
        this.runtimes10Errors = new long[100];
        this.runtimes15Errors = new long[100];
        this.runtimes20Errors = new long[100];
        
        this.words = new ArrayList<String>();
        getTestWords();
    }

    public void getTestWords() {
        try {
            Scanner reader = new Scanner(new File("dictionary_long.txt"));
            while (reader.hasNext()) {
                String word = reader.nextLine();
                if (word.length() == 1 && !word.equals("i") && !word.equals("a")) {
                    continue;
                }
                this.words.add(word);
            }
        } catch (Exception exception) {
            System.out.println("Dictionary creation failed.");
        }
    }

    public void run() {
        getAllRuntimes();
    }

    public String formCipherText(int numberOfWords) {
        return formCipherTextWithErrors(numberOfWords, 0);
    }
    
    public String formCipherTextWithErrors(int numberOfWords, int errors) {
        numberOfWords -= errors;
        Crypter crypter = new Crypter();
        String text = "";
        Random ran = new Random();
        for (int i = 0; i < numberOfWords; i++) {
            int index = ran.nextInt(this.words.size());
            text += this.words.get(index);
            text += " ";
        }
        for (int i = 0; i < errors; i++) {
            String errorWord = "";
            int wordLength = 5 + ran.nextInt(10);
            for (int j = 0; j < wordLength; j++) {
                char letter = (char) (97 + ran.nextInt(26));
                errorWord += letter;
            }
            text += errorWord;
            text += " ";
        }
        return crypter.encrypt(text);
    }

    public void getTimes(long[] times, int words) {
        for (int i = 0; i < times.length; i++) {
            String cipher = formCipherText(words);
            long start = System.currentTimeMillis();
            String text = this.decrypter.decrypt(cipher);
            long stop = System.currentTimeMillis();
            times[i] = stop - start;
        }
    }
    
    public void getTimesWithErrors(long[] times, int words, int errors) {
        for (int i = 0; i < times.length; i++) {
            String cipher = formCipherTextWithErrors(words, errors);
            long start = System.currentTimeMillis();
            String text = this.decrypter.decrypt(cipher);
            long stop = System.currentTimeMillis();
            times[i] = stop - start;
        }
    }

    public void getAllRuntimes() {
        // runtimes when all words are found in the dictionary
        getTimes(this.runtimes100words, 100);
        getTimes(this.runtimes500words, 500);
        getTimes(this.runtimes1000words, 1000);
        getTimes(this.runtimes5000words, 5000);
        getTimes(this.runtimes10000words, 10000);
        
        // runtimes with randomly formed error words
        //System.out.println("0 errors");
        getTimesWithErrors(this.runtimes0Errors, 200, 0);
        //System.out.println("5 errors");
        getTimesWithErrors(this.runtimes5Errors, 200, 5);
        //System.out.println("10 errors");
        getTimesWithErrors(this.runtimes10Errors, 200, 10);
        //System.out.println("15 errors");
        getTimesWithErrors(this.runtimes15Errors, 200, 15);
        //System.out.println("20 errors");
        getTimesWithErrors(this.runtimes20Errors, 200, 20);
    }

    public long getAverage(long[] times) {
        long sum = 0;
        for (int i = 0; i < times.length; i++) {
            sum += times[i];
        }
        return sum / times.length;
    }

    @Override
    public String toString() {
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
