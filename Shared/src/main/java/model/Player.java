package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Utils.TrainColor;
import Utils.PlayerColor;

public class Player {
    private String playerName;
    private String authtoken;
    private PlayerColor color;
    private Boolean isTurn;
    private Map<TrainColor, List<TrainCarCard>> trainCards;
    private List<DestinationTicketCard> destinationTickets;
    private Integer score;
    private Integer remainingTrainCars;
    private boolean hasCompletedLastTurn;
    private List<Integer> claimedRoutesID;
    private Set<String> visitedCities;

    public Player(User user) {
        this.playerName = user.getUsername();
        this.authtoken = user.getAuthtoken();
        this.color = user.getPlayerColor();
        this.hasCompletedLastTurn = false;
        isTurn = false;
        trainCards = new HashMap<>();
        destinationTickets = new ArrayList<>();
        score = 0;
        remainingTrainCars = 45;
        claimedRoutesID = new ArrayList<>();
        visitedCities = new HashSet<>();
    }

    public Player() {
        playerName = "gaskint";
        authtoken = "blah";
        color = PlayerColor.BLACK;
        isTurn = false;
        trainCards = new HashMap<>();
        destinationTickets = new ArrayList<>();
        score = 0;
        remainingTrainCars = 45;
        claimedRoutesID = new ArrayList<>();
        visitedCities = new HashSet<>();
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public Boolean getTurn() {
        return isTurn;
    }

    public void setTurn(Boolean turn) {
        isTurn = turn;
    }

    public Integer getScore() {
        return score;
    }

    public void adjustScore(Integer points) {
        score += points;
    }

    public Integer getRemainingTrainCars() {
        return remainingTrainCars;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setRemainingTrainCars(Integer remainingTrainCars) {
        this.remainingTrainCars = remainingTrainCars;
    }

    public void useTrainCars(Integer trainCarsUsed) {
        if(remainingTrainCars - trainCarsUsed < 0)
        {
            System.out.println("Shared -> Model -> Player -> useTrainCars method: remaining train cars = " + remainingTrainCars + " < requested train cars: " + trainCarsUsed);
        }
        else
        {
            remainingTrainCars -= trainCarsUsed;
        }
    }

    public List<DestinationTicketCard> getDestinationTickets() {
        return destinationTickets;
    }

    public void addDestinationTickets(List<DestinationTicketCard> drawnCards)
    {
        for(DestinationTicketCard d : drawnCards)
        {
            destinationTickets.add(d);
        }
    }

    public void addTrainCarCards(List<TrainCarCard> drawnCards)
    {
        for(TrainCarCard t : drawnCards)
        {
            if (trainCards.containsKey(t.getColor()))
            {
                trainCards.get(t.getColor()).add(t);
            }
            else
            {
                trainCards.put(t.getColor(),new ArrayList<TrainCarCard>());
                trainCards.get(t.getColor()).add(t);
            }
        }
    }

    public void useTrainCarCards(List<TrainCarCard> usedCards)
    {
        for(TrainCarCard t : usedCards)
        {
            this.trainCards.get(t.getColor()).remove(0);
        }
    }

    public Integer getNumColoredTrainCardCards(TrainColor color) {
        if(trainCards.get(color) != null) {
            return trainCards.get(color).size();
        }
        else {
            return 0;
        }
    }

    public List<TrainCarCard> getColoredTrainCardCards(TrainColor color) {
        if(trainCards.get(color) != null) {
            return trainCards.get(color);
        }
        else {
            List<TrainCarCard> emptyList = new ArrayList<>();
            return emptyList;
        }
    }

    public void setDestinationTickets(List<DestinationTicketCard> destinationTickets) {
        this.destinationTickets = destinationTickets;
    }

    public Integer getNumTrainCards() {
        int numTrainCards = 0;
        for(List<TrainCarCard> cardList: trainCards.values()) {
            numTrainCards += cardList.size();
        }
        return numTrainCards;
    }

    public boolean getHasCompletedLastTurn() {
        return hasCompletedLastTurn;
    }

    public void setHasCompletedLastTurn(boolean lastTurn) {
        hasCompletedLastTurn = lastTurn;
    }

    public List<Integer> getClaimedRoutes() {
        return claimedRoutesID;
    }

    public Set<String> getVisitedCities() {
        return visitedCities;
    }
}
