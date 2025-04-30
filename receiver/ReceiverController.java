package receiver;

import utils.InputProvider;
import utils.LatestInputProvider;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceiverController {
    private static final double screenWidth;
    private static final double screenHeight;
    static {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.getWidth();
        screenHeight = screenSize.getHeight();
    }
    public void start(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            ControllerManager.init();
            out.println("config:"+ screenWidth + ',' + screenHeight);

            LatestInputProvider dirtyReader = new LatestInputProvider(in);
            new Thread(dirtyReader).start();

            while (!clientSocket.isClosed() && !(InputProvider.getNonBlockingInput().equals("n"))) {
                String data = dirtyReader.readLatestLine();
                if (data == null) {continue;}
                if (data.startsWith("d:")) {
                    ControllerManager.redirect(data.substring(2));
                } else if(data.equals("END")) {
                    clientSocket.close();
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            System.out.println();
            System.out.println("Connection terminated.");
        }
    }
}
