package com.svgs;

import com.google.gson.Gson;

import static spark.Spark.*;
import java.util.ArrayList;

public class Main {

    public static Room room = new Room();
    public static Gson gson = new Gson();

    public static void main(String[] args) {
        disableCORS();
        post("/newGame", (req,res) -> {
            ArrayList<Object> list = new ArrayList<>(); //List to be returned
            Object canJoin;
            Object message;
            Object playerName = req.body();
            if (room.player1 != null) { //Add the player
                room.player2 = playerName.toString();
            } else {
                room.player1 = playerName.toString();
            }
            if (room.player1 == null || room.player2 == null){ //Is the room NOT full?
                canJoin = Boolean.TRUE;
                message = "Lobby Can Be Joined";
            }
            else {
                canJoin = Boolean.FALSE;
                message = "Lobby Can Not Be Joined";
            }
            list.add(canJoin);
            list.add(playerName);
            list.add(message);
            return list;
        });
        get("/shipPlacements", (req,res) -> {
            ShipPlacements placements = gson.fromJson(req.body(), ShipPlacements.class); //Get the requested stuff
            if (placements.playerName.equals(room.player1)) { //Is this player1's ships or player2's?
                room.ships1 = placements.shipPlacements;
            } else {
                room.ships2 = placements.shipPlacements;
            }
            return "Received"; //Tell the server it has all gone through
        });
        get("/updateGame", (req,res) -> {

            return "";
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