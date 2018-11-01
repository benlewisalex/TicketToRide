package cs340.client.view;

import java.util.List;

import model.EventMessage;

public interface IHistoryView {

    void updateChat(List<EventMessage> chatHistory);

    void updateGameHistory(List<EventMessage> gameHistory);

    void displayMessage(String toastMessage);
}
