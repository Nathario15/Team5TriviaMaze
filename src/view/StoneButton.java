package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import model.ResourceManager;

/**
 * Implements the Stone texture onto a JButton.
 * @author Team 5
 * @version 1.0
 */
public class StoneButton extends JButton {
	/** Serialization. */
    private static final long serialVersionUID = 1L;
    /** Default stone texture. */
    private Image myDefaultTexture;
    /** Default stone hover texture. */
    private Image myHoverTexture;

    /** Constructor. */
    public StoneButton(final String theText) {
        super(theText);
        myDefaultTexture = ResourceManager.getInstance().loadImage("/images/stone.png");
        myHoverTexture = ResourceManager.getInstance().loadImage("/images/stone_hover.png");

        // Set the font and text color
        this.setFont(loadCustomFont());
        this.setForeground(Color.WHITE);  // Set text color to white

        // Set other button properties
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        this.setFocusPainted(false);

        // Hover effect - Repainting on mouse events to change textures
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(final java.awt.event.MouseEvent theEvt) {
                // Trigger repaint when the mouse enters the button area
                StoneButton.this.repaint();
            }

            @Override
            public void mouseExited(final java.awt.event.MouseEvent theEvt) {
                // Trigger repaint when the mouse exits the button area
                StoneButton.this.repaint();
            }
        });
    }

    /**
     * Paints the texture onto the JButton.
     */
    @Override
    protected void paintComponent(final Graphics theG) {
        super.paintComponent(theG); // Paint the button’s default background and border

        // Determine which texture to use based on hover state
        Image textureToDraw = myDefaultTexture;
        if (getModel().isPressed() || getModel().isRollover()) {
            textureToDraw = myHoverTexture;
        }

        // Draw the chosen texture
        theG.drawImage(textureToDraw, 0, 0, getWidth(), getHeight(), this);

        // Set the font and color for the text
        theG.setFont(this.getFont());
        theG.setColor(this.getForeground());

        // Draw the button’s text (ensuring it's centered)
        final FontMetrics fm = theG.getFontMetrics();
        final int x = (getWidth() - fm.stringWidth(getText())) / 2;
        final int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
        theG.drawString(getText(), x, y);
    }
    
    /**
     * Loads the custom font as desired.
     * @return custom font.
     */
    private static Font loadCustomFont() {
        return ResourceManager.getInstance().loadMinecraftFont();
    }
}