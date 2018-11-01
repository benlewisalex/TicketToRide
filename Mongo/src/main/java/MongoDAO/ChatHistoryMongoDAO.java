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
import model.ChatHistory;
import model.ChatHistoryList;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IChatHistoryDAO;

public class ChatHistoryMongoDAO implements IChatHistoryDAO {

    private MongoClient mongoClient;
    MongoEncoder encoder;

    public ChatHistoryMongoDAO(){
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
    public void addChat(String chatJsonString, String gameID){
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("ChatHistories");

        DBObject chatHistory = new BasicDBObject("gameID", gameID)
                .append("serializedChatHistory", chatJsonString);

        collection.insert(chatHistory);

        closeMongoClient();
    }

    @Override
    public void clear(){
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("ChatHistories");

        BasicDBObject clearDocument = new BasicDBObject();

        collection.remove(clearDocument);

        closeMongoClient();
    }

    @Override
    public void clearChatHistory(String gameID){
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("ChatHistories");

        BasicDBObject clearDocument = new BasicDBObject("gameID",gameID);

        collection.remove(clearDocument);

        closeMongoClient();
    }

    @Override
    public String getChatHistoryJsonString(String gameID){
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("ChatHistories");

        DBObject chatHistory = new BasicDBObject("gameID",gameID);
        DBCursor cursor = collection.find(chatHistory);
        DBObject returnedHistory = cursor.one();
        String serializedChatHistory = (String) returnedHistory.get("serializedChatHistory");

        closeMongoClient();
        return serializedChatHistory;
    }

    @Override
    public String getAllChatHistories(){
        initializeMongoClient();

        DB database = mongoClient.getDB("TicketToRide");
        DBCollection collection = database.getCollection("ChatHistories");

        DBCursor cursor = collection.find();

        List<ChatHistory> chatHistoryList = new ArrayList<>();

        while(cursor.hasNext()){
            DBObject returnedHistory = cursor.next();
            String serializedChatHistory = (String) returnedHistory.get("serializedChatHistory");
            ChatHistory chatHistory = (ChatHistory) encoder.decode(serializedChatHistory, ChatHistory.class);
            chatHistoryList.add(chatHistory);
        }

        ChatHistoryList dtoChatHistoryList = new ChatHistoryList(chatHistoryList);

        String serializedChatHistoryList = encoder.encode(dtoChatHistoryList);

        closeMongoClient();
        return serializedChatHistoryList;
    }
}
