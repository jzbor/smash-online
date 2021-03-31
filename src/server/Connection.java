package server;

import exceptions.IllegalMessageException;
import model.Message;
import org.json.simple.parser.ParseException;
import util.Logger;

import java.io.*;
import java.net.Socket;

public class Connection extends Thread {
    // @TODO add cleanup
    private int clientId;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public Connection(int clientId) {
        this.clientId = clientId;
    }

    public void addSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));
        this.printWriter = new PrintWriter(new OutputStreamWriter(this.outputStream));
        Logger.log("Added socket for " + clientId + " (" + this.getId() + ")");
    }

    @Override
    public void run() {
        try {
            Message message;
            while (true) {
                while (bufferedReader.ready()) {
                    String nextLine = bufferedReader.readLine();
                    Logger.log(clientId + ": " + nextLine, Logger.DEBUG);
                    try {
                        message = Message.deserialize(nextLine);
                        Logger.log("Received message of type " + message.type + " from " + message.senderId);
                    } catch (Exception e) {
                        Logger.log("Unable to parse message '" + nextLine + "'", Logger.ERROR);
                        Logger.log("Reason: " + e + ": " + e.getMessage(), Logger.ERROR);
                    }
                }
            }
        } catch (IOException e) {
            Logger.log("Connection failed: " + clientId, Logger.ERROR);
            // @TODO add cleanup
        }
    }
}
