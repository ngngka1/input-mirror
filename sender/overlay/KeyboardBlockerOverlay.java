package sender.overlay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KeyboardBlockerOverlay extends JFrame {
    private final JTextField textField;
    private final JTextField devNullField;

    public void updateText(String newText) {
        textField.setText(newText);
    }

    public KeyboardBlockerOverlay(String title) {
        super(title);
        this.setUndecorated(true);
        this.setAlwaysOnTop(true);
        this.setSize(400, 300);
        this.setOpacity(0.3f); // Adjust opacity as needed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - this.getWidth(); // X position for bottom right
        int y = screenSize.height - this.getHeight(); // Y position for bottom right

        this.setLocation(x, y);


        textField = new JTextField(0);
        textField.setFocusable(false);
        textField.setFont(new Font("Arial", Font.BOLD, 12));
        textField.setCursor(Cursor.getDefaultCursor());
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0; // Column
        constraints.gridy = 0; // Row
        constraints.anchor = GridBagConstraints.CENTER; // Center alignment

        devNullField = new JTextField();
        devNullField.setSize(1, 1);
        devNullField.setBorder(BorderFactory.createEmptyBorder());
        devNullField.setCursor(Cursor.getDefaultCursor());

        // Add a KeyListener to discard input
        devNullField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Discard the input by consuming the event
                e.consume();
            }
        });
        devNullField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
//                System.out.println("requesting focus to devnull");
                devNullField.requestFocusInWindow();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.add(textField, constraints);
        panel.add(devNullField);

        this.add(panel);

        // Continuously request focus
//        new Timer(100, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (frame.isVisible()) {
//                    text.requestFocusInWindow(); // Request focus
//                }
//            }
//        }).start();

    }
}
