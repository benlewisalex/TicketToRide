package phase04_server.cs340.byu.persitenceproviderinterfaces.DAO;

public interface IChatHistoryDAO {
    void addChat(String chatHistoryJsonString, String gameID);
    void clear();
    void clearChatHistory(String gameID);
    String getChatHistoryJsonString(String gameID);
    String getAllChatHistories();
}
