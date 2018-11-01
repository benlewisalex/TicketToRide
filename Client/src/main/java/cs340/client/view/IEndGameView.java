package cs340.client.view;

import java.util.List;

import model.Player;

public interface IEndGameView {

    public void setStats(List<Player> players);

    public void displayMessage(String toastMessage);
}
