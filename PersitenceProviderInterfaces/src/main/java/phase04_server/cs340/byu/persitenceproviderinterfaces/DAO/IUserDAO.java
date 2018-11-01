package phase04_server.cs340.byu.persitenceproviderinterfaces.DAO;

public interface IUserDAO {
    void addUsers(String userListJsonString);
    void clear();
    String getUserListJsonString();
}
