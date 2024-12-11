package receiver;

import java.awt.*;
import java.util.Arrays;

public class Controller {

    public static void init() {
        try {
            Robot robot = new Robot();
            CursorController.init(robot);
            KeyboardController.init(robot);
        } catch (AWTException e) {
            System.out.println("AWT exception");
        }
    }

    public static void redirect(String[] parsedData) {
        System.out.println("parsedData: " + Arrays.toString(parsedData));
        if (parsedData.length > 1) {
            CursorController.moveTo(Integer.parseInt(parsedData[0]), Integer.parseInt(parsedData[1]));
        } else {
            KeyboardController.pressHotkey(parsedData[0]);
        }
    }
}
