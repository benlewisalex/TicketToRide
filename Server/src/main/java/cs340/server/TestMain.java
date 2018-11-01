package cs340.server;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import Command.ICommand;
import Command.Command;
import Utils.PlayerColor;
import model.User;

/**
 * Created by benle on 7/7/2018.
 *
 */

public class TestMain {
    public static void main(String args[]) throws UnknownHostException{
        User user = new User("chuck", "asdf", null);
        user.setPlayerColor(PlayerColor.GREEN);
        Gson gson = new Gson();
        String userJSON = gson.toJson(user);
        User user1 = gson.fromJson(userJSON, User.class);
        System.out.println(user1.getPlayerColor());

        MongoClient mongoClient = new MongoClient();
        DB database = mongoClient.getDB("TestDB3");
        DBCollection collection = database.getCollection("TestCollection");

        BasicDBObject document = new BasicDBObject();

        // Delete All documents from collection Using blank BasicDBObject
        collection.remove(document);


        List<Integer> books = Arrays.asList(27464, 747854);
        DBObject person = new BasicDBObject("_id", "jo")
                .append("name", "Jo Bloggs")
                .append("address", new BasicDBObject("street", "123 Fake St")
                        .append("city", "Faketon")
                        .append("state", "MA")
                        .append("zip", 12345))
                .append("books", books);

        collection.insert(person);

        DBObject query = new BasicDBObject("_id", "jo");
        DBCursor cursor = collection.find(query);
        DBObject jo = cursor.one();
        String name = (String) jo.get("name");
        BasicDBObject addressObj = (BasicDBObject) jo.get("address");
        String address = (String) addressObj.get("street");
        String city = (String) addressObj.get("city");
        String state = (String) addressObj.get("state");
        List<Integer> bookList = (List<Integer>) jo.get("books");
        System.out.println(name);
        System.out.println(address);
        System.out.println(city);
        System.out.println(state);
        for(Integer book: books){
            System.out.println(book);
        }
        mongoClient.close();



//  Class<?>[] paramTypes = new Class<?>[2]//        paramTypes[0] = String.class;
//        paramTypes[1] = String.class;
//        Object[] params = new Object[2];
//        params[0] = "balexander";
//        params[1] = "abc.123";
//        String method = "register";
//        String authToken = "";
//        for(int i = 0; i < 5; i++){
//            Command command = new Command("cs340.server.model.ServerModel", method, paramTypes, params);
//            try{
//                System.out.println("Method: " + method + " Username: " + params[0] + " Password: " + params[1]);
//                List<ICommand> commands = command.execute();
//                Command theCommand = (Command) commands.get(0);
//                if(theCommand.getParams()[0] == null){
//                    System.out.println(theCommand.getParams()[1] + "\n");
//                }
//                else{
//                    System.out.println(theCommand.getParams()[0] + "\n");
//                    authToken = (String) theCommand.getParams()[0];
//                }
//                if(i == 1){
//                    method = "login";
//                }
//                if(i == 2){
//                    params[1] = "abc123";
//                }
//                if(i == 3){
//                    params[0] = "balexanders";
//                    params[1] = "abc.123";
//                }
//            }
//            catch(Exception e){
//                e.printStackTrace();
//                System.out.println(e.getMessage());
//            }
//        }
//        Class<?>[] paramTypes2 = new Class<?>[4];
//        paramTypes2[0] = String.class;
//        paramTypes2[1] = String.class;
//        paramTypes2[2] = PlayerColor.class;
//        paramTypes2[3] = int.class;
//        Object[] params2 = new Object[4];
//        params2[0] = "mygameID";
//        params2[1] = authToken;
//        params2[2] = new PlayerColor(0);
//        params2[3] = 5;
//        String method2 = "createGame";
//        for(int i = 0; i < 1; i++){
//            Command command = new Command("cs340.server.model.ServerModel", method2, paramTypes2, params2);
//            try{
//                List<ICommand> commands = command.execute();
//                Command theCommand = (Command) commands.get(0);
//            }
//            catch(Exception e){
//                e.printStackTrace();
//                System.out.println(e.getMessage());
//            }
//        }
//
//
//        Class<?>[] paramTypes3 = new Class<?>[3];
//        paramTypes3[0] = String.class;
//        paramTypes3[1] = String.class;
//        paramTypes3[2] = PlayerColor.class;
//        Object[] params3 = new Object[3];
//        params3[0] = "mygameID";
//        params3[1] = authToken;
//        params3[2] = new PlayerColor(1);
//        String method3 = "joinGame";
//        for(int i = 0; i < 1; i++){
//            Command command = new Command("cs340.server.model.ServerModel", method3, paramTypes3, params3);
//            try{
//                List<ICommand> commands = command.execute();
//                Command theCommand = (Command) commands.get(0);
//            }
//            catch(Exception e){
//                e.printStackTrace();
//                System.out.println(e.getMessage());
//            }
//        }

    }
}
