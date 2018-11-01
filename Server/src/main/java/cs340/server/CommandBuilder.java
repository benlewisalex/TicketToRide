package cs340.server;

import java.util.List;

import Command.Command;
import cs340.server.model.ServerModel;
import model.ActiveGame;
import model.ChatHistory;
import model.DestinationTicketCard;
import model.DestinationTicketList;
import model.Game;
import model.GameList;
import model.Player;
import model.Route;
import model.TrainCarCard;
import model.TrainCarCardList;

public class CommandBuilder {

    private static final String commandFacadePath = "cs340.client.model.CommandFacade";

    private ServerModel serverModel = ServerModel.getInstance();

    public Command buildLoginCommand(String authToken, boolean isLogin) {
        Class<?>[] logRegClasses = new Class<?>[1];
        logRegClasses[0] = String.class;
        Object[] logRegObjects = new Object[1];
        logRegObjects[0] = authToken;
        Command loginCommand = new Command(commandFacadePath, "login", logRegClasses, logRegObjects);
        return loginCommand;
    }

    public Command buildRegisterCommand(String authToken, boolean isLogin) {
        Class<?>[] logRegClasses = new Class<?>[1];
        logRegClasses[0] = String.class;
        Object[] logRegObjects = new Object[1];
        logRegObjects[0] = authToken;
        Command registerCommand = new Command(commandFacadePath, "register", logRegClasses, logRegObjects);
        return registerCommand;
    }

    public Command buildUpdateGameListCommand() {
        Class<?>[] updateClasses = new Class<?>[1];
        updateClasses[0] = GameList.class;
        Object[] updateObjects = new Object[1];
        List<Game> lobbyGames = serverModel.getLobbyGames();
        updateObjects[0] = new GameList(lobbyGames);
//        for (Game game : lobbyGames) {
//            System.out.println(String.format("Game %s has %d players", game.getGameName(), game.getCurrentNumPlayers()));
//        }
        Command updateGameCommand = new Command(commandFacadePath, "updateGameList", updateClasses, updateObjects);
        return updateGameCommand;
    }

    public Command buildDisplayMessageCommand(String errorMessage) {
        Class<?>[] displayMessageClasses = new Class<?>[1];
        displayMessageClasses[0] = String.class;
        Object[] displayMessageObjects = new Object[1];
        displayMessageObjects[0] = errorMessage;
        Command updateGameCommand = new Command(commandFacadePath, "displayMessage", displayMessageClasses, displayMessageObjects);
        return updateGameCommand;
    }

    public Command buildUpdateChatCommand(ChatHistory chatHistory){
        Class<?>[] updateChatClasses = new Class<?>[1];
        updateChatClasses[0] = ChatHistory.class;
        Object[] updateChatObjects = new Object[1];
        updateChatObjects[0] = chatHistory;
        Command waitForActiveGame = new Command(commandFacadePath, "updateChat", updateChatClasses, updateChatObjects);
        return waitForActiveGame;
    }

    public Command buildSetDestinationTicketsOptionsCommand(String playerName, List<DestinationTicketCard> options){
        Class<?>[] setDestTickClasses = new Class<?>[2];
        setDestTickClasses[0] = String.class;
        setDestTickClasses[1] = DestinationTicketList.class;
        Object[] setDestTickObjects = new Object[2];
        setDestTickObjects[0] = playerName;
        setDestTickObjects[1] = new DestinationTicketList(options);
        Command setDestTick = new Command(commandFacadePath, "setDestinationTicketsOptions", setDestTickClasses, setDestTickObjects);
        return setDestTick;
    }

    public Command buildClaimRouteCommand(Route route, Player owner){
        Class<?>[] setClaimRouteClasses = new Class<?>[2];
        setClaimRouteClasses[0] = Route.class;
        setClaimRouteClasses[1] = Player.class;
        Object[] setClaimRouteObjects = new Object[2];
        setClaimRouteObjects[0] = route;
        setClaimRouteObjects[1] = owner;
        Command setDestTick = new Command(commandFacadePath, "claimRoute", setClaimRouteClasses, setClaimRouteObjects);
        return setDestTick;
    }

    public Command buildAdvanceTurnCommand(String nextPlayerName){
        Class<?>[] setAdvanceTurnClasses = new Class<?>[1];
        setAdvanceTurnClasses[0] = String.class;
        Object[] setAdvanceTurnObjects = new Object[1];
        setAdvanceTurnObjects[0] = nextPlayerName;
        Command setDestTick = new Command(commandFacadePath, "advanceTurn", setAdvanceTurnClasses, setAdvanceTurnObjects);
        return setDestTick;
    }

    public Command buildSetPlayerDestinationTicketsCommand(String playerName, List<DestinationTicketCard> tickets){
        Class<?>[] setPlayerDestinationTicketsClasses = new Class<?>[2];
        setPlayerDestinationTicketsClasses[0] = String.class;
        setPlayerDestinationTicketsClasses[1] = DestinationTicketList.class;
        Object[] setPlayerDestinationTicketsObjects = new Object[2];
        setPlayerDestinationTicketsObjects[0] = playerName;
        setPlayerDestinationTicketsObjects[1] = new DestinationTicketList(tickets);
        Command setDestTick = new Command(commandFacadePath, "setPlayerDestinationTickets", setPlayerDestinationTicketsClasses, setPlayerDestinationTicketsObjects);
        return setDestTick;
    }

