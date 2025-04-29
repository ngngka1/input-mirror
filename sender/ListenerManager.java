package sender;

import com.github.kwhat.jnativehook.GlobalScreen;

public class ListenerManager {
    private static CursorListener cursorListener;
    private static MouseButtonListener mouseButtonListener;
    private static KeyboardListener keyboardListener;

    public static void init(MouseButtonListener mouseButtonListener, KeyboardListener keyboardListener, CursorListener cursorListener) {
        try {
            // Register the global key listener
            GlobalScreen.registerNativeHook();
            System.out.println("Global hook registered, listening for key events...");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        ListenerManager.cursorListener = cursorListener;
        ListenerManager.mouseButtonListener = mouseButtonListener;
        ListenerManager.keyboardListener = keyboardListener;

        GlobalScreen.addNativeMouseListener(mouseButtonListener);
        GlobalScreen.addNativeKeyListener(keyboardListener);
    }

    public static String poll() {
        int[] cursorPos = cursorListener.getPos(); // cursor position
        int mbMask = mouseButtonListener.getButtonMask(); // mouse button mask
        // keyboard wip (considering scalability)

        return "d:" + cursorPos[0] + "," + cursorPos[1] + ":" + mbMask;
    }
}
