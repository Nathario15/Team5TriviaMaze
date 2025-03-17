package controller;

import javax.swing.SwingUtilities;
import model.DatabaseManager;
import view.GameView;

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