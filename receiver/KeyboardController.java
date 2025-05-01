package receiver;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyboardController {
    private static Robot robot;
    public static void init(Robot robot) {
        KeyboardController.robot = robot;
    }
    private static final Map<String, Integer> keyMap = new HashMap<>();

    static {
        keyMap.put("A", KeyEvent.VK_A);
        keyMap.put("B", KeyEvent.VK_B);
        keyMap.put("C", KeyEvent.VK_C);
        keyMap.put("D", KeyEvent.VK_D);
        keyMap.put("E", KeyEvent.VK_E);
        keyMap.put("F", KeyEvent.VK_F);
        keyMap.put("G", KeyEvent.VK_G);
        keyMap.put("H", KeyEvent.VK_H);
        keyMap.put("I", KeyEvent.VK_I);
        keyMap.put("J", KeyEvent.VK_J);
        keyMap.put("K", KeyEvent.VK_K);
        keyMap.put("L", KeyEvent.VK_L);
        keyMap.put("M", KeyEvent.VK_M);
        keyMap.put("N", KeyEvent.VK_N);
        keyMap.put("O", KeyEvent.VK_O);
        keyMap.put("P", KeyEvent.VK_P);
        keyMap.put("Q", KeyEvent.VK_Q);
        keyMap.put("R", KeyEvent.VK_R);
        keyMap.put("S", KeyEvent.VK_S);
        keyMap.put("T", KeyEvent.VK_T);
        keyMap.put("U", KeyEvent.VK_U);
        keyMap.put("V", KeyEvent.VK_V);
        keyMap.put("W", KeyEvent.VK_W);
        keyMap.put("X", KeyEvent.VK_X);
        keyMap.put("Y", KeyEvent.VK_Y);
        keyMap.put("Z", KeyEvent.VK_Z);
        keyMap.put("F1", KeyEvent.VK_F1);
        keyMap.put("F2", KeyEvent.VK_F2);
        keyMap.put("F3", KeyEvent.VK_F3);
        keyMap.put("F4", KeyEvent.VK_F4);
        keyMap.put("F5", KeyEvent.VK_F5);
        keyMap.put("F6", KeyEvent.VK_F6);
        keyMap.put("F7", KeyEvent.VK_F7);
        keyMap.put("Ctrl", KeyEvent.VK_CONTROL);
        keyMap.put("Enter", KeyEvent.VK_ENTER);
        keyMap.put("Space", KeyEvent.VK_SPACE);
        keyMap.put("Escape", KeyEvent.VK_ESCAPE);
        keyMap.put("Tab", KeyEvent.VK_TAB);
        keyMap.put("Backspace", KeyEvent.VK_BACK_SPACE);
    }


    public static void control(String keyboardData) {
        if (keyboardData.isEmpty()) {
            return;
        }
        String[] parsedData = keyboardData.split("[,]");
//        System.out.println("keyboard data in receiver: " + keyboardData);
        try {
            for (String x : parsedData) {
                int key = Integer.parseInt(x);
                String keyText = NativeKeyEvent.getKeyText(Math.abs(key));
                // note: this implementation is buggy, imagine if only the keyPress event is read in the receiver,
                // the key will be pressed down forever, which is why i am still thinking about an effective way
                // to pass keyboard data to receiver
                if (keyMap.containsKey(keyText)){
                    int awtKeyEvent = keyMap.get(keyText);
                    if (key >= 0) {
                        robot.keyPress(awtKeyEvent);
                    } else {
                        robot.keyRelease(awtKeyEvent);
                    }
                } else {
//                    System.out.println("No key '" + keyText + "' for awt key event"); // for debug
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error in keyboard data, all entries should be integers");
        }
    }
}
