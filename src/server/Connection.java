package server;

import util.Logger;

import java.io.*;
import java.net.Socket;

public class Connection extends Thread {
    private int playerId;
    private Socket serverMessageSocket, clientMessageSocket;
    private InputStream serverMessageInputStream, clientMessageInputStream;
    private OutputStream serverMessageOutputStream, clientMessageOutputStream;
    private BufferedReader serverMessageBufferedReader, clientMessageBufferedReader;
    private PrintWriter serverMessagePrintWriter, clientMessagePrintWriter;

    public Connection(Socket serverMessageSocket, int playerId) throws IOException {
        this.playerId = playerId;
        this.serverMessageSocket = serverMessageSocket;
        this.serverMessageInputStream = serverMessageSocket.getInputStream();
        this.serverMessageOutputStream = serverMessageSocket.getOutputStream();
        this.serverMessageBufferedReader = new BufferedReader(new InputStreamReader(this.serverMessageInputStream));
        this.serverMessagePrintWriter = new PrintWriter(new OutputStreamWriter(this.serverMessageOutputStream));
    }

    public boolean addClientMessageSocket(int playerId, Socket clientMessageSocket) throws IOException {
        if (playerId != this.playerId)
            return false;
        this.clientMessageSocket = clientMessageSocket;
        this.clientMessageInputStream = clientMessageSocket.getInputStream();
        this.clientMessageOutputStream = clientMessageSocket.getOutputStream();
        this.clientMessageBufferedReader = new BufferedReader(new InputStreamReader(this.clientMessageInputStream));
        this.clientMessagePrintWriter = new PrintWriter(new OutputStreamWriter(this.clientMessageOutputStream));
        return true;
    }

    @Override
    public void run() {
        try {
            while (true) {
                while (serverMessageBufferedReader.ready()) {
                    Logger.log(playerId + ": " + serverMessageBufferedReader.readLine(), Logger.DEBUG);
                }
                while (clientMessageBufferedReader.ready()) {
                    Logger.log(playerId + ": " + clientMessageBufferedReader.readLine(), Logger.DEBUG);
                }
            }
        } catch (IOException e) {
            Logger.log("Connection failed: " + playerId, Logger.ERROR);
            // @TODO add cleanup
        }
    }
}
