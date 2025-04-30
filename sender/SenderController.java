package sender;

import utils.InputProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SenderController {
    public void start(Socket clientSocket) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("ACK"); // tells receiver that the sender is now ready to send data through connection
            System.out.println("waiting for config data from target device...");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String configResponse = in.readLine().split("[:]")[1];
            String[] dimensionResponse = configResponse.split("[,]");
            double[] targetDimension =  {1920.0, 1080.0};
            try {
                targetDimension[0] = Double.parseDouble(dimensionResponse[0]);
                targetDimension[1] = Double.parseDouble(dimensionResponse[1]);
            } catch (NumberFormatException e) {

            }
            System.out.println("received config data from target device");

            String prevPoll = "";
            MouseButtonListener mouseButtonListener = new MouseButtonListener();
            MouseScrollListener mouseScrollListener = new MouseScrollListener();
            KeyboardListener keyboardListener = new KeyboardListener();
            CursorListener cursorListener = new CursorListener(targetDimension[0], targetDimension[1]);

            ListenerManager.init(mouseButtonListener, mouseScrollListener, keyboardListener, cursorListener);

            System.out.println();
            System.out.println("Start sending inputs to target device");
            while (!(InputProvider.getNonBlockingInput().equals("n"))) {
                String poll = ListenerManager.poll();
                if (!prevPoll.equals(poll)) {
//                    System.out.println(poll); // for debug
                    out.println(poll);
                    prevPoll = poll;
                }
            }
            out.println("END");
            clientSocket.close();
        } catch (IOException e) {
            System.out.println();
            System.out.println("Connection terminated.");
        }
    }
}
