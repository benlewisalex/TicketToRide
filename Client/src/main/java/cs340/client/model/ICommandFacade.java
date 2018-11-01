package cs340.client.model;

import model.ActiveGame;
import model.ChatHistory;
import model.DestinationTicketList;
import model.GameList;
import model.Player;
import model.Route;
import model.TrainCarCard;
import model.TrainCarCardList;

public interface ICommandFacade {
    void login(String authtoken);
    void register(String authtoken);
    void updateGameList(GameList games);
    void displayMessage(String message);
    void startGame(ActiveGame activeGame);
    void setActiveGame(ActiveGame activeGame);
    void updateActiveGame(ActiveGame activeGame);
    void updateChat(ChatHistory messages);
    void overridePlayerDestinationTickets(String playerName, DestinationTicketList tickets);
    void useTrainCarCards(String playerName, TrainCarCardList trainCards);
//    void setInitialDestinationTickets(List<DestinationTicketCard> initialDestinationTickets);

    void setDestinationTicketsOptions(String playerName, DestinationTicketList destinationTicketOptions);
    void claimRoute(Route route, Player owner);
    void advanceTurn(String nextPlayerName);
    void setPlayerDestinationTickets(String playerName, DestinationTicketList tickets);
    void addTrainCarCard(String playerName, TrainCarCard card);
    void setDisplayedTrainCarCards(TrainCarCardList cards);
    void endGame();
    void setJoinedGame(String gameID);
    void endTurn(Player player);
}
