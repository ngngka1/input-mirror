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
        keyMap.put("F6", KeyEvent.VK_F6);
        keyMap.put("F7", KeyEvent.VK_F7);
        keyMap.put("ENTER", KeyEvent.VK_ENTER);
        keyMap.put("SPACE", KeyEvent.VK_SPACE);
        keyMap.put("ESCAPE", KeyEvent.VK_ESCAPE);
        keyMap.put("TAB", KeyEvent.VK_TAB);
        keyMap.put("BACKSPACE", KeyEvent.VK_BACK_SPACE);
    }


    public static void control(String keyboardData) {
        if (keyboardData.isEmpty()) {
            return;
        }
        String[] parsedData = keyboardData.split("[,]");
        try {
            for (String x : parsedData) {
                int key = Integer.parseInt(x);
                String keyText = NativeKeyEvent.getKeyText(Math.abs(key));
                if (keyMap.containsKey(keyText)){
                    int awtKeyEvent = keyMap.get(keyText);
                    if (key >= 0) {
                        robot.keyPress(awtKeyEvent);
                    } else {
                        robot.keyRelease(awtKeyEvent);
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error in keyboard data, all entries should be integers");
        }
    }
}
