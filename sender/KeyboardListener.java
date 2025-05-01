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
        int modifier = e.getModifiers();
//        System.out.println("keyCode: " + keyCode);
//        System.out.println("modifier: " + modifier);
        int mask = HotkeyManager.getHotkeyModifierMask();
        // Note: only works if hotkey uses only one modifier (e.g. CTRL), have to check with additional logic for more modifiers
        if (modifier != 0 && (modifier & mask) == modifier) { // NativeKeyEvent.CTRL_MASK = 10 0010 (34), press ctrl-R = 10 0000 (32)
            HotkeyAction hotkeyAction = HotkeyManager.getHotKeyAction(keyCode);
            if (hotkeyAction != null) {
                HotkeyManager.invokeHotkey(hotkeyAction);
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
