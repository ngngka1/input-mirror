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
            System.out.println(targetDimension[0] + " " + targetDimension[1]); // for debug

            String prevPoll = "";
            MouseButtonListener mouseButtonListener = new MouseButtonListener();
            KeyboardListener keyboardListener = new KeyboardListener();
            CursorListener cursorListener = new CursorListener(targetDimension[0], targetDimension[1]);

            ListenerManager.init(mouseButtonListener, keyboardListener, cursorListener);

            System.out.println("Start sending inputs to target device");
            System.out.println("return because testing");
            if (true)
                return;
            while (!(InputProvider.getNonBlockingInput().equals("n"))) {
                String poll = ListenerManager.poll();
                if (!prevPoll.equals(poll))
                    out.println(poll);
                prevPoll = poll;
            }
        } catch (IOException e) {

        }
    }
}
