package cs340.client.communication;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Command.Command;
import Command.ICommand;
import Utils.TrainColor;
import cs340.client.model.ClientModel;
import cs340.client.model.CommandFacade;
import model.DestinationTicketCard;
import model.DestinationTicketList;
import model.EventMessage;
import model.Player;
import model.Route;

public class ClientCommunicator implements IServerProxy {

    //variables to hold the needed data
    private final String TAG = "ClientCommunicator";
    private String ipAddress = "10.0.2.2";
    private final String http = "http://";
    private final String command = "/cmd/";
    private final String colon = ":";
    private String port = "8080";
    private String urlPath = new String("http://10.0.2.2:8080/cmd/");
    private String serverFacadeClassPath = "cs340.server.ServerFacade";
    private ClientModel user = ClientModel.getInstance();
    private CommandFacade facade = new CommandFacade();

    @Override
    public void setIpAddressandPort(String ipAddress, String port)
    {
        this.ipAddress = ipAddress;
        this.port = port;
        urlPath = new String(http + this.ipAddress + colon + port + command);
    }


    private class AsyncRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(urlPath);
                Poller.getInstance().disable();
                return new HttpClient().getUrl(url, strings[0], strings[1]);

            } catch (Exception exp) {
                Log.e(TAG, "Failed in AsyncGet: ", exp);
                return exp.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                super.onPostExecute(s);
                Gson gson = new Gson();
                Type commandListType = new TypeToken<List<Command>>() {}.getType();
                List<ICommand> commands = gson.fromJson(s, commandListType);
                if(commands != null && commands.size() != 0) {
                    for (int i = 0; i < commands.size(); i++) {
                        commands.get(i).execute();
                    }
                    if(ClientModel.getInstance().userHasActiveGame()) {
                        List<ICommand> commandsToAdd = new ArrayList<ICommand>();
                        for(ICommand command: commands) {
                            if(!command.getMethodName().equals("displayMessage")
                                    && !command.getMethodName().equals("updateChat")
                                    && !command.getMethodName().equals("setJoinedGame")
                                    && !command.getMethodName().equals("updateGameList")) {
                                commandsToAdd.add(command);
                            }
                            if(command.getMethodName().equals("advanceTurn")) {
                                if (ClientModel.getInstance().getActiveGame().determineIsOnLastRound()) {
                                    for (Player player : ClientModel.getInstance().getActiveGame().getPlayers()) {
                                        if(player.getTurn()) {
                                            facade.displayMessage("Buddy has decided that it's " + player.getPlayerName() + "'s Last Turn");
                                        }
                                    }
                                }
                                else {
                                    for (Player player : ClientModel.getInstance().getActiveGame().getPlayers()) {
                                        if(player.getTurn()) {
                                            facade.displayMessage("Buddy has decided that it's " + player.getPlayerName() + "'s Turn");
                                        }
                                    }
                                }
                            }
                        }
                        ClientModel.getInstance().addGameHistory(commandsToAdd);
//                        if (!commands.get(0).getMethodName().equals("displayMessage")) {
//                            user.addGameHistory(commands);
////                        user.setCommandIndex(user.getCommandIndex() + commands.size());
//                        }
                    }
//                    if(ClientModel.getInstance().userHasActiveGame()) {
//                        System.out.println("Current client command history");
//                        for (EventMessage command : ClientModel.getInstance().getGameHistory()) {
//                            System.out.println(command.toString());
//                        }
//                    }
                }

                else {
                    Log.d(TAG, String.format("Commands were null, Response was: %s", s));
                }
                Poller.getInstance().enable();


            } catch(InvocationTargetException e) {
                Log.d(TAG, "Caught InvocationTargetException" + e.toString());
                Log.e(TAG, e.getTargetException().toString());
                e.printStackTrace();
            } catch(Exception e) {
                Log.e(TAG, "Threw following exception while executing commands" +
                        " the server was calling on the client\n" + e.toString());
                e.printStackTrace();
            }
        }
    }

    public void sendCmd(ICommand cmd, String authToken) {
        Gson gson = new Gson();
        String json = gson.toJson(cmd, Command.class);
        new AsyncRequest().execute(json, authToken);
    }

    public ClientCommunicator() {
//        urlPath = new String("http://10.0.2.2:8080/cmd/");
////        user = ClientModel.getInstance();
//        urlPath = new String(http + this.ipAddress + colon + port + command);
    }

    @Override
    public void getGames(String authToken) {
        ICommand updateGamesCommand = new Command(serverFacadeClassPath, "getGames", new Class[]{String.class}, new Object[]{authToken});
        sendCmd(updateGamesCommand, null);
    }

    @Override
    public void createGame(String gameID, Double numPossiblePlayers, String authToken) {
        ICommand createGameCommand = new Command(serverFacadeClassPath, "createGame", new Class[]{String.class, Double.class}, new Object[]{gameID, numPossiblePlayers});
        sendCmd(createGameCommand, null);
    }

    @Override
    public void joinGame(String gameID, String authToken, Double playerColorDouble) {
        ICommand joinGameCommand = new Command(serverFacadeClassPath, "joinGame", new Class[]{String.class, String.class, Double.class}, new Object[]{gameID, authToken, playerColorDouble});
        sendCmd(joinGameCommand, null);
    }

    @Override
    public void requestStartGame(String gameID) {
        ICommand startGameCommand = new Command(serverFacadeClassPath, "startGame", new Class[]{String.class}, new Object[]{gameID});
        sendCmd(startGameCommand, gameID);
    }

    @Override
    public void login(String username, String password) {
        ICommand loginCommand = new Command(serverFacadeClassPath, "login", new Class[]{String.class, String.class}, new Object[]{username, password});
        sendCmd(loginCommand, null);
    }

    @Override
    public void register(String username, String password) {
        ICommand registerCommand = new Command(serverFacadeClassPath, "register", new Class[]{String.class, String.class}, new Object[]{username, password});
        sendCmd(registerCommand, null);
    }

