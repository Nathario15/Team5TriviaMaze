package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JMenuItem;
import model.ResourceManager;

/**
 * Implements the Stone texture onto a JMenuItem.
 * @author Team 5
 * @version 1.0
 */
public class StoneMenuItem extends JMenuItem {
    /** Serialization. */
    private static final long serialVersionUID = 1L;
    /** The stone texture. */
    private static final String TEXTURE_PATH = "/images/stone.png";
    /** Default stone texture. */
    private Image myDefaultTexture;

    /** Constructor. */
    public StoneMenuItem(final String theText) {
        super(theText);
        myDefaultTexture = ResourceManager.getInstance().loadImage(TEXTURE_PATH);
        setFont(loadCustomFont());
        setForeground(Color.WHITE); // Set text color to white
    }

    /**
     * Paints the texture onto the JMenuItem.
     */
    @Override
    protected void paintComponent(final Graphics theG) {
        super.paintComponent(theG);
        
        // Always draw the default stone texture (no hover effect)
        theG.drawImage(myDefaultTexture, 0, 0, getWidth(), getHeight(), this);

        // Set text properties
        theG.setFont(getFont());
        theG.setColor(getForeground());

        // Center the text
        final String text = getText();
        final int x = (getWidth() - theG.getFontMetrics().stringWidth(text)) / 2;
        final int y = ((getHeight() - theG.getFontMetrics().getHeight()) / 2) + theG.getFontMetrics().getAscent();

        // Draw the text over the texture
        theG.drawString(text, x, y);
    }
    
    /**
     * Loads the custom font as desired.
     * @return custom font.
     */
    private static Font loadCustomFont() {
        return ResourceManager.getInstance().loadMinecraftFont();
    }
}