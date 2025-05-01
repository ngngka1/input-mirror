package utils;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import controller.DeviceController;
import receiver.ReceiverController;
import sender.ListenerManager;
import sender.SenderController;
import types.HotkeyAction;

import java.util.HashMap;


public class HotkeyManager {
    // Note: mappings refer to which action key user has to press to invoke an action,
    // after pressing the modifier keys (Ctrl+Shift by default)
    private static int HOTKEY_MODIFIER_MASK = NativeKeyEvent.CTRL_MASK;
    private static final HashMap<HotkeyAction, Integer> mappings;


    private static DeviceController controllerInstance = null;

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
        mappings.put(HotkeyAction.TERMINATE_CONNECTION, NativeKeyEvent.VC_N);
    }
    public static int getMapping(HotkeyAction action) {
        return mappings.getOrDefault(action, -1);
    }

    public static void hook(DeviceController controllerInstance) {
        HotkeyManager.controllerInstance = controllerInstance;
    }

    public static void invokeHotkey(HotkeyAction hotkeyAction) {
        if (controllerInstance == null) {
            System.out.println("listenerManager has to be hooked to the HotkeyManager Class for hotkeys to work with running listeners!");
            return
        }
        if (hotkeyAction == HotkeyAction.TERMINATE_CONNECTION) {
            controllerInstance.terminate();
            return;
        }

        if (controllerInstance instanceof SenderController) {
            SenderController x = ((SenderController) controllerInstance);
            if (hotkeyAction == HotkeyAction.TOGGLE_MOUSE) {
                x.setMouseFlag((x.getMouseFlag() + 1) % 3);
            } else if (hotkeyAction == HotkeyAction.TOGGLE_KEYBOARD) {
                x.setKeyboardFlag((x.getKeyboardFlag() + 1) % 3);
            }
        } else {
            ReceiverController x = ((ReceiverController) controllerInstance);

        }

        // may change this
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
