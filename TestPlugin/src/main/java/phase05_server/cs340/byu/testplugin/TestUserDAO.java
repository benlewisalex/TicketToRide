package phase05_server.cs340.byu.testplugin;

import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IUserDAO;

public class TestUserDAO implements IUserDAO {
    @Override
    public void addUsers(String s) {
        System.out.println("TestUserDAO: Inside addUsers()");
    }

    @Override
    public void clear() {
        System.out.println("TestUserDAO: Inside clear()");
    }

    @Override
    public String getUserListJsonString() {
        System.out.println("TestUserDAO: Inside getUserListJsonString()");
        return null;
    }
}
