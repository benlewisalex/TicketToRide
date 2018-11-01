package phase04_server.cs340.byu.persitenceproviderinterfaces.DAO;

public interface IActiveGameDAO {
    void addActiveGame(String activeGameJsonString, String gameID);
    void clear();
    void clearActiveGame(String gameID);
    String getActiveGameJsonString(String gameID);
    String getAllActiveGames();
}
