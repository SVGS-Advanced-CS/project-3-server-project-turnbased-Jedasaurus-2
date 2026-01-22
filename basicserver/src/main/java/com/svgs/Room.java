package com.svgs;

public class Room {
    //public long roomID;
    public String player1;
    public String player2;
    public int[][] ships1;
    public int[][] ships2;
    public int[][] guessBoard1;
    public int[][] guessBoard2;

/*
    public Room(String player1, String player2, int[][] ships1, int[][] ships2){
        this.player1 = player1;
        this.player2 = player2;
        this.ships1 = ships1;
        this.ships2 = ships2;
    }

    public Room(String player1, String player2){
        this.player1 = player1;
        this.player2 = player2;
        ships1 = null;
        ships2 = null;
    }
*/

    public Room(){
        player1 = "No Player";
        player2 = "No Player";
        ships1 = null;
        ships2 = null;
        guessBoard1 = null;
        guessBoard2 = null;
    }
}
