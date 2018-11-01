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
import model.User;
import model.UserList;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IUserDAO;

public class UserMongoDAO implements IUserDAO {

    private MongoClient mongoClient;
    MongoEncoder encoder;

    public UserMongoDAO() {
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
    public void addUsers(String userListJsonString) {
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("Users");

        DBObject user = new BasicDBObject("users", userListJsonString);

        collection.insert(user);

        closeMongoClient();
    }

    @Override
    public void clear() {
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("Users");

        BasicDBObject clearDocument = new BasicDBObject();

        collection.remove(clearDocument);

        closeMongoClient();
    }

    @Override
    public String getUserListJsonString() {
        initializeMongoClient();
        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("Users");
        DBCursor cursor = collection.find();

        DBObject userListObject = cursor.one();

        closeMongoClient();

        if(userListObject != null){
            return (String) userListObject.get("users");
        }
        else{
            List<User> users = new ArrayList<>();
            UserList userList = new UserList(users);
            return encoder.encode(userList);
        }
    }
}
