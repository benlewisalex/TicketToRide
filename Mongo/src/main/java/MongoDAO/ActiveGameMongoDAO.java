package MongoDAO;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import Tools.MongoEncoder;
import model.ActiveGame;
import model.ActiveGameList;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IActiveGameDAO;

public class ActiveGameMongoDAO implements IActiveGameDAO {

    private MongoClient mongoClient;
    MongoEncoder encoder;

    public ActiveGameMongoDAO(){
        initializeMongoClient();
        closeMongoClient();
        encoder = new MongoEncoder();
    }

    private void initializeMongoClient(){
        try{
            mongoClient = new MongoClient();
        }
        catch (UnknownHostException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void closeMongoClient(){
        mongoClient.close();
    }

    @Override
    public void addActiveGame(String activeGameJsonString, String gameID){
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("ActiveGames");

        DBObject activeGame = new BasicDBObject("gameID", gameID)
                .append("serializedActiveGame", activeGameJsonString);

        collection.insert(activeGame);

        closeMongoClient();
    }

    @Override
    public void clear(){
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("ActiveGames");

        BasicDBObject clearDocument = new BasicDBObject();

        collection.remove(clearDocument);

        closeMongoClient();
    }

    @Override
    public void clearActiveGame(String gameID){
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("ActiveGames");

        BasicDBObject clearDocument = new BasicDBObject("gameID",gameID);

        collection.remove(clearDocument);

        closeMongoClient();
    }

    @Override
    public String getActiveGameJsonString(String gameID){
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("ActiveGames");

        DBObject activeGame = new BasicDBObject("gameID",gameID);
        DBCursor cursor = collection.find(activeGame);
        DBObject returnedGame = cursor.one();
        String serializedActiveGame = (String) returnedGame.get("serializedActiveGame");

        closeMongoClient();
        return serializedActiveGame;
    }

    @Override
    public String getAllActiveGames(){
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("ActiveGames");

        DBCursor cursor = collection.find();

        List<ActiveGame> activeGameList = new ArrayList<>();

        while(cursor.hasNext()){
            DBObject returnedGame = cursor.next();
            String serializedActiveGame = (String) returnedGame.get("serializedActiveGame");
            ActiveGame activeGame = (ActiveGame) encoder.decode(serializedActiveGame, ActiveGame.class);
            activeGameList.add(activeGame);
        }

        ActiveGameList dtoActiveGameList = new ActiveGameList(activeGameList);

        String serializedActiveGameList = encoder.encode(dtoActiveGameList);

        closeMongoClient();
        return serializedActiveGameList;
    }
}
