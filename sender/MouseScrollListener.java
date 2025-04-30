package sender;

import com.github.kwhat.jnativehook.mouse.NativeMouseWheelEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelListener;

public class MouseScrollListener implements NativeMouseWheelListener {
    private int scrollRotations = 0;

    public MouseScrollListener() {
        scrollRotations = 0;
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
        scrollRotations = e.getWheelRotation();
    }

    public int getRotations() {
        if (scrollRotations != 0) {
            int temp = scrollRotations;
            scrollRotations = 0;
            return temp;
        }
        return 0;
    }


}