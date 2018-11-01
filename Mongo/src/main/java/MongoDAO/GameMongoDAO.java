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
import model.Game;
import model.GameList;
import model.User;
import model.UserList;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IGameDAO;

public class GameMongoDAO implements IGameDAO {

    private MongoClient mongoClient;
    MongoEncoder encoder;

    public GameMongoDAO() {
        initializeMongoClient();
        closeMongoClient();
        encoder = new MongoEncoder();
    }

    private void initializeMongoClient() {
        try {
            mongoClient = new MongoClient();
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void closeMongoClient() {
        mongoClient.close();
    }

    @Override
    public void addGameList(String gameListJsonString) {
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("Games");

        DBObject game = new BasicDBObject("games", gameListJsonString);

        collection.insert(game);

        closeMongoClient();
    }

    @Override
    public void clear() {
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("Games");

        BasicDBObject clearDocument = new BasicDBObject();

        collection.remove(clearDocument);

        closeMongoClient();
    }

    @Override
    public String getGameListJsonString() {
        initializeMongoClient();
        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("Games");
        DBCursor cursor = collection.find();

        DBObject gameListObject = cursor.one();

        closeMongoClient();

        if(gameListObject != null){
            return (String) gameListObject.get("games");
        }
        else{
            List<Game> games = new ArrayList<>();
            GameList gameList = new GameList(games);
            return encoder.encode(gameList);
        }
    }
}
