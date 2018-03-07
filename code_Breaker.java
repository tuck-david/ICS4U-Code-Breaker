import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Author: David Tuck and Abdalla Date: Mar 1, 2018
 */

public class code_Breaker {
	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {// David and Abdalla
		final int SIZE = 4;
		final int TRIES = 10;
		boolean playAgain = false;
		final String VALID_CHARS = ("GRBYOP");
		System.out.println("Hello and Welcome to the code breaker game");
		do {// Beginning of new game
			System.out.println(
					"If you would like to see the rules please enter 'r' now. Enter anything else to continue");
			if (input.nextLine().equalsIgnoreCase("r")) {
				rules(VALID_CHARS, SIZE);
			}
			System.out.println("\t**********NEW GAME**********");
			int tryCount = 0;// Counts how many guesses user has made
			boolean exit = false;
			String[][] guessHistory = new String[TRIES][SIZE];// 2d array the stores all guesses
			String[][] answerHistory = new String[TRIES][VALID_CHARS.length()];
			for (String[] strings : answerHistory) {// fills array so there is no null
				Arrays.fill(strings, "");
			}
			for (String[] strings : guessHistory) {// fills array so there is no null
				Arrays.fill(strings, "");
			}
			playAgain = false;
			String[] genCode = createCode(VALID_CHARS, SIZE);// genCode is the code the user is trying to guess
			do {// loop for each guess of a game
				if (tryCount >= TRIES) {
					System.out.println("I'm sorry you lose. The correct code was " + Arrays.toString(genCode));
					exit = true;
				} else {
					int writePos = 0;
					System.out.println(Arrays.toString(genCode));
					System.out.println(displayGame(guessHistory, answerHistory, tryCount));
					String[] currentGuess = getinput(SIZE, VALID_CHARS);
					for (int i = 0; i < SIZE; i++) {
						guessHistory[tryCount][i] = currentGuess[i];
					}
					boolean congrads = true;
					for (int i = 0; i < SIZE; i++) {
						if (!currentGuess[i].equalsIgnoreCase(genCode[i])) {
							congrads = false;
						}
					}
					if (congrads == true) {
						System.out
								.println("Congratulations! it took you " + (tryCount + 1) + " guess to find the code");
						exit = true;
						break;
					}
					String[] fullyCorrectAns = findFullyCorrect(genCode, currentGuess);
					currentGuess = removeFullyCorrect(genCode, currentGuess);
					String[] colourCorrctAns = findColourCorrect(genCode, currentGuess);
					List<String> answerHistoryList = new ArrayList<String>();
					for (int i = 0; i < fullyCorrectAns.length; i++) {
						answerHistory[tryCount][writePos] = fullyCorrectAns[i];
						writePos++;
					}
					for (int i = 0; i < colourCorrctAns.length; i++) {
						answerHistory[tryCount][writePos] = colourCorrctAns[i];
						writePos++;
					}
				}
				tryCount++;
			} while (exit == false);
			String playAgainAnswer;
			do {// do loop is for looping the game so you can play more than one game
				System.out.println("Do you want to play again. press y for yes and press n for no");
				playAgainAnswer = input.nextLine();
			} while (valid(("yn"), 1, playAgainAnswer) == false);
			if (playAgainAnswer.equalsIgnoreCase("y")) {
				playAgain = true;
			}
		} while (playAgain == true);
		System.out.println("Thank you for playing code breker we hope you come back soon!!!");
	}

	/**
	 * Generates a size of 4 random color code from the 6 colors that is provided,
	 * that the player has to guess in the game.
	 *
	 * @param VALID_CHARS using the colors in the array to generate the code
	 *        SIZE element of the size/length of the code generated
	 * @return the code generated by math.random * 6, that would contain four of the
	 *         six colors in the list duplicated or not
	 */

	public static String[] createCode(String VALID_CHARS, int SIZE) {// Abdalla
		char[] chars = new char[VALID_CHARS.length() - 1];
		for (int i = 0; i < VALID_CHARS.length() - 1; i++) {
			chars[i] = VALID_CHARS.charAt(i);
		}
		String[] code = new String[SIZE];
		for (int i = 0; i < SIZE; i++) {
			code[i] = Character.toString(VALID_CHARS.charAt((int) (Math.random() * 6)));
		}
		return code;
	}

