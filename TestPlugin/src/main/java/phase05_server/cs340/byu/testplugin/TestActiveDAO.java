package phase05_server.cs340.byu.testplugin;

import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IActiveGameDAO;

public class TestActiveDAO implements IActiveGameDAO {


    @Override
    public void addActiveGame(String s, String s1) {
        System.out.println("TestActiveDAO: Inside addActiveGame()");
    }

    @Override
    public void clear() {
        System.out.println("TestActiveDAO: Inside clear()");
    }

    @Override
    public void clearActiveGame(String s) {
        System.out.println("TestActiveDAO: Inside clearActiveGame()");
    }

    @Override
    public String getActiveGameJsonString(String s) {
        System.out.println("TestActiveDAO: Inside getActiveGameJsonString()");
        return null;
    }
}
