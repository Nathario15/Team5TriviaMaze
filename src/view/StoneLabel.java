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
import javax.swing.JLabel;

public class StoneLabel extends JLabel {
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
    public StoneLabel(final String theText) {
        super(theText);
        myTexture = new ImageIcon(TEXTURE_PATH).getImage();
        setFont(loadCustomFont());
        setForeground(Color.WHITE);  // Set text color to white
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    @Override
    protected void paintComponent(final Graphics theG) {
        super.paintComponent(theG);
        theG.drawImage(myTexture, 0, 0, getWidth(), getHeight(), this);
        theG.setFont(getFont());
        theG.setColor(getForeground());
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