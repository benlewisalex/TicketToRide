package sql;

import Command.Command;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IActiveGameDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IChatHistoryDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.ICommandDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IGameDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IPersistanceProvider;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IUserDAO;

public class PersistenceProvider implements IPersistanceProvider {
//    private static PersistenceProvider instance;
    public PersistenceProvider() {

    }

//    public static PersistenceProvider getInstance() {
//        if (instance == null) {
//            instance = new PersistenceProvider();
//        }
//        return instance;
//    }

    public void clear() {
        UserDAO userDao = (UserDAO) getUserDAO();
        GameDAO gameDao = (GameDAO) getGameDAO();
        ActiveGameDAO activeGameDao = (ActiveGameDAO) getActiveGameDAO();
        ChatDAO chatDao = (ChatDAO) getChatHistoryDAO();
        CommandDAO commandDao = (CommandDAO) getCommandDAO();
        userDao.clear();
        gameDao.clear();
        activeGameDao.clear();
        chatDao.clear();
        commandDao.clear();
    }

    public void startTransaction() {

    }

    public void endTransaction() {

    }

    public IUserDAO getUserDAO() {
//        System.out.println("Calling getUserDAO");
        return UserDAO.getInstance();
    }

    public IGameDAO getGameDAO() {
        return GameDAO.getInstance();
    }

    public IActiveGameDAO getActiveGameDAO() {
        return ActiveGameDAO.getInstance();
    }

    public ICommandDAO getCommandDAO() {
        return CommandDAO.getInstance();
    }

    public IChatHistoryDAO getChatHistoryDAO() {
        return ChatDAO.getInstance();
    }
}
