package kalah.gui;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

public abstract class Window<B> extends JPanel {
    static final int WIDTH = 600;
    static final int HEIGHT = 300;
    static final String TITLE = "Kalah";
    static final String FONT_NAME = "Arial";
    static final int FONT_STYLE = Font.PLAIN;
    static final Insets INSETS = new Insets(10, 10, 10, 10);

    static GridBagConstraints generateGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = INSETS;
        gbc.gridy = GridBagConstraints.RELATIVE;
        return gbc;
    }

    static JLabel generateErrorMessageLabel() {
        JLabel errorMessage = new JLabel();
        errorMessage.setFont(new Font(FONT_NAME, FONT_STYLE, 14));
        errorMessage.setForeground(Color.RED);
        return errorMessage;
    }

    protected abstract void initGui();

    protected abstract Map<B, JButton> getButtonMap();

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Window.WIDTH, Window.HEIGHT);
    }

    void addButtonListener(B button, ActionListener listener) {
        getButtonMap().get(button).addActionListener(listener);
    }

    // source: https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
    void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }

    Image getImage(String path) {
        try {
            Image image = ImageIO.read(new File(path));
            return image;
        } catch (Exception e) {
            throw new RuntimeException("Can't open image\n");
        }
    }
}
