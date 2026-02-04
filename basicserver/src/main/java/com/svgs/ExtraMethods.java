package com.svgs;

public class ExtraMethods { //This is extra methods!

    public static boolean find(int[][] list, int x) { // Returns true if list contains x
        for (int[] subList : list) {
            for (int subInt : subList) {
                if (subInt == x) {
                    return true;
                }
            }
        }
        return false;
    }
}
