package sender;

import com.github.kwhat.jnativehook.GlobalScreen;

public class ListenerManager {
    private static final long POLLING_RATE = 500; // Polling rate in hz
    private static final long POLLING_INTERVAL = 1000_000_000 / POLLING_RATE; // Interval in ns
    private static long lastPollTime;


    private static MouseButtonListener mouseButtonListener;
    private static MouseScrollListener mouseScrollListener;
    private static KeyboardListener keyboardListener;
    private static CursorListener cursorListener;

    public static void init(MouseButtonListener mouseButtonListener, MouseScrollListener mouseScrollListener, KeyboardListener keyboardListener, CursorListener cursorListener) {
        try {
            // Register the global key listener
            GlobalScreen.registerNativeHook();
            System.out.println("Global hook registered, listening for key events...");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        lastPollTime = 0;
        ListenerManager.mouseButtonListener = mouseButtonListener;
        ListenerManager.mouseScrollListener = mouseScrollListener;
        ListenerManager.keyboardListener = keyboardListener;
        ListenerManager.cursorListener = cursorListener; // no need to add this to global screen

        GlobalScreen.addNativeMouseListener(ListenerManager.mouseButtonListener);
        GlobalScreen.addNativeMouseWheelListener(ListenerManager.mouseScrollListener);
        GlobalScreen.addNativeKeyListener(ListenerManager.keyboardListener);
    }

    public static String poll() {
        long startTime = System.nanoTime();
        int[] cursorPos = cursorListener.getPos(); // cursor position
        int mbMask = mouseButtonListener.getButtonMask(); // mouse button mask
        int scrollRotations = mouseScrollListener.getRotations();
        // keyboard wip (considering scalability)

        long elapsedTime = System.nanoTime() - startTime;
        long sleepTime = POLLING_INTERVAL - elapsedTime;
        try {
            Thread.sleep(sleepTime / 1_000_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            lastPollTime = System.nanoTime();
        }
        return "d:" + cursorPos[0] + "," + cursorPos[1] + ":" + mbMask + ":" + scrollRotations;
    }
}
