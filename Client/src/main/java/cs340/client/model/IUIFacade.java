package cs340.client.model;
import android.util.EventLog;

import java.util.List;

import Utils.PlayerColor;
import Utils.TrainColor;
import model.DestinationTicketCard;
import model.EventMessage;
import model.Location;
import model.Player;
import model.Route;

public interface IUIFacade {

    //Server Requests
    void requestLogin(String username, String password);
    void requestRegister(String username, String password);
    void requestJoinGame(PlayerColor color, String gameID);
    void requestStartGame(String gameID);
    void requestCreateGame(String gameID, Integer numPlayers);
    void updateChat(String message);
    void requestDestinationTickets();
    void selectDestinationTickets(List<DestinationTicketCard> selectedCards);
    void claimRoute(Route route, TrainColor color);
    void takeCard(int index);
    void drawCard();
    void requestUpdateGameList();
//    void requestGetActiveGame();
//    void updateActiveGame(ActiveGame activeGame);
//    void endTurn(ActiveGame endTurnGameState);

    //Model requests
    List<Player> getPlayers();
    Player getClientPlayer();
    Player getCurrentPlayer();
    List<EventMessage> getChatHistory();
    List<EventMessage> getGameHistory();
    List<Route> getRoutes();
    List<Route> getUnclaimedRoutes();
    List<TrainColor> getFaceupCards();
    Boolean deckIsEmpty();
    Boolean isMyTurn();
    List<DestinationTicketCard> getDestinationOptions();
    List<Location> getCities();
    boolean getGameOver();
    void setIPandPort(String ipAddress, String port);


}
