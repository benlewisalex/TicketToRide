package model;

import java.util.ArrayList;
import java.util.List;

public class ChatHistory {
    private String gameID;
    private List<EventMessage> chats;

    public ChatHistory(String gameID){
        this.gameID = gameID;
        chats = new ArrayList<>();
    }

    public List<EventMessage> getChats() {
        return chats;
    }

    public String getGameID() {
        return gameID;
    }
}
