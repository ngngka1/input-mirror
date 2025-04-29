package sender;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

public class MouseButtonListener implements NativeMouseListener {

    private int buttonMask = 0b00000; // indicates which buttons (MB1-MB5)are pressed

    public int getButtonMask() {
        return buttonMask;
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
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
        int mask = (1 << (button-1));
        if (pressed) {
            // Set the appropriate bit for the pressed button
            buttonMask |= mask; // Use OR to set the bit
        } else {
            // Clear the appropriate bit for the released button
            buttonMask &= ~mask; // Use AND NOT to clear the bit
        }
    }


}
