package phase04_server.cs340.byu.persitenceproviderinterfaces.DAO;

public interface IGameDAO {
    void addGameList(String gameListJsonString);
    void clear();
    String getGameListJsonString();
}
