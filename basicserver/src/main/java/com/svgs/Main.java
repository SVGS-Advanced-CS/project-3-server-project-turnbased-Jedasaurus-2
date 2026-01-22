package com.svgs;

import com.google.gson.Gson;
import static spark.Spark.*;

public class Main {

    public static Room room = new Room();
    public static Gson gson = new Gson();
    private static String np = "No Player";

    public static void main(String[] args) {
        System.out.println("Hello, world");
        disableCORS();
        post("/newGame", (req, res) -> {
            NewGameReturn newGameReturn = new NewGameReturn(); // Object to be returned
            newGameReturn.playerName = req.body(); // Return the players name? IDK why I did this
            if (!room.player1.equals(np) && !room.player2.equals(np)) { //Is there a open slot in the lobby?
                if (room.player1.equals(np)) { // Is player1 in the lobby
                    room.player1 = newGameReturn.playerName; // If not, make player1
                } else {
                    room.player2 = newGameReturn.playerName; // If they are, make player2
                }
                    newGameReturn.canJoin = Boolean.TRUE;
                    newGameReturn.message = "Lobby Can Be Joined";
            } else { //otherwise, reject them
                newGameReturn.canJoin = Boolean.FALSE;
                newGameReturn.message = "Lobby Can Not Be Joined";
            }
            return gson.toJson(newGameReturn);
        });
        get("/shipPlacements", (req, res) -> {
            ShipPlacements placements = gson.fromJson(req.body(), ShipPlacements.class); // Get the requested stuff
            if (placements.playerName.equals(room.player1)) { // Is this player1's ships or player2's?
                room.ships1 = placements.shipPlacements;
                room.guessBoard1 = new int[10][10];
            }
            if (placements.playerName.equals(room.player2)) { // Repeat for P2
                room.ships2 = placements.shipPlacements;
                room.guessBoard2 = new int[10][10];
            }
            return gson.toJson("Received"); // Tell the server it has all gone through
        });
        get("/updateGame", (req, res) -> {
            UpdateGameReturn updateGameReturn = new UpdateGameReturn();
            if (req.body().equals(room.player1)) { // Is it currently P1's or P2's turn
                updateGameReturn.userBoard = room.ships1;
                updateGameReturn.guessBoard = room.guessBoard1;
                updateGameReturn.isOver = false;
                updateGameReturn.isStarted = true;
                updateGameReturn.turn = room.player2;
                if (!room.player2.equals(np)) { // is there another player
                    updateGameReturn.otherPlayer = room.player2;
                }
                if (!Extras.find(room.ships1, 1)) { // Does player1 have any ships left?
                    // Player2 wins
                }
                if (!Extras.find(room.ships2, 1)) { // Does player2 have any ships left?
                    // Player1 wins
                }
            }
            if (req.body().equals(room.player2)) { // If its P2's turn...
                updateGameReturn.userBoard = room.ships2;
                updateGameReturn.guessBoard = room.guessBoard2;
                updateGameReturn.isOver = false;
                updateGameReturn.isStarted = true;
                updateGameReturn.turn = room.player1;
                if (room.player1 != null) { // is there another player?
                    updateGameReturn.otherPlayer = room.player1;
                }
            }
            return gson.toJson(updateGameReturn);
        });
        post("/makeMove", (req, res) -> {
            MakeMove request = gson.fromJson(req.body(), MakeMove.class);
            if (request.playerName.equals(room.player1)) {
                int[] guess = request.guess; // Guess is an array of x and y
                if (room.ships2[guess[0]][guess[1]] == 0) {
                    room.guessBoard1[guess[0]][guess[1]] = 2; // 2 is a hit on the guessboard
                    room.ships2[guess[0]][guess[1]] = 2; // 2 is a hit ship on your own board
                }
                if (room.ships2[guess[0]][guess[1]] == 1) { // Is there a ship (1) there? (0 is empty)
                    room.guessBoard1[guess[0]][guess[1]] = 1; // 2 is a hit on the guessboard
                    room.ships2[guess[0]][guess[1]] = 3; // 2 is a hit ship on your own board
                } else {
                    // Can't do that
                }
            }
            if (request.playerName.equals(room.player2)) {
                int[] guess = request.guess;
                if (room.ships1[guess[0]][guess[1]] == 1) { // Is there a ship (1) there? (0 is empty)
                    room.guessBoard2[guess[0]][guess[1]] = 2; // 2 is a hit on the guessboard
                    room.ships1[guess[0]][guess[1]] = 2; // 2 is a hit ship on your own board
                } else {
                    // Can't do that
                }
            }
            return gson.toJson("");
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