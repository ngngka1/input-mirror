package receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
    private final int port; // Port of the receiver
    private static String[] parseData(String data) {
        return data.split("[|]");
    }
    public Receiver() {
        this(443);
    }

    public Receiver(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Listening for mouse data on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String data = in.readLine();
                    if (data != null) {
                        Controller.redirect(parseData(data));
                    }
                } catch (IOException e) {
                    System.err.println("Error while processing client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port + ": " + e.getMessage());
        }
    }
}
