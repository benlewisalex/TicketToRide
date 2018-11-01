package model;

import Utils.TrainColor;

public class Route {
    private Location startCity;
    private Location endCity;
    private int length;
    private int points;
    private TrainColor color;
    private Player owner;
    private Integer routeID;


    public Location getStartCity() {
        return startCity;
    }

    public void setStartCity(Location startCity) {
        this.startCity = startCity;
    }

    private void setPoints() {
        switch (length)
        {
            case 1:
                points = 1;
                break;
            case 2:
                points = 2;
                break;
            case 3:
                points = 4;
                break;
            case 4:
                points = 7;
                break;
            case 5:
                points = 10;
                break;
            case 6:
                points = 15;
                break;
            default:
                break;
        }
    }

    public Route(Location startCity, Location endCity, int length, TrainColor color, Integer routeID) {
        this.startCity = startCity;
        this.endCity = endCity;
        this.length = length;
        setPoints();
        this.color = color;
        this.routeID = routeID;
        this.owner = null;
    }

    public Location getEndCity() {
        return endCity;
    }

    public void setEndCity(Location endCity) {
        this.endCity = endCity;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public TrainColor getColor() {
        return color;
    }

    public void setColor(TrainColor color) {
        this.color = color;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Integer getRouteID() {
        return routeID;
    }

    public void setRouteID(Integer routeID) {
        this.routeID = routeID;
    }

    public void claim(Player owner) {
        this.owner = owner;
        this.owner.adjustScore(this.points);
        this.owner.getVisitedCities().add(this.getStartCity().getCity());
        this.owner.getVisitedCities().add(this.getEndCity().getCity());
        owner.setRemainingTrainCars(owner.getRemainingTrainCars() - this.getLength());
//        owner.getClaimedRoutes().add(this);
    }

    public boolean isClaimed()
    {
        return owner != null;
    }

    public Player claimedBy()
    {
        return owner;
    }
}
