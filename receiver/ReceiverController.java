package receiver;

import utils.InputProvider;

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
            out.write("ACK"); // tells sender that the socket is now ok
            out.write("config:"+ screenWidth + ',' + screenHeight);

            while (!(InputProvider.getNonBlockingInput().equals("n"))) {
                String data = in.readLine();
                if (data != null && data.startsWith("d:")) {
                    ControllerManager.redirect(data.substring(2));
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
