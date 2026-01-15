package com.svgs;

import static spark.Spark.*;
import java.util.ArrayList;

public class Main {

    public static Room room = new Room();

    public static void main(String[] args) {
        disableCORS();
        post("/newGame", (req,res) -> {
            ArrayList<Object> list = new ArrayList<>();
            Object canJoin;
            if (room.player1 == null || room.player2 == null){
                canJoin = Boolean.TRUE;
            }
            else {
                canJoin = Boolean.FALSE;
            }
            Object playerName = req.body();
            Object message;
            if (canJoin == Boolean.TRUE) {
                message = "Lobby Can Be Joined";
            }
            else {
                message = "Lobby Can Not Be Joined";
            }
            list.add(canJoin);
            list.add(playerName);
            list.add(message);
            return list;
        });
        get("/shipPlacements", (req,res) -> {
            return null;
        });
        get("/updateGame", (req,res) -> {
            return null;
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