package model;

public class DestinationTicketCard {
    private Location startCity;
    private Location endCity;
    private int points;
    private boolean completed;

    public DestinationTicketCard(Location startCity, Location endCity, int points) {
        this.startCity = startCity;
        this.endCity = endCity;
        this.points = points;
        this.completed = false;
    }

    public Location getStartCity() {
        return startCity;
    }

    public void setStartCity(Location startCity) {
        this.startCity = startCity;
    }

    public Location getEndCity() {
        return endCity;
    }

    public void setEndCity(Location endCity) {
        this.endCity = endCity;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setCompleted(boolean isCompleted) {
        completed = isCompleted;
    }

    public boolean getCompleted() {
        return completed;
    }
}
