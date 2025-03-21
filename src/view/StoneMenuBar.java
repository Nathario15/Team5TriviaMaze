package view;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JMenuBar;
import model.ResourceManager;

public class StoneMenuBar extends JMenuBar {
    /** Serialization. */
    private static final long serialVersionUID = 1L;
    /** The stone texture for the menu bar. */
    private static final String TEXTURE_PATH = "/images/stone.png";
    /** Stone texture image. */
    private Image myTexture;

    /** Constructor. */
    public StoneMenuBar() {
        // Load the stone texture
    	myTexture = ResourceManager.getInstance().loadImage(TEXTURE_PATH);
    }

    @Override
    protected void paintComponent(final Graphics theG) {
        super.paintComponent(theG);
        // Draw the stone texture on the entire menu bar
        theG.drawImage(myTexture, 0, 0, getWidth(), getHeight(), this);
    }
}