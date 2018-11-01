package cs340.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import Command.Command;
import Command.ICommand;
import Utils.PlayerColor;
import Utils.TrainColor;
import cs340.server.model.ServerModel;
import model.ActiveGame;
import model.ChatHistory;
import model.DestinationTicketCard;
import model.DestinationTicketList;
import model.EventMessage;
import model.Game;
import model.Player;
import model.Route;
import model.TrainCarCard;
import model.User;
import sun.rmi.runtime.Log;

/**
 * The ServerFacade class implements the IServerFacade interface. This class handles many of the commands sent from the client.
 * The class is intended as a facade to handle the commands sent from the client and alter the ServerModel as needed.
 */
public class ServerFacade implements IServerFacade {
    /**
     * This is a standard object used to create commands that can be returned to the client.
     */
    private CommandBuilder cmdBuilder;

    /**
     * Standard object used to access the server model.
     */
    private ServerModel serverModel;
    /**
     * This is our static instance variable of our class. Necessary to make this class a Singleton class.
     */
    private static ServerFacade singleton_instance = null;

    /**
     * This is the basic constructor for the class. It simply initializes the cmdBuilder object
     *
     * @post cmdBuilder != null
     */
    public ServerFacade() {
        cmdBuilder = new CommandBuilder();
        serverModel = ServerModel.getInstance();
    }

    /**
     * This method will either initialize the ServerFacade singleton class and return it, or else simply return the already
     * existing instance of this class.
     *
     * @return Returns the instance of the ServerFacade singleton class
     */
    public static ServerFacade getInstance() {
        if (singleton_instance == null)
            singleton_instance = new ServerFacade();
        return singleton_instance;
    }

