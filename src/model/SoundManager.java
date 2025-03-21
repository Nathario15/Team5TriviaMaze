package model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Manages sound effects and background music in the Trivia Maze game.
 * @author Team 5
 * @version 1.0
 */
public final class SoundManager {
	
	/** Singleton instance of SoundManager. */
	private static SoundManager instance;
	
	/** Stores the values of the current background music files. */
    private static final String[] BACKGROUND_MUSIC_FILES = {
            "/sounds/background_music_1.wav",
            "/sounds/background_music_2.wav",
            "/sounds/background_music_3.wav",
            "/sounds/background_music_4.wav",
            "/sounds/background_music_5.wav"
    };
    
    /** Controls whether the music is enabled or not. */
    private boolean myMusicEnabled = true;
	
	/** Represents the current background music playing. */
    private Clip myCurrentBackgroundMusic;
    
    private SoundManager() {
    }

    /**
     * Gets the singleton instance of SoundManager.
     * 
     * @return instance
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Plays a sound from a file path.
     * 
     * @param theFilePath The path to the sound file
     */
    public void playSound(final String theFilePath) {
        try {
            final InputStream audioFileStream = getClass().getResourceAsStream(theFilePath);
            if (audioFileStream == null) {
                System.err.println("Audio file not found: " + theFilePath);
            } else {
                // Buffer the input stream to make it support mark/reset
                final BufferedInputStream bufferedStream = new BufferedInputStream(audioFileStream);
                
                final AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedStream);
                final Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            }
        } catch (final UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Plays background music if music is enabled.
     */
    public void playBackgroundMusic() {
        if (!myMusicEnabled) {
            return;
        }
        
        try {
            stopBackgroundMusic(); // Stop any current music first

            final String selectedMusic = BACKGROUND_MUSIC_FILES[new Random().nextInt(BACKGROUND_MUSIC_FILES.length)];
            final InputStream audioFileStream = getClass().getResourceAsStream(selectedMusic);
            if (audioFileStream == null) {
                System.err.println("Background music file not found: " + selectedMusic);
            } else {
                // Buffer the input stream to make it support mark/reset
                final BufferedInputStream bufferedStream = new BufferedInputStream(audioFileStream);
                
                final AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedStream);

                myCurrentBackgroundMusic = AudioSystem.getClip();
                myCurrentBackgroundMusic.open(audioStream);

                myCurrentBackgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                myCurrentBackgroundMusic.start();
            }
        } catch (final UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Stops background music.
     */
    public void stopBackgroundMusic() {
        if (myCurrentBackgroundMusic != null && myCurrentBackgroundMusic.isRunning()) {
            myCurrentBackgroundMusic.stop();
        }
    }
    
    /**
     * Helps toggle the music.
     * 
     * @param volume
     */
    public void toggleMusic() {
        myMusicEnabled = !myMusicEnabled;
        if (myMusicEnabled) {
            playBackgroundMusic();
        } else {
            stopBackgroundMusic();
        }
    }
    
    /**
     * Provides if music is enabled.
     * 
     * @param music enabled
     */
    public boolean getMusicEnabled() {
    	return myMusicEnabled;
    }

    /**
     * Plays a click sound.
     */
    public void playClickSound() {
        playSound("/sounds/click.wav");
    }

    /**
     * Plays the correct answer sound.
     */
    public void playCorrectAnswerSound() {
        playSound("/sounds/correct_answer.wav");
    }

    /**
     * Plays the incorrect answer sound.
     */
    public void playIncorrectAnswerSound() {
        playSound("/sounds/incorrect_answer.wav");
    }

    /**
     * Plays the win sound.
     */
    public void playWinSound() {
        playSound("/sounds/win_sound.wav");
    }

    /**
     * Plays the lose sound.
     */
    public void playLoseSound() {
        playSound("/sounds/lose_sound.wav");
    }
}
