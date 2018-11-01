package cs340.client.presenter;

import android.content.Intent;
import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Utils.TrainColor;
import cs340.client.R;
import cs340.client.model.ClientModel;
import cs340.client.model.IUIFacade;
import cs340.client.model.UIFacade;
import cs340.client.view.ClaimRouteView;
import cs340.client.view.DestinationView;
import cs340.client.view.DrawCardView;
import cs340.client.view.AbstractGameView;
import cs340.client.view.EndGameView;
import cs340.client.view.GameView;
import cs340.client.view.HistoryView;
import cs340.client.view.MapFragment;
import cs340.client.view.StatsView;
import model.ActiveGame;
import model.DestinationTicketCard;
import model.EventMessage;
import model.Game;
import model.Player;
import model.Route;
import model.TrainCarCard;
import model.TrainCarCardManager;

public class GamePresenter implements IGamePresenter, Observer {
    private static final String TAG = "GamePresenter";

    private static GamePresenter instance;
    private GameViewState state;
    private final GameViewState defaultState;

    private IUIFacade facade;
    private AbstractGameView view;
    private GameView gameView;
    List<DestinationTicketCard> destinationOptions;

    private GamePresenter() {
//        activeGame = new ActiveGame();
        defaultState = new MapState();
        facade = UIFacade.getInstance();

        state = null;

        Observable o = ClientModel.getInstance();
        o.addObserver(this);
    }

    public static GamePresenter getInstance() {
        if (instance == null)
            instance = new GamePresenter();
        return instance;
    }

    @Override
    public void openClaimRouteView() {
        Intent intent = new Intent(view, ClaimRouteView.class);
        view.startActivity(intent);
    }

    @Override
    public void openDestinationView() {
        facade.requestDestinationTickets();
    }

    @Override
    public void openDrawCardView() {
        Intent intent = new Intent(view, DrawCardView.class);
        view.startActivity(intent);
    }

    @Override
    public void openHistoryView() {
        Intent intent = new Intent(view, HistoryView.class);
        view.startActivity(intent);
    }

    @Override
    public void openStatsView() {
        Intent intent = new Intent(view, StatsView.class);
        view.startActivity(intent);
    }

    @Override
    public void openEndGameView() {
        Intent intent = new Intent(view, EndGameView.class);
        view.startActivity(intent);
    }

    @Override
    public void attach(AbstractGameView view) {
        // Don't open the same view twice
        if (this.view != null && this.view.getClass() == view.getClass()) {
            view.finish();
            return;
        }

        this.view = view;
        if (DestinationView.class.isInstance(view)) {
            state = new DestinationState();
        } else if (ClaimRouteView.class.isInstance(view)) {
            state = new ClaimRouteState();
        } else if (DrawCardView.class.isInstance(view)) {
            state = new DrawCardState();
        }else if (HistoryView.class.isInstance(view)) {
            state = new HistoryState();
        } else if (StatsView.class.isInstance(view)) {
            state = new StatsState();
        } else if (EndGameView.class.isInstance(view)) {
            state = new EndGameState();
        } else {
            state = defaultState;
        }

        state.attach(view);
        state.update();
        Log.d(TAG, "Attached " + view.getClass().getName());
    }

    @Override
    public void takeCard(int index) {
        facade.takeCard(index);
    }

    @Override
    public void drawCard() {
        facade.drawCard();
    }

    @Override
    public boolean selectDestinations(List<DestinationTicketCard> choices) {
        boolean success = false;
        if (facade.getClientPlayer().getDestinationTickets().size() == 0 && choices.size() < 2) {
            view.displayMessage("You must choose at least two destinations");
        } else if (choices.size() < 1) {
            view.displayMessage("You must choose at least one destination");
        } else {
            view.displayMessage(String.format("Chose %d destination cards", choices.size()));
            facade.selectDestinationTickets(choices);
//            destinationOptions = null;
            success = true;
        }
        return success;
    }

    @Override
    public void update(Observable obs, Object o) {
        state.update();
    }

    @Override
    public void sendChat(String message) {
        facade.updateChat(message);
        /*EventMessage chat = new EventMessage(getUserName(), message);
        facade.updateChat(chat, activeGame.getGameID());*/
    }

