package phase05_server.cs340.byu.testplugin;

import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IGameDAO;

public class TestGameDAO implements IGameDAO {
    @Override
    public void addGameList(String s) {
        System.out.println("TestGameDAO: Inside addGameList()");
    }

    @Override
    public void clear() {
        System.out.println("TestGameDAO: Inside clear()");
    }

    @Override
    public String getGameListJsonString() {
        System.out.println("TestGameDAO: Inside getGameListJsonString()");
        return null;
    }
}
