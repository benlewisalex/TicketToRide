package cs340.client.presenter;

import Utils.PlayerColor;
import cs340.client.view.ILobbyView;

public interface ILobbyPresenter {
    public void attach(ILobbyView view);
    public void createGame(String gameID, Integer playerCount);
    public void joinGame(String gameID, PlayerColor color);
}