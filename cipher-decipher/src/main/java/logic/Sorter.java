package logic;

public class Sorter {
    
    public void sortWords(String[] list) {
        sortAlphabetically(list);
        sortByLength(list);
    }
    
    public void sortByLength(String[] list) {
        sortLength(list, 0, list.length - 1);
    }
    
    public void sortLength(String[] list, int a, int b) {
        if (a == b) {
            return;
        }
        int k = (a + b) / 2;
        sortLength(list, a, k);
        sortLength(list, k + 1, b);
        mergeLength(list, a, k, k + 1, b);
    }
    
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
    
    public void sortAlphabetically(String[] list) {
        sortAlphabetical(list, 0, list.length - 1);
    }
    
    public void sortAlphabetical(String[] list, int a, int b) {
        if (a == b) {
            return;
        }
        int k = (a + b) / 2;
        sortAlphabetical(list, a, k);
        sortAlphabetical(list, k + 1, b);
        mergeAlphabetical(list, a, k, k + 1, b);
    }
    
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
