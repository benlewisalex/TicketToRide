package cs340.server;

import java.util.List;

import Command.ICommand;
import Utils.TrainColor;
import model.DestinationTicketCard;
import model.DestinationTicketList;
import model.EventMessage;
import model.Route;

/**
 * Created by benle on 7/6/2018.
 */

public interface IServerFacade {

    public List<ICommand> login(String username, String password);

    public List<ICommand> register(String username, String password);

    public List<ICommand> createGame(String gameID, Double numPossiblePlayers);

    public List<ICommand> joinGame(String gameID, String authToken, Double playerColorDouble);

    public List<ICommand> updateChat(EventMessage message, String gameID);

    public List<ICommand> requestDestinationTickets(String gameID, String authToken);

    public List<ICommand> selectDestinationTickets(DestinationTicketList selectedCards, String gameID, String authToken);

    public List<ICommand> claimRoute(Route route, TrainColor color, String authtoken);

    public List<ICommand> takeCard(Double index, String authToken);

    public List<ICommand> drawCard(String authToken);

    public List<ICommand> updateCommandList(Double commandListIndex, String authtoken, String gameID);

    public List<ICommand> getGames(String authToken);

}
