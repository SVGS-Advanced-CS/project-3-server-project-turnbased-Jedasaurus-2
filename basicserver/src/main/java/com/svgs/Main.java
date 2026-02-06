package com.svgs;

import com.google.gson.Gson;
import static spark.Spark.*;

public class Main {

    public static Room room = new Room();
    public static Gson gson = new Gson();
    private static final String np = "No Player";

    public static void main(String[] args) {
        System.out.println("Hello, world"); //So I know it started
        port(4567);
        disableCORS();
        post("/newGame", (req, res) -> {
            NewGameReturn newGameReturn = new NewGameReturn(); // Object to be returned
            newGameReturn.playerName = req.body(); // Return the players name? IDK why I did this
            if (room.player1.equals(np)) { // Is player1 in the lobby
                room.player1 = newGameReturn.playerName; // If not, make player1
                newGameReturn.canJoin = Boolean.TRUE;
                newGameReturn.message = "Lobby Can Be Joined";
            } else if (room.player2.equals(np)) {
                room.player2 = newGameReturn.playerName; // If they are, make player2
                newGameReturn.canJoin = Boolean.TRUE;
                newGameReturn.message = "Lobby Can Be Joined";
                return gson.toJson(newGameReturn);
            } else {                                                    // otherwise, reject them
                newGameReturn.canJoin = Boolean.FALSE;
                newGameReturn.message = "Lobby Can Not Be Joined";
            }
            room.turn = room.player1;
            return gson.toJson(newGameReturn);
        });
        post("/shipPlacements", (req, res) -> {
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
            UpdateGame updateGame = new UpdateGame(req.queryParams("User"));
            if (updateGame.User.equals(room.player1)) { // Is it currently P1's or P2's turn
                updateGameReturn.userBoard = room.ships1;
                updateGameReturn.guessBoard = room.guessBoard1;
                updateGameReturn.isOver = false;
                updateGameReturn.isStarted = true;
                updateGameReturn.turn = room.turn;
                if (!room.player2.equals(np)) { // is there another player
                    updateGameReturn.otherPlayer = room.player2;
                }
            }
            if (updateGame.User.equals(room.player2)) { // If its P2's turn...
                updateGameReturn.userBoard = room.ships2;
                updateGameReturn.guessBoard = room.guessBoard2;
                updateGameReturn.isOver = false;
                updateGameReturn.isStarted = true;
                updateGameReturn.turn = room.turn;
                if (room.player1 != null) { // is there another player?
                    updateGameReturn.otherPlayer = room.player1;
                }
            }
            if (!ExtraMethods.find(room.ships1, 1)) { // Does player1 have any ships left? If  not...
                updateGameReturn.isOver = true;
                updateGameReturn.message = "Player 2 wins";
            }
            if (!ExtraMethods.find(room.ships2, 1)) { // Does player2 have any ships left? If not...
                updateGameReturn.isOver = true;
                updateGameReturn.message = "Player 1 wins";
            }
            return gson.toJson(updateGameReturn);
        });
        post("/makeMove", (req, res) -> {
            MakeMove request = gson.fromJson(req.body(), MakeMove.class);
            MakeMoveReturn makeMoveReturn = new MakeMoveReturn();
            if (request.playerName.equals(room.turn)) {
                if (request.playerName.equals(room.player1)) {
                    int[] guess = request.guess; // Guess is an array of x and y
                    if (room.ships2[guess[0]][guess[1]] == 0) { // Did they miss?
                        room.guessBoard1[guess[0]][guess[1]] = 1; // 1 is a miss on the guessboard
                        room.ships2[guess[0]][guess[1]] = 1; // 1 is a miss on the opponents board
                        room.turn = room.player2;
                        makeMoveReturn.turn = room.turn;
                        makeMoveReturn.message = "Hit";
                    } else if (room.ships2[guess[0]][guess[1]] == 3) { // Did they hit?
                        room.guessBoard1[guess[0]][guess[1]] = 2; // 2 is a hit on the guessboard
                        room.ships2[guess[0]][guess[1]] = 2; // 2 is a hit on the opponents board
                        room.turn = room.player2;
                        makeMoveReturn.turn = room.player2;
                        makeMoveReturn.message = "Miss";
                    } else { //If they have already guessed?
                        makeMoveReturn.message = "Illegal Move, cannot guess the same place twice";
                    }
                }
                if (request.playerName.equals(room.player2)) {
                    int[] guess = request.guess; // Guess is an array of x and y
                    if (room.ships1[guess[0]][guess[1]] == 0) { // Did they miss?
                        room.guessBoard2[guess[0]][guess[1]] = 1; // 1 is a miss on the guessboard
                        room.ships1[guess[0]][guess[1]] = 1; // 1 is a miss on the opponents board
                        room.turn = room.player1;
                        makeMoveReturn.turn = room.turn;
                        makeMoveReturn.message = "Hit";
                    } else if (room.ships1[guess[0]][guess[1]] == 3) { // Did they hit?
                        room.guessBoard2[guess[0]][guess[1]] = 2; // 2 is a hit on the guessboard
                        room.ships1[guess[0]][guess[1]] = 2; // 2 is a hit on the opponents board
                        room.turn = room.player1;
                        makeMoveReturn.turn = room.player1;
                        makeMoveReturn.message = "Miss";
                    } else { //If they have already guessed?
                        makeMoveReturn.message = "Illegal Move, cannot guess the same place twice";
                    }
                }
                return gson.toJson(makeMoveReturn);
            }
            makeMoveReturn.turn = room.turn;
            makeMoveReturn.message = "Not your turn";
            return gson.toJson(makeMoveReturn);
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