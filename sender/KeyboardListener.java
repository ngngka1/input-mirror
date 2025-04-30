package sender;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import utils.HotkeyManager;
import types.HotkeyAction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class KeyboardListener implements NativeKeyListener {
    private final Queue<Integer> keysQueue;

    public KeyboardListener() {
        keysQueue = new LinkedList<>();
    }

    private void invokeHotkeyAction(HotkeyAction hotkeyAction) {

    }

    public List<Integer> getKeys() {
        Integer x;
        List<Integer> keys = new ArrayList<>();
        while ((x = keysQueue.poll()) != null) {
            keys.add(x);
        }
        return keys;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        if ((e.getModifiers() & (NativeKeyEvent.CTRL_MASK | NativeKeyEvent.SHIFT_MASK)) == 0) {
            HotkeyAction hotkeyAction = HotkeyManager.getHotKeyAction(keyCode);
            if (hotkeyAction != null) {
                invokeHotkeyAction(hotkeyAction);
            }
        } else {
            keysQueue.add(keyCode);
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        keysQueue.add(-keyCode);
    }

//    @Override
//    public void nativeKeyTyped(NativeKeyEvent e) {
//        System.out.println("Key Typed: " + e.getKeyCode());
//    }
}
