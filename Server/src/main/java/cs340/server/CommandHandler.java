package cs340.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;

import Command.ICommand;
import Command.Command;

/**
 * Created by benle on 7/6/2018.
 */

public class CommandHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        Encoder encoder = new Encoder();
        try {
            InputStream requestBody = exchange.getRequestBody();
            String requestJson = encoder.readString(requestBody);
            Command command = (Command) encoder.decode(requestJson, Command.class);
            List<ICommand> commands = command.execute();
            String gameID = exchange.getRequestHeaders().getFirst("Authorization");
            if(!command.getMethodName().equals("updateCommandList") && !command.getMethodName().equals("getGames")) {
                DatabaseManager.getInstance().storeCommand(requestJson, gameID);
            }
            String responseJson = encoder.encode(commands);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            OutputStream respBody = exchange.getResponseBody();
            encoder.writeString(responseJson, respBody);
            respBody.close();
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        } catch (Exception e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            String errorMessage = e.getMessage();
            System.out.println(errorMessage);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
