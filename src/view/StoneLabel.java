package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import model.ResourceManager;

/**
 * Implements the Stone texture onto a JLabel.
 * @author Team 5
 * @version 1.0
 */
public class StoneLabel extends JLabel {
	/** Serialization. */
    private static final long serialVersionUID = 1L;
    /** The stone texture. */
    private static final String TEXTURE_PATH = "/images/stone.png";
    /** Stone texture. */
    private Image myTexture;

    /** Constructor. */
    public StoneLabel(final String theText) {
        super(theText);
        myTexture = ResourceManager.getInstance().loadImage(TEXTURE_PATH);
        setFont(loadCustomFont());
        setForeground(Color.WHITE);  // Set text color to white
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    /**
     * Paints the texture onto the JLabel.
     */
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
    
    /**
     * Loads the custom font as desired.
     * @return custom font.
     */
    private static Font loadCustomFont() {
        return ResourceManager.getInstance().loadMinecraftFont();
    }
}