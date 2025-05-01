package utils;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import sender.ListenerManager;
import types.HotkeyAction;

import java.util.HashMap;


public class HotkeyManager {
    // Note: mappings refer to which action key user has to press to invoke an action,
    // after pressing the modifier keys (Ctrl+Shift by default)
    private static int HOTKEY_MODIFIER_MASK = NativeKeyEvent.CTRL_MASK;
    private static final HashMap<HotkeyAction, Integer> mappings;


    private static ListenerManager listenerManagerInstance = null;

    public static int getHotkeyModifierMask() {
        return HOTKEY_MODIFIER_MASK;
    }

    public static String getHotkeyText(HotkeyAction action) {
        int x = getMapping(action);
        if (x < 0) {
            throw new IllegalArgumentException("Unrecognized hotkey action!");
        }

        return NativeKeyEvent.getModifiersText(HOTKEY_MODIFIER_MASK) + "+" + NativeKeyEvent.getKeyText(x);
    }

    static {
        mappings = new HashMap<>();
//        mappings.put("toggleDevice", NativeKeyEvent.VC_D);
        mappings.put(HotkeyAction.TOGGLE_MOUSE, NativeKeyEvent.VC_M);
        mappings.put(HotkeyAction.TOGGLE_KEYBOARD, NativeKeyEvent.VC_K);
    }
    public static int getMapping(HotkeyAction action) {
        return mappings.getOrDefault(action, -1);
    }

    public static void hook(ListenerManager listenerManager) {
        HotkeyManager.listenerManagerInstance = listenerManager;
    }

    public static void invokeHotkey(HotkeyAction hotkeyAction) {
        if (listenerManagerInstance == null) {
            System.out.println("listener manager has to be hooked to the Hotkey Class for hotkeys to work!");
            return;
        }

        // may change this
        if (hotkeyAction == HotkeyAction.TOGGLE_MOUSE) {
            listenerManagerInstance.setMouseFlag((listenerManagerInstance.getMouseFlag() + 1) % 3);
        } else if (hotkeyAction == HotkeyAction.TOGGLE_KEYBOARD) {
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
