package receiver;

import java.awt.*;

public class CursorController {
    private static Robot robot;
    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {

        }
    }

    public static void moveTo(int x, int y) {
        robot.mouseMove(x, y);
    }
}
