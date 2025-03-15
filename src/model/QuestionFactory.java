package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the factory design pattern and the Singleton Design Pattern.
 * @author Team 5
 * @version 0.5
 */
public class QuestionFactory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		/**
		 * A list of questions.
		 */
		private static ArrayList<AbstractQuestion> myQuestions;

		/**
		 * creates a question manager.
		 * 
		 * @param theQuestions the questions that will be asked.
		 */
		public static void IntializeQuestionFactory() {
			myQuestions = new ArrayList<AbstractQuestion>();
			for (int i = 0; i<48; i++) {
				AbstractQuestion a = DatabaseManager.getInstance().getRandomQuestion();
				if(myQuestions.contains(a)) {
					i--;
				}else {
					myQuestions.add(a);
				}
			}
		}
		/**
		 * The map will stay the same size, but it's contents will change.
		 */
		protected static void loadQuestions(ArrayList<AbstractQuestion> arr) {
			myQuestions = arr;
		}

		/**
		 * The map will stay the same size, but it's contents will change.
		 */
		protected static ArrayList<AbstractQuestion> returnQuestions() {
			return myQuestions;
		}

		/**
		 * Chooses a random question, makes sure it won't be asked again.
		 * 
		 * @return
		 */
		public static AbstractQuestion getQuestion() {
			final int index = (int) Math.round(Math.random() * (myQuestions.size() - 1));
			return myQuestions.remove(index);
		}
	}