    @Override
    public void claimRoute(Route route, TrainColor color) {
        facade.claimRoute(route, color);
       /* route.claim(getCurrentPlayer());
        getCurrentPlayer().adjustScore(route.getPoints());
        getCurrentPlayer().useTrainCars(route.getLength());
//        getCurrentPlayer().useTrainCarCards(route.getColor(), route.getLength());

        EventMessage event = new EventMessage(getUserName(), "Claimed Route");
        facade.addGameEvent(event);*/
    }

    private interface GameViewState {
        public void attach(AbstractGameView view);
        public void update();
    }

    private class ClaimRouteState implements GameViewState {
        private ClaimRouteView view;

        @Override
        public void attach(AbstractGameView v) {
            view = (ClaimRouteView) v;
            view.setAvailableRoutes(facade.getUnclaimedRoutes());
        }

        @Override
        public void update() {
            if (ClientModel.getInstance().getToastMessage() != null) {
                view.displayMessage(ClientModel.getInstance().getToastMessage());
            }
            if (!facade.isMyTurn()) {
                view.finish();
            }
            view.setStats(facade.getClientPlayer());
        }
    }

    private class DestinationState implements GameViewState {
        private DestinationView view;

        @Override
        public void attach(AbstractGameView v) {
            view = (DestinationView) v;
            view.setDestinationOptions(facade.getDestinationOptions());
        }

        @Override
        public void update() {
            if (ClientModel.getInstance().getToastMessage() != null) {
                view.displayMessage(ClientModel.getInstance().getToastMessage());
            }
            if (facade.getDestinationOptions() == null || facade.getDestinationOptions().size() == 0) {
                view.finish();
            }
        }
    }

    private class DrawCardState implements GameViewState {
        private DrawCardView view;

        @Override
        public void attach(AbstractGameView v) {
            view = (DrawCardView) v;
        }

        @Override
        public void update() {
            if (ClientModel.getInstance().getToastMessage() != null) {
                view.displayMessage(ClientModel.getInstance().getToastMessage());
            }
            view.setFaceUpCards(facade.getFaceupCards());
            view.setDeckEmpty(facade.deckIsEmpty());

            if (!facade.isMyTurn()) {
                view.finish();
            }
        }
    }

    private class HistoryState implements GameViewState {
        private HistoryView view;

        @Override
        public void attach(AbstractGameView v) {
            view = (HistoryView) v;
        }

        @Override
        public void update() {
            if(facade.getGameOver()) {
                openEndGameView();
                return;
            }
            if (ClientModel.getInstance().getToastMessage() != null) {
                view.displayMessage(ClientModel.getInstance().getToastMessage());
            }
            view.updateChat(facade.getChatHistory());
            view.updateGameHistory(facade.getGameHistory());
        }
    }

    private class MapState implements GameViewState {
        private GameView view;

        @Override
        public void attach(AbstractGameView v) {
            view = (GameView) v;
        }

        @Override
        public void update() {
            if(facade.getGameOver()) {
                openEndGameView();
                return;
            }
            if (ClientModel.getInstance().getToastMessage() != null) {
                view.displayMessage(ClientModel.getInstance().getToastMessage());
            }
            view.setButtonsEnabled(facade.isMyTurn());
            if (facade.getDestinationOptions() != null) {
                Intent intent = new Intent(view, DestinationView.class);
                view.startActivity(intent);
            }
            view.updateClaimedRoutes();
        }
    }

    private class StatsState implements GameViewState {
        private StatsView view;

        @Override
        public void attach(AbstractGameView v) {
            view = (StatsView) v;
            this.view.setActivePlayer(facade.getClientPlayer());
        }

        @Override
        public void update() {
            if(facade.getGameOver()) {
                openEndGameView();
                return;
            }
            if (ClientModel.getInstance().getToastMessage() != null) {
                view.displayMessage(ClientModel.getInstance().getToastMessage());
            }
            Log.d(TAG, "Updating the state to stats state");
            view.setPlayers(facade.getPlayers());
        }
    }

    private class EndGameState implements GameViewState {
        private EndGameView view;

        @Override
        public void attach(AbstractGameView v) {
            view = (EndGameView) v;
        }

        @Override
        public void update() {
            if (ClientModel.getInstance().getToastMessage() != null) {
                view.displayMessage(ClientModel.getInstance().getToastMessage());
            }
            Log.d(TAG, "Updating the state to end game state");
            view.setStats(facade.getPlayers());
        }
    }
}
