package sender;

import java.awt.*;

public class CursorPositionRetriever {
    private static final double targetWidth = 1920.0;
    private static final double targetHeight = 1080.0;
    private static final double screenWidth;
    private static final double screenHeight;
    static {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.getWidth();
        screenHeight = screenSize.getHeight();
    }
    public static String get() {
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        int normalizedX = (int)(mousePosition.getX() / screenWidth * targetWidth);
        int normalizedY = (int) (mousePosition.getY() / screenHeight * targetHeight);
        return normalizedX + "|" + normalizedY;
    }
}