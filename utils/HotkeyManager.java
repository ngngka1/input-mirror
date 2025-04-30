package utils;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import sender.ListenerManager;
import types.HotkeyAction;

import java.util.HashMap;

public class HotkeyManager {
    // Note: mappings refer to which action key user has to press to invoke an action,
    // after pressing the modifier keys (Ctrl+Shift by default)
    private static final HashMap<HotkeyAction, Integer> mappings;
    public static final HotkeyAction TOGGLE_MOUSE = new HotkeyAction("toggleMouse");
    public static final HotkeyAction TOGGLE_KEYBOARD = new HotkeyAction("toggleKeyboard");

    private static ListenerManager listenerManagerInstance = null;

    static {
        mappings = new HashMap<>();
//        mappings.put("toggleDevice", NativeKeyEvent.VC_D);
        mappings.put(TOGGLE_MOUSE, NativeKeyEvent.VC_M);
        mappings.put(TOGGLE_KEYBOARD, NativeKeyEvent.VC_K);
    }
    public static HashMap<HotkeyAction, Integer> getMappings() {
        return mappings;
    }

    public static void hook(ListenerManager listenerManager) {
        HotkeyManager.listenerManagerInstance = listenerManager;
    }

    public static void invokeHotkey(HotkeyAction hotkeyAction) {
        if (listenerManagerInstance == null) {
            System.out.println("listener manager has to be hooked to the Hotkey Class for hotkeys to work!");
            return;
        }

        // implementation that doesn't scale nice, might optimize in the future
        if (hotkeyAction == TOGGLE_MOUSE) {
            listenerManagerInstance.setMouseFlag((listenerManagerInstance.getMouseFlag() + 1) % 3);
        } else if (hotkeyAction == TOGGLE_KEYBOARD) {
            listenerManagerInstance.setKeyboardFlag((listenerManagerInstance.getKeyboardFlag() + 1) % 3);
        }
    }

    public static HotkeyAction getHotKeyAction(int actionKeyCode) {
        for (HotkeyAction key : mappings.keySet()) {
            if (mappings.get(key) == actionKeyCode) {
                return key;
            }
        }
        return null;
    }
}
