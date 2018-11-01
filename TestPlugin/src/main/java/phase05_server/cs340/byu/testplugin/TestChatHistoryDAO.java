package phase05_server.cs340.byu.testplugin;

import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IChatHistoryDAO;

public class TestChatHistoryDAO implements IChatHistoryDAO {
    @Override
    public void addChat(String s, String s1) {
        System.out.println("TestChatHistoryDAO: Inside addChat()");
    }

    @Override
    public void clear() {
        System.out.println("TestChatHistoryDAO: Inside clear()");
    }

    @Override
    public void clearChatHistory(String s) {
        System.out.println("TestChatHistoryDAO: Inside clearChatHistory()");
    }

    @Override
    public String getChatHistoryJsonString(String s) {
        System.out.println("TestChatHistoryDAO: Inside getChatHistoryJsonString()");
        return null;
    }
}
