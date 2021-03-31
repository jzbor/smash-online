package server;

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
            while (true) {
                while (bufferedReader.ready()) {
                    Logger.log(clientId + ": " + bufferedReader.readLine(), Logger.DEBUG);
                }
            }
        } catch (IOException e) {
            Logger.log("Connection failed: " + clientId, Logger.ERROR);
            // @TODO add cleanup
        }
    }
}
