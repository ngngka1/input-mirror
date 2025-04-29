package sender;

import java.awt.*;

public class CursorListener {
    private final double targetWidth;
    private final double targetHeight;
    private final double screenWidth;
    private final double screenHeight;
    CursorListener(double targetWidth, double targetHeight) {
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.getWidth();
        screenHeight = screenSize.getHeight();
    }

    public int[] getPos() {
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        int normalizedX = (int)(mousePosition.getX() / screenWidth * targetWidth);
        int normalizedY = (int) (mousePosition.getY() / screenHeight * targetHeight);
        return new int[] {normalizedX, normalizedY};
    }
}