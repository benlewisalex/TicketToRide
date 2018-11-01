package cs340.client.communication;


import java.util.List;

import Utils.TrainColor;
import model.DestinationTicketCard;
import model.EventMessage;
import model.Route;

public interface IServerProxy {

    public void login(String username, String password);

    public void register(String username, String password);

    public void joinGame(String gameID, String authToken, Double playerColorDouble);

    public void requestStartGame(String gameID);

    public void createGame(String gameID, Double numPossiblePlayers, String authToken);

    public void updateChat(EventMessage eventMessage, String gameID);

    public void requestDestinationTickets(String gameID, String authtoken);

    public void selectDestinationTickets(List<DestinationTicketCard> cardsToChooseFrom, String gameID, String authtoken);

    public void requestClaimRoute(Route route, TrainColor color, String authtoken);

    public void requestTakeCard(Integer index, String authtoken, String gameID);

    public void requestDrawCard(String authtoken, String gameID);

    public void requestUpdateCommandList(Integer commandListIndex, String authtoken, String gameID);

    public void getGames(String authToken);

    public void setIpAddressandPort(String ipAddress, String port);

//    public void getActiveGame(String joinGameID);

//    public void updateActiveGame(ActiveGame activeGame);

//    public void endTurn(ActiveGame endTurnGameState);
}
