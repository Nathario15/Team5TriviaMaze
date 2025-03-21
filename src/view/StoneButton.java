package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class StoneButton extends JButton {
	/** Serialization. */
    private static final long serialVersionUID = 1L;
    /** The stone texture. */
    private static final String TEXTURE_PATH = "resources/images/stone.png";
    /** The stone hover texture. */
    private static final String HOVER_TEXTURE_PATH = "resources/images/stone_hover.png";
    /** Minecraftia font scale. */
    private static final float MINECRAFTIA = 16f;
    /** Arial backup value. */
	private static final int ARIAL = 16;
    /** Default stone texture. */
    private Image myDefaultTexture;
    /** Default stone hover texture. */
    private Image myHoverTexture;

    /** Constructor. */
    public StoneButton(final String theText) {
        super(theText);
        myDefaultTexture = new ImageIcon(TEXTURE_PATH).getImage();
        myHoverTexture = new ImageIcon(HOVER_TEXTURE_PATH).getImage();

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
    
    private static Font loadCustomFont() {
	    try {
	        // Load the font from the resources directory
	        final File fontFile = new File("resources/fonts/Minecraftia.ttf");
	        
	        // Create the font from the file
	        final Font minecraftiaFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
	        
	        // Register the font with the GraphicsEnvironment
	        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        ge.registerFont(minecraftiaFont);
	        
	        // Return the font with the desired size
	        return minecraftiaFont.deriveFont(MINECRAFTIA);
	        
	    } catch (final FontFormatException | IOException e) {
	        e.printStackTrace();
	        return new Font("Arial", Font.PLAIN, ARIAL);
	    }
	}
}