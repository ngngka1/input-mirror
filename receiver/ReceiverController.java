package receiver;

import controller.DeviceController;
import utils.MinimumDelayReader;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceiverController extends DeviceController {

    public ReceiverController(Socket clientSocket) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            setOutputWriter(new PrintWriter(clientSocket.getOutputStream(), true));
            MinimumDelayReader lossyReader = new MinimumDelayReader(in);
            new Thread(lossyReader).start();
            setInputReader(lossyReader);
        } catch (IOException e) {
            throw new RuntimeException("Error trying to obtain the I/O stream, stopping connection");
        }
        sendData("config:"+ screenWidth + ',' + screenHeight);

        try {
            Robot robot = new Robot();
            MouseController.init(robot);
            KeyboardController.init(robot);
        } catch (AWTException e) {
            throw new RuntimeException("Error while initializing keyboard/mouse controller, stopping connection");
        }
    }

    public void start() {
        try {
            while (!isTerminated()) {
                String data = readData();
                if (data == null) {continue;}
                if(data.equals("END")) {
                    terminate();
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
        } catch (IOException e) {
        }
        System.out.println();
        System.out.println("Connection terminated.");
    }
}
