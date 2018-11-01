package sql;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import model.User;
import model.UserList;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IUserDAO;

public class UserDAO extends DAO implements IUserDAO {
    private static UserDAO instance;
    private UserDAO() {
//        System.out.println("Calling UserDAO constructor");
        try {
            if (!tableExists(tableName)) {
                execute(createTable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String tableName = "Users";
    private static final String createTable =
            "CREATE TABLE Users (\n" +
                    "    id integer primary key,\n" +
                    "    users text\n" +
                    ")";
    private static final String add =
            "insert or replace into Users (id, users)" +
            "values (1, ?)";
    private static final String deleteAll = "delete from Users";
    private static final String get = "select users from Users where id = 1";

    public static UserDAO getInstance() {
//        System.out.println("Calling UserDAO getInstance");
        if (instance == null) {
            instance = new UserDAO();
        }

        return instance;
    }

    public void addUsers(String userListJsonString) {
        try {
            execute(add, userListJsonString);
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

    public String getUserListJsonString() {
        try {
            List<List<String>> result = runQuery(get);
            if(result.size() == 0) {
                return new Gson().toJson(new UserList(new LinkedList<User>()));
            }
            return result.get(0).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Gson().toJson(new UserList(new LinkedList<User>()));
    }
}
