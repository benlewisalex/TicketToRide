package cs340.server.model;

import java.util.ArrayList;
import java.util.List;

import model.ActiveGame;
import model.ChatHistory;
import model.DestinationTicketCard;
import model.Game;
import model.Player;
import model.User;

/**
 * Created by benle on 7/6/2018.
 */

public class ServerModel {
    private List<ActiveGame> activeGames;
    private List<ChatHistory> chatHistories;
    private List<Game> lobbyGames;
    private List<User> users;
    private static ServerModel singleton_instance = null;


    private static final String commandFacadePath = "cs340.client.model.CommandFacade";

    private ServerModel() {
        lobbyGames = new ArrayList<>();
        activeGames = new ArrayList<>();
        chatHistories = new ArrayList<>();
        users = new ArrayList<>();
    }

    public static ServerModel getInstance() {
        if (singleton_instance == null)
            singleton_instance = new ServerModel();
        return singleton_instance;
    }

    public void setActiveGames(List<ActiveGame> activeGames) {
        this.activeGames = activeGames;
    }

    public void setChatHistories(List<ChatHistory> chatHistories) {
        this.chatHistories = chatHistories;
    }

    public void setLobbyGames(List<Game> lobbyGames) {
        this.lobbyGames = lobbyGames;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<ActiveGame> getActiveGames() {
        return activeGames;
    }

    public List<ChatHistory> getChatHistories(){
        return chatHistories;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUser(String authToken){
        for(User user : users){
            if(user.getAuthtoken().equals(authToken)){
                return user;
            }
        }
        return null;
    }

    public ActiveGame getActiveGame(String gameID){
        for(ActiveGame game : activeGames){
            if(game.getGameID().equals(gameID)){
                return game;
            }
        }
        return null;
    }

    public List<Game> getLobbyGames() {
        return lobbyGames;
    }

    public void createGame(Game game) {
        lobbyGames.add(game);
    }

    public void register(User user) {
        users.add(user);
    }

    public ChatHistory getChatHistory(String gameID){
        for(ChatHistory chatHistory : chatHistories){
            if(chatHistory.getGameID().equals(gameID)){
                return chatHistory;
            }
        }
        return null;
    }




}
