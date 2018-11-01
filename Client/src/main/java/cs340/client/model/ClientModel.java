package cs340.client.model;


import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Command.ICommand;
import cs340.client.communication.Poller;
import model.ActiveGame;
import model.DestinationTicketCard;
import model.DestinationTicketList;
import model.EventMessage;
import model.Game;
import model.GameList;
import model.Player;
import model.Route;
import model.TrainCarCard;
import model.TrainCarCardList;
import model.User;

public class ClientModel extends Observable {
    private static final String TAG = "ClientModel";
    private List<Game> currentGames;
    private ActiveGame activeGame;
    private User user;
    private String tempPassword;
    private String tempUsername;
    private static ClientModel singleton_instance;
    private String toastMessage;
    private List<EventMessage> chatHistory;
    private List<ICommand> clientGameHistory;
    private List<DestinationTicketCard> destinationTicketChoices;
    private List<Observer> observers;
    private String serverModelClassPath = "cs340.server.ServerFacade";
    private Poller poller;
    private int commandIndex;
    private boolean gameOver;

    public String getAuthtoken() {
        return user.getAuthtoken();
    }

    private ClientModel() {
        this.user = new User(null, null, null);
        this.activeGame = null;
        this.chatHistory = new ArrayList<>();
        commandIndex = 0;
        clientGameHistory = new ArrayList<ICommand>();
        gameOver = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        if (this.gameOver)
        {
            for (Player p : activeGame.getPlayers())
            {
                p.setTurn(false);
            }
            setChanged();
            notifyObservers();
        }
    }

    public void useTrainCarCards(String playerName, TrainCarCardList trainCards)
    {
        for (Player p : activeGame.getPlayers())
        {
            if(p.getPlayerName().equals(playerName))
            {
                p.useTrainCarCards(trainCards.getCards());
            }
        }
    }

    public void overridePlayerDestinationTickets(String playerName, DestinationTicketList tickets)
    {
//        if (playerName.equals(ClientModel.getInstance().getUser().getUsername())) {
            for (Player p : ClientModel.getInstance().getActiveGame().getPlayers())
            {
                if(p.getPlayerName().equals(playerName))
                {
                    p.setDestinationTickets(tickets.getTickets());
                    setChanged();
                    notifyObservers();
                }
            }
//        }
    }

    public void setChatHistory(List<EventMessage> chatHistory) {
        this.chatHistory = chatHistory;
        setChanged();
        notifyObservers();
    }

    public List<EventMessage> getChatHistory() {
        return chatHistory;
    }

    public List<EventMessage> getGameHistory() {
        List<EventMessage> history = new LinkedList<>();
        for (String s : getGameHistoryStrings()) {
            history.add(new EventMessage("", s));
        }
        return history;
    }

    private List<String> getGameHistoryStrings()
    {
        List<String> gameHistoryStrings = new ArrayList<>();
        for(ICommand c : clientGameHistory)
        {
            if (c.getHistoryMessage() != null)
            {
                gameHistoryStrings.add(c.getHistoryMessage());
            }
        }
        return gameHistoryStrings;
    }

    public void setGameHistory(List<ICommand> clientGameHistory) {
        this.clientGameHistory = clientGameHistory;
    }

    public void addGameHistory(List<ICommand> gameHistory) {
        if(gameHistory != null) {
            for (ICommand command : gameHistory) {
                clientGameHistory.add(command);
            }
        }
    }

    public void setCurrentGames(List<Game> currentGames) {
        this.currentGames = currentGames;
        assignUserGameIfJoined();
        setChanged();
        notifyObservers();
    }

    public void setDestinationTicketChoices(List<DestinationTicketCard> destinationTicketChoices)
    {
        this.destinationTicketChoices = destinationTicketChoices;
        if (this.destinationTicketChoices != null)
        {
            setChanged();
            notifyObservers();
        }
    }

