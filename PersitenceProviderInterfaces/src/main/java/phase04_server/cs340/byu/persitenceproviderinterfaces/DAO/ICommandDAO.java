package phase04_server.cs340.byu.persitenceproviderinterfaces.DAO;

import java.util.List;

public interface ICommandDAO {
    void addCommand(String jsonCommand, String gameID);
    void clearCommands(String gameID);
    void clear();
    List<String> getCommands(String gameID);
}
