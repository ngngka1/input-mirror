package types;

public class HotkeyAction {
    public static final HotkeyAction TOGGLE_MOUSE = new HotkeyAction("toggleMouse");
    public static final HotkeyAction TOGGLE_KEYBOARD = new HotkeyAction("toggleKeyboard");
    public static final HotkeyAction TERMINATE_CONNECTION = new HotkeyAction("terminateConnection");

    private final String text;

    public HotkeyAction(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }

}
