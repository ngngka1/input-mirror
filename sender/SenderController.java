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
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String configResponse = in.readLine().split("[:]")[1];
            String[] dimensionResponse = configResponse.split("[,]");
            double[] targetDimension =  {1920.0, 1080.0};
            try {
                targetDimension[0] = Double.parseDouble(dimensionResponse[0]);
                targetDimension[1] = Double.parseDouble(dimensionResponse[1]);
            } catch (NumberFormatException e) {

            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String prevPoll = "";
            MouseButtonListener mouseButtonListener = new MouseButtonListener();
            KeyboardListener keyboardListener = new KeyboardListener();
            CursorListener cursorListener = new CursorListener(targetDimension[0], targetDimension[1]);

            ListenerManager.init(mouseButtonListener, keyboardListener, cursorListener);

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
