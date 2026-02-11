package com.svgs;

public class ExtraMethods { //This is extra methods!

    public static boolean find(int[][] list, int x) { // Returns true if list contains x
        for (int[] subList : list) {
            if (find(subList, x)) {
                return true;
            }
        }
        return false;
    }

    public static boolean find(int[] list, int x){
        for (int y : list){
            if (y == x){
                return true;
            }
        }
        return false;
    }
}
