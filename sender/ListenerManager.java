package sender;

import com.github.kwhat.jnativehook.GlobalScreen;

public class ListenerManager {
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
        ListenerManager.mouseButtonListener = mouseButtonListener;
        ListenerManager.mouseScrollListener = mouseScrollListener;
        ListenerManager.keyboardListener = keyboardListener;
        ListenerManager.cursorListener = cursorListener; // no need to add this to global screen

        GlobalScreen.addNativeMouseListener(ListenerManager.mouseButtonListener);
        GlobalScreen.addNativeMouseWheelListener(ListenerManager.mouseScrollListener);
        GlobalScreen.addNativeKeyListener(ListenerManager.keyboardListener);
    }

    public static String poll() {
        int[] cursorPos = cursorListener.getPos(); // cursor position
        int mbMask = mouseButtonListener.getButtonMask(); // mouse button mask
        int scrollRotations = mouseScrollListener.getRotations();
        // keyboard wip (considering scalability)

        return "d:" + cursorPos[0] + "," + cursorPos[1] + ":" + mbMask + ":" + scrollRotations;
    }
}