    public List<DestinationTicketCard> getDestinationTicketChoices() {
        return destinationTicketChoices;
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public String getTempUsername() {
        return tempUsername;
    }

    public void setTempUsername(String tempUsername) {
        this.tempUsername = tempUsername;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static ClientModel getInstance() {
        if (singleton_instance == null)
            singleton_instance = new ClientModel();
        return singleton_instance;
    }

    public List<User> getCurrentPlayers() {
        return currentGames.get(currentGames.indexOf(user.getJoinedGameID())).getCurrentPlayers();
    }

    public List<Game> getCurrentGames() {
        return currentGames;
    }

    public String getJoinedGameID()
    {
        return user.getJoinedGameID();
    }

    public Game getJoinedGame() {
        for (Game game : currentGames)
            if (game.getGameID().equals(user.getJoinedGameID()))
                return game;
        return null;
    }

    public boolean isLoggedIn() {
        if (user != null && user.getAuthtoken() != null)
            return true;
        return false;
    }

    public ActiveGame getActiveGame()
    {
        return activeGame;
    }

    public void setActiveGame(ActiveGame activeGame) {
        this.activeGame = activeGame;
        setChanged();
        notifyObservers();
    }

    public String getToastMessage() {
        return toastMessage;
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers();
        this.toastMessage = null;
    }

    public void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        setChanged();
        notifyObservers();
    }

    private void assignUserGameIfJoined() {
        for (Game game : currentGames)
        {
            for (User u : game.getCurrentPlayers())
            {
                if (u.getAuthtoken().equals(user.getAuthtoken()))
                {
                    user.setJoinedGameID(game.getGameID());
                    setChanged();
                    notifyObservers();
                    return;
                }
            }
        }
    }

    private boolean userGameIsFull() {
        for (Game game : currentGames)
        {
            if (game.getGameID().equals(user.getJoinedGameID()))
                return false;
        }
        return true;
    }

    public boolean userHasActiveGame() {
        if(user.getJoinedGameID() == null)
            return false;
        else if(getActiveGame() != null)
            return true;
        else if (userGameIsFull())
            return true;
        else
            return false;
    }

    public void updateGameList(GameList games) {
        for (Game game : games.getGames()) {
            Log.d(TAG, String.format("Game %s has %d players", game.getGameName(), game.getCurrentNumPlayers()));
        }
        currentGames = games.getGames();
        assignUserGameIfJoined();
        if(user.getJoinedGameID() != null)
        {
//            currentGames.get(currentGames.indexOf(user.getJoinedGameID()))
            if (currentGames.contains(user.getJoinedGameID()))
            {
                System.out.println("current num players of user joined game:" + currentGames.get(currentGames.indexOf(user.getJoinedGameID())).getCurrentNumPlayers());
//                user.setJoinedGame(currentGames.get(currentGames.indexOf(user.getJoinedGame())));
//                System.out.println("current num players post:" + user.getJoinedGame().getCurrentNumPlayers());
            }
        }
        setChanged();
        notifyObservers();
    }

    public void login(String authtoken) {
        user.setUsername(tempUsername);
        user.setPassword(tempPassword);
        user.setAuthtoken(authtoken);
        setChanged();
        notifyObservers();
    }

    public void claimRoute(Route route, Player owner) {
        activeGame.claimRoute(route, owner);
        setChanged();
        notifyObservers();
    }

    public void advanceTurn(String nextPlayerName) {
        activeGame.advanceTurn(nextPlayerName);
        setChanged();
        notifyObservers();
    }

    public void addTrainCarCard(String playerName, TrainCarCard card) {
        activeGame.addTrainCarCard(playerName, card);
        setChanged();
        notifyObservers();
    }

    public void setDisplayedTrainCards(List<TrainCarCard> cards) {
        activeGame.setDisplayedTrainCards(cards);
        setChanged();
        notifyObservers();
    }

    public void setPlayers(List<Player> players) {
        activeGame.setPlayers(players);
        setChanged();
        notifyObservers();
    }

    public int getCommandIndex() {
        return clientGameHistory.size();
    }

    public void setCommandIndex(int index) {
        commandIndex = index;
    }

    public void endTurn(Player player)
    {
        for (Player p : ClientModel.getInstance().getActiveGame().getPlayers())
        {
            if (p.getPlayerName().equals(player.getPlayerName()))
            {
                p.setTurn(false);
                setChanged();
                notifyObservers();
            }
        }
    }
}
