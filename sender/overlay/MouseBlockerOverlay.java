package sender.overlay;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MouseBlockerOverlay extends JFrame {
    private final JTextField textField;

    public void updateText(String newText) {
        textField.setText(newText);
//        int width = textField.getPreferredSize().width + 10; // Add some padding
//        textField.setSize(width, textField.getHeight());
    }

    public MouseBlockerOverlay(String title) {
        super(title);
        this.setUndecorated(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setOpacity(0.3f); // Adjust opacity as needed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setAlwaysOnTop(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        // hide the cursor when visible, so it feels like the mouse input is on another device
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
        panel.setCursor(blankCursor);

        textField = new JTextField(0);
//        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
//        textField.setText();

        textField.setFocusable(false);
        textField.setFont(new Font("Arial", Font.BOLD, 24));
        textField.setCursor(blankCursor);
        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.setHorizontalAlignment(JTextField.CENTER);
//        textField.setSize(screenSize.width, 32);
        textField.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0; // Column
        constraints.gridy = 0; // Row
        constraints.anchor = GridBagConstraints.CENTER; // Center alignment

        panel.add(textField, constraints);


        this.add(panel);

    }
}
