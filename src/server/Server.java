package server;

import util.Config;
import util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable {
    // @TODO dont crash on single failed connection
    // @TODO handle two clients with the same id
    private static Server instance = new Server();
    private boolean quit;
    private ServerSocket serverSocket;
    private Map<Integer, Connection> connectionMap;

    private Server() {
        connectionMap = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            // Setup server socket
            serverSocket = new ServerSocket(Config.DEFAULT_SERVER_PORT);
            serverSocket.setReuseAddress(true);
            Logger.log("Server listening on " + serverSocket.getInetAddress().getHostAddress() + ":" + Config.DEFAULT_SERVER_PORT, Logger.INFO);

            // Get new clients
            Socket clientSocket;
            while (!quit) {
                clientSocket = serverSocket.accept();
                addConnection(clientSocket);
            }
        } catch (IOException e) {
            Logger.log("Server crashed unexpectedly", Logger.ERROR);
            e.printStackTrace();
        } finally {
            quit = true;
            // Clean up server socket
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    Logger.log("Server cleanup failed", Logger.ERROR);
                }
            }
        }
        Logger.log("Server shut down", Logger.INFO);
    }

    public synchronized int addConnection(Socket clientSocket) throws IOException {
        // read clientId from socket
        BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        while (!br.ready());
        String line = br.readLine();
        int clientId = Integer.parseInt(line);
        // create Connection if not already there
        if (!connectionMap.containsKey(clientId)) {
            connectionMap.put(clientId, new Connection(clientId));
            connectionMap.get(clientId).addSocket(clientSocket);
            connectionMap.get(clientId).start();
            Logger.log("New client connected (" + clientSocket.getInetAddress().getHostAddress() + " => " + clientId + ")", Logger.INFO);
        } else {
            // @TODO Add proper handling
            connectionMap.get(clientId).addSocket(clientSocket);
            Logger.log("New socket created with existing client connection: (" + clientSocket.getInetAddress().getHostAddress() + " => " + clientId + ")", Logger.WARNING);
        }

        return clientId;
    }

    public static void main(String[] args) {
        Server.getInstance().run();
    }

    public static Server getInstance() {
        return instance;
    }
}
