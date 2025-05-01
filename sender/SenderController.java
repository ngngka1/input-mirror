package sender;

import com.github.kwhat.jnativehook.GlobalScreen;
import controller.DeviceController;
import types.CustomBufferedReader;
import utils.ThreadedScanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class SenderController extends DeviceController {

    private static final long POLLING_RATE = 500; // Polling rate in hz
    private static final long POLLING_INTERVAL = 1_000_000_000 / POLLING_RATE; // how many nanoseconds for one poll
    private static long lastPollTime;

    private static final int BOTH_FLAG = 0;
    private static final int RECEIVER_FLAG = 1;
    private static final int SENDER_FLAG = 2;
    private final InputBlocker localInputBlocker;

    private int mouseFlag = 0; // indicate which device the mouse will control, (0=both, 1=receiver, 2=sender)
    private int keyboardFlag = 0; // indicate which device the keyboard will control, (0=both, 1=receiver, 2=sender)

    private final MouseButtonListener mouseButtonListener;
    private final MouseScrollListener mouseScrollListener;
    private final KeyboardListener keyboardListener;
    private final CursorListener cursorListener;

    public SenderController(Socket clientSocket) {
        setClientSocket(clientSocket);
        try {
            setOutputWriter(new PrintWriter(clientSocket.getOutputStream(), true));
            setInputReader(new CustomBufferedReader(new InputStreamReader(clientSocket.getInputStream())));
        } catch (IOException e) {
            throw new RuntimeException("Error trying to obtain the I/O stream, stopping connection");
        }

        sendData("ACK"); // tells receiver that the sender is now ready to send data through connection
        System.out.println("waiting for config data from target device...");

        String configResponse;
        try {
            configResponse = readData().split("[:]")[1];
        } catch (IOException e) {
            throw new RuntimeException("Error while reading config, stopping connection");
        }
        String[] dimensionResponse = configResponse.split("[,]");
        double[] targetDimension =  {1920.0, 1080.0};
        try {
            targetDimension[0] = Double.parseDouble(dimensionResponse[0]);
            targetDimension[1] = Double.parseDouble(dimensionResponse[1]);
        } catch (NumberFormatException e) {
            throw new RuntimeException("config from receiver contains error: screen dimension should be decimal numbers (width,height)");
        }
        System.out.println("received config data from target device");

        try {
            // Register the global key listener
            GlobalScreen.registerNativeHook();
            System.out.println("Global hook registered, listening for key events...");
        } catch (Exception e) {
            throw new RuntimeException("Unable to register global hook, stopping connection");
        }
        this.mouseButtonListener = new MouseButtonListener();;
        this.mouseScrollListener = new MouseScrollListener();
        this.keyboardListener = new KeyboardListener();
        this.cursorListener = new CursorListener(targetDimension[0], targetDimension[1]); // no need to add this to global screen

        GlobalScreen.addNativeMouseListener(this.mouseButtonListener);
        GlobalScreen.addNativeMouseWheelListener(this.mouseScrollListener);
        GlobalScreen.addNativeKeyListener(this.keyboardListener);

        localInputBlocker = new InputBlocker();
        lastPollTime = System.nanoTime();
    }

    // synchronization is not necessary for these two flags
    public void setMouseFlag(int mouseFlag) {
        switch (mouseFlag) {
            case BOTH_FLAG: {
                System.out.println("Mouse input is now on both device");
                localInputBlocker.enableMouseInput();
                break;
            }
            case RECEIVER_FLAG: {
                System.out.println("Mouse input is now exclusively on receiver's device");
                localInputBlocker.disableMouseInput();
                break;
            }
            case SENDER_FLAG: {
                System.out.println("Mouse input is now exclusively on sender's device");
                localInputBlocker.enableMouseInput();
                break;
            }
        }
        this.mouseFlag = mouseFlag;
    }

    public int getMouseFlag() {
        return mouseFlag;
    }

    public void setKeyboardFlag(int keyboardFlag) {
        switch (keyboardFlag) {
            case BOTH_FLAG: {
                System.out.println("Keyboard input is now on both device");
                localInputBlocker.enableKeyboardInput();
                break;
            }
            case RECEIVER_FLAG: {
                System.out.println("Keyboard input is now exclusively on receiver's device (WIP, won't work if the local device refocus somewhere else)");
                localInputBlocker.disableKeyboardInput();
                break;
            }
            case SENDER_FLAG: {
                System.out.println("Keyboard input is now exclusively on sender's device");
                localInputBlocker.enableKeyboardInput();
                break;
            }
        }
        this.keyboardFlag = keyboardFlag;
    }

    public int getKeyboardFlag() {
        return keyboardFlag;
    }

    public String mousePoll() {
        int[] cursorPos = cursorListener.getPos(); // cursor position
        int mbMask = mouseButtonListener.getButtonMask();  // mouse button mask
        int scrollRotations = mouseScrollListener.getRotations(); // mouse wheel rotation

        return cursorPos[0] + "," + cursorPos[1] + ":" + mbMask + ":" + scrollRotations;
    }

    public String keyboardPoll() {
        List<Integer> keys = keyboardListener.getKeys();
        StringBuilder pollBuilder = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            pollBuilder.append(keys.get(i));
            if (i + 1 < keys.size()) {
                pollBuilder.append(',');
            }
        }
        return pollBuilder.toString();
    }

    public String poll() {
        String mouseData = "m:" + (mouseFlag != SENDER_FLAG ? mousePoll() : "");
        String keyboardData = "k:" + (keyboardFlag != SENDER_FLAG ? keyboardPoll() : "");

        long elapsedTimeNs = System.nanoTime() - lastPollTime; // in ns
        long sleepTimeNs = (POLLING_INTERVAL - elapsedTimeNs) / 1_000_000; // in ns
        try {
            if (sleepTimeNs > 0)
                Thread.sleep(sleepTimeNs / 1_000_000, (int) sleepTimeNs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        lastPollTime = System.nanoTime();

        return mouseData + "|" + keyboardData;
    }

    public void start() {
        System.out.println();
        System.out.println("Start sending inputs to target device");
        String prevPoll = "";
        try {
            while (!isTerminated()) {
                String poll = this.poll();
                if (!prevPoll.equals(poll)) {
    //                    System.out.println(poll); // for debug
                    sendData(poll);
                    prevPoll = poll;
                }
                if (readDataNonBlocking().equals("END")) {
                    terminate();
                    break;
                }
            }
            sendData("END");
        } catch (IOException e) {

        }
        System.out.println();
        System.out.println("Connection terminated.");
    }
}