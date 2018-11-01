package cs340.client.presenter;

import java.util.List;

import Utils.TrainColor;
import cs340.client.view.AbstractGameView;
import model.DestinationTicketCard;
import model.EventMessage;
import model.Player;
import model.Route;

public interface IGamePresenter {
    public void takeCard(int index);
    public void drawCard();
    public void attach(AbstractGameView state);
    public void openClaimRouteView();
    public void openDestinationView();
    public void openDrawCardView();
    public void openHistoryView();
    public void openStatsView();
    public void openEndGameView();
    public void sendChat(String message);
    public boolean selectDestinations(List<DestinationTicketCard> choices);
    public void claimRoute(Route route, TrainColor color);
}