    /**
     * This method takes in a username and a password and registers a user with those credentials. Returns an updateGameList command
     * and a login command through a list of commands if the username and password match up with a user in the database.
     * Otherwise, it will return a displayMessage error command.
     *
     * @param username
     * @param password
     * @return Returns a list of ICommand objects to send back to the client for execution
     * @pre username != null
     * @pre password != null
     */
    @Override
    public List<ICommand> login(String username, String password) {
        List<ICommand> toClientCommands = new ArrayList<>();
        List<User> users = serverModel.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                toClientCommands.add(cmdBuilder.buildLoginCommand(user.getAuthtoken(), true));
                toClientCommands.add(cmdBuilder.buildUpdateGameListCommand());
                return toClientCommands;
            }
        }
        String errorMessage = "Sorry, that username and password combination does not match a user in our database. Try again, buddy.";
        toClientCommands.add(cmdBuilder.buildDisplayMessageCommand(errorMessage));
        return toClientCommands;
    }

    /**
     * This method takes in a username and a password and registers a user with those credentials. Returns an updateGameList command
     * and a login command through a list of commands if the username and password match up with a user in the database.
     * Otherwise, it will return a displayMessage error command.
     *
     * @param username
     * @param password
     * @return Returns a list of ICommand objects to send back to the client for execution
     * @pre username != null
     * @pre password != null
     * @pre username does not already exist in the model
     * @post new ServerModel.getInstance().getUsers().size() = old ServerModel.getInstance().getUsers().size() + 1
     */
    @Override
    public List<ICommand> register(String username, String password) {
        List<ICommand> toClientCommands = new ArrayList<>();
        List<User> users = serverModel.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                String errorMessage = "Sorry, that username already exists in our database. Try again, buddy.";
                toClientCommands.add(cmdBuilder.buildDisplayMessageCommand(errorMessage));
                return toClientCommands;
            }
        }

        User user = new User(username, password, UUID.randomUUID().toString());
        serverModel.register(user);
        toClientCommands.add(cmdBuilder.buildRegisterCommand(user.getAuthtoken(), false));
        toClientCommands.add(cmdBuilder.buildUpdateGameListCommand());
        return toClientCommands;
    }

    /**
     * This method takes in a gameName and a number representing the number of players allowed and creates a game based on that game info.
     *
     * @param gameName
     * @param numPossiblePlayersDouble
     * @return Returns a list of ICommand objects to send back to the client for execution
     * @pre 2 <= numPossiblePlayersDouble <= 5
     * @pre gameName != null
     * @post a new game is created with the passed in gameName and number of players, and is added to the ServerModel
     * @post old ServerModel.getInstance().getLobbyGames().size() = new ServerModel.getInstance().getLobbyGames().size() - 1
     */
    @Override
    public List<ICommand> createGame(String gameName, Double numPossiblePlayersDouble) {
        Integer numPossiblePlayers = numPossiblePlayersDouble.intValue();
        List<ICommand> toClientCommands = new ArrayList<>();
        if (numPossiblePlayers < 2 || numPossiblePlayers > 5) {
            String errorMessage = "Sorry, you must have 2 to 5 players in a game. Try again, buddy.";
            toClientCommands.add(cmdBuilder.buildDisplayMessageCommand(errorMessage));
            return toClientCommands;
        }
        Game game = new Game(numPossiblePlayers, gameName, UUID.randomUUID().toString());
        serverModel.getInstance().createGame(game);
        toClientCommands.add(cmdBuilder.buildUpdateGameListCommand());
        return toClientCommands;
    }

    /**
     * This command takes in a gameID, and authToken, and a a playerColorDouble. With that information, it locates a game and joins the game
     * for that player with the associated playerColor. Returns an updateGameList command through a list of commands.
     *
     * @param gameID
     * @param authToken
     * @param playerColorDouble
     * @return Returns a list of ICommand objects to send back to the client for execution
     * @pre gameID represents the ID of an actual active game found on the server
     * @pre authToken is a valid user authToken
     * @pre playerColorDouble represents a valid Player Color
     * @post if reached max number of players { old ServerModel.getInstance().getLobbyGames().size() =  new ServerModel.getInstance().getLobbyGames().size() + 1}
     * @post if playerColor has not already been chosen { old game.getPlayers().size() = new game.getPlayers().size() - 1 }
     */
    @Override
    public List<ICommand> joinGame(String gameID, String authToken, Double playerColorDouble) {
        PlayerColor playerColor = PlayerColor.valueOf(playerColorDouble.intValue());
        //System.out.println(String.format("Got join game request for game %s, auth token %s, color %s", gameID, authToken, playerColor.getName()));
        List<ICommand> toClientCommands = new ArrayList<>();
        List<Game> lobbyGames = serverModel.getLobbyGames();
        List<User> users = serverModel.getUsers();
        for (Game lobbyGame : lobbyGames) {
            if (!lobbyGame.getGameID().equals(gameID)) {
                continue;
            }
            if (lobbyGame.getCurrentNumPlayers() >= lobbyGame.getNumPossiblePlayers()) {
                String errorMessage = "Sorry, this game is full. Try again, buddy.";
                toClientCommands.add(cmdBuilder.buildDisplayMessageCommand(errorMessage));
                return toClientCommands;
            }

            if (!lobbyGame.isPlayerColorOpen(playerColor)) {
                String errorMessage = "Sorry, that player color has already been chosen. Try again, buddy.";
                toClientCommands.add(cmdBuilder.buildDisplayMessageCommand(errorMessage));
                return toClientCommands;
            }
            for (User user : users) {
                if (!user.getAuthtoken().equals(authToken)) {
                    continue;
                }
                //System.out.println("Auth Token matched");
                if (lobbyGame.isUserAlreadyInGame(user)) {
                    //System.out.println("User is already in the game");
                    String errorMessage = "Sorry, you have already joined this game.";
                    toClientCommands.add(cmdBuilder.buildDisplayMessageCommand(errorMessage));
                    return toClientCommands;
                }
                //System.out.println("Checking if user is part of any game");
                for (Game game : lobbyGames) {
                    if (game.isUserAlreadyInGame(user)) {
                        //System.out.println("Looks like he is");
                        //System.out.println("Removing him from the game");
                        game.removePlayer(user);
                    }
                }
                //System.out.println("Setting joined game");
                user.setJoinedGameID(lobbyGame.getGameID());
                user.setPlayerColor(playerColor);
                lobbyGame.addPlayer(user);
                lobbyGame.setStarted();
                if (lobbyGame.hasStarted()) {
                    ActiveGame game = new ActiveGame(lobbyGame);
                    ChatHistory chatHistory = new ChatHistory(lobbyGame.getGameID());
                    serverModel.getActiveGames().add(game);
                    serverModel.getChatHistories().add(chatHistory);
                    lobbyGames.remove(lobbyGame);
                    for (Player player : game.getPlayers()) {
                        player.setTurn(true);
                    }
                    toClientCommands.add(cmdBuilder.buildUpdateGameListCommand());
                    Command startGame = cmdBuilder.startGameCommand(game);
                    startGame.setHistoryMessage("The game has started.");
                    game.getGameHistory().add(startGame);
                    toClientCommands.add(startGame);
                    toClientCommands.add(cmdBuilder.setJoinedGame(game.getGameID()));
                    for (Player player : game.getPlayers()) {
                        List<ICommand> destinationCommands = requestDestinationTickets(game.getGameID(), player.getAuthtoken());
                        toClientCommands.add(destinationCommands.get(0));
                    }
                    return toClientCommands;
                }
                //System.out.println("Updating game list");
                toClientCommands.add(cmdBuilder.buildUpdateGameListCommand());
                return toClientCommands;
            }
        }
        String errorMessage = "Sorry, that game no longer exists. Try again, buddy.";
        toClientCommands.add(cmdBuilder.buildDisplayMessageCommand(errorMessage));
        return toClientCommands;
    }

    /**
     * This method takes in an EventMessage and a gameID, then adds the EventMessage the the ChatHistory associated to the passed in
     * gameID. It then creates an updateChat command to send back to the client.
     *
     * @param message
     * @param gameID
     * @return Returns a list of ICommand objects to send back to the client for execution
     * @pre gameID != null
     * @pre gameID matches the gameID of a single game in the ServerModel
     * @pre eventMessage.getEventOwner() != null
     * @pre eventMessage.getEvent() != null
     * @post new game.getChatHistory().size() = old game.getChatHistory().size() + 1
     */
    @Override
    public List<ICommand> updateChat(EventMessage message, String gameID) {
        List<ICommand> toClientCommands = new ArrayList<>();
        for (ChatHistory chatHistory : serverModel.getChatHistories()) {
            if (chatHistory.getGameID().equals(gameID)) {
                chatHistory.getChats().add(message);
                toClientCommands.add(cmdBuilder.buildUpdateChatCommand(chatHistory));
                return toClientCommands;
            }
        }
        return toClientCommands;
    }

    /**
     * This method takes in a gameID and an authToken, and with those, finds an active game with the same gameID, selects
     * three destination tickets for the user associated with the authToken, and then returns them to the client through a
     * setDestinationTicketOptions command via a list of commands
     *
     * @param gameID
     * @param authToken
     * @return Returns a list of ICommand objects to send back to the client for execution
     * @pre gameID != null
     * @pre gameID matches the gameID of a single game in the ServerModel
     * @pre authToken != null
     * @pre authToken matches the authToken of a single player in the associated game in the ServerModel
     * @post new activeGame.getDestinationCards().size() = old activeGame.getDestinationCards().size() - 3
     */
    @Override
    public List<ICommand> requestDestinationTickets(String gameID, String authToken) {
        List<ICommand> toClientCommands = new ArrayList<>();
        for (ActiveGame game : serverModel.getActiveGames()) {
            if (game.getGameID().equals(gameID)) {

                List<DestinationTicketCard> destinationOptions = game.getDestinationTicketCards().getDestinationCards();

                ICommand newCommand = cmdBuilder.buildSetDestinationTicketsOptionsCommand(serverModel.getUser(authToken).getUsername(), destinationOptions);
                newCommand.setHistoryMessage(serverModel.getUser(authToken).getUsername() + " has received three destination tickets.");
                game.getGameHistory().add((Command) newCommand);
                toClientCommands.add(newCommand);
                return toClientCommands;
            }
        }
        return toClientCommands;
    }

    /**
     * This method takes in a list of DestinationTicket cards, a gameID, and an authToken. It then finds a matching active game, the
     * player within it matching the provided authToken, and assigns them the passed in DestinationTicket cards. It then creates a
     * waitForActiveGame command to send back to the client.
     *
     * @param selectedCards
     * @param gameID
     * @param authToken
     * @return Returns a list of ICommand objects to send back to the client for execution
     * @pre gameID != null
     * @pre gameID matches the gameID of a single game in the ServerModel
     * @pre authToken != null
     * @pre authToken matches the authToken of a single player in the associated game in the ServerModel
     * @pre 2 <= selectedCards.size() <= 3
     * @post new activeGame.getPlayer(authToken).getDestinationTickets().size() = old activeGame.getPlayer(authToken).getDestinationTickets().size() + selectedCards.size()
     */
    @Override
    public List<ICommand> selectDestinationTickets(DestinationTicketList selectedCards, String gameID, String authToken) {
        List<ICommand> toClientCommands = new ArrayList<>();
        for (ActiveGame game : serverModel.getActiveGames()) {
            if (game.getGameID().equals(gameID)) {
                for (Player player : game.getPlayers()) {
                    if (player.getAuthtoken().equals(authToken)) {
                        player.addDestinationTickets(selectedCards.getTickets());
                        Command choseTickets = cmdBuilder.buildSetPlayerDestinationTicketsCommand(player.getPlayerName(), selectedCards.getTickets());
                        choseTickets.setHistoryMessage(player.getPlayerName() + " has chosen his/her destination tickets.");
                        toClientCommands.add(choseTickets);
                        game.getGameHistory().add(choseTickets);


                        if (!game.hasStarted()) {
                            game.getPlayerOrder().add(player);
                            Command endTurnCommand = cmdBuilder.buildEndTurnCommand(player);
                            if (!game.allPlayersChoseTickets()) {
                                endTurnCommand.setHistoryMessage(player.getPlayerName() + " is waiting for his buddies to pick his/her tickets.");
                            }
                            game.getGameHistory().add(endTurnCommand);
                            toClientCommands.add(endTurnCommand);
                            player.setTurn(false);

                            if (game.allPlayersChoseTickets()) {
                                game.setCurrentPlayerIndex(0);
                                game.getPlayerOrder().get(0).setTurn(true);
                                Command advanceTurn = cmdBuilder.buildAdvanceTurnCommand(game.getCurrentPlayer().getPlayerName());
                                advanceTurn.setHistoryMessage("It is now " + game.getCurrentPlayer().getPlayerName() + "'s turn.");
                                game.getGameHistory().add(advanceTurn);
                                toClientCommands.add(advanceTurn);
                                game.setHasStarted(true);
                            }
                        } else {
                            if(game.updateTicketStatus(player)){
                                Command overrideTickets = cmdBuilder.buildOverridePlayerDestinationTickets(player.getPlayerName(), player.getDestinationTickets());
                                toClientCommands.add(overrideTickets);
                                game.getGameHistory().add(overrideTickets);
                            }

                            if(game.isOnLastRound()){
                                player.setHasCompletedLastTurn(true);
                            }

                            if(game.lastTurnsAreOver()){
                                Command endGameCommand = cmdBuilder.buildEndGameCommand();
                                endGameCommand.setHistoryMessage("Game is over!");
                                toClientCommands.add(endGameCommand);
                                game.getGameHistory().add(endGameCommand);
                            }
                            else{
                                game.nextPlayer();
                                Command advanceTurn = cmdBuilder.buildAdvanceTurnCommand(game.getCurrentPlayer().getPlayerName());
                                advanceTurn.setHistoryMessage("It is now " + game.getCurrentPlayer().getPlayerName() + "'s turn.");
                                toClientCommands.add(advanceTurn);
                                game.getGameHistory().add(advanceTurn);
                            }
                        }

                        return toClientCommands;
                    }
                }
            }
        }

        return toClientCommands;
    }

    public List<ICommand> claimRoute(Route clientRoute, TrainColor color, String authToken) {
        List<ICommand> toClientCommands = new ArrayList<>();
        User user = serverModel.getUser(authToken);
        String gameID = user.getJoinedGameID();
        ActiveGame game = serverModel.getActiveGame(gameID);
        Player currentPlayer = game.getCurrentPlayer();
        Route route = game.getRoute(clientRoute.getRouteID());
        if (route.isClaimed()) {
            toClientCommands.add(cmdBuilder.buildDisplayMessageCommand("Sorry, that route has already been claimed. Try again, buddy."));
            return toClientCommands;
        }
        if (!currentPlayer.getAuthtoken().equals(authToken)) {
            toClientCommands.add(cmdBuilder.buildDisplayMessageCommand("Sorry, it's not currently your turn. Try again, buddy."));
            return toClientCommands;
        }

        if(game.getPlayers().size() < 4 && game.isDoubleRoute(route) && game.getDoubleRoute(route).isClaimed()) {
            toClientCommands.add(cmdBuilder.buildDisplayMessageCommand("Sorry Buddy, that's a double route"));
            return toClientCommands;
        }

        if(game.isDoubleRoute(route) && game.getDoubleRoute(route).isClaimed()) {
            toClientCommands.add(cmdBuilder.buildDisplayMessageCommand("LOL Buddy, you already own that route's twin!"));
            return toClientCommands;
        }

        Route routeToClaim = null;
        for (Route unclaimedRoute : game.getUnclaimedRoutes()) {
            if (route.getRouteID().equals(unclaimedRoute.getRouteID())) {
                routeToClaim = unclaimedRoute;
                break;
            }
        }

        if (routeToClaim.getLength() > currentPlayer.getRemainingTrainCars()) {
            toClientCommands.add(cmdBuilder.buildDisplayMessageCommand("Sorry, you don't have enough train cars remaining. Try again, buddy."));
            return toClientCommands;
        }

        TrainColor cardColor;
        if (routeToClaim.getColor().getName().equals(TrainColor.ANY.getName())) {
            cardColor = color;
        } else {
            cardColor = routeToClaim.getColor();
        }

        int numSpecificCards = currentPlayer.getNumColoredTrainCardCards(cardColor);
        TrainColor wildColor = TrainColor.nameOf(TrainColor.WILD.getName());
        int numWilds = currentPlayer.getNumColoredTrainCardCards(wildColor);

        if (numSpecificCards + numWilds < routeToClaim.getLength()) {
            toClientCommands.add(cmdBuilder.buildDisplayMessageCommand("Sorry, you don't have enough of the right cards for this route. Try again, buddy."));
            return toClientCommands;
        }
        //passed all tests!!!! ready to claim a route!!!!!
        //the following code is what removes the used up train cards
        List<TrainCarCard> playerColorCards = currentPlayer.getColoredTrainCardCards(cardColor);
        List<TrainCarCard> playerWildCards = currentPlayer.getColoredTrainCardCards(wildColor);
        List<TrainCarCard> usedCards = new ArrayList<>();
        if (numSpecificCards > routeToClaim.getLength()) {
            for (int i = 0; i < routeToClaim.getLength(); i++) {
                usedCards.add(playerColorCards.remove(0));
            }
        } else if (numSpecificCards == routeToClaim.getLength()) {
            int numColorCards = playerColorCards.size();
            for(int i = 0; i < numColorCards; i++){
                usedCards.add(playerColorCards.remove(0));
            }
        } else {
            int numColorCards = playerColorCards.size();
            for(int i = 0; i < numColorCards; i++){
                usedCards.add(playerColorCards.remove(0));
            }
            int wildsNecessary = routeToClaim.getLength() - numSpecificCards;
            for (int i = 0; i < wildsNecessary; i++) {
                usedCards.add(playerWildCards.remove(0));
            }
        }

        game.getTrainCardsManager().discard(usedCards);
        Command usedCardsCommand = cmdBuilder.buildUseTrainCarCards(currentPlayer.getPlayerName(), usedCards);
        toClientCommands.add(usedCardsCommand);
        game.getGameHistory().add(usedCardsCommand);

        routeToClaim.claim(currentPlayer);
        if(game.isOnLastRound()){
            currentPlayer.setHasCompletedLastTurn(true);
        }
        if (currentPlayer.getRemainingTrainCars() <= 2) {
            game.setOnLastRound(true);
        }

//        if(game.isOnLastRound()){
//            currentPlayer.setHasCompletedLastTurn(true);
//        }

        Command claimRoute = cmdBuilder.buildClaimRouteCommand(route, currentPlayer);

        claimRoute.setHistoryMessage(currentPlayer.getPlayerName() + " has claimed a route.");
        toClientCommands.add(claimRoute);
        game.getGameHistory().add(claimRoute);
        //////////////
        ///////////

        ////////////
        ///////////////
        currentPlayer.getClaimedRoutes().add(routeToClaim.getRouteID());
        ///////
        //////
        //////
        ///////
        //////

        if(game.updateTicketStatus(currentPlayer)){
            Command overrideTickets = cmdBuilder.buildOverridePlayerDestinationTickets(currentPlayer.getPlayerName(), currentPlayer.getDestinationTickets());
            toClientCommands.add(overrideTickets);
            game.getGameHistory().add(overrideTickets);
        }


        if(game.lastTurnsAreOver()){
            Command endGameCommand = cmdBuilder.buildEndGameCommand();
            endGameCommand.setHistoryMessage("Game is over!");
            toClientCommands.add(endGameCommand);
            game.getGameHistory().add(endGameCommand);
        }
        else{
            game.nextPlayer();
            Command advanceTurn = cmdBuilder.buildAdvanceTurnCommand(game.getCurrentPlayer().getPlayerName());
            advanceTurn.setHistoryMessage("It is now " + game.getCurrentPlayer().getPlayerName() + "'s turn.");
            toClientCommands.add(advanceTurn);
            game.getGameHistory().add(advanceTurn);
        }

        return toClientCommands;
    }

    public List<ICommand> takeCard(Double index, String authToken) {
        List<ICommand> toClientCommands = new ArrayList<>();
        User user = serverModel.getUser(authToken);
        ActiveGame game = serverModel.getActiveGame(user.getJoinedGameID());
        Player currentPlayer = game.getCurrentPlayer();
        if (!currentPlayer.getAuthtoken().equals(authToken)) {
            toClientCommands.add(cmdBuilder.buildDisplayMessageCommand("Sorry, it's not currently your turn. Try again, buddy."));
            return toClientCommands;
        }
        if (game.getTrainCardsManager().hasDrawnCard()) {
            TrainCarCard card = game.getTrainCardsManager().getCard(index.intValue());
            if (card.getColor().getName().equals(TrainColor.WILD.getName())) {
                toClientCommands.add(cmdBuilder.buildDisplayMessageCommand("Sorry, you cannot choose a wild card at this time. Try again, buddy."));
                return toClientCommands;
            }
            card = game.getTrainCardsManager().takeCard(index.intValue());
            List<TrainCarCard> cards = new ArrayList<>();
            cards.add(card);
            currentPlayer.addTrainCarCards(cards);
            game.getTrainCardsManager().setHasDrawnCard(false);

            Command addCard = cmdBuilder.buildAddTrainCarCardCommand(currentPlayer.getPlayerName(), card);
            addCard.setHistoryMessage(currentPlayer.getPlayerName() + " has chosen a faceup card.");
            toClientCommands.add(addCard);
            game.getGameHistory().add(addCard);

            Command newFaceups = cmdBuilder.buildSetDisplayedTrainCarCardsCommand(game.getTrainCardsManager().getFaceupCards());
//            newFaceups.setHistoryMessage("The faceup card was replaced with a " + game.getTrainCardsManager().getFaceupCards().get(index.intValue()).getColor().getName() + "card.");
            toClientCommands.add(newFaceups);
            game.getGameHistory().add(newFaceups);

            if(game.isOnLastRound()){
                currentPlayer.setHasCompletedLastTurn(true);
            }

            if(game.lastTurnsAreOver()){
                Command endGameCommand = cmdBuilder.buildEndGameCommand();
                endGameCommand.setHistoryMessage("Game is over!");
                toClientCommands.add(endGameCommand);
                game.getGameHistory().add(endGameCommand);
            }
            else{
                game.nextPlayer();
                Command advanceTurn = cmdBuilder.buildAdvanceTurnCommand(game.getCurrentPlayer().getPlayerName());
                advanceTurn.setHistoryMessage("It is now " + game.getCurrentPlayer().getPlayerName() + "'s turn.");
                toClientCommands.add(advanceTurn);
                game.getGameHistory().add(advanceTurn);
            }
        } else {
            TrainCarCard card = game.getTrainCardsManager().takeCard(index.intValue());
            List<TrainCarCard> cards = new ArrayList<>();
            cards.add(card);
            currentPlayer.addTrainCarCards(cards);
            if (card.getColor().getName().equals(TrainColor.WILD.getName())) {
                Command addCard = cmdBuilder.buildAddTrainCarCardCommand(currentPlayer.getPlayerName(), card);
                addCard.setHistoryMessage(currentPlayer.getPlayerName() + " has chosen a faceup card.");
                toClientCommands.add(addCard);
                game.getGameHistory().add(addCard);

                Command newFaceups = cmdBuilder.buildSetDisplayedTrainCarCardsCommand(game.getTrainCardsManager().getFaceupCards());
//                newFaceups.setHistoryMessage("The faceup card was replaced with a " + game.getTrainCardsManager().getFaceupCards().get(index.intValue()).getColor().getName() + "card.");
                toClientCommands.add(newFaceups);
                game.getGameHistory().add(newFaceups);

                if(game.isOnLastRound()){
                    currentPlayer.setHasCompletedLastTurn(true);
                }

                if(game.lastTurnsAreOver()){
                    Command endGameCommand = cmdBuilder.buildEndGameCommand();
                    endGameCommand.setHistoryMessage("Game is over!");
                    toClientCommands.add(endGameCommand);
                    game.getGameHistory().add(endGameCommand);
                }
                else{
                    game.nextPlayer();
                    Command advanceTurn = cmdBuilder.buildAdvanceTurnCommand(game.getCurrentPlayer().getPlayerName());
                    advanceTurn.setHistoryMessage("It is now " + game.getCurrentPlayer().getPlayerName() + "'s turn.");
                    toClientCommands.add(advanceTurn);
                    game.getGameHistory().add(advanceTurn);
                }
            } else {
                game.getTrainCardsManager().setHasDrawnCard(true);
                Command addCard = cmdBuilder.buildAddTrainCarCardCommand(currentPlayer.getPlayerName(), card);
                addCard.setHistoryMessage(currentPlayer.getPlayerName() + " has chosen a faceup card.");
                toClientCommands.add(addCard);
                game.getGameHistory().add(addCard);

                Command newFaceups = cmdBuilder.buildSetDisplayedTrainCarCardsCommand(game.getTrainCardsManager().getFaceupCards());
//                newFaceups.setHistoryMessage("The faceup card was replaced with a " + game.getTrainCardsManager().getFaceupCards().get(index.intValue()).getColor().getName() + "card.");
                toClientCommands.add(newFaceups);
                game.getGameHistory().add(newFaceups);
            }
        }
        return toClientCommands;
    }

    public List<ICommand> drawCard(String authToken) {
        List<ICommand> toClientCommands = new ArrayList<>();
        User user = serverModel.getUser(authToken);
        ActiveGame game = serverModel.getActiveGame(user.getJoinedGameID());
        Player currentPlayer = game.getCurrentPlayer();
        if (!currentPlayer.getAuthtoken().equals(authToken)) {
            toClientCommands.add(cmdBuilder.buildDisplayMessageCommand("Sorry, it's not currently your turn. Try again, buddy."));
            return toClientCommands;
        }

        TrainCarCard card = game.getTrainCardsManager().draw();
        if (card == null) {
            toClientCommands.add(cmdBuilder.buildDisplayMessageCommand("Sorry, there are no more cards in the deck. Try again, buddy."));
            return toClientCommands;
        }
        List<TrainCarCard> cards = new ArrayList<>();
        cards.add(card);
        currentPlayer.addTrainCarCards(cards);

        if (game.getTrainCardsManager().hasDrawnCard()) {

            game.getTrainCardsManager().setHasDrawnCard(false);

            Command addCard = cmdBuilder.buildAddTrainCarCardCommand(currentPlayer.getPlayerName(), card);
            addCard.setHistoryMessage(currentPlayer.getPlayerName() + " has chosen a card from the deck.");
            toClientCommands.add(addCard);
            game.getGameHistory().add(addCard);

            if(game.isOnLastRound()){
                currentPlayer.setHasCompletedLastTurn(true);
            }

            if(game.lastTurnsAreOver()){
                Command endGameCommand = cmdBuilder.buildEndGameCommand();
                endGameCommand.setHistoryMessage("Game is over!");
                toClientCommands.add(endGameCommand);
                game.getGameHistory().add(endGameCommand);
            }
            else{
                game.nextPlayer();
                Command advanceTurn = cmdBuilder.buildAdvanceTurnCommand(game.getCurrentPlayer().getPlayerName());
                advanceTurn.setHistoryMessage("It is now " + game.getCurrentPlayer().getPlayerName() + "'s turn.");
                toClientCommands.add(advanceTurn);
                game.getGameHistory().add(advanceTurn);
            }
        } else {
            game.getTrainCardsManager().setHasDrawnCard(true);

            Command addCard = cmdBuilder.buildAddTrainCarCardCommand(currentPlayer.getPlayerName(), card);
            addCard.setHistoryMessage(currentPlayer.getPlayerName() + " has chosen a card from the deck.");
            toClientCommands.add(addCard);
            game.getGameHistory().add(addCard);
        }
        return toClientCommands;
    }

    public List<ICommand> updateCommandList(Double commandListIndex, String authtoken, String gameID) {
        List<ICommand> toClientCommands = new ArrayList<>();
        ActiveGame game = serverModel.getActiveGame(gameID);
        List<Command> gameHistory = game.getGameHistory();

//        System.out.println(serverModel.getUser(authtoken).getUsername() + "'s details -> commandListIndex: " + commandListIndex.intValue() + " gameHistory Size: " + gameHistory.size());

        for (int i = commandListIndex.intValue(); i < gameHistory.size(); i++) {
            toClientCommands.add(gameHistory.get(i));
        }

//        //System.out.println("Current Game History");
//        for (ICommand command : gameHistory) {
//            System.out.println(" \"" + command.getMethodName() + "\" ");
//        }
        //System.out.println("Client Game History");
        //System.out.println(toClientCommands);
        toClientCommands.add(cmdBuilder.buildUpdateChatCommand(serverModel.getChatHistory(gameID)));
        return toClientCommands;
    }

    /**
     * This method returns a list of command objects with only one actual command object. If the player is part of an active game,
     * it will return a startGame command. If not, it will return an updateGameList command.
     *
     * @param authToken
     * @return Returns a list of ICommand objects to send back to the client for execution
     * @pre authToken is a valid user authToken
     */
    @Override
    public List<ICommand> getGames(String authToken) {
        List<ICommand> toClientCommands = new ArrayList<>();
        ServerModel serverModel = ServerModel.getInstance();
        for (ActiveGame game : serverModel.getActiveGames()) {
            for (Player player : game.getPlayers()) {
                if (player.getAuthtoken().equals(authToken)) {
                    toClientCommands.add(cmdBuilder.startGameCommand(game));
                    return toClientCommands;
                }
            }
        }
        toClientCommands.add(cmdBuilder.buildUpdateGameListCommand());
        return toClientCommands;
    }

