package cs340.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import cs340.server.model.ServerModel;
import phase04_server.cs340.byu.persitenceproviderinterfaces.DAO.IPersistanceProvider;


public class ServerCommunicator {

    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    private void run(String portNumber) {
        System.out.println("Initializing HTTP Server");
        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.setExecutor(null);
        System.out.println("Creating contexts");
        server.createContext("/cmd", new CommandHandler());
        System.out.println("Starting server");
        server.start();
        System.out.println("Server started");
        System.out.println(portNumber);
    }

    public static void main(String[] args) {
        String portNumber = args[0];
        String persistenceType = args[1];
        Integer numCmdsBetweenBackups = Integer.parseInt(args[2]);
        boolean clear = false;
        if(args.length > 3) {
            clear = true;
        }
        ServerModel.getInstance();

        IPersistanceProvider persistanceProvider = new LoadProvider().getProvider(persistenceType);

        DatabaseManager.getInstance().setPersistanceProvider(persistanceProvider);
        DatabaseManager.getInstance().setNumCmdsBetweenBackups(numCmdsBetweenBackups);

        if(clear) {
            DatabaseManager.getInstance().clear();
        }
        else{
            DatabaseManager.getInstance().serverInit();
        }

        new ServerCommunicator().run(portNumber);
    }
}
