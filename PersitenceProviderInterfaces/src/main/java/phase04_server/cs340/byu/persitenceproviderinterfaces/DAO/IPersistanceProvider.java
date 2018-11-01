package phase04_server.cs340.byu.persitenceproviderinterfaces.DAO;

public interface IPersistanceProvider {
    void clear();
    void startTransaction();
    void endTransaction();
    IUserDAO getUserDAO();
    IGameDAO getGameDAO();
    IActiveGameDAO getActiveGameDAO();
    ICommandDAO getCommandDAO();
    IChatHistoryDAO getChatHistoryDAO();
}
