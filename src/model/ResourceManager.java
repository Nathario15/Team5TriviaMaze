package model;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.ImageIcon;

/**
 * Handles loading of resources from the classpath (works in both development and JAR).
 * @author Team 5
 * @version 1.0
 */
public final class ResourceManager {
    /** Arial backup font size. */
    private static final int ARIAL = 16;
    
    /** Minecraftia font size. */
    private static final float MINECRAFTIA = 16f;
    
    /** Arial font name constant. */
    private static final String ARIAL_FONT = "Arial";
    
    /** Singleton instance. */
    private static ResourceManager myInstance;
    
    /** Private constructor for singleton pattern. */
    private ResourceManager() {
        // Private constructor
    }
    
    /**
     * Gets the singleton instance.
     * @return The ResourceManager instance
     */
    public static synchronized ResourceManager getInstance() {
        if (myInstance == null) {
            myInstance = new ResourceManager();
        }
        return myInstance;
    }
    
    /**
     * Loads a font resource from the classpath.
     * 
     * @param thePath The path to the font resource (e.g., "/fonts/Minecraftia.ttf")
     * @param theSize The font size
     * @return The loaded font, or Arial as fallback
     */
    public Font loadFont(final String thePath, final float theSize) {
        Font result;
        try {
            final InputStream fontStream = getClass().getResourceAsStream(thePath);
            if (fontStream == null) {
                System.err.println("Font resource not found: " + thePath);
                result = new Font(ARIAL_FONT, Font.PLAIN, ARIAL);
            } else {
                // Create the font from the input stream
                final Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                
                // Register the font with the GraphicsEnvironment
                final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
                
                // Clean up the stream
                fontStream.close();
                
                // Set the result with the desired size
                result = customFont.deriveFont(theSize);
            }
        } catch (final FontFormatException | IOException e) {
            System.err.println("Error loading font: " + e.getMessage());
            result = new Font(ARIAL_FONT, Font.PLAIN, ARIAL);
        }
        return result;
    }
    
    /**
     * Loads the Minecraft font (or Arial as fallback).
     * 
     * @return The Minecraft font or Arial as fallback
     */
    public Font loadMinecraftFont() {
        return loadFont("/fonts/Minecraftia.ttf", MINECRAFTIA);
    }
    
    /**
     * Loads an image from the classpath.
     * 
     * @param thePath The path to the image (e.g., "/images/stone.png")
     * @return The loaded image, or null if not found
     */
    public Image loadImage(final String thePath) {
        Image result = null;
        final java.net.URL imageURL = getClass().getResource(thePath);
        if (imageURL != null) {
            result = new ImageIcon(imageURL).getImage();
        } else {
            System.err.println("Image resource not found: " + thePath);
        }
        return result;
    }
}