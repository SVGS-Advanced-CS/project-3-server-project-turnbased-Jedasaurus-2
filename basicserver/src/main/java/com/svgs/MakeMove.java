package com.svgs;

public class MakeMove {
    public String playerName;
    public int[] guess; // X and Y value

    public MakeMove(String playerName, int[] guess) {
        this.playerName = playerName;
        this.guess = guess;
    }
}