//    //Todo: may not need this method
//
//    /**
//     * This method returns a list of command objects with either one or two command objects. If all players have selected destination cards, it will
//     * return an updateActiveGame command as well as an updateChat command. Otherwise, it will only return an updateChat command.
//     *
//     * @param gameID
//     * @return Returns a list of ICommand objects to send back to the client for execution
//     * @pre gameID represents the ID of an actual active game found on the server
//     */
//    public List<ICommand> getActiveGame(String gameID) {
//        if (gameID == null) {
//            List<ICommand> toClientCommands = new ArrayList<>();
//            return toClientCommands;
//        } else {
//            for (ActiveGame a : ServerModel.getInstance().getActiveGames()) {
//                if (a.getGameID().equals(gameID)) {
//                    //need to modify this to create logic for the destination tickets
//                    List<ICommand> toClientCommands = new ArrayList<>();
//                    if (a.allPlayersChoseTickets()) {
//                        //if all players have chosen initial destination tickets, then return the whole game
//                        toClientCommands.add(cmdBuilder.buildUpdateActiveGameCommand(a));
//                    }
//                    for (ChatHistory chatHistory : ServerModel.getInstance().getChatHistories()) {
//                        if (chatHistory.getGameID().equals(gameID)) {
//                            toClientCommands.add(cmdBuilder.buildUpdateChatCommand(chatHistory));
//                            return toClientCommands;
//                        }
//                    }
//                }
//            }
//            List<ICommand> toClientCommands = new ArrayList<>();
//            toClientCommands.add(cmdBuilder.buildDisplayMessageCommand("There isn't an active game with that ID, buddy!"));
//            return toClientCommands;
//        }
//    }
//
//    //Todo: may not need this method
//
//    /**
//     * This method passes in an activeGame object. This activeGame overwrites whatever activeGame in the ServerModel that has
//     * a matching gameID. It then returns an updateActiveGame command and an updateChat command. It also advances the playerOrder index by 1.
//     *
//     * @param endTurnGameState
//     * @return Returns a list of ICommand objects to send back to the client for execution
//     * @pre activeGame != null
//     * @pre activeGame.getGameID() matches the gameID of some active game in the server
//     * @post whatever active game that had the same gameID as the passed in game is overwritten by the passed in game
//     * @post the games advances turns to whoever is the next player in the game's playerOrder list
//     */
//    public List<ICommand> endTurn(ActiveGame endTurnGameState) {
//        List<ICommand> toClientCommands = new ArrayList<>();
//        ServerModel serverModel = ServerModel.getInstance();
//        for (ActiveGame game : serverModel.getActiveGames()) {
//            if (game.getGameID().equals(endTurnGameState.getGameID())) {
//                game = endTurnGameState;
//                game.nextPlayer();
//                toClientCommands.add(cmdBuilder.buildUpdateActiveGameCommand(game));
//                for (ChatHistory chatHistory : serverModel.getChatHistories()) {
//                    if (chatHistory.getGameID().equals(game.getGameID())) {
//                        toClientCommands.add(cmdBuilder.buildUpdateChatCommand(chatHistory));
//                        return toClientCommands;
//                    }
//                }
//            }
//        }
//        return toClientCommands;
//    }

}
