package util;

/**
 * Interface for tester classes.
 *
 */
public interface Tester {

    /**
     * Run all tests.
     */
    public void run();

    /**
     * Return the test results as a string.
     * 
     * @return the results as a formatted string
     */
    public String getResults();

}
