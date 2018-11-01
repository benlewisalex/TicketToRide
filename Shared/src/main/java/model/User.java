package model;

import java.util.UUID;

import Utils.PlayerColor;

public class User {
    private String username;
    private String password;
    private String authtoken;
    private String joinedGameID;
    private PlayerColor playerColor;

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public User(String username, String password, String authtoken) {
        this.username = username;
        this.password = password;
        this.authtoken = authtoken;
        this.playerColor = null;
        this.joinedGameID = null;
//                new Game(2,"sweetie pie", "bcc660d0-08b8-4fc3-a49f-b1a5240af550");

    }

    public void setJoinedGameID(String joinedGameID) {
        this.joinedGameID = joinedGameID;
    }

    public void joinGame(String newGameID) {
        this.joinedGameID = newGameID;
    }

    public String getJoinedGameID() {
        return joinedGameID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
}

