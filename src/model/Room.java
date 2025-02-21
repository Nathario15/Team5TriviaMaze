package model;

import controller.SystemControl;
import java.io.Serializable;

/**
 * @author Ibrahim ELnikety
 * @version 1 A room, contains a question you have to answer, and a door that
 *          will open.
 */
public class Room implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a room object.
	 */
	public Room() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * When player enters a room.
	 */
	public void enter() {
		SystemControl.triggerQuestion();
	}
}
