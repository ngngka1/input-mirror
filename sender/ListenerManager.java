package sender;

import com.github.kwhat.jnativehook.GlobalScreen;

import java.util.List;

public class ListenerManager {
    private static final long POLLING_RATE = 500; // Polling rate in hz
    private static final long POLLING_INTERVAL = 1_000_000_000 / POLLING_RATE; // how many nanoseconds for one poll
    private static long lastPollTime;

    private static final int BOTH_FLAG = 0;
    private static final int RECEIVER_FLAG = 1;
    private static final int SENDER_FLAG = 2;

    private int mouseFlag = 0; // indicate which device the mouse will control, (0=both, 1=receiver, 2=sender)
    private int keyboardFlag = 0; // indicate which device the keyboard will control, (0=both, 1=receiver, 2=sender)

    private MouseButtonListener mouseButtonListener;
    private MouseScrollListener mouseScrollListener;
    private KeyboardListener keyboardListener;
    private CursorListener cursorListener;


    // synchronization is not necessary for these two flags
    public void setMouseFlag(int mouseFlag) {
        this.mouseFlag = mouseFlag;
    }

    public int getMouseFlag() {
        return mouseFlag;
    }

    public void setKeyboardFlag(int keyboardFlag) {
        this.keyboardFlag = keyboardFlag;
    }

    public int getKeyboardFlag() {
        return keyboardFlag;
    }

    public ListenerManager(MouseButtonListener mouseButtonListener, MouseScrollListener mouseScrollListener, KeyboardListener keyboardListener, CursorListener cursorListener) {
        try {
            // Register the global key listener
            GlobalScreen.registerNativeHook();
            System.out.println("Global hook registered, listening for key events...");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        lastPollTime = System.nanoTime();

        this.mouseButtonListener = mouseButtonListener;
        this.mouseScrollListener = mouseScrollListener;
        this.keyboardListener = keyboardListener;
        this.cursorListener = cursorListener; // no need to add this to global screen

        GlobalScreen.addNativeMouseListener(this.mouseButtonListener);
        GlobalScreen.addNativeMouseWheelListener(this.mouseScrollListener);
        GlobalScreen.addNativeKeyListener(this.keyboardListener);
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
}
