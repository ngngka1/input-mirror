package sender;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyboardListener implements NativeKeyListener {
//    @Override
//    public void nativeKeyPressed(NativeKeyEvent e) {
//        switch (e.getKeyCode()) {
//            case NativeKeyEvent.VC_Z: {
//                if (!keyLock) {
//                    KeyListener.out.println("f7");
//                    keyLock = true;
//                }
//                break;
//            }
////            case NativeKeyEvent.VC_X: {
////                KeyListener.out.println("x");
////                break;
////            }
//            case NativeKeyEvent.VC_ESCAPE: {
//                KeyListener.out.println("escape");
//                keyLock = false;
//                break;
//            }
////            case NativeKeyEvent.VC_BACKQUOTE: {
////                KeyListener.out.println("f6");
////                break;
////            }
//        }
//    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        // Optional: Handle key release events
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // Optional: Handle key typed events
    }
}
