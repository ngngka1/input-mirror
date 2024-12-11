package receiver;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyboardController {
    private static Robot robot;
    public static void init(Robot robot) {
        KeyboardController.robot = robot;
    }
    private static final Map<String, Integer> keyMap = new HashMap<>();

    static {
        keyMap.put("A", KeyEvent.VK_A);
//        keyMap.put("`", KeyEvent.VK_F6);
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
    public static void pressHotkey(String keyCombination) {
        String[] keys = keyCombination.split("[+]");

        int[] keyEvents = new int[keys.length];
        int i = 0;
        for (String key : keys) {
            key = key.toUpperCase();
            if (!keyMap.containsKey(key)) {
                if (key.equals("MB1")) {
                    try {
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        Thread.sleep(100);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    } catch (InterruptedException e) {}

                }
                else
                    System.out.println("No such key: " + key);
                return;
            } else {
                keyEvents[i] = keyMap.get(key);
                robot.keyPress(keyEvents[i++]);
            }
        }
        for (int keyEvent : keyEvents) {
            robot.keyRelease(keyEvent);
        }
    }
}
