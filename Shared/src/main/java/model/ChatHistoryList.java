package model;

import java.util.List;

public class ChatHistoryList {
    List<ChatHistory> chatHistories;

    public List<ChatHistory> getChatHistories() {
        return chatHistories;
    }

    public ChatHistoryList(List<ChatHistory> chatHistories) {

        this.chatHistories = chatHistories;
    }
}
