package client;

import java.io.*;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public Connection() {

    }

    public void connect(String address, int port) throws IOException {
        socket = new Socket(address, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        printWriter.println(Client.getInstance().getClientId());
        printWriter.flush();
        printWriter.println("Hello there");
        printWriter.flush();
    }
}
