package model;

import model.Player;

public class EventMessage {
    private String eventOwner;
    private String event;

    public EventMessage(String eventOwner, String event) {
        this.eventOwner = eventOwner;
        this.event = event;
    }

    public String getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(String eventOwner) {
        this.eventOwner = eventOwner;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return eventOwner + ": " + event;
    }
}
