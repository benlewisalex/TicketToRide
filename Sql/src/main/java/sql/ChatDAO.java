package sql;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import model.ChatHistory;
import model.ChatHistoryList;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IChatHistoryDAO;

public class ChatDAO extends DAO implements IChatHistoryDAO {
    private static ChatDAO instance;
    private ChatDAO()  {
        try {
            if (!tableExists(tableName)) {
                execute(createTable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ChatDAO getInstance() {
        if (instance == null) {
            instance = new ChatDAO();
        }
        return instance;
    }

    private static final String tableName = "Chat";
    private static final String createTable =
            "CREATE TABLE Chat (\n" +
                    "    id integer primary key autoincrement,\n" +
                    "    gameID text,\n" +
                    "    message text,\n" +
                    "    foreign key (gameID) references ActiveGames (gameID)\n" +
                    ")";
    private static final String add = "insert or replace into Chat (gameID, message) values (?, ?)";
    private static final String deleteAll = "delete from Chat";
    private static final String clear = "delete from Chat where gameID = ?";
    private static final String get = "select message from Chat where gameID = ?";

    public void addChat(String chatHistoryJsonString, String gameID) {
        try {
            execute(add, gameID, chatHistoryJsonString);
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

    public void clearChatHistory(String gameID) {
        try {
            execute(clear, gameID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getChatHistoryJsonString(String gameID) {
        String s = null;
        try {
            s = runQuery(get, gameID).get(0).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }

    public String getAllChatHistories() {
        List<ChatHistory> hist = new LinkedList<>();
        Gson gson = new Gson();
        try {
            for (List<String> l : runQuery("select message from Chat")) {
                hist.add(gson.fromJson(l.get(0), ChatHistory.class));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return gson.toJson(new ChatHistoryList(hist));
    }
}