package receiver;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import types.HotkeyAction;
import utils.HotkeyManager;

public class HotkeyListener implements NativeKeyListener {

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        int modifier = e.getModifiers();
        int mask = HotkeyManager.getHotkeyModifierMask();
        // Note: only works if hotkey uses only one modifier (e.g. CTRL), have to check with additional logic for more modifiers
        if (modifier != 0 && (modifier & mask) == modifier) { // NativeKeyEvent.CTRL_MASK = 10 0010 (34), press ctrl-R = 10 0000 (32)
            HotkeyAction hotkeyAction = HotkeyManager.getHotKeyAction(keyCode);
            if (hotkeyAction != null) {
                HotkeyManager.invokeHotkey(hotkeyAction);
            }
        }
    }
}
