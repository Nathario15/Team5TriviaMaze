package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JMenu;
import javax.swing.border.LineBorder;
import model.ResourceManager;

/**
 * Implements the Stone texture onto a JMenu.
 * @author Team 5
 * @version 1.0
 */
public class StoneMenu extends JMenu {
    /** Serialization. */
    private static final long serialVersionUID = 1L;
    /** The stone texture. */
    private static final String TEXTURE_PATH = "/images/stone.png";
    /** Stone texture. */
    private Image myTexture;

    /** Constructor. */
    public StoneMenu(final String theText) {
        super(theText);
        myTexture = ResourceManager.getInstance().loadImage(TEXTURE_PATH);
        setFont(loadCustomFont());
        setForeground(Color.WHITE); // Set text color to white
        setBorder(new LineBorder(Color.BLACK, 1)); // Set a 1px white border
    }

    /**
     * Paints the texture onto the JMenu.
     */
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
    
    /**
     * Loads the custom font as desired.
     * @return custom font.
     */
    private static Font loadCustomFont() {
        return ResourceManager.getInstance().loadMinecraftFont();
    }
}