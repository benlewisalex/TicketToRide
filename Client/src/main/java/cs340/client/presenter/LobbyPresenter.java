package cs340.client.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Utils.PlayerColor;
import cs340.client.model.ClientModel;
import cs340.client.model.IUIFacade;
import cs340.client.model.UIFacade;
import cs340.client.view.ILobbyView;
import model.Game;
import model.User;

public class LobbyPresenter implements ILobbyPresenter, Observer {
    private static final String TAG = "LobbyPresenter";
    private static LobbyPresenter instance;

    private ILobbyView view;
    private IUIFacade facade;

    private Game activeGame;
    private List<Game> gameList;

    private LobbyPresenter() {
        facade = UIFacade.getInstance();
        gameList = new ArrayList<Game>();
    }

    public static LobbyPresenter getInstance() {
        if (instance == null)
            instance = new LobbyPresenter();
        return instance;
    }

    @Override
    public void attach(ILobbyView view) {
        this.view = view;
        view.setGameList(gameList);

        Observable o = ClientModel.getInstance();
        o.addObserver(this);
    }

    @Override
    public void createGame(String gameID, Integer playerCount) {
        facade.requestCreateGame(gameID, playerCount);
    }

    @Override
    public void joinGame(String gameID, PlayerColor color) {
        facade.requestJoinGame(color, gameID);
    }

    @Override
    public void update(Observable obs, Object o) {
        if (ClientModel.getInstance().getToastMessage() != null) {
            view.displayMessage(ClientModel.getInstance().getToastMessage());
        }

        if (ClientModel.getInstance().userHasActiveGame()) {
            view.startGame(ClientModel.getInstance().getActiveGame());
            obs.deleteObserver(this);
            return;
        }

        Game g = ClientModel.getInstance().getJoinedGame();
        Log.d(TAG, String.format("Joined game is %s", g));
        if (g != null && !g.equals(activeGame)) {
            activeGame = g;
            view.setJoinedGame(g);
        }

        boolean updateGameList = false;
        List<Game> currentGames = ClientModel.getInstance().getCurrentGames();
        if (gameList.size() != currentGames.size()) {
            updateGameList = true;
        } else {
            for (int i = 0; i < gameList.size(); ++i) {
                Game a = gameList.get(i);
                Game b = currentGames.get(i);
                List<User> aPlayers = a.getCurrentPlayers();
                List<User> bPlayers = b.getCurrentPlayers();
                if (!a.getGameID().equals(b.getGameID()) || aPlayers.size() != bPlayers.size()) {
                    updateGameList = true;
                    break;
                } else {
                    for (int j = 0; j < aPlayers.size(); ++j) {
                        User aUser = aPlayers.get(j);
                        User bUser = bPlayers.get(j);
                        if (!aUser.getUsername().equals(bUser.getUsername())) {
                            updateGameList = true;
                            break;
                        }
                    }
                    if (updateGameList)
                        break;
                }
            }
        }

        if (updateGameList) {
            gameList = ClientModel.getInstance().getCurrentGames();
            view.setGameList(gameList);
        }
    }
}
