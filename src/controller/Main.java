package controller;

import javax.swing.SwingUtilities;
import model.DatabaseManager;
import view.GameView;

/**
 * The main class that can start the game.
 * 
 * @author Team 5
 * @version 1.0
 */
public final class Main {
	private Main() {
	}

	/**
	 * Starts game.
	 * 
	 * @param theArgs command line arguments
	 */
	public static void main(final String[] theArgs) {
		// Initialize database (required for application)
		DatabaseManager.getInstance();

		// Create SystemControl (required for application)
		SystemControl.getInstance();

		// Create and display the view
		SwingUtilities.invokeLater(() -> {
			final GameView view = new GameView();
			view.setVisible(true);
		});
	}
}