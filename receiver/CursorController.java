package receiver;

import java.awt.*;

public class CursorController {
//    private static final instance;
    private static Robot robot;
    public static void init(Robot robot) {
        CursorController.robot = robot;
    }

    public static void moveTo(int x, int y) {
        if (robot == null) throw new RuntimeException();
        robot.mouseMove(x, y);
    }
}
