package phase05_server.cs340.byu.testplugin;

import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.ICommandDAO;

public class TestCommandDAO implements ICommandDAO {
    @Override
    public void addCommand(String s, String s1) {
        System.out.println("TestCommandDAO: Inside addCommand()");
    }

    @Override
    public void clearCommands(String s) {
        System.out.println("TestCommandDAO: Inside clearCommands()");
    }

    @Override
    public void clear() {
        System.out.println("TestCommandDAO: Inside clear()");
    }

    @Override
    public String getCommands(String s) {
        System.out.println("TestCommandDAO: Inside getCommands()");
        return null;
    }
}