//    @Override
//    public void getActiveGame(String joinGameID) {
//        ICommand getActiveGameCommand = new Command(serverFacadeClassPath, "getActiveGame", new Class[]{String.class}, new Object[]{joinGameID});
//        sendCmd(getActiveGameCommand, null);
//    }

//    @Override
//    public void updateActiveGame(ActiveGame activeGame) {
//        ICommand updateActiveGameCommand = new Command(serverFacadeClassPath, "updateActiveGame", new Class[]{ActiveGame.class}, new Object[]{activeGame});
//        sendCmd(updateActiveGameCommand, null);
//    }

    @Override
    public void updateChat(EventMessage eventMessage, String gameID) {
        ICommand updateChatCommand = new Command(serverFacadeClassPath, "updateChat", new Class[]{EventMessage.class, String.class}, new Object[]{eventMessage, gameID});
        sendCmd(updateChatCommand, gameID);
    }

    @Override
    public void requestDestinationTickets(String gameID, String authtoken) {
        ICommand getInitialDestinationTicketsCommand = new Command(serverFacadeClassPath, "requestDestinationTickets", new Class[]{String.class, String.class}, new Object[]{gameID, authtoken});
        sendCmd(getInitialDestinationTicketsCommand, gameID);
    }

    @Override
    public void selectDestinationTickets(List<DestinationTicketCard> selectedCards, String gameID, String authtoken) {
        ICommand setInitialDestinationTicketsCommand = new Command(serverFacadeClassPath, "selectDestinationTickets", new Class[]{DestinationTicketList.class, String.class, String.class}, new Object[]{new DestinationTicketList(selectedCards), gameID, authtoken});
        sendCmd(setInitialDestinationTicketsCommand, gameID);
    }

    @Override
    public void requestClaimRoute(Route route, TrainColor color, String authtoken) {
        ICommand claimRouteCommand = new Command(serverFacadeClassPath, "claimRoute", new Class[]{Route.class, TrainColor.class, String.class}, new Object[]{route, color, authtoken});
        sendCmd(claimRouteCommand, ClientModel.getInstance().getJoinedGameID());
    }

    @Override
    public void requestTakeCard(Integer index, String authtoken, String gameID) {
//        ICommand takeCardCommand = new Command(serverFacadeClassPath, "takeCard", new Class[]{Double.class, String.class, String.class}, new Object[]{index.doubleValue(), authtoken, gameID});
        ICommand takeCardCommand = new Command(serverFacadeClassPath, "takeCard", new Class[]{Double.class, String.class}, new Object[]{index.doubleValue(), authtoken});
        sendCmd(takeCardCommand, gameID);
    }

    @Override
    public void requestDrawCard(String authtoken, String gameID) {
        ICommand drawCardCommand = new Command(serverFacadeClassPath, "drawCard", new Class[]{String.class}, new Object[]{authtoken});
        sendCmd(drawCardCommand, gameID);
    }

    @Override
    public void requestUpdateCommandList(Integer commandListIndex, String authtoken, String gameID) {
        ICommand updateCommandListCommand = new Command(serverFacadeClassPath, "updateCommandList", new Class[]{Double.class , String.class, String.class}, new Object[]{commandListIndex.doubleValue(), authtoken, gameID});
        sendCmd(updateCommandListCommand, null);
    }

//    @Override
//    public void endTurn(ActiveGame endTurnGameState) {
//        ICommand endTurnCommand = new Command(serverFacadeClassPath, "endTurn", new Class[]{ActiveGame.class}, new Object[]{endTurnGameState});
//        sendCmd(endTurnCommand, null);
//    }
}
