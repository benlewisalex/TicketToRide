package sql;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.ActiveGame;
import model.ActiveGameList;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IActiveGameDAO;

public class ActiveGameDAO extends DAO implements IActiveGameDAO {
    private static ActiveGameDAO instance;
    private ActiveGameDAO() {
        try {
            if (!tableExists(tableName)) {
                execute(createTable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String tableName = "ActiveGames";
    private static final String createTable =
            "CREATE TABLE ActiveGames (\n" +
            "    id integer primary key autoincrement,\n" +
            "    gameID text unique,\n" +
            "    game text\n" +
            ")";
    private static final String add = "insert into ActiveGames (gameID, game) values (?, ?)";
    private static final String deleteAll = "delete from ActiveGames";
    private static final String clear = "delete from ActiveGames where gameID = ?";
    private static final String get = "select game from ActiveGames where gameID = ?";

    public static ActiveGameDAO getInstance() {
        if (instance == null) {
            instance = new ActiveGameDAO();
        }
        return instance;
    }

    public void addActiveGame(String activeGameJsonString, String gameID) {
        try {
            execute(add, gameID, activeGameJsonString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        try {
            execute(deleteAll);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearActiveGame(String gameID) {
        try {
            execute(clear, gameID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getActiveGameJsonString(String gameID) {
        String s = null;
        try {
            s = runQuery(get, gameID).get(0).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }

    public String getAllActiveGames() {
        List<ActiveGame> games = new LinkedList<>();
        Gson gson = new Gson();
        try {
            for (List<String> l : runQuery("select game from ActiveGames")) {
                games.add(gson.fromJson(l.get(0), ActiveGame.class));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return gson.toJson(new ActiveGameList(new ArrayList<ActiveGame>()));
        }
        return gson.toJson(new ActiveGameList(games));
    }
}