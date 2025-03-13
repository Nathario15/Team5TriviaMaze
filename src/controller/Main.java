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
	 * @param args
	 */
	public static void main(final String[] theArgs) {
		try {
			// Initialize database
			final DatabaseManager dbManager = DatabaseManager.getInstance();

			// Create SystemControl (controller)
			final SystemControl controller = SystemControl.getInstance();

			// Create and display the view
			SwingUtilities.invokeLater(() -> {
				final GameView view = new GameView();
				view.setVisible(true);
			});

		} catch (final Exception TheE) {
			System.err.println("Error starting application: " + TheE.getMessage());
			TheE.printStackTrace();
		}
	}
}