package receiver;

import java.awt.*;
import java.awt.event.InputEvent;

public class MouseController {
//    private static final instance;
    private static Robot robot;
    private static int prevMbMask = 0b00000;
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

    public static void scrollMouseByRotations(int rotations) {
        robot.mouseWheel(rotations);
    }

    public static void flipKeyAction(int i) {
        if (((prevMbMask >> i) & 1) == 0) {
            robot.mousePress(InputEvent.getMaskForButton(i + 1));
        } else {
            robot.mouseRelease(InputEvent.getMaskForButton(i + 1));
        }
    }

}
