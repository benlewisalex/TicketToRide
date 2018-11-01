package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import Utils.PlayerColor;

public class Game implements Serializable {
    private int numPossiblePlayers;
    private int currentNumPlayers;
    private List<User> currentPlayers;
    private String gameName;
    private String gameID;
    private boolean started;

    public Game(int numPossiblePlayers, String gameName, String gameID) {
        this.numPossiblePlayers = numPossiblePlayers;
        this.currentNumPlayers = 0;
        this.gameID = gameID;
        this.gameName = gameName;
        this.currentPlayers = new LinkedList<User>();
        this.started = false;
    }

    public List<User> getCurrentPlayers() {
        return currentPlayers;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public boolean hasStarted() {
        return started;
    }

    public void setStarted() {
        if(currentNumPlayers == numPossiblePlayers){
            started = true;
        }
    }

    public void addPlayer(User newPlayer) {
        if(!isUserAlreadyInGame(newPlayer) && started == false) {
            currentPlayers.add(newPlayer);
            currentNumPlayers++;
            if(currentNumPlayers == numPossiblePlayers){
                started = true;
            }
        }
    }

    public void removePlayer(User playerToRemove) {
        for (int i = 0; i < currentPlayers.size(); i++) {
            if (currentPlayers.get(i).equals(playerToRemove)) {
                currentPlayers.remove(i);
                currentNumPlayers--;
            }
        }
    }

    public int getNumPossiblePlayers() {
        return numPossiblePlayers;
    }

    public void setNumPossiblePlayers(int numPossiblePlayers) {
        this.numPossiblePlayers = numPossiblePlayers;
    }

    public int getCurrentNumPlayers() {
        return currentNumPlayers;
    }

    public void setCurrentNumPlayers(int currentNumPlayers) {
        this.currentNumPlayers = currentNumPlayers;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public boolean isPlayerColorOpen(PlayerColor playerColor){
        for(int i = 0; i < currentPlayers.size(); i++){
            if(currentPlayers.get(i).getPlayerColor() == playerColor){
                return false;
            }
        }
        return true;
    }

    public boolean isUserAlreadyInGame(User user){
        for(int i = 0; i < currentPlayers.size(); i++){
            if(currentPlayers.get(i).equals(user)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Game)
        {
            return ((Game) o).getGameID().equals(this.getGameID());
        }
        return false;
    }
}
