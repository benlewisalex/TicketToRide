package sql;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.ICommandDAO;

public class CommandDAO extends DAO implements ICommandDAO {
    private static CommandDAO instance;
    private CommandDAO()  {
        try {
            if (!tableExists(tableName)) {
                execute(createTable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static CommandDAO getInstance() {
        if (instance == null) {
            instance = new CommandDAO();
        }
        return instance;
    }

    private static final String tableName = "Commands";
    private static final String createTable =
            "CREATE TABLE Commands (\n" +
                    "    id integer primary key autoincrement,\n" +
                    "    gameID text,\n" +
                    "    command text,\n" +
                    "    foreign key (gameID) references ActiveGames (gameID)\n" +
                    ")";
    private static final String add = "insert into Commands (gameID, command) values (?, ?)";
    private static final String clear = "delete from Commands where gameID = ?";
    private static final String deleteAll = "delete from Commands";
    private static final String get = "select command from Commands where gameID = ?";

    public void addCommand(String jsonCommand, String gameID) {
        try {
            execute(add, gameID, jsonCommand);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearCommands(String gameID) {
        try {
            execute(clear, gameID);
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

    public List<String> getCommands(String gameID) {
        List<String> cmds = new LinkedList<>();
        try {
            for (List<String> l : runQuery(get, gameID)) {
                cmds.add(l.get(0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return cmds;
    }
}
