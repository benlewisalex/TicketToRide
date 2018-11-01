package cs340.client.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Command.ICommand;
import Utils.PlayerColor;
import Utils.TrainColor;
import cs340.client.communication.ClientCommunicator;
import cs340.client.communication.IServerProxy;
import model.DestinationTicketCard;
import model.EventMessage;
import model.Location;
import model.Player;
import model.Route;
import model.TrainCarCard;

public class UIFacade implements IUIFacade {
    private static final String TAG = "UIFacade";
    private IServerProxy serverProxy = new ClientCommunicator();
    private static UIFacade singleton_instance;
    private static ClientModel client;

    public static UIFacade getInstance() {
        if (singleton_instance == null)
            singleton_instance = new UIFacade();
            client = ClientModel.getInstance();
        return singleton_instance;
    }


    @Override
    public void requestLogin(String username, String password) {
        client.setTempUsername(username);
        client.setTempPassword(password);
        serverProxy.login(username, password);
    }

    @Override
    public void requestRegister(String username, String password) {
        client.setTempUsername(username);
        client.setTempPassword(password);
        serverProxy.register(username, password);
    }

    @Override
    public void requestJoinGame(PlayerColor color, String gameID) {
        serverProxy.joinGame(gameID, ClientModel.getInstance().getAuthtoken(), (double)color.getValue());
        Log.d(TAG, "Requesting to join game");
    }

    @Override
    public void requestStartGame(String gameID) {
        serverProxy.requestStartGame(gameID);
    }

    @Override
    public void requestCreateGame(String gameId, Integer numPlayers) {
        serverProxy.createGame(gameId, (double)numPlayers, ClientModel.getInstance().getAuthtoken());
    }
    @Override
    public void requestUpdateGameList() {
        serverProxy.getGames(ClientModel.getInstance().getAuthtoken());
    }

    @Override
    public List<Player> getPlayers() {
        return ClientModel.getInstance().getActiveGame().getPlayers();
    }

    @Override
    public Player getClientPlayer() {
        List<Player> players = ClientModel.getInstance().getActiveGame().getPlayers();
        for(Player p : players)
        {
            if (p.getAuthtoken().equals(ClientModel.getInstance().getAuthtoken()))
                return p;
        }
        System.out.println("UIFacade Error: getClientPlayer --> no current player matches ClientModels user.");
        return null;
    }

    @Override
    public Player getCurrentPlayer() {
        List<Player> players = ClientModel.getInstance().getActiveGame().getPlayers();
        for(Player p : players)
        {
            if (p.getTurn())
                return p;
        }
        System.out.println("UIFacade Error: getCurrentPlayer --> It is no player's current turn.");
        return null;
    }

    @Override
    public List<EventMessage> getChatHistory() {
        return ClientModel.getInstance().getChatHistory();
    }

    @Override
    public List<EventMessage> getGameHistory() {
        return ClientModel.getInstance().getGameHistory();
    }

    @Override
    public List<Route> getRoutes() {
        return ClientModel.getInstance().getActiveGame().getRoutes();
    }

    @Override
    public List<Route> getUnclaimedRoutes() {
        return ClientModel.getInstance().getActiveGame().getUnclaimedRoutes();
    }

    @Override
    public List<TrainColor> getFaceupCards() {
        List<TrainCarCard> cards = ClientModel.getInstance().getActiveGame().getTrainCardsManager().getFaceupCards();
        List<TrainColor> colors = new ArrayList<TrainColor>(5);
        for(TrainCarCard card: cards) {
            if(card != null) {
                colors.add(card.getColor());
            }
            else {
                colors.add(null);
            }
        }
        return colors;
    }

    @Override
    public Boolean deckIsEmpty() {
        return false;
        //return ClientModel.getInstance().getActiveGame().getDestinationTicketCards().getDestinationCards().size() == 0;
    }

    @Override
    public Boolean isMyTurn() {
        List<Player> players = ClientModel.getInstance().getActiveGame().getPlayers();
        for(Player p : players)
        {
            if (p.getAuthtoken().equals(ClientModel.getInstance().getAuthtoken()))
                return p.getTurn();
        }
        return false;
    }

    @Override
    public List<DestinationTicketCard> getDestinationOptions() {
        return ClientModel.getInstance().getDestinationTicketChoices();
    }

    @Override
    public List<Location> getCities() {
        return ClientModel.getInstance().getActiveGame().getCities();
    }

    @Override
    public boolean getGameOver() {
        return ClientModel.getInstance().isGameOver();
    }

    @Override
    public void setIPandPort(String ipAddress, String port) {
        serverProxy.setIpAddressandPort(ipAddress, port);
    }

    @Override
    public void updateChat(String message) {
        EventMessage eventMessage = new EventMessage(getClientPlayer().getPlayerName(), message);
        serverProxy.updateChat(eventMessage, ClientModel.getInstance().getJoinedGameID());
    }

    @Override
    public void requestDestinationTickets() {
        serverProxy.requestDestinationTickets(ClientModel.getInstance().getJoinedGameID(), ClientModel.getInstance().getAuthtoken());
    }

    @Override
    public void selectDestinationTickets(List<DestinationTicketCard> selectedCards) {
        serverProxy.selectDestinationTickets(selectedCards, ClientModel.getInstance().getJoinedGameID(), ClientModel.getInstance().getAuthtoken());
    }

    @Override
    public void claimRoute(Route route, TrainColor color) {
        serverProxy.requestClaimRoute(route, color, ClientModel.getInstance().getAuthtoken());
    }

    @Override
    public void takeCard(int index) {
        serverProxy.requestTakeCard(index, ClientModel.getInstance().getAuthtoken(), ClientModel.getInstance().getJoinedGameID());
    }

    @Override
    public void drawCard() {
        serverProxy.requestDrawCard(ClientModel.getInstance().getAuthtoken(), ClientModel.getInstance().getJoinedGameID());
    }
}
