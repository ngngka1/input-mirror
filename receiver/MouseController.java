package receiver;

import java.awt.*;

public class MouseController {
//    private static final instance;
    private static Robot robot;
    private static int prevMbMask = 0b00101;
    public static void init(Robot robot) {
        MouseController.robot = robot;
    }

    public static void moveTo(int x, int y) {
        if (robot == null) throw new RuntimeException();
        robot.mouseMove(x, y);
    }

    public static void controlMouseButtons(int mbMask) {
        int changeMask = prevMbMask ^ mbMask;
        int i = 0;
        while (changeMask > 0) {
            if ((changeMask & 1) == 1)
                flipKeyAction(i);
            changeMask = changeMask >> 1;
            i++;
        }
        prevMbMask = mbMask;
    }

    public static void flipKeyAction(int i) {
        if ((prevMbMask >> i & 1) == 0) {
            robot.mousePress(i);
        } else {
            robot.mouseRelease(i);

        }
    }

}