    public Command buildAddTrainCarCardCommand(String playerName, TrainCarCard card){
        Class<?>[] setAddTrainCardClasses = new Class<?>[2];
        setAddTrainCardClasses[0] = String.class;
        setAddTrainCardClasses[1] = TrainCarCard.class;
        Object[] setAddTrainCardObjects = new Object[2];
        setAddTrainCardObjects[0] = playerName;
        setAddTrainCardObjects[1] = card;
        Command setDestTick = new Command(commandFacadePath, "addTrainCarCard", setAddTrainCardClasses, setAddTrainCardObjects);
        return setDestTick;
    }

    public Command buildUseTrainCarCards(String playerName, List<TrainCarCard> cards){
        Class<?>[] useTrainClasses = new Class<?>[2];
        useTrainClasses[0] = String.class;
        useTrainClasses[1] = TrainCarCardList.class;
        Object[] useTrainObjects = new Object[2];
        useTrainObjects[0] = playerName;
        useTrainObjects[1] = new TrainCarCardList(cards);
        Command setDestTick = new Command(commandFacadePath, "useTrainCarCards", useTrainClasses, useTrainObjects);
        return setDestTick;
    }

    public Command buildSetDisplayedTrainCarCardsCommand(List<TrainCarCard> cards){
        Class<?>[] setDisplayedTrainCardsClasses = new Class<?>[1];
        setDisplayedTrainCardsClasses[0] = TrainCarCardList.class;
        Object[] setDisplayedTrainCardsObjects = new Object[1];
        setDisplayedTrainCardsObjects[0] = new TrainCarCardList(cards);
        Command setDestTick = new Command(commandFacadePath, "setDisplayedTrainCarCards", setDisplayedTrainCardsClasses, setDisplayedTrainCardsObjects);
        return setDestTick;
    }

    public Command buildEndGameCommand(){
        Class<?>[] setEndGameClasses = new Class<?>[0];
        Object[] setEndGameObjects = new Object[0];
        Command setDestTick = new Command(commandFacadePath, "endGame", setEndGameClasses, setEndGameObjects);
        return setDestTick;
    }

    public Command buildEndTurnCommand(Player player){
        Class<?>[] setEndTurnClasses = new Class<?>[1];
        setEndTurnClasses[0] = Player.class;
        Object[] setEndTurnObjects = new Object[1];
        setEndTurnObjects[0] = player;
        Command setDestTick = new Command(commandFacadePath, "endTurn", setEndTurnClasses, setEndTurnObjects);
        return setDestTick;
    }

    public Command buildOverridePlayerDestinationTickets(String playerName, List<DestinationTicketCard> tickets){
        Class<?>[] overrideClasses = new Class<?>[2];
        overrideClasses[0] = String.class;
        overrideClasses[1] = DestinationTicketList.class;
        Object[] overrideObjects = new Object[2];
        overrideObjects[0] = playerName;
        overrideObjects[1] = new DestinationTicketList(tickets);
        Command setDestTick = new Command(commandFacadePath, "overridePlayerDestinationTickets", overrideClasses, overrideObjects);
        return setDestTick;
    }


//    public Command buildUpdateActiveGameCommand(ActiveGame activeGame) {
//        Class<?>[] getActiveGameClasses = new Class<?>[1];
//        getActiveGameClasses[0] = ActiveGame.class;
//        Object[] getActiveGameObjects = new Object[1];
//        getActiveGameObjects[0] = activeGame;
//        Command getActiveGame = new Command(commandFacadePath, "updateActiveGame", getActiveGameClasses, getActiveGameObjects);
//        return getActiveGame;
//    }



//Todo: Might not need this
    public Command startGameCommand(ActiveGame activeGame)
    {
        Class<?>[] startGameClasses = new Class<?>[1];
        startGameClasses[0] = ActiveGame.class;
        Object[] startGameObjects = new Object[1];
        startGameObjects[0] = activeGame;
        Command updateGameCommand = new Command(commandFacadePath, "startGame", startGameClasses, startGameObjects);
        return updateGameCommand;
    }

//Todo: Might not need this
    public Command buildWaitForActiveGame(){
        Class<?>[] setDestTickClasses = new Class<?>[0];
        Object[] setDestTickObjects = new Object[0];
        Command waitForActiveGame = new Command(commandFacadePath, "waitForActiveGame", setDestTickClasses, setDestTickObjects);
        return waitForActiveGame;
    }

//Todo: Might not need this
    public Command setJoinedGame(String gameID){
        Class<?>[] setJoinedGameClasses = new Class<?>[1];
        setJoinedGameClasses[0] = String.class;
        Object[] setJoinedGameObjects = new Object[1];
        setJoinedGameObjects[0] = gameID;
        Command waitForActiveGame = new Command(commandFacadePath, "setJoinedGame", setJoinedGameClasses, setJoinedGameObjects);
        return waitForActiveGame;
    }
}
