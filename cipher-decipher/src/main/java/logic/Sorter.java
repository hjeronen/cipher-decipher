package logic;

public class Sorter {
    
    public void sortWords(String[] list) {
        sort(list, 0, list.length - 1);
    }
    
    public void sort(String[] list, int a, int b) {
        if (a == b) {
            return;
        }
        int k = (a + b) / 2;
        sort(list, a, k);
        sort(list, k + 1, b);
        merge(list, a, k, k + 1, b);
    }
    
    public void merge(String[] list, int a1, int b1, int a2, int b2) {
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
}
