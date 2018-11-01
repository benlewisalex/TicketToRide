package cs340.client.view;

import java.util.List;

import model.ActiveGame;
import model.Game;

public interface ILobbyView {
    public void setJoinedGame(Game game);
    public void setGameList(List<Game> games);
    public void startGame(ActiveGame game);
    public void displayMessage(String msg);
}