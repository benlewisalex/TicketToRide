package cs340.server;

import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import Command.Command;
import Command.ICommand;
import cs340.server.model.ServerModel;
import model.ActiveGame;
import model.ActiveGameList;
import model.ChatHistoryList;
import model.GameList;
import model.User;
import model.UserList;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IActiveGameDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IChatHistoryDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.ICommandDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IGameDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IPersistanceProvider;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IUserDAO;

public class DatabaseManager {
    private static DatabaseManager _instance;
    IPersistanceProvider persistanceProvider;
    Map<String, Integer> commandsPerGame;
    int numCmdsBetweenBackups;
    private final String TAG = "Database Manager";

    public DatabaseManager() {
        commandsPerGame = new HashMap<>();
        numCmdsBetweenBackups = 1;
    }

    public static DatabaseManager getInstance() {
        if (_instance == null)
            _instance = new DatabaseManager();
        return _instance;
    }

    public void clear()
    {
        persistanceProvider.startTransaction();
        persistanceProvider.getActiveGameDAO().clear();
        persistanceProvider.getChatHistoryDAO().clear();
        persistanceProvider.getCommandDAO().clear();
        persistanceProvider.getGameDAO().clear();
        persistanceProvider.getUserDAO().clear();
        persistanceProvider.endTransaction();
    }

    public void setPersistanceProvider(IPersistanceProvider persistanceProvider) {
        this.persistanceProvider = persistanceProvider;
    }

    public int getNumCmdsBetweenBackups() {
        return numCmdsBetweenBackups;
    }

    public void setNumCmdsBetweenBackups(int numCmdsBetweenBackups) {
        this.numCmdsBetweenBackups = numCmdsBetweenBackups;
    }

    private void store(String jsonCommandString, String gameID)
    {
        persistanceProvider.startTransaction();
        ICommandDAO commandDAO = persistanceProvider.getCommandDAO();
        commandDAO.addCommand(jsonCommandString, gameID);
        persistanceProvider.endTransaction();
    }

    private void backupActiveGame(String gameID)
    {
        Gson gson = new Gson();
        String activeGameJsonString = gson.toJson(ServerModel.getInstance().getActiveGame(gameID));
        String chatHistoryJsonString = gson.toJson(ServerModel.getInstance().getChatHistory(gameID));
        persistanceProvider.startTransaction();
        IActiveGameDAO activeGameDAO = persistanceProvider.getActiveGameDAO();
        activeGameDAO.clearActiveGame(gameID);
        activeGameDAO.addActiveGame(activeGameJsonString, gameID);
        IChatHistoryDAO chatHistoryDAO = persistanceProvider.getChatHistoryDAO();
        chatHistoryDAO.clearChatHistory(gameID);
        chatHistoryDAO.addChat(chatHistoryJsonString, gameID);
        ICommandDAO commandDAO = persistanceProvider.getCommandDAO();
        commandDAO.clearCommands(gameID);
        persistanceProvider.endTransaction();
    }

    public void serverInit()
    {
        Gson gson = new Gson();
        persistanceProvider.startTransaction();
        IUserDAO userDAO = persistanceProvider.getUserDAO();
        String userListJsonString = userDAO.getUserListJsonString();
        ServerModel.getInstance().setUsers(gson.fromJson(userListJsonString, UserList.class).getUsers());
        IGameDAO gameDAO = persistanceProvider.getGameDAO();
        String gameListJsonString = gameDAO.getGameListJsonString();
        ServerModel.getInstance().setLobbyGames(gson.fromJson(gameListJsonString, GameList.class).getGames());
        IActiveGameDAO activeGameDAO = persistanceProvider.getActiveGameDAO();
        String activeGameListJsonString = activeGameDAO.getAllActiveGames();
        ServerModel.getInstance().setActiveGames(gson.fromJson(activeGameListJsonString, ActiveGameList.class).getActiveGames());
        IChatHistoryDAO chatHistoryDAO = persistanceProvider.getChatHistoryDAO();
        String chatHistoryListJsonString = chatHistoryDAO.getAllChatHistories();
        ServerModel.getInstance().setChatHistories(gson.fromJson(chatHistoryListJsonString, ChatHistoryList.class).getChatHistories());
        ICommandDAO commandDAO = persistanceProvider.getCommandDAO();
        for (ActiveGame g : ServerModel.getInstance().getActiveGames())
        {
            for (String s : commandDAO.getCommands(g.getGameID())) {
                try {
                    ICommand command = (Command) gson.fromJson(s, Command.class);
                    command.execute();
                } catch (InvocationTargetException e) {
                    System.out.println(TAG + ": Caught InvocationTargetException" + e.toString());
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println(TAG + ": Threw following exception while executing commands" +
                            " the database was calling on the server\n" + e.toString());
                    e.printStackTrace();
                }
            }
            backupActiveGame(g.getGameID());
            commandsPerGame.put(g.getGameID(), 0);
        }
        persistanceProvider.endTransaction();
    }

    private void backupUsersAndGames()
    {
        Gson gson = new Gson();
        persistanceProvider.startTransaction();
        IUserDAO userDAO = persistanceProvider.getUserDAO();
        IGameDAO gameDAO = persistanceProvider.getGameDAO();
        String userListJsonString = gson.toJson(new UserList(ServerModel.getInstance().getUsers()));
        String gameListJsonString = gson.toJson(new GameList(ServerModel.getInstance().getLobbyGames()));
        userDAO.clear();
        userDAO.addUsers(userListJsonString);
        gameDAO.clear();
        gameDAO.addGameList(gameListJsonString);
        persistanceProvider.endTransaction();
    }

    public void storeCommand(String jsonCommandString, String gameID)
    {
        if (gameID != null)
        {
            if (!commandsPerGame.containsKey(gameID))
            {
                commandsPerGame.put(gameID, 0);
                backupActiveGame(gameID);
            }
            else if (commandsPerGame.get(gameID) + 1 == numCmdsBetweenBackups)
            {
                backupActiveGame(gameID);
                commandsPerGame.put(gameID, 0);
            }
            else
            {
                store(jsonCommandString, gameID);
                Integer i = commandsPerGame.get(gameID);
                commandsPerGame.put(gameID, i + 1);
            }
        }
        else
        {
            backupUsersAndGames();
        }
    }
}
