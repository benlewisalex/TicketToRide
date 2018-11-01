package cs340.client.model;

import android.util.Log;

import cs340.client.communication.Poller;
import model.ActiveGame;
import model.ChatHistory;
import model.DestinationTicketList;
import model.Game;
import model.GameList;
import model.Player;
import model.Route;
import model.TrainCarCard;
import model.TrainCarCardList;

public class CommandFacade implements ICommandFacade {
    private Poller poller;
    private static final String TAG = "CommandFacade";
    private static CommandFacade singleton_instance;

    public static CommandFacade getInstance() {
        if (singleton_instance == null)
            singleton_instance = new CommandFacade();
        return singleton_instance;
    }

    @Override
    public void displayMessage(String errorMessage) {
        ClientModel.getInstance().setToastMessage(errorMessage);
    }

    @Override
    public void login(String authtoken) {
        ClientModel.getInstance().login(authtoken);
//        poller = new Poller();
        Poller.getInstance().enable();
    }

    @Override
    public void register(String authtoken) {
        ClientModel.getInstance().login(authtoken);
//        poller = new Poller();
        Poller.getInstance().enable();
    }

    @Override
    public void startGame(ActiveGame activeGame) {
        ClientModel.getInstance().setActiveGame(activeGame);
    }

    @Override
    public void updateGameList(GameList games) {
        for (Game game : games.getGames()) {
            Log.d(TAG, String.format("Game %s has %d players", game.getGameName(), game.getCurrentNumPlayers()));
        }
        ClientModel.getInstance().setCurrentGames(games.getGames());
    }

    @Override
    public void setActiveGame(ActiveGame activeGame) {
        ClientModel.getInstance().setActiveGame(activeGame);
    }

    @Override
    public void updateActiveGame(ActiveGame activeGame) {
        ClientModel.getInstance().setActiveGame(activeGame);
    }

    @Override
    public void updateChat(ChatHistory messages) {
        ClientModel.getInstance().setChatHistory(messages.getChats());
    }

    @Override
    public void overridePlayerDestinationTickets(String playerName, DestinationTicketList tickets) {
        ClientModel.getInstance().overridePlayerDestinationTickets(playerName, tickets);
    }

    @Override
    public void useTrainCarCards(String playerName, TrainCarCardList trainCards) {
        ClientModel.getInstance().useTrainCarCards(playerName, trainCards);
    }

//    @Override
//    public void setInitialDestinationTickets(List<DestinationTicketCard> initialDestinationTickets) {
//        ClientModel.getInstance().setDestinationTicketChoices(initialDestinationTickets);
//    }

    @Override
    public void setDestinationTicketsOptions(String playerName, DestinationTicketList destinationTicketOptions) {
        if (playerName.equals(ClientModel.getInstance().getUser().getUsername())) {
            ClientModel.getInstance().setDestinationTicketChoices(destinationTicketOptions.getTickets());
        }
    }

    @Override
    public void claimRoute(Route route, Player owner) {
        ClientModel.getInstance().claimRoute(route, owner);
    }

    @Override
    public void advanceTurn(String nextPlayerName) {
        ClientModel.getInstance().advanceTurn(nextPlayerName);
    }

    @Override
    public void setPlayerDestinationTickets(String playerName, DestinationTicketList tickets) {
        for (Player p : ClientModel.getInstance().getActiveGame().getPlayers()) {
            if(p.getPlayerName().equals(playerName)) {
                p.addDestinationTickets(tickets.getTickets());
                if (playerName.equals(ClientModel.getInstance().getUser().getUsername())) {
                    ClientModel.getInstance().setDestinationTicketChoices(null);
                }
            }
        }
    }

    @Override
    public void addTrainCarCard(String playerName, TrainCarCard card) {
        ClientModel.getInstance().addTrainCarCard(playerName, card);
    }

    @Override
    public void setDisplayedTrainCarCards(TrainCarCardList cards) {
        ClientModel.getInstance().setDisplayedTrainCards(cards.getCards());
    }

    @Override
    public void endGame() {
        ClientModel.getInstance().setGameOver(true);
    }

    @Override
    public void setJoinedGame(String gameID) {
        ClientModel.getInstance().getUser().setJoinedGameID(gameID);
    }

    @Override
    public void endTurn(Player player) {
        ClientModel.getInstance().endTurn(player);
    }
}
