package client;

import util.Config;
import util.Logger;

import java.io.IOException;
import java.util.Random;

public class Client implements Runnable {

    private static Client instance = new Client();
    private final int clientId;
    private Connection connection;

    private Client() {
        Logger.log(instance + ", " + getInstance());
        connection = new Connection();
        clientId = Math.abs(new Random().nextInt());
    }

    @Override
    public void run() {
        // connecting to server
        try {
            connection.connect("localhost", Config.DEFAULT_SERVER_PORT);
        } catch (IOException e) {
            Logger.log("Unable to connect to server", Logger.ERROR);
            e.printStackTrace();
        }
    }

    public int getClientId() {
        return clientId;
    }

    public static Client getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        getInstance().run();
    }
}
