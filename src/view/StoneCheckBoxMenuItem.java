package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;

public class StoneCheckBoxMenuItem extends JCheckBoxMenuItem {
    /** Serialization. */
    private static final long serialVersionUID = 1L;
    /** The stone texture. */
    private static final String TEXTURE_PATH = "resources/images/stone.png";
    /** Minecraftia font scale. */
    private static final float MINECRAFTIA = 16f;
    /** Arial backup value. */
    private static final int ARIAL = 16;
    /** Default stone texture. */
    private Image myDefaultTexture;

    /** Constructor. */
    public StoneCheckBoxMenuItem(final String theText) {
        super(theText);
        myDefaultTexture = new ImageIcon(TEXTURE_PATH).getImage();
        setFont(loadCustomFont());
        setForeground(Color.WHITE); // Set text color to white
    }

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