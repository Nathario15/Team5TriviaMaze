package model;

import java.io.File;
import java.io.IOException;
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
            "sounds/background_music_1.wav",
            "sounds/background_music_2.wav",
            "sounds/background_music_3.wav"
    };
    
    /** Controls whether the music is enabled or not. */
    private boolean myMusicEnabled = true;
	
	/** Represents the current background music playing. */
    private Clip myCurrentBackgroundMusic;
    
    private SoundManager() {
        System.out.println("SoundManager instance created: " + this);
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
     * Method that helps with the functionality of playing a sound.
     * @param filePath
     */
    public void playSound(final String theFilePath) {
        try {
            final AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(theFilePath));
            final Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (final UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays background music.
     */
    public void playBackgroundMusic() {
        if (!myMusicEnabled) {
            return;
        }
        try {
            stopBackgroundMusic(); // Stop any current music first

            final String selectedMusic = BACKGROUND_MUSIC_FILES[new Random().nextInt(BACKGROUND_MUSIC_FILES.length)];
            final File musicFile = new File(selectedMusic);

            final AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);

            myCurrentBackgroundMusic = AudioSystem.getClip();
            myCurrentBackgroundMusic.open(audioStream);

            myCurrentBackgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            myCurrentBackgroundMusic.start();

        } catch (final UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops background music.
     */
    public void stopBackgroundMusic() {
        if (myCurrentBackgroundMusic != null && myCurrentBackgroundMusic.isRunning()) {
            myCurrentBackgroundMusic.stop();
        } else {
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
        playSound("sounds/click.wav");
    }

    /**
     * Plays the correct answer sound.
     */
    public void playCorrectAnswerSound() {
        playSound("sounds/correct_answer.wav");
    }

    /**
     * Plays the incorrect answer sound.
     */
    public void playIncorrectAnswerSound() {
        playSound("sounds/incorrect_answer.wav");
    }

    /**
     * Plays the win sound.
     */
    public void playWinSound() {
        playSound("sounds/win_sound.wav");
    }

    /**
     * Plays the lose sound.
     */
    public void playLoseSound() {
        playSound("sounds/lose_sound.wav");
    }
}
