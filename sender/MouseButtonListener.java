package sender;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

public class MouseButtonListener implements NativeMouseListener {

    private int buttonMask = 0b00000; // indicates which buttons (MB1-MB5)are pressed

    public int getButtonMask() {
        return buttonMask;
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        // Check if the left mouse button was clicked
        int buttonClicked = e.getButton();
        updateButtonMask(buttonClicked, true);
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        int buttonReleased = e.getButton();
        updateButtonMask(buttonReleased, false);

    }


    private void updateButtonMask(int button, boolean pressed) {
        // brute forcing here because Java.awt.Robot has reversed mask for MB2 and MB3, compared to button mask
        if (button == 2 || button == 3) {
            button = 0b0001 ^ button; // 0011 becomes 0010 (3->2); 0010 becomes 0011 (2->3)
        }

        int mask = (1 << (4 - (button-1))); //
        if (pressed) {
            // Set the appropriate bit for the pressed button
            buttonMask |= mask; // Use OR to set the bit
        } else {
            // Clear the appropriate bit for the released button
            buttonMask &= ~mask; // Use AND NOT to clear the bit
        }
    }


}
