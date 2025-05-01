package sender;

import sender.overlay.KeyboardBlockerOverlay;
import sender.overlay.MouseBlockerOverlay;
import types.HotkeyAction;
import utils.HotkeyManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class InputBlocker {
    private final MouseBlockerOverlay mouseOverlay;
    private final KeyboardBlockerOverlay keyboardOverlay;

    public InputBlocker() {
        // Overlay for mouse input
        mouseOverlay = new MouseBlockerOverlay("mouseBlocker");
        mouseOverlay.setVisible(false);
        mouseOverlay.updateText("Mouse input exclusive to another device (press " + HotkeyManager.getHotkeyText(HotkeyAction.TOGGLE_MOUSE) + " to toggle)");

//        mouseOverlay.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                System.out.println("Mouse pressed: " + e.getPoint());
//            }
//        });

        // Overlay for keyboard input
        keyboardOverlay = new KeyboardBlockerOverlay("keyboardBlocker");
        keyboardOverlay.updateText("Keyboard input exclusive to another device (press " + HotkeyManager.getHotkeyText(HotkeyAction.TOGGLE_KEYBOARD) + " to toggle)");
        keyboardOverlay.setVisible(false);
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (keyboardOverlay.isVisible() && !keyboardOverlay.isFocusOwner())
//                    System.out.println("Requesting focus to keyboard blocker");
                    keyboardOverlay.requestFocusInWindow();
            }
        });
        timer.start();
//        keyboardOverlay.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                System.out.println("Requesting focus for the entire window");
//                keyboardOverlay.requestFocusInWindow();
//            }
//        });
//        keyboardOverlay.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                System.out.println("Key pressed: " + KeyEvent.getKeyText(e.getKeyCode()));
//            }
//        });

    }

    public void disableMouseInput() {
        mouseOverlay.setVisible(true);
    }

    public void enableMouseInput() {
        mouseOverlay.setVisible(false);
    }

    public void disableKeyboardInput() {
        keyboardOverlay.setVisible(true);
//        while (keyboardOverlay.isVisible()) {
//            if (!keyboardOverlay.isFocused()) {
//                System.out.println("Forcing focus on keyboard blocker");
//                keyboardOverlay.interceptKeyboardInput();
//            }
//        }
    }

    public void enableKeyboardInput() {
        keyboardOverlay.setVisible(false);

    }

//    public static void main(String[] args) {
//        InputBlocker blocker = new InputBlocker();
//
//        // Example to disable input for 5 seconds
//        blocker.disableKeyboardInput();
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        blocker.enableKeyboardInput();
//    }
}
