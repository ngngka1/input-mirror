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
            out.println("config:"+ screenWidth + ',' + screenHeight);

            LatestInputProvider lossyReader = new LatestInputProvider(in);
            new Thread(lossyReader).start();

            try {
                Robot robot = new Robot();
                MouseController.init(robot);
                KeyboardController.init(robot);
            } catch (AWTException e) {
                System.err.println("AWT exception");
                return;
            }

            while (!clientSocket.isClosed() && !(InputProvider.getNonBlockingInput().equals("n"))) {
                String data = lossyReader.readLatestLine();
                if (data == null) {continue;}
                if(data.equals("END")) {
                    clientSocket.close();
                    break;
                }

                String[] parsedData = data.split("[|]");
                if (parsedData.length == 2 && parsedData[0].startsWith("m:") && parsedData[1].startsWith("k:")) {
                    MouseController.control(parsedData[0].substring(2));
                    KeyboardController.control(parsedData[1].substring(2));
                }
                else {
                    System.out.println("Error in format of data received from sender");
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            System.out.println();
            System.out.println("Connection terminated.");
        }
    }
}
