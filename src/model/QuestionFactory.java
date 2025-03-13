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
	 * The database manager reference.
	 */
	private transient DatabaseManager myDatabaseManager;
	/**
	 * A list of question Ids that have been used.
	 */
	private final List<Integer> myUsedQuestionIds;

	/**
	 * creates a question manager.
	 * 
	 * @param theQuestions the questions that will be asked.
	 */
	public QuestionFactory() {
//		myQuestions=DatabaseManager.getInstance().getRandomQuestion();
		myDatabaseManager = DatabaseManager.getInstance();
		myUsedQuestionIds = new ArrayList<>();
	}

	/**
	 * Chooses a random question, makes sure it won't be asked again.
	 * 
	 * @param difficulty The difficulty level for the question
	 * @return A random question of the specified difficulty
	 */
	public AbstractQuestion getQuestion(Difficulty difficulty) {
		myDatabaseManager.setDifficulty(difficulty);
        AbstractQuestion question = myDatabaseManager.getRandomQuestion();
        if (question != null) {
            myUsedQuestionIds.add(question.hashCode());
        }
        return question;
    }
	
    private void readObject(ObjectInputStream in) 
    		throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        myDatabaseManager = DatabaseManager.getInstance();
    }
}
