package phase05_server.cs340.byu.testplugin;

import com.google.gson.Gson;

import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IActiveGameDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IChatHistoryDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.ICommandDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IGameDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IPersistanceProvider;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IUserDAO;


public class PluginTest implements IPersistanceProvider {

    private static jsonTest test1;
    private static jsonTest test2;


    public static void main(String[] args) {
        test1 = new jsonTest("happy", "hoop.da","yippee");
        test2 = new jsonTest("happy2", "hoop.da.2", "yippee.2");

        jsonTest[] testArray = {test1, test2};

        Gson gson = new Gson();
        String jsonString = gson.toJson(testArray, jsonTest[].class);
        System.out.println(jsonString);
    }

    public static class jsonTest{
        public String plugin_name;
        public String jar_file_path;
        public String class_path;

        public jsonTest(String plugin_name, String jar_file_path, String class_path) {
            this.plugin_name = plugin_name;
            this.jar_file_path = jar_file_path;
            this.class_path = class_path;
        }
    }

//    @Override
    public void clear() {
        System.out.println("PluginTest: Inside clear()");
    }

//    @Override
    public void startTransaction() {
        System.out.println("PluginTest: Inside startTransaction()");
    }

//    @Override
    public void endTransaction() {
        System.out.println("PluginTest: Inside endTransaction()");
    }

//    @Override
    public IUserDAO getUserDAO() {
        System.out.println("PluginTest: Inside getUserDAO()");
        return new TestUserDAO();
    }

//    @Override
    public IGameDAO getGameDAO() {
        System.out.println("PluginTest: Inside getGameDAO()");
        return new TestGameDAO();
    }

//    @Override
    public IActiveGameDAO getActiveGameDAO() {
        System.out.println("PluginTest: Inside getActiveGameDAO()");
        return new TestActiveDAO();
    }

//    @Override
    public ICommandDAO getCommandDAO() {
        System.out.println("PluginTest: Inside getCommandDAO()");
        return new TestCommandDAO();
    }

//    @Override
    public IChatHistoryDAO getChatHistoryDAO() {
        System.out.println("PluginTest: Inside getChatHistoryDAO()");
        return new TestChatHistoryDAO();
    }
}
