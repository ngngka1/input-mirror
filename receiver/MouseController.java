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

    public static void control(String mouseData) {
        if (mouseData.isEmpty()) {
            return;
        }
        String[] parsedData = mouseData.split("[:]");
        String[] posArgs = parsedData[0].split("[,]");
        int mbMask = Integer.parseInt(parsedData[1]);
        int scrollRotations = Integer.parseInt(parsedData[2]);

        MouseController.moveTo(Integer.parseInt(posArgs[0]), Integer.parseInt(posArgs[1]));
        MouseController.controlMouseButtons(mbMask);
        MouseController.scrollMouseByRotations(scrollRotations);
    }



    public static void controlMouseButtons(int mbMask) {
        // example:
        // prevMbMask (previous state) = 00101 (1 = key pressed, 0 = nothing/released)

        // mbMask (new state) = 00110 (1 = key pressed, 0 = nothing/released)

        // mousePressMask = (~prevMbMask) & mbMask = 11010 & 00110 = 00010
        // mouseReleaseMask = prevMbMask & (prevMbMask ^ mbMask) = 00101 & (00101 ^ 00110) = 00101 & 00011 = 00001

        int mousePressMask = (~prevMbMask) & mbMask;
        int mouseReleaseMask = prevMbMask & (prevMbMask ^ mbMask);
        robot.mousePress(mousePressMask);
        robot.mouseRelease(mouseReleaseMask);
        prevMbMask = mbMask;
    }


    public static void scrollMouseByRotations(int rotations) {
        robot.mouseWheel(rotations);
    }

}
