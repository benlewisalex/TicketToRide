package model;

import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Destination;

public class DestinationTicketList {
    List<DestinationTicketCard> tickets;

    public DestinationTicketList(List<DestinationTicketCard> tickets){
        this.tickets = new ArrayList<>();
        for(DestinationTicketCard ticket : tickets){
            this.tickets.add(ticket);
        }
    }

    public List<DestinationTicketCard> getTickets(){
        return tickets;
    }
}
