package sender;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

import java.io.PrintWriter;

public class KeyListener implements NativeKeyListener, NativeMouseListener {
    private static PrintWriter out;
    private static boolean keyLock;
    public static void init(PrintWriter out) {
        KeyListener.out = out;
        keyLock = false;
        try {
            // Register the global key listener
            GlobalScreen.registerNativeHook();
            System.out.println("Global hook registered, listening for key events...");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        KeyListener listener = new KeyListener();
        GlobalScreen.addNativeMouseListener(listener);
        GlobalScreen.addNativeKeyListener(listener);
    }
    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        // Check if the left mouse button was clicked
        if (e.getButton() == NativeMouseEvent.BUTTON1) { // BUTTON1 is the left mouse button
            KeyListener.out.println("MB1");
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        switch (e.getKeyCode()) {
            case NativeKeyEvent.VC_Z: {
                if (!keyLock) {
                    KeyListener.out.println("f7");
                    keyLock = true;
                }
                break;
            }
//            case NativeKeyEvent.VC_X: {
//                KeyListener.out.println("x");
//                break;
//            }
            case NativeKeyEvent.VC_ESCAPE: {
                KeyListener.out.println("escape");
                keyLock = false;
                break;
            }
//            case NativeKeyEvent.VC_BACKQUOTE: {
//                KeyListener.out.println("f6");
//                break;
//            }
        }
    }

//    @Override
//    public void nativeKeyReleased(NativeKeyEvent e) {
//        // Optional: Handle key release events
//    }
//
//    @Override
//    public void nativeKeyTyped(NativeKeyEvent e) {
//        // Optional: Handle key typed events
//    }
}
