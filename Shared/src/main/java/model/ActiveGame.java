package model;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Command.Command;
import Utils.TrainColor;

public class ActiveGame {
    private String gameID;
    private String gameName;
    private List<Location> cities;
    private List<Route> routes;
    private List<Player> players;
    private List<Player> playerOrder;
    /**
     * The index of the current player in the playerOrder list
     */
    private int currentPlayer;
    private TrainCarCardManager trainCardsManager;
    private DestinationTicketDeck destinationTicketCards;
    private List<Command> gameHistory;
    //private String [] cityNames = {"Montreal", "Boston", "New York", "Washington", "Raleigh", "Charleston", "Miami", "Toronto", "Pittsburgh", "Atlanta", "Nashville", "Sault St. Marie", "Chicago", "Saint Louis", "Little Rock", "New Orleans", "Houston", "Dallas", "Oklahoma City", "Kansas City", "Omaha", "Duluth", "Winnipeg", "Calgary", "Helena", "Denver", "Santa Fe", "El Paso", "Phoenix", "Las Vegas", "Salt Lake City", "Los Angeles", "San Francisco", "Portland", "Seattle", "Vancouver"};
    private String[][] routeCities = {{"Montreal", "Boston", "2", "ANY"}, {"Montreal", "Boston", "2", "ANY"}, {"Montreal", "Sault St. Marie", "5", "BLACK"}, {"Montreal", "Toronto", "3", "ANY"}, {"Montreal", "New York", "3", "BLUE"}, {"Boston", "New York", "2", "YELLOW"}, {"Boston", "New York", "2", "RED"}, {"New York", "Washington", "2", "ORANGE"}, {"New York", "Washington", "2", "BLACK"}, {"New York", "Pittsburgh", "2", "WHITE"}, {"New York", "Pittsburgh", "2", "GREEN"}, {"Washington", "Pittsburgh", "2", "ANY"}, {"Washington", "Raleigh", "2", "ANY"}, {"Washington", "Raleigh", "2", "ANY"}, {"Raleigh", "Charleston", "2", "ANY"}, {"Raleigh", "Pittsburgh", "2", "ANY"}, {"Raleigh", "Nashville", "3", "BLACK"}, {"Raleigh", "Atlanta", "2", "ANY"}, {"Raleigh", "Atlanta", "2", "ANY"}, {"Charleston", "Miami", "4", "PURPLE"}, {"Charleston", "Atlanta", "2", "ANY"}, {"Miami", "Atlanta", "5", "BLUE"}, {"Miami", "New Orleans", "6", "RED"}, {"New Orleans", "Atlanta", "4", "YELLOW"}, {"New Orleans", "Atlanta", "4", "ORANGE"}, {"New Orleans", "Houston", "2", "ANY"}, {"New Orleans", "Little Rock", "3", "GREEN"}, {"Atlanta", "Nashville", "1", "ANY"}, {"Nashville", "Little Rock", "3", "WHITE"}, {"Nashville", "Saint Louis", "2", "ANY"}, {"Nashville", "Pittsburgh", "4", "YELLOW"}, {"Pittsburgh", "Saint Louis", "5", "GREEN"}, {"Pittsburgh", "Chicago", "3", "ORANGE"}, {"Pittsburgh", "Chicago", "3", "BLACK"}, {"Pittsburgh", "Toronto", "2", "ANY"}, {"Toronto", "Sault St. Marie", "2", "ANY"}, {"Toronto", "Duluth", "6", "PURPLE"}, {"Toronto", "Chicago", "4", "WHITE"}, {"Sault St. Marie", "Winnipeg", "6", "ANY"}, {"Sault St. Marie", "Duluth", "3", "ANY"}, {"Chicago", "Duluth", "3", "RED"}, {"Chicago", "Omaha", "4", "BLUE"}, {"Chicago", "Saint Louis", "2", "GREEN"}, {"Chicago", "Saint Louis", "2", "WHITE"}, {"Winnipeg", "Duluth", "4", "BLACK"}, {"Winnipeg", "Calgary", "6", "WHITE"}, {"Winnipeg", "Helena", "4", "BLUE"}, {"Duluth", "Helena", "6", "ORANGE"}, {"Duluth", "Omaha", "2", "ANY"}, {"Duluth", "Omaha", "2", "ANY"}, {"Saint Louis", "Kansas City", "2", "PURPLE"}, {"Saint Louis", "Kansas City", "2", "BLUE"}, {"Saint Louis", "Little Rock", "2", "ANY"}, {"Little Rock", "Dallas", "2", "ANY"}, {"Little Rock", "Oklahoma City", "2", "ANY"}, {"Omaha", "Kansas City", "1", "ANY"}, {"Omaha", "Kansas City", "1", "ANY"}, {"Omaha", "Denver", "4", "PURPLE"}, {"Omaha", "Helena", "5", "RED"}, {"Kansas City", "Denver", "4", "BLACK"}, {"Kansas City", "Denver", "4", "ORANGE"}, {"Kansas City", "Oklahoma City", "2", "ANY"}, {"Kansas City", "Oklahoma City", "2", "ANY"}, {"Oklahoma City", "Denver", "4", "RED"}, {"Oklahoma City", "Santa Fe", "3", "BLUE"}, {"Oklahoma City", "El Paso", "5", "YELLOW"}, {"Oklahoma City", "Dallas", "2", "ANY"}, {"Oklahoma City", "Dallas", "2", "ANY"}, {"Dallas", "Houston", "1", "ANY"}, {"Dallas", "Houston", "1", "ANY"}, {"El Paso", "Houston", "6", "GREEN"}, {"Dallas", "El Paso", "6", "GREEN"}, {"El Paso", "Los Angeles", "6", "BLACK"}, {"El Paso", "Phoenix", "3", "ANY"}, {"El Paso", "Santa Fe", "2", "ANY"}, {"Santa Fe", "Phoenix", "3", "ANY"}, {"Santa Fe", "Denver", "2", "ANY"}, {"Denver", "Phoenix", "5", "WHITE"}, {"Los Angeles", "Phoenix", "3", "ANY"}, {"Denver", "Helena", "4", "GREEN"}, {"Denver", "Salt Lake City", "3", "RED"}, {"Denver", "Salt Lake City", "3", "YELLOW"}, {"Helena", "Salt Lake City", "3", "PURPLE"}, {"Helena", "Seattle", "6", "YELLOW"}, {"Helena", "Calgary", "4", "ANY"}, {"Calgary", "Vancouver", "3", "ANY"}, {"Calgary", "Seattle", "4", "ANY"}, {"Vancouver", "Seattle", "1", "ANY"}, {"Vancouver", "Seattle", "1", "ANY"}, {"Seattle", "Portland", "1", "ANY"}, {"Seattle", "Portland", "1", "ANY"}, {"Portland", "Salt Lake City", "5", "BLUE"}, {"Portland", "San Francisco", "5", "GREEN"}, {"Portland", "San Francisco", "5", "PURPLE"}, {"San Francisco", "Salt Lake City", "5", "ORANGE"}, {"San Francisco", "Salt Lake City", "5", "WHITE"}, {"San Francisco", "Los Angeles", "3", "YELLOW"}, {"San Francisco", "Los Angeles", "3", "PURPLE"}, {"Los Angeles", "Las Vegas", "2", "ANY"}, {"Las Vegas", "Salt Lake City", "3", "ORANGE"}};
    private boolean hasStarted;
    private boolean onLastRound;


