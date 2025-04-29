package receiver;

import java.awt.*;

public class ControllerManager {
    private static String[] parseData(String data) {
        return data.split("[:]");
    }

    public static void init() {
        try {
            Robot robot = new Robot();
            MouseController.init(robot);
            KeyboardController.init(robot);
        } catch (AWTException e) {
            System.out.println("AWT exception");
        }
    }

    public static void redirect(String data) {
        String[] parsedData = parseData(data);
        String[] posArgs = parsedData[0].split("[,]");
        int mbMask = Integer.parseInt(parsedData[1]);

        MouseController.moveTo(Integer.parseInt(posArgs[0]), Integer.parseInt(posArgs[1]));
        MouseController.controlMouseButtons(mbMask);
//        Mouse.pressHotkey(parsedData[0]);

    }
}