	/**
	 * Allows the user to input their guess of the code that was randomly generated.
	 *
	 * @param VALID_CHARS
	 *            the colors that the user would have to use when inputting their
	 *            guess SIZE the SIZE/length of the code that the user must guess
	 * @param VALID_CHARS    the colors that the user would have to use when inputting their
	 *                       guess
	 *        SIZE the size/length of the code that the user must guess
	 * @return the guess of the user, if the guess was incorrect the user will be
	 *         able to guess once again, until the last attempt, or guess the
	 *         correct code that was generated
	 *
	 */

	public static String[] getinput(int SIZE, String VALID_CHARS) {
		boolean valid = true;
		char[] chars = new char[VALID_CHARS.length() - 1];
		String answer;
		String again = " ";
		String[] currectAnswer = new String[SIZE];
		do {
			valid = true;
			System.out.print("Please enter your guess" + again + "with a length of " + (SIZE)
					+ " using the capital letters " + VALID_CHARS);
			System.out.println();
			answer = input.nextLine();
			if (valid(VALID_CHARS, SIZE, answer) == false) {
				valid = false;
				again = " again ";
			}
			for (int i = 0; i < currectAnswer.length; i++) {// converts string to single char arrays
				currectAnswer[i] = Character.toString(answer.charAt(i));
			}
		} while (valid == false);
		return currectAnswer;
	}

	/**
	 * Allows the user to input their guess of the code that was randomly generated.
	 *
	 * @param VALID_CHARS
	 *            SIZE guess
	 *
	 *
	 *
	 */
	public static boolean valid(String VALID_CHARS, int SIZE, String guess) {// Abdalla
		boolean guessHas = true;

		if (guess.length() != SIZE) {
			return false;
		} else {
			for (int i = 0; i < SIZE; i++) {
				if (!VALID_CHARS.contains(Character.toString(guess.charAt(i)))) {
					guessHas = false;
				}
			}
			return guessHas;
		}
	}

	public static String[] findFullyCorrect(String[] gencode, String[] currentGuess) {// Abdalla

		int blacks = 0;
		for (int i = 0; i < gencode.length; i++) {
			if (gencode[i].equals(currentGuess[i]))
				blacks++;
		}

		String[] toReturn = new String[blacks];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = "b";
		}
		return toReturn;
	}

	public static String[] removeFullyCorrect(String[] gencode, String[] currentGuess) {// David
		ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(currentGuess));
		for (int i = 0; i < gencode.length; i++) {
			if (gencode[i].equals(currentGuess[i])) {
				arrayList.remove(gencode[i]);
			}
		}
		String[] myArray = arrayList.toArray(new String[arrayList.size()]);
		return myArray;
	}

	public static String[] findColourCorrect(String[] gencode, String[] currentGuess) {// David
		List<String> myArrayList = new ArrayList<String>();
		for (int i = 0; i < currentGuess.length; i++) {
			for (int a = 0; a < currentGuess.length; a++) {
				if (currentGuess[i].equals(gencode[a])) {
					myArrayList.add("w");
				}
			}
		}
		String[] myArray = myArrayList.toArray(new String[myArrayList.size()]);
		return myArray;
	}

	public static String displayGame(String[][] guessHistory, String[][] answerHistory, int tryCount) {// David
		String toDisplay = "Guess \t \tClues\n****************\n";
		String temp;
		for (int i = 0; i < tryCount; i++) {// Adds multiple rows
			for (int j = 0; j < guessHistory[0].length; j++) {// adds guess history
				temp = guessHistory[i][j];
				toDisplay = toDisplay + temp + " ";
			}
			toDisplay += "\t";// format to add new line
			for (int j = 0; j < guessHistory[0].length; j++) {// adds clue history
				temp = answerHistory[i][j];
				toDisplay = toDisplay + temp + " ";
			}
			toDisplay += "\n";// format to add new line
		}
		return toDisplay;
	}

	public static void rules(String VALID_CHARS, int SIZE) {
		System.out.println(" You get 10 attempts at breaking the code. \n"
				+ "If you can't discover it by then, then you lose that game. \n"
				+ "But that's OK. You can get a new code and try again.\n\n" + "The Code Breaker code is made up of "
				+ SIZE + " colors from " + VALID_CHARS.length() + " available colors.\n"
				+ "These colors are symbolized as the following characters+VALID_CHARS+. \n"
				+ "In addition b=black and w= white. To win you must place the  right marbles in the right order.\n"
				+ "After each attempt you will be told how you  went.\n\n"
				+ "White clue - Means that there is one of your colors is in the code but not in  the right place. \n"
				+ "Black clue - Means that there is one of your colors in the code and in the right place. \n"
				+ "The answer clues are not in any particular order and don't line up with your guess colors.");
	}
}