    class Data1 {
        public Location[] data;
    }

    public ActiveGame(Game game) {
        this.gameID = game.getGameID();
        this.gameName = game.getGameName();
        this.players = new ArrayList<>();
        this.playerOrder = new ArrayList<>();
        for (User u : game.getCurrentPlayers())
            players.add(new Player(u));
        trainCardsManager = new TrainCarCardManager();
        initializePlayerTrainCards();
        gameHistory = new ArrayList<>();
        initializeCities();
        initializeRoutes();
        destinationTicketCards = new DestinationTicketDeck();
        this.hasStarted = false;
        this.onLastRound = false;
    }

    //Constructor
    public ActiveGame() {
        gameID = "jajaja";
        gameName = "jack";
        players = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            players.add(new Player(new User("jack", "jack", "jack")));
        }
        trainCardsManager = new TrainCarCardManager();
        initializePlayerTrainCards();
        gameHistory = new ArrayList<>();
        initializeCities();
        initializeRoutes();
        destinationTicketCards = new DestinationTicketDeck();
    }

    public List<Location> getCities() {
        return cities;
    }

    public Location getLocationFromName(String cityName) {
        for (Location l : cities) {
            if (l.getCity().equals(cityName))
                return l;
        }
        return null;
    }

    private void initializeCities() {
        try {
            String json = new String(Files.readAllBytes(Paths.get("cityLocations.json"))); //use new AuthToken(Files.readAllBytes(Paths.get("locations.json"))); as the call parameter to this function...do the same for the rest
            Gson gson = new Gson();
            Data1 locArray = gson.fromJson(json, Data1.class);
            cities = Arrays.asList(locArray.data);
        } catch (IOException e) {
            System.out.println("IOException in ActiveGame -> initializeCities method: ");
            e.printStackTrace();
            System.out.println("Generating Test Data Instead");
            cities = new ArrayList<Location>();
            cities.add(new Location(45.5017, 73.0589, "Boston", "United States"));
            cities.add(new Location(38.9072, 77.0369, "Washington", "United States"));
            cities.add(new Location(35.7796, 78.6382, "Raleigh", "United States"));
            cities.add(new Location(32.7765, 79.9311, "Charleston", "United States"));
            cities.add(new Location(25.7617, 80.1918, "Miami", "United States"));
            cities.add(new Location(40.4406, 79.9959, "Pittsburgh", "United States"));
            cities.add(new Location(33.7490, 84.3880, "Atlanta", "United States"));
        }
    }

    private void initializeRoutes() {
        if (cities.size() > 10) {
            List<Route> nonAlphaRoutes = new ArrayList<>();
            for (int i = 0; i < routeCities.length; i++) {
                nonAlphaRoutes.add(new Route(getLocationFromName(routeCities[i][0]), getLocationFromName(routeCities[i][1]), Integer.valueOf(routeCities[i][2]), TrainColor.nameOf(routeCities[i][3]), i));
            }
            routes = new ArrayList<>();
            for (Route nonAlphaRoute : nonAlphaRoutes) {
                if (routes.size() == 0) {
                    routes.add(nonAlphaRoute);
                } else {
                    for (int i = 0; i < routes.size(); i++) {
                        if (routes.get(i).getStartCity().getCity().compareTo(nonAlphaRoute.getStartCity().getCity()) == 0) {
                            routes.add(i, nonAlphaRoute);
                            break;
                        } else if (routes.get(i).getStartCity().getCity().compareTo(nonAlphaRoute.getStartCity().getCity()) > 0) {
                            //new city comes BEFORE old city
                            routes.add(i, nonAlphaRoute);
                            break;
                        } else if (i == routes.size() - 1) {
                            routes.add(nonAlphaRoute);
                            break;
                        }
                    }
                }
            }
        } else {
            routes = new ArrayList<>();
            for (int i = 0; i < routeCities.length; i++) {
                routes.add(new Route(getLocationFromName(routeCities[i][0]), getLocationFromName(routeCities[i][1]), Integer.valueOf(routeCities[i][2]), TrainColor.nameOf(routeCities[i][3]), i));
            }
        }
    }


    private void initializePlayerTrainCards() {
        for (Player player : players) {
            List<TrainCarCard> initialCards = new ArrayList<TrainCarCard>();
            for (int i = 0; i < 4; i++) {
                initialCards.add(trainCardsManager.draw());
            }
            player.addTrainCarCards(initialCards);
        }
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void advanceTurn(String nextPlayerTurn) {
        for (Player player : players) {
            if (player.getPlayerName().equals(nextPlayerTurn)) {
                player.setTurn(true);
            } else {
                player.setTurn(false);
            }
        }
    }

    public DestinationTicketDeck getDestinationTicketCards() {
        return destinationTicketCards;
    }

    public List<Player> getPlayerOrder() {
        return playerOrder;
    }

    public void nextPlayer() {
        playerOrder.get(currentPlayer).setTurn(false);
        currentPlayer++;
        if (currentPlayer == playerOrder.size()) {
            currentPlayer = 0;
        }
        playerOrder.get(currentPlayer).setTurn(true);
    }

    public boolean allPlayersChoseTickets() {
        if (playerOrder.size() == players.size()) {
            return true;
        } else {
            return false;
        }
    }

    public TrainCarCardManager getTrainCardsManager() {
        return trainCardsManager;
    }

    public List<Route> getUnclaimedRoutes() {
        List<Route> result = new ArrayList<Route>();
        for (Route route : routes) {
            if (!route.isClaimed()) {
                result.add(route);
            }
        }
        return result;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public Route getRoute(Integer routeID) {
        for (Route route : routes) {
            if (route.getRouteID().equals(routeID)) {
                return route;
            }
        }
        return null;
    }

    public void claimRoute(Route route, Player owner) {
        route.setOwner(owner);
        for (Route currentRoute : routes) {
            if (currentRoute.getRouteID().equals(route.getRouteID())) {
                for (Player player : players) {
                    if (player.getPlayerName().equals(owner.getPlayerName())) {
                        currentRoute.claim(player);
                    }
                }
            }
        }
    }

    public void addTrainCarCard(String playerName, TrainCarCard card) {
        for (Player player : players) {
            if (player.getPlayerName().equals(playerName)) {
                player.addTrainCarCards(Arrays.asList(card));
            }
        }
    }

    public void setDisplayedTrainCards(List<TrainCarCard> cards) {
        trainCardsManager.setFaceUpCards(cards);
    }

    public void setCurrentPlayerIndex(int index) {
        currentPlayer = index;
    }

    public Player getCurrentPlayer() {
        return playerOrder.get(currentPlayer);
    }

    public List<Command> getGameHistory() {
        return gameHistory;
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public boolean isOnLastRound() {
        return onLastRound;
    }

    public void setOnLastRound(boolean onLastRound) {
        this.onLastRound = onLastRound;
    }

    public boolean updateTicketStatus(Player player) {
        boolean hasChanged = false;
        for (DestinationTicketCard ticket : player.getDestinationTickets()) {
            if (ticket.getCompleted() == false) {
                for (String cityName : player.getVisitedCities()) {
                    if (cityName.equals(ticket.getStartCity().getCity())) {
                        if (recursiveRouteChecker(cityName, ticket.getEndCity().getCity(), player, new ArrayList<String>())) {
                            ticket.setCompleted(true);
                            hasChanged = true;
                        }
                    }
                }
            }
        }
        return hasChanged;
    }

    public static void main(String[] args) {
        ActiveGame game = new ActiveGame(new Game(2, "gameName", "gameID"));
        Player player = new Player();
        player.getDestinationTickets().add(game.getDestinationTicketCards().getDestinationCards().get(0));
        player.getDestinationTickets().add(game.getDestinationTicketCards().getDestinationCards().get(1));
        player.getDestinationTickets().add(game.getDestinationTicketCards().getDestinationCards().get(2));
        for (Route route : game.getRoutes()) {
            player.getClaimedRoutes().add(route.getRouteID());
            player.getVisitedCities().add(route.getStartCity().getCity());
            player.getVisitedCities().add(route.getEndCity().getCity());
        }
        if (game.updateTicketStatus(player)) {
            System.out.println("The ticket has been completed");
        } else {
            System.out.println("The ticket has NOT been completed");
        }

    }

    private boolean alreadyVisited(List<String> visitedCities, String cityToVisit) {
        for (String cityName : visitedCities) {
            if (cityName.equals(cityToVisit)) {
                return true;
            }
        }
        return false;
    }

    public boolean recursiveRouteChecker(String startCity, String endCity, Player player, List<String> visitedCities) {
        visitedCities.add(startCity);
        if (startCity.equals(endCity)) {
            return true;
        }
        for (Integer routeID : player.getClaimedRoutes()) {
            Route route = this.getRoute(routeID);
            if (route.getStartCity().getCity().equals(startCity) && !alreadyVisited(visitedCities, route.getEndCity().getCity())) {
                if (recursiveRouteChecker(route.getEndCity().getCity(), endCity, player, visitedCities)) {
                    return true;
                }
                visitedCities.remove(visitedCities.size() - 1);
            } else if (route.getEndCity().getCity().equals(startCity) && !alreadyVisited(visitedCities, route.getStartCity().getCity())) {
                if (recursiveRouteChecker(route.getStartCity().getCity(), endCity, player, visitedCities)) {
                    return true;
                }
                visitedCities.remove(visitedCities.size() - 1);
            }
        }
        return false;
    }

    public boolean lastTurnsAreOver() {
        for (Player player : players) {
            if (!player.getHasCompletedLastTurn()) {
                return false;
            }
        }
        return true;
    }

    public boolean isDoubleRoute(Route route) {
        for (Route currentRoute : routes) {
            if (!currentRoute.getRouteID().equals(route.getRouteID())
                    && currentRoute.getStartCity().equals(route.getStartCity())
                    && currentRoute.getEndCity().equals(route.getEndCity())) {
                return true;
            }
        }
        return false;
    }

    public Route getDoubleRoute(Route route) {
        for (Route currentRoute : routes) {
            if (!currentRoute.getRouteID().equals(route.getRouteID())
                    && currentRoute.getStartCity().equals(route.getStartCity())
                    && currentRoute.getEndCity().equals(route.getEndCity())) {
                return currentRoute;
            }
        }
        return null;
    }

    public boolean determineIsOnLastRound() {
        for (Player player : players) {
            if (player.getRemainingTrainCars() <= 2) {
                return true;
            }
        }
        return false;
    }

}
