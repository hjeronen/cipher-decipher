package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import logic.Decrypter;

public class PerformanceTester {

    private Decrypter decrypter;
    private long[] runtimes100words;
    private long[] runtimes1000words;
    private long[] runtimes10000words;
    private ArrayList<String> words;

    public PerformanceTester(Decrypter d) {
        this.decrypter = d;
        this.runtimes100words = new long[100];
        this.runtimes1000words = new long[100];
        this.runtimes10000words = new long[100];
        this.words = new ArrayList<String>();
        getTestWords();
    }

    public void getTestWords() {
        try {
            Scanner reader = new Scanner(new File("dictionary_long.txt"));
            while (reader.hasNext()) {
                this.words.add(reader.nextLine());
            }
        } catch (Exception exception) {
            System.out.println("Dictionary creation failed.");
        }
    }

    public void run() {
        getAllRuntimes();
    }

    public String formCipherText(int numberOfWords) {
        Crypter crypter = new Crypter();
        String text = "";
        Random ran = new Random();
        for (int i = 0; i < numberOfWords; i++) {
            int index = ran.nextInt(this.words.size());
            text += this.words.get(index);
            if (i < numberOfWords) {
                text += " ";
            }
        }
        return crypter.encrypt(text);
    }

    public void getTimes(long[] times, int words) {
        for (int i = 0; i < 100; i++) {
            String cipher = formCipherText(words);
            long start = System.currentTimeMillis();
            String text = this.decrypter.decrypt(cipher);
            long stop = System.currentTimeMillis();
            times[i] = stop - start;
        }
    }

    public void getAllRuntimes() {
        getTimes(this.runtimes100words, 100);
        getTimes(this.runtimes1000words, 1000);
        getTimes(this.runtimes10000words, 10000);
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
        r += "100 words average time ms: " + getAverage(this.runtimes100words) + "\n";
        r += "1000 words average time ms: " + getAverage(this.runtimes1000words) + "\n";
        r += "10000 words average time ms: " + getAverage(this.runtimes10000words);
        return r;
    }
}
