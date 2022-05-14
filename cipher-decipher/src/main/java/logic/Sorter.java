package logic;

/**
 * A class for sorting word arrays. Sorts words alphabetically and by length.
 */
public class Sorter {

    /**
     * Sorts words first alphabetically and then by length.
     *
     * @param list list of words that is sorted
     */
    public void sortWords(String[] list) {
        sortAlphabetically(list);
        sortByLength(list);
    }

    /**
     * Sorts words by length using a merge sort algorithm.
     *
     * @param list list of words that is sorted
     */
    public void sortByLength(String[] list) {
        sortLength(list, 0, list.length - 1);
    }

    /**
     * Sort for merge sort words by length.
     *
     * @param list list of words that is sorted
     * @param a first index of the list
     * @param b last index of the list
     */
    public void sortLength(String[] list, int a, int b) {
        if (a == b) {
            return;
        }
        int k = (a + b) / 2;
        sortLength(list, a, k);
        sortLength(list, k + 1, b);
        mergeLength(list, a, k, k + 1, b);
    }

    /**
     * Merge for merge sort words by length.
     *
     * @param list list of words that is sorted
     * @param a1 first index of the first half of the list
     * @param b1 last index of the first half of the list
     * @param a2 first index of the second half of the list
     * @param b2 last index of the second half of the list
     */
    public void mergeLength(String[] list, int a1, int b1, int a2, int b2) {
        int a = a1;
        int b = b2;
        String[] temp = new String[b + 1];
        for (int i = a; i <= b; i++) {
            if (a2 > b2 || (a1 <= b1 && list[a1].length() >= list[a2].length())) {
                temp[i] = list[a1];
                a1 += 1;
            } else {
                temp[i] = list[a2];
                a2 += 1;
            }
        }
        for (int i = a; i <= b; i++) {
            list[i] = temp[i];
        }
    }

    /**
     * Sorts words alphabetically using a merge sort algorithm.
     *
     * @param list list of words that is sorted
     */
    public void sortAlphabetically(String[] list) {
        sortAlphabetical(list, 0, list.length - 1);
    }

    /**
     * Sort for merge sort words alphabetically.
     *
     * @param list list of words that is sorted
     * @param a first index of the list
     * @param b last index of the list
     */
    public void sortAlphabetical(String[] list, int a, int b) {
        if (a == b) {
            return;
        }
        int k = (a + b) / 2;
        sortAlphabetical(list, a, k);
        sortAlphabetical(list, k + 1, b);
        mergeAlphabetical(list, a, k, k + 1, b);
    }

    /**
     * Merge for merge sort words alphabetically.
     *
     * @param list list of words that is sorted
     * @param a1 first index of the first half of the list
     * @param b1 last index of the first half of the list
     * @param a2 first index of the second half of the list
     * @param b2 last index of the second half of the list
     */
    public void mergeAlphabetical(String[] list, int a1, int b1, int a2, int b2) {
        int a = a1;
        int b = b2;
        String[] temp = new String[b + 1];
        for (int i = a; i <= b; i++) {
            if (a2 > b2 || (a1 <= b1 && list[a1].compareTo(list[a2]) <= 0)) {
                temp[i] = list[a1];
                a1 += 1;
            } else {
                temp[i] = list[a2];
                a2 += 1;
            }
        }
        for (int i = a; i <= b; i++) {
            list[i] = temp[i];
        }
    }
}
