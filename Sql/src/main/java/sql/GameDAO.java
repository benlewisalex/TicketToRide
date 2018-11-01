package sql;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import model.ActiveGame;
import model.Game;
import model.GameList;
import model.User;
import model.UserList;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IGameDAO;

public class GameDAO extends DAO implements IGameDAO {
    private static GameDAO instance;
    private GameDAO() {
        try {
            if (!tableExists(tableName)) {
                execute(createTable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String tableName = "Games";
    private static final String createTable =
            "CREATE TABLE Games (\n" +
                    "    id integer primary key,\n" +
                    "    games text\n" +
                    ")";
    private static final String add = "insert or replace into Games (id, games) values (1, ?)";
    private static final String deleteAll = "delete from Games";
    private static final String get = "select games from Games where id = 1";

    public static GameDAO getInstance() {
        if (instance == null) {
            instance = new GameDAO();
        }
        return instance;
    }

    public void addGameList(String gameListJsonString) {
        try {
            execute(add, gameListJsonString);
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

    public String getGameListJsonString() {
        String s = null;
        try {
            List<List<String>> result = runQuery(get);
            if(result.size() == 0) {
                return new Gson().toJson(new GameList(new LinkedList<Game>()));
            }
            else {
                return result.get(0).get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Gson().toJson(new GameList(new LinkedList<Game>()));
    }
}
