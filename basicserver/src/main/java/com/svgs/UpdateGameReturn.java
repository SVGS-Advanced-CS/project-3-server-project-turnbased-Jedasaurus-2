package com.svgs;

public class UpdateGameReturn {
    public boolean isStarted;
    public boolean isOver;
    public int[][] guessBoard;
    public int[][] userBoard;
    public String turn; //The user who should go next
    //So if this returns player2 the front end should make it player2's turn
}
