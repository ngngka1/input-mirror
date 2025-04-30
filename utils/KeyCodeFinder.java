package utils;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class KeyCodeFinder {

    public static int getKeyCodeByText(String keyText) {
        for (int i = 0; i < NativeKeyEvent.getKeyText(i).length(); i++) {
            String currentKeyText = NativeKeyEvent.getKeyText(i);
            if (currentKeyText.equalsIgnoreCase(keyText)) {
                return i; // Return the key code if there's a match
            }
        }
        return -1; // Return -1 if the key text is not found
    }


}