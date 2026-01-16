package com.svgs;

import com.google.gson.Gson;

import static spark.Spark.*;

public class Main {

    public static Room room = new Room();
    public static Gson gson = new Gson();

    public static void main(String[] args) {
        disableCORS();
        post("/newGame", (req,res) -> {
            NewGameReturn newGameReturn = new NewGameReturn(); //Object to be returned
            newGameReturn.playerName = req.body(); //Return the players name? IDK why I did this
            if (room.player1 != null) { //Add the player
                room.player2 = newGameReturn.playerName;
            } else {
                room.player1 = newGameReturn.playerName;
            }
            if (room.player1 == null || room.player2 == null){ //Is the room NOT full?
                newGameReturn.canJoin = Boolean.TRUE;
                newGameReturn.message = "Lobby Can Be Joined";
            }
            else {
                newGameReturn.canJoin = Boolean.FALSE;
                newGameReturn.message = "Lobby Can Not Be Joined";
            }
            return newGameReturn;
        });
        get("/shipPlacements", (req,res) -> {
            ShipPlacements placements = gson.fromJson(req.body(), ShipPlacements.class); //Get the requested stuff
            if (placements.playerName.equals(room.player1)) { //Is this player1's ships or player2's?
                room.ships1 = placements.shipPlacements;
                room.guessBoard1 = new int[10][10];
            }
            if (placements.playerName.equals(room.player2)) { //Repeat for P2
                room.ships2 = placements.shipPlacements;
                room.guessBoard2 = new int[10][10];
            }
            return "Received"; //Tell the server it has all gone through
        });
        get("/updateGame", (req,res) -> {
            UpdateGameReturn updateGameReturn = new UpdateGameReturn();
            if (req.body().equals(room.player1)) { //Is it currently P1's or P2's turn
                updateGameReturn.userBoard = room.ships1;
                updateGameReturn.guessBoard = room.guessBoard1;
                updateGameReturn.isOver = false;
                updateGameReturn.isStarted = true;
                updateGameReturn.turn = room.player2;
            }
            if (req.body().equals(room.player2)) { //If its P2's turn...
                updateGameReturn.userBoard = room.ships2;
                updateGameReturn.guessBoard = room.guessBoard2;
                updateGameReturn.isOver = false;
                updateGameReturn.isStarted = true;
                updateGameReturn.turn = room.player1;
            }
            return updateGameReturn;
        });
        post("/makeMove", (req,res) -> {
            return null;
        });
    }

    public static void disableCORS() {
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });

        options("/*", (req, res) -> {
            String accessControlRequestHeaders = req.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                res.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = req.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                res.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });
    }
}