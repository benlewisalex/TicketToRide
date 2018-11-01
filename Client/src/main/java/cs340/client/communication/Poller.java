package cs340.client.communication;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cs340.client.model.ClientModel;
import cs340.client.model.IUIFacade;
import cs340.client.model.UIFacade;
//import cs340.client.model.UIFacade;

public class Poller {
    private Timer timer;
    private final int DELAY = 1000; //number of milliseconds between requests
    private ClientModel user;
    private ClientCommunicator comm;
    private static Poller instance;
    private boolean disabled;

    private Poller() {
        timer = new Timer();
        timer.schedule(new PollerTask(), DELAY);
        user = ClientModel.getInstance();
        comm = new ClientCommunicator();
        disabled = false;
    }

    public static Poller getInstance() {
        if(instance == null) {
            instance = new Poller();
        }
        return instance;
    }

    public void enable() {
        disabled = false;
    }

    public void disable() {
        disabled = true;
    }

    class PollerTask extends TimerTask {
        public void run() {
            if(!disabled) {
                if(ClientModel.getInstance().userHasActiveGame()) {
                    comm.requestUpdateCommandList(user.getCommandIndex(), user.getAuthtoken(), user.getActiveGame().getGameID());
                }
                else {
                    comm.getGames(user.getAuthtoken());
                }
            }
            timer.schedule(new PollerTask(), DELAY);
        }
    }

}