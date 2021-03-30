package server;

import util.Config;
import util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server implements Runnable {
    private static Server instance = new Server();
    private boolean quit;
    private ServerSocket serverMessageSocket, clientMessageSocket;
    private List<Connection> connectionList;
    private ServerMessageController serverMessageController;

    private Server() {
        connectionList = new LinkedList();
        serverMessageController = new ServerMessageController();
    }

    @Override
    public void run() {
        try {
            // Setup server socket
            serverMessageSocket = new ServerSocket(Config.DEFAULT_SERVER_PORT);
            serverMessageSocket.setReuseAddress(true);
            clientMessageSocket = new ServerSocket(Config.DEFAULT_SERVER_PORT+1);
            clientMessageSocket.setReuseAddress(true);
            new ClientMessageSocketConnector().start();
            Logger.log("Server listening on " + serverMessageSocket.getInetAddress().getHostAddress() + ":" + Config.DEFAULT_SERVER_PORT + "/" + (Config.DEFAULT_SERVER_PORT + 1), Logger.INFO);

            // Get new clients
            Socket clientSocket;
            while (!quit) {
                clientSocket = serverMessageSocket.accept();
                addServerMessageConnection(clientSocket);
                Logger.log("New client connected (" + clientSocket.getInetAddress().getHostAddress() + ")", Logger.INFO);
            }
        } catch (IOException e) {
            Logger.log("Server crashed unexpectedly", Logger.ERROR);
            e.printStackTrace();
        } finally {
            quit = true;
            // Clean up server socket
            if (serverMessageSocket != null) {
                try {
                    serverMessageSocket.close();
                } catch (IOException e) {
                    Logger.log("Server cleanup failed", Logger.ERROR);
                }
            }
        }
        Logger.log("Server shut down", Logger.INFO);
    }

    public void addServerMessageConnection(Socket clientSocket) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        int playerId = Integer.parseInt(br.readLine());
        connectionList.add(new Connection(clientSocket, playerId));
    }

    public void assignClientMessageConnection(Socket clientSocket) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        int playerId = Integer.parseInt(br.readLine());
        while (true) {
            for (Connection c :
                    connectionList) {
                if (c.addClientMessageSocket(playerId, clientSocket))
                    return;
            }
        }
    }

    public static void main(String[] args) {
        Server.getInstance().run();
    }

    public static Server getInstance() {
        return instance;
    }

    class ClientMessageSocketConnector extends Thread {
        @Override
        public void run() {
            try {
                boolean found;
                Socket clientSocket;
                while (true) {
                    clientSocket = clientMessageSocket.accept();
                    assignClientMessageConnection(clientSocket);
                }
            } catch (IOException e) {
                Logger.log("ClientMessageSocketConnector crashed unexpectedly", Logger.ERROR);
                e.printStackTrace();
            } finally {
                quit = true;
                // Clean up server socket
                if (clientMessageSocket != null) {
                    try {
                        clientMessageSocket.close();
                    } catch (IOException e) {
                        Logger.log("Server cleanup failed", Logger.ERROR);
                    }
                }
            }
        }
    }
}
