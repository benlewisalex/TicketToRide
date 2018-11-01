package MongoPersistenceProvider;

import MongoDAO.ActiveGameMongoDAO;
import MongoDAO.ChatHistoryMongoDAO;
import MongoDAO.CommandMongoDAO;
import MongoDAO.GameMongoDAO;
import MongoDAO.UserMongoDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IActiveGameDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IChatHistoryDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.ICommandDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IGameDAO;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IPersistanceProvider;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IUserDAO;

public class MongoPersistenceProvider implements IPersistanceProvider {

    @Override
    public void clear() {
        ActiveGameMongoDAO activeGameDAO = new ActiveGameMongoDAO();
        ChatHistoryMongoDAO chatHistoryDAO = new ChatHistoryMongoDAO();
        CommandMongoDAO commandDAO = new CommandMongoDAO();
        GameMongoDAO gameDAO = new GameMongoDAO();
        UserMongoDAO userDAO = new UserMongoDAO();
        activeGameDAO.clear();
        chatHistoryDAO.clear();
        commandDAO.clear();
        gameDAO.clear();
        userDAO.clear();
    }

    @Override
    public void startTransaction() {
        //unnecessary in this plugin
    }

    @Override
    public void endTransaction() {
        //unnecessary in this plugin
    }

    @Override
    public IUserDAO getUserDAO() {
        return new UserMongoDAO();
    }

    @Override
    public IGameDAO getGameDAO() {
        return new GameMongoDAO();
    }

    @Override
    public IActiveGameDAO getActiveGameDAO() {
        return new ActiveGameMongoDAO();
    }

    @Override
    public ICommandDAO getCommandDAO() {
        return new CommandMongoDAO();
    }

    @Override
    public IChatHistoryDAO getChatHistoryDAO() {
        return new ChatHistoryMongoDAO();
    }
}
