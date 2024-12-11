package sender;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender {
    private final String serverAddress; // Change to the receiver's IP address if needed
    private final int port; // Port of the receiver
    public Sender(String ipAddress, int port) {
        serverAddress = ipAddress;
        this.port = port;
    }

    public void start() {

        try (Socket socket = new Socket(serverAddress, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (true) {
                System.out.println("CursorPositionRetriever.get(): " + CursorPositionRetriever.get());
                out.println(CursorPositionRetriever.get());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
}
