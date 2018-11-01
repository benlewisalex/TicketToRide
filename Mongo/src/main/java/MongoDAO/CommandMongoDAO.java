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

import Command.Command;
import Tools.MongoEncoder;
import model.ActiveGame;
import model.ActiveGameList;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.ICommandDAO;

public class CommandMongoDAO implements ICommandDAO {

    private MongoClient mongoClient;
    MongoEncoder encoder;

    public CommandMongoDAO(){
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
    public void addCommand(String jsonCommand, String gameID){
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("Commands");

        DBObject command = new BasicDBObject("gameID", gameID)
                .append("jsonCommand", jsonCommand);

        collection.insert(command);

        closeMongoClient();
    }

    @Override
    public void clearCommands(String gameID){
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("Commands");

        BasicDBObject clearDocument = new BasicDBObject("gameID",gameID);

        collection.remove(clearDocument);

        closeMongoClient();
    }

    @Override
    public void clear(){
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("Commands");

        BasicDBObject clearDocument = new BasicDBObject();

        collection.remove(clearDocument);

        closeMongoClient();
    }

    @Override
    public List<String> getCommands(String gameID){
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("Commands");

        DBCursor cursor = collection.find();

        List<String> commandList = new ArrayList<>();

        while(cursor.hasNext()){
            DBObject returnedCommand = cursor.next();
            String serializedCommand = (String) returnedCommand.get("jsonCommand");
            commandList.add(serializedCommand);
        }

        closeMongoClient();
        return commandList;
    }
}
