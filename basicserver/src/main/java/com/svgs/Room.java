package com.svgs;

public class Room {
    //public long roomID;
    public String player1; //Username
    public String player2;
    public int[][] ships1; //This has 0, no ship, 1 there is a ship,
    public int[][] ships2; //2, there is a destroyed ship, and 3, the opponent missed here
    public int[][] guessBoard1; //0, blank, 1 miss, 2 hit
    public int[][] guessBoard2;
    public String turn;

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
