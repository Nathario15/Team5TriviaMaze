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
import javax.swing.JMenu;
import javax.swing.border.LineBorder;

public class StoneMenu extends JMenu {
    /** Serialization. */
    private static final long serialVersionUID = 1L;
    /** The stone texture. */
    private static final String TEXTURE_PATH = "resources/images/stone.png";
    /** Minecraftia font scale. */
    private static final float MINECRAFTIA = 16f;
    /** Arial backup value. */
    private static final int ARIAL = 16;
    /** Stone texture. */
    private Image myTexture;

    /** Constructor. */
    public StoneMenu(final String theText) {
        super(theText);
        myTexture = new ImageIcon(TEXTURE_PATH).getImage();
        setFont(loadCustomFont());
        setForeground(Color.WHITE); // Set text color to white
        setBorder(new LineBorder(Color.BLACK, 1)); // Set a 1px white border
    }

    @Override
    protected void paintComponent(final Graphics theG) {
        super.paintComponent(theG);
        
        // Draw the texture as background
        theG.drawImage(myTexture, 0, 0, getWidth(), getHeight(), this);
        
        // Set the text properties
        theG.setFont(getFont());
        theG.setColor(getForeground());
        
        // Center the text
        final String text = getText();
        final int x = (getWidth() - theG.getFontMetrics().stringWidth(text)) / 2;
        final int y = ((getHeight() - theG.getFontMetrics().getHeight()) / 2) + theG.getFontMetrics().getAscent();
        
        // Draw the text over the texture
        theG.drawString(text, x, y);
        
        // Draw a white border around the menu (box)
        theG.setColor(Color.WHITE);
        theG.drawRect(0, 0, getWidth() - 1, getHeight() - 1); // Draw 1px white border
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