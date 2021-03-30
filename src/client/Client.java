package client;

public class Client implements Runnable {

    private static Client instance = new Client();

    private Client() {

    }

    @Override
    public void run() {

    }

    public static Client getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        getInstance().run();
    }
}
