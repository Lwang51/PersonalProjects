/*		This is a Java program that will ask the user for a sudoku puzzle size. The program will then produce a puzzle for the user.
 * The user will then be able to put in numbers and try to solve the puzzle. The user will also be allowed to ask for hints, but with limits.
 * Since it is impossible to distinguish between the numbers placed by the user and the numbers that are already there, answers will be recorded.
 * After the user finish solveing the puzzle, they can submit the puzzle to check if they are right or wrong. The user can then ask for a new puzzle.
 * Furthermore, if the user thinks the puzzle is too hard, they can pass and get a new one.
 * 
 * This program is part one of the sudoku puzzle project. Part two is a python program that will solve a soduku puzzle of any size.
 * 
 * Most of the concept here are obtained from the planing word doc: Sudoku Java Program Project Plan.docx
 * Backup code of the last sucessful execution is stored in the txt document: Sudoku Backup Code.txt
 */

import java.util.ArrayList;	
import java.util.Comparator;		//to sort arrayList
import java.util.List;
import java.util.Scanner;

public class SudokuPuzzle {
	public static void main(String[] args) {
		Scanner keyboard = new Scanner (System.in);
		List<String[][]> puzzles;		//a list of 2D String arrays; one for the answer key and another for the puzzle
		String[][] ansKey;				//2D String array where the answer key for the sudoku puzzle will be stored
		String[][] sudoku;				//2D String array where the unfinished sudoku puzzle will be stored
		boolean cont = false;			//a boolean variable to decide whether or not to make a new sudoku puzzle
		int puzzleSize;					//an integer variable that stores the size of the sudoku puzzle
		String response;				//String variable that stores the user response of where or not they want a new puzzle

		//Introduction and instruction for the puzzle
		System.out.println("This is a program that will create a sudoku puzzle of any size to your liking for you! \n"
				+ "Sudoku is a critical thinking puzzle. No math is involve here, just your brain along with \n"
				+ "some reasoning and logic! To complete the puzzle, fill in the grid so that every row, \n"
				+ "column, and box contains the digits 1 - (size of puzzle). ");

		//as long as the user wants a new puzzle...
		do {
			System.out.println("What sudoku puzzle size would you like?");		//prompt user for a puzzle size
			puzzleSize = keyboard.nextInt();			//get the puzzle size from the user
			keyboard.nextLine();			//move scanner to next line (currently at line where it takes in puzzle size)
			puzzles = createSudoku(puzzleSize);			//create the puzzle then get unfinished and finished puzzle
			ansKey = puzzles.get(0);			//get the answer key and assign it to ansKey 2D array
			sudoku = puzzles.get(1);			//get the unfinished puzzle and assign it to sudku 2D array
			cont = getAnswers(keyboard, ansKey, sudoku, puzzleSize);			//get user input answers and decide whether 
																				//or not do create a new sudoku puzzle
			if(!cont) {			//will only run if the user completes the puzzle with no wrong answers
				System.out.println("Do you want another puzzle? (y/n)");			//ask the user if they want a new puzzle
				response = keyboard.nextLine();			//get user response
				while (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n")) {			//check response validity
					System.out.println("Please enter a valid response. (y/n)");			//prompt user for a new response
					response = keyboard.nextLine();			//get a new respone from user
				}
				if(response.equalsIgnoreCase("y")) {			//check if user wants to continue
					cont = true;			//set continue to be true to allow a new puzzle to be made
				}
			}
		} while(cont);

		System.out.println("Hope you enjoyed! Thank you!");			//closeing
		keyboard.close();			//close scanner
	}

	//method to generate the sudoku puzzle; called by the main method
	private static List <String[][]> createSudoku(int size) {
		int arraySize = ((2 * size) + 2);							//the total size of the 2D array will always be ([2 * size] + 2)
		List <String[][]> puzzles = new ArrayList <>();				//a arrayList of 2D arrays to store the answer key and unfinished puzzle
		String filledSudoku[][] = new String[arraySize][arraySize];	//2D array to store unfinished puzzle
		String unfilledSudoku[][];									//2D array to store the answer key for the sudoku puzzle
		int[] boxSize = new int [2];								//interger array for two elements: width of puzzle and  height of puzzle
		List <Integer> availibleRow = new ArrayList<>();			//This and the following three variables can be placed in fillPuzzle method,
		List <Integer> availibleCol = new ArrayList<>();			//but this is more space efficient. Since fillPuzzle method is being called
		List <Integer> prevRowCoords = new ArrayList<>();			//N times (where N equals size of puzzle). Placing these variables here
		List <Integer> prevColCoords = new ArrayList<>();			//create just 4 memory spaces for the variables instead of 4*N memory spaces
		int pointer = 2;			//integer variable to act as a pointer around the 2D array
		String count = "1";				//integer variable to number the row and column numbers for the user
		
		for(int i = 0; i < arraySize; i ++) {			//fill the whole array up wth "  " so that its not filled with null
			for (int j = 0; j < arraySize; j++) {		//might not really be neccessary, but easier to work with the array like this
				filledSudoku[i][j] = "  ";
			}
		}

		boxSize = getPuzzleBoxSize(size);			//get the height and width of each boxes in the sudoku puzzle in respective order

		filledSudoku[1][1] = " ┌";									//set the boarder for upper left corner of the puzzle
		filledSudoku[1][(arraySize - 1)] = "┐ ";					//set the boarder for upper right corner of the puzzle
		filledSudoku[(arraySize - 1)][1] = " └";					//set the boarder for lower left corner fo the puzzle
		filledSudoku[(arraySize - 1)][(arraySize - 1)] = "┘ ";		//set the boarder for lower right corner of the puzzle

		//use the 0th row and 0th column to make a row and column number boarder to allow user to reference where to place their answer
		while(pointer < arraySize) {
			if (Integer.parseInt(count) < 10) {			//if the number is less then 10, add a '0' in front of it, so that it become two digits
				count = "0" + count;
			}
			filledSudoku[0][pointer] = count;			//use the 0th row to hold the column numbers
			filledSudoku[pointer][0] = count;			//use the 0th column to hold the row numbers
			count = String.valueOf(Integer.parseInt(count) + 1);			//change count into integer, incremnt it, then change it back to String
			pointer += 2;			//increment the pointer by 2, because only even numbered array index element will be used store answers while
		}							//odd numbered array index elements are used to store the puzzle barder designs

		//reset pointer, then create the boarder design for the outer boarder of the puzzle
		pointer = 2;
		while(pointer < (arraySize - 1)) {
			filledSudoku[1][pointer] = "══";					//the top boarder will have the design of "══"
			filledSudoku[pointer][1] = " ǁ";					//the left boarder will have a design of " ǁ"
			filledSudoku[(arraySize - 1)][pointer] = "══";		//the bottom boarder will have a design of "══"
			filledSudoku[pointer][(arraySize - 1)] = "ǁ ";		//the right boarder will have a design of "ǁ "
			pointer ++;
		}

		//get the width and height of the boxes inside the puzzle (the fist and secodn element of the boxSize array respectively),
		//and widthPointer and heightPointer integer variable are ones that points to the end of the box width and height
		int boxWidth = ((2 * boxSize[0]) + 1);
		int boxHeight = ((2 * boxSize[1]) + 1);
		
		for(int i = 2; i < (arraySize - 1); i++) {			//i and j start at 2, because all 0th and 1st elements are set by two while loops above
			for(int j = 2; j < (arraySize - 1); j++) {
				if(((i % 2) == 0) && ((j % 2) != 0) && (j != boxWidth)) {			
						filledSudoku[i][j] = " |";			//if i is even, and j is odd and within a box width, set index element to " |"
				}
				else if(((i % 2) != 0)) {
					if((i < boxHeight)) {
						if((j < boxWidth) && ((j % 2) == 0)) {			//if i is odd and j is even, and both are within a box...
							filledSudoku[i][j] = "--";						//set that index element to "--"
						}
						else if((j < boxWidth) && ((j % 2) != 0)) {			//if i and j are both odd, and both are within a box...
							filledSudoku[i][j] = " +";							//set that index element to " +"
						}
					}
					else if((i == boxHeight)) {
						if(j < boxWidth) {			//if i is at a horizontal box boarder, and j is within a box...
							filledSudoku[i][j] = "══";	//set that index element to "══"
						}
						else if(j == boxWidth) {			//if both i and j is at a box boarder...
							filledSudoku[i][j] = "═╬";			//set that index element to "═╬"
							boxWidth += (((2 * boxSize[0]) + 1) - 1);			//move the width pointer to the next box width
						}
					}
				}
				if((j == boxWidth) && (i != boxHeight)) {//if j is at a box vertical boarder, but i is not at a horizontal box boarder, set index
					filledSudoku[i][j] = " ǁ";			 //element to " ǁ" and move the width pointer to the end of the next box
					boxWidth += (((2 * boxSize[0]) + 1) - 1);
				}

//				System.out.print(filledSudoku[i][j]);			//For debugging use
			}
//			System.out.println();						//for debugging use

			boxWidth = ((2 * boxSize[0]) + 1);			//once a row is iterated throught, reset the widthPointer
			if(i == boxHeight) {						//once a row of boxes are iterated through, move the heightPointer to the next box
				boxHeight += (((2 * boxSize[1]) + 1) - 1);
			}
		}

		count = "1";
		while(Integer.parseInt(count) <= size) {			//completly fill the puzzle with numbers, this will become the answer key
			if (fillPuzzle(filledSudoku, count, boxSize[0], boxSize[1], prevRowCoords, prevColCoords, availibleRow, availibleCol)) {
				count = String.valueOf(Integer.parseInt(count) + 1);			//if a number is sucessfully placed in the puzzle, move on
			}
			else {
				count = "1";			//if any number fails to be sucessfully, placed into the puzzle for a x amount of time, the program
			}							//is designed to discard all the past numbers and restart from the beginning
		}

		puzzles.add(filledSudoku);			//add the answer key (the one made from the while loop above) to the list of 2D arrays
		unfilledSudoku = whiteOutNum(filledSudoku, arraySize, boxSize[0], boxSize[1], availibleRow, availibleCol);   //randomly drop numbers
		puzzles.add(unfilledSudoku);			//add the unfinished puzzle (made from the method above) to the list of 2D arrays

		return puzzles;			//return the answer key 2D array and the unfinished sudoku 2D array to the main method
	}

	//method to get the width and height of the boxes int he sudoku puzzle; called by the createSudoku() method
	private static int [] getPuzzleBoxSize(int size) {
		int[] temp = new int [2];			//declare and initialze a array of two elemts to store width and height
		double sqrtSize = Math.sqrt(size);			//take the square root of the sudoku size

		if(sqrtSize == 0.0) {
			temp[0] = (int)sqrtSize;			//if the size is a perfect square root, then the width and height
			temp[1] = (int)sqrtSize;			//are the square roots of the size
			return temp;			//return the array to the createSudoku method
		}
		else {
			while(size % Math.floor(sqrtSize) != 0) {			//if size is not perfect square root, check if
				sqrtSize -= 1;									//square root of (square rooted size - 1) is perfect
			}													//continue on with this until it is

			temp[0] = (int)Math.floor(sqrtSize);			//the width is the floor of the adjusted root of size
			temp[1] = size / temp[0];			//the height is the size of puzzle divided by the width
			return temp;			//return the array to the createSudoku method
		}		
	}
	
	//method to implement an AI to fill the sudoku with numbers; called by the createSudoku() method
	private static boolean fillPuzzle(String[][] sudoku, String num, int w, int h, List<Integer> rowRec, List<Integer> colRec, 
									  List<Integer> rows, List<Integer> cols) {
		int rowLimit = (h - 1);			//the end pointer for the rows that can be randomly chosen
		int colLimit = (w - 1);			//the end pointer for the columns that can be randomly chosen
		int colMin = 0;					//the starting pointer for the columns that can be randomly chosen
		int rowNum = 0;					//used to store the row number that is chosen
		int colNum = 0;					//used to store the column number that is chosen
		int tries = 0;					//used to record the number of tries to get a valid row and column number
		int trial = 1;					//used to record the number of times the program tries to reorder the number
		int clearCount = 1;				//used to record how many times the program has cleared the number from the board
		int temp = 0;					//a dummy variable used to temporary store other variables

		if(Integer.parseInt(num) < 10) {
			num = "0" + num;			//make every number two digits since each element in array consist of two spaces
		}

		for(int i = 2; i < sudoku[0].length; i += 2) {			//add all availible rows and columns to rows and cols array
			rows.add(i);
			cols.add(i);
		}

		outerLoop1:
			for(int i = 0; i < w; i ++) {
				for(int j = 0; j < h; j ++) {
					do {													//try to get a valid row and col with N amount 
						//(Math.random() * ((max - min) + 1)) + min			//of attampt, where N equals the size of box
						rowNum = (int)(Math.random() * ((rowLimit - 0) + 1)) + 0;
						colNum = (int)(Math.random() * ((colLimit - colMin) + 1)) + colMin;
						tries ++;
					} while((!sudoku[rows.get(rowNum)][cols.get(colNum)].replaceAll("\\s","").isEmpty()) && tries < (w * h));

					//The following section deads with deadlock issues. Deadlocks as in reference to when the program has no
					//where else to put a number into a specific index. There is already a number in that specific box, and
					//all the column in the specific row is used. Also, there is no other rows to place the number without
					//breaking the criteria of a sudoku puzzle. The following if statement is only executed when the program
					//was unsuccessful in geting a row and col within N amount of tries.
					//_____________________________________________Combating Deadlocks_____________________________________________
					if(tries >= (w * h)) {
						//below if statement clears the puzzle of the current number
						if(trial == w) {		//not set number; should be in perspective of the puzzle size (can be w or h)
							temp = rowRec.size();				//to store a set rowRec array size, as the array size will
							for(int k = 0; k < temp; k++) {		//change through out the for loop below
								rowNum = rowRec.get(rowRec.size() - 1);		//This for-loop block basically goes all previous 
								colNum = colRec.get(colRec.size() - 1);		//indexes where number was places, and clear it.
								//colMin -= ((w - 1) - i);					//Basically,program has failed to shift numbers
								sudoku[rowNum][colNum] = "  ";				//many times to, so now, it will clear number
								rows.add(0, rowNum);						//from puzzle and retry.
								cols.add(colNum);			//replaced next line of commented code (paired with col.sort())
								//openCol.add(colMin, colNum);			//plan was to have the program know where to insert
								rowRec.remove(rowRec.size() - 1);		//selected column number (which reduces the runtime),
								colRec.remove(colRec.size() - 1);		//but doesn't really work (the above commented out
							}											//code is also part of this)
							cols.sort(Comparator.comparing(Integer::valueOf));			//can remove if above said plan works
							clearCount++;					//keep count of how many time program cleared puzzle of number
							i = -1;				//reset the variable
							j = 0;				//reset the variable
							rowLimit = (h - 1);	//reset the variable
							colLimit = (w - 1);	//reset the variable
							colMin = 0;			//reset the variable
							tries = 0;			//reset the variable
							trial = 0;			//reset the variable
							continue outerLoop1;
						}
						//below else if statement clears the whole puzzle of numbers
						else if((clearCount == 3)) {
							for(int k = 2; k <= (sudoku[0].length - 2); k += 2) {			//current number has been removed from
								for(int l = 2; l <= (sudoku[0].length - 2); l += 2) {		//puzzle three times, but still is not
									sudoku[k][l] = "  ";									//successful with placement
								}
							}
							rows.clear();			//reset rows, cols, and their records
							cols.clear();
							rowRec.clear();			//number has failed to be placed throughout sudoku, tell createSudoku() to
							rowRec.clear();			//the numbering from very beginning (1).
							return false;
						}
						//below else if statement swaps the column positions of the number in the top box if availible
						else if(rowRec.size() > h) {			//make sure that the program is not one the first row of boxes
							temp = rowRec.get(rowRec.size() - w);						//it runs into a deadlock, but only if it is
							if(sudoku[temp][cols.get(colNum)].equals("  ")) {			//not on the first row of boxes
								sudoku[temp][colRec.get(colRec.size() - w)] = "  ";
								sudoku[temp][cols.get(colNum)] = num;				//finds row number where current number in top
								temp = cols.get(colNum);							//box is located, then check if trouble column
								cols.set(colNum, colRec.get(colRec.size() - w));	//at that row is empty. If so, move number to
								colRec.set((colRec.size() - w), temp);				//that column, and update col and colRec
								
								if(sudoku[rows.get(rowNum)][cols.get(colNum)].equals("  ")) {
									sudoku[rows.get(rowNum)][cols.get(colNum)] = num;
									rowRec.add(rows.get(rowNum));
									colRec.add(cols.get(colNum));
									rows.remove(rowNum);				//check and see if current row and updated col is availible
									cols.remove(colNum);				//and place number there if so
									rowLimit--;
									colMin += ((w - 1) - i);			//this if statement has the same concept as the else
									colLimit += ((w  - 1)- i);			//statement below
									continue;
								}
							}
						}
						//below else if statement clears the number from the row of boxes 
						if((rowRec.size() > 0) && (colRec.size() > 0)) {
							rowLimit = (h - 1);			//reset the row limit
							for(int k = 0; k < j; k++) {			//the j here acts like a box counter (how many boxes the
								colLimit -= ((w  - 1)- i);			//program has iterated through on a row of boxes)
								colMin -= ((w - 1) - i);
								rowNum = rowRec.get(rowRec.size() - 1);
								colNum = colRec.get(colRec.size() - 1);			//"undo" colLimit and colMin count while
								sudoku[rowNum][colNum] = "  ";					//getting last location of where the number was
								rows.add(0, rowNum);							//placed. Then, clear that index, and add index
								cols.add(colMin, colNum);						//location to rows and cols arrayList and remove
								rowRec.remove(rowRec.size() - 1);				//the index location from the row and col record
								colRec.remove(colRec.size() - 1);
							}
							trial++;			//only place where a trial counts
						}
						j = -1;			//only the last two if staments will/can execute this
					}
					//___________________________________________End of Combating DeadLocks__________________________________________
					else {
						sudoku[rows.get(rowNum)][cols.get(colNum)] = num;	//this else statement executes if everthing runs smoothly
						rowRec.add(rows.get(rowNum));			//put the number in the correcsponding location of the sudoku array,
						colRec.add(cols.get(colNum));			//then add the location information to the row and col records while
						rows.remove(rowNum);					//removing them from the rows and cols arrayList
						cols.remove(colNum);			//The program is using every row in a set/range of row before moving on to
						rowLimit--;						//next range/set, so its limit pointer only needs to be decremented by one
						colMin += ((w - 1) - i);		//as a row number is removed. For columns, program only uses one per set, so
						colLimit += ((w  - 1)- i);		//its limit and min pointer must move to next set as it removes a col number.
					}

//					System.out.println("RowLimit: " + rowLimit + ", \tColLimit: " + colLimit + "\tColMin: " + colMin + 
//							"\nopenRow: " + rows.toString() + "\tprevRowCoords: " + rowRec.toString() + 
//							"\nopenCol: " + cols.toString() + "\tprevColCoords: " + colRec.toString() +
//							"\nI: " + i + ", \tJ: " + j + "\ttries: " + tries + "\ttrial: " + trial);
//					printArray(sudoku);
					
					tries = 0;			//reset tries
				}
				colLimit = ((w - 1) - (i + 1));			//reset colLimit as the program moves on to the next set of row of boxes
				colMin = 0;			//reset colMin as the program moves on to the next set of row of boxes
				rowLimit = (h - 1);			//reset rowLimit as it moves to next set/range (represents new set of rows of boxes)
			}
		rowRec.clear();			//the number was sucessfully placed in the sudoku, clear its row record
		colRec.clear();			//the number was sucessfully placed in the sudoku, clear its col record
		return true;			//number was sucessfully placed throughtout sudoku; tell createSudoku() to move on to next number
	}

	//method to randomly erase numbers fromt he sudoku; called by the createSudoku() method
	private static String[][] whiteOutNum(String[][] key, int arraySize, int w, int h, List<Integer> rows, List<Integer> cols) {
		String sudoku [][] = new String[arraySize][arraySize];			//a second 2D array to deep copy the answer key
		double num;						//a doble varibale used to store the number of numbers to be dropped in each box
		int dropCount;					//an integer number used to track how many numbers have been dropped
		int rowLimit = (h - 1);			//the end pointer for the rows that can be randomly chosen
		int rowMin = 0;					//the start pointer for the rows that can be randomly chosen
		int colLimit = (w - 1);			//the end pointer for the columns that can be randomly chosen
		int colMin = 0;					//the start pointer for the columns that can be randomly chosen
		int rowNum = 0;					//used to store the row number that is chosen
		int colNum = 0;					//used to store the column number that is chosen

		for(int i = 0; i < key[0].length; i++) {
			for(int j = 0; j < key[0].length; j++) {			//deep copy the answer key array to another 2D array
				sudoku[i][j] = key[i][j];						//this way, the program can work with the second 2d array
			}													//without changing the answer key array
		}

		for(int i = 2; i < key[0].length; i += 2) {			//add availible row and columns to its prespective arraayList
			rows.add(i);
			cols.add(i);
		}

		for(int i = 0; i < (w * h); i++) {
			num = ((w * h) * ((Math.random() * (0.75 - 0.50)) + 0.50));			//get a number that's 50-705% of box size

			if((num - ((int)num)) >= 0.50) {
				dropCount = ((int)num + 1);				//if num's decimal value is above .5, increment it by 1
			}											//otherwise, if its decial value is below .25, decrement it by 1
			else if((num - ((int)num)) >= 0.25){
				dropCount = (int)num;
			}
			else {
				dropCount = ((int)num - 1);
			}

			for(int j = 0; j < dropCount; j++) {			//in each box, drop num amount of numbers
				do {			//get a valid row and col to drop its number
					rowNum = (int)(Math.random() * ((rowLimit - rowMin) + 1)) + rowMin;
					colNum = (int)(Math.random() * ((colLimit - colMin) + 1)) + colMin;
				} while (sudoku[rows.get(rowNum)][cols.get(colNum)].replaceAll("\\s","").isEmpty());

				sudoku[rows.get(rowNum)][cols.get(colNum)] = "  ";
			}

			if((colMin + w) < cols.size()) {			//move to the next availible box on the row of box
				colLimit += w;
				colMin += w;
			}
			else {							//if there is no more boxes left on the row of boxes,
				rowLimit += h;				//move to the next row of boxes and rest the colLimit and colMin
				rowMin += h;
				colLimit = (w - 1);
				colMin = 0;
			}
		}

		System.out.println("HERE IS A " + w + " BY " + h + " SUDOKU PUZZLE!");			//give user the puzzle
		printArray(sudoku);	
//		printArray(key);
		
		return sudoku;			//pass the unfinished sudoku to createSudoku() method
	}

	//method to obtain the answers from the user; called by main method
	private static boolean getAnswers(Scanner input, String[][] ansKey, String[][] sudoku, int puzzleSize) {
		ArrayList <String> ansRec = new ArrayList <> ();			//to hold the record for user answers
		String ans;			//String variable to store user input
		int hint = 0;		//integer variable to count how many hints are left
		int row;			//integer variable to get the row number from user input
		int col;			//integer variable to get the column number form user input
		String num;			//Strign variable to store the user's answer for a spcific point
		
		//user instructions for simple input to play the sudoku
		System.out.println("Please enter your answers in the following format of \"row, col, answer\"" +
				"\nOnce you are done with the puzzle, simply leave the answer blank and hit \"Enter\"" +
				"\nTo replace an old answer, simply type the new answer with the said format. " +
				"\nHints can also be provides. However, they are limited depending on the size of the puzzle." +
				"\nTo get a hint, type \"hint\" into the prompt." +
				"\nSince it will be hard to differentiate between your answers and the numbers that are " + 
				"\nalready there, a record of your answers will be kept. To access this, type \"ans\" in the prompt." + 
				"\nLastly, if the puzzle is too hard, type \"pass\" for a new puzzle.\n");

		do {			//do the following as long as th user inoput is not empty
			do {
				System.out.println("Answer: ");			//prompt user for an answer
				ans = input.nextLine();			//get an answer from the user
			} while(!isValid(sudoku, ans, puzzleSize));			//check if the recieved input is a valid one
			
			//if user input is empty...
			if(ans.isEmpty()) {
				System.out.println("Submitting the puzzle? (y/n)");			//check if user accidently pressed "enter"
				ans = input.nextLine();			//get user's response
				while (!ans.equalsIgnoreCase("y") && !ans.equalsIgnoreCase("n")) {
					System.out.println("Invalid input. Please enter \"y\" or \"n\".");
					ans = input.nextLine();			//get a new user response
				}
				if(ans.equalsIgnoreCase("y")) {			//jump out of the while loop with response is "y"
					break;
				}
				else {
					continue;			//continue on with the do-while loop otherwise
				}
			}
			//if user response is "pass"...
			else if(ans.equalsIgnoreCase("pass")) {
				return true;			//tell the main method to create a new sudoku puzzle
			}
			//if user response is "ans"...
			else if(ans.equalsIgnoreCase("ans")) {
				System.out.println("Here are your previous answers:");			//print the ansRec arrayList
				System.out.println("\tRow:\tCol:\tAns:");
				for(int i = 0; i < ansRec.size(); i++) {
					ans = ansRec.get(i);
					System.out.println("\t" + ans.substring(0, ans.indexOf(',')).replaceAll("\\s+", "") + 
							"\t" + ans.substring(ans.indexOf(',') + 1, ans.indexOf(',', ans.indexOf(',') + 1)).replaceAll("\\s+","") + 
							"\t" + ans.substring(ans.lastIndexOf(',') + 1).replaceAll("\\s+", ""));
				}
				continue;
			}
			//if user response is "hint"...
			else if(ans.equalsIgnoreCase("hint")) {
				if(hint < puzzleSize) {			//check if user has any more hints left (amount of hints given to the user is equal to
					outerForLoop:				//the size of the puzzle, also equivalent to a whole box)
					for(int i = 2; i <= sudoku[0].length - 2; i += 2) {				//traverse through the 2D array for the first "<>"
						for(int j = 2; j <= sudoku[0].length - 2; j += 2) {			//(if user decided to call for hints while making
							if(sudoku[i][j] == "  " || sudoku[i][j] == "<>") {		//corrections) or "  "
								num = ansKey[i][j];			//get the number from the answer key
								sudoku[i][j] = num;			//copy it over to the unfinished puzzle
								hint++;			//decrease the amount of hints left by one
								System.out.println("Given hint:" + "\n\tRow: " + (i / 2) + "\tCol: " + (j / 2) + "\tNum: " + num);
								System.out.println("Hints left: " + (puzzleSize - hint));			//show user where the hint was
								printArray(sudoku);													//placed and show remaining hints
								break outerForLoop;			//exit the for-loops
							}
						}
					}
					continue;
				}
				else {
					System.out.println("Sorry, out of hints...");
					continue;
				}
			}
			
			//get substring of input starting from beginning to first "," while removing all white spaces; convert it to an integer
			row = Integer.parseInt(ans.substring(0, ans.indexOf(',')).replaceAll("\\s+", ""));
			
			//get substring of input starting from first "," to next ",", while removing all white spaces; convert it to an integer
			col = Integer.parseInt(ans.substring(ans.indexOf(',') + 1, ans.indexOf(',', ans.indexOf(',') + 1)).replaceAll("\\s+",""));
			
			//get substring from the last occurance of "," to the end of input, while removing all white spaces
			num = ans.substring(ans.lastIndexOf(',') + 1).replaceAll("\\s+", "");

			if(Integer.parseInt(num) < 10) {			//like before, make all numbers to have two digits
				num = "0" + num;
			}
			
			//every row and column number can be obtained by its index in the 2D array and dividing it by to
			//can also apply reverse phycology to get index number form row and col number like below
			//make sure that user cannot modify the original puzzle by only allowing answers that are the same as the one in the
			//answer key or that the index element in the sudoku is not equal to the index element in the ansKey
			if(!sudoku[(row * 2)][(col * 2)].equals(ansKey[(row * 2)][(col * 2)]) || num.equals(ansKey[(row * 2)][(col * 2)])) {
				sudoku[(row * 2)][(col * 2)] = num;			//insert the answer
				ansRec.add(ans);			//record user input
				printArray(sudoku);			//print updated sudoku puzzle
			}
			else {
				System.out.println("Invalid attemp to change the puzzle. Please enter a new set of answers.");
				continue;
			}
		} while(!ans.isEmpty());
		
		if(!checkAns(ansKey, sudoku)) {			//check if there is any wrong answers
			System.out.println("Do you want to make corrections? (y/n)");			//prompt user if they want to make corrections
			ans = input.nextLine();			//get user response
			while (!ans.equalsIgnoreCase("y") && !ans.equalsIgnoreCase("n")) {			//check the validity of user response
				System.out.println("Invalid input. Please enter \"y\" or \"n\".");			//prompt user for new response if invalid
				ans = input.nextLine();			//get new response if invalid
			}
			if(ans.equalsIgnoreCase("y")) {			//recurse through the method if user wants to make corrections
				getAnswers(input, ansKey, sudoku, puzzleSize);
			}
		}
		
		return false;			//user has accurately completed puzzle; tell main() to ask user if they want a new sudoku puzzle
	}

	//method to check if in answer input is valid; called by getAnswer() method
	private static boolean isValid(String[][] puz, String a, int size) {
		//if user input is blank, "ans", "hint", or "pass", validate it
		if(a.isEmpty() || a.replaceAll("\\s+", "").equalsIgnoreCase("ans") || 
		   a.replaceAll("\\s+", "").equalsIgnoreCase("hint") || a.replaceAll("\\s+", "").equalsIgnoreCase("pass")) {
			return true;
		}
		else {
			//try the following three if statements and catch number format and string index out of bound exceptions
			try {
				//get the row from "row, col, num", and check if it is greater or than equal to size or less than 1. Validate if not.
				if(((Integer.parseInt(a.substring(0, a.indexOf(',')).replaceAll("\\s+", "")) * 2) >= size) ||
				   ((Integer.parseInt(a.substring(0, a.indexOf(',')).replaceAll("\\s+", "")) * 2) < 1)) {
					System.out.println("Invalid row. Please enter a new set of answer.");
					return false;
				}
				//get the col from "row, col, num", and check if it is greater than or equal to size or less than 1. Validate if not.
				if((Integer.parseInt(a.substring(a.indexOf(',') + 1, a.indexOf(',', a.indexOf(',') + 1)).replaceAll("\\s+","")) >= size) ||
				   (Integer.parseInt(a.substring(a.indexOf(',') + 1, a.indexOf(',', a.indexOf(',') + 1)).replaceAll("\\s+","")) < 1)) {
					System.out.println("Invalid column. Please enter a new set of answer.");
					return false;
				}
				//get the num from "row, col, num", and check if it is greater than 0 or less than equal to size. Invalidate if not.
				if((Integer.parseInt(a.substring(a.lastIndexOf(',') + 1).replaceAll("\\s+", "")) <= 0) || 
				   (Integer.parseInt(a.substring(a.lastIndexOf(',') + 1).replaceAll("\\s+", "")) > size)) {
					System.out.println("Invalid answer. Please enter a new set of answer.");
					return false;
				}
			} catch(NumberFormatException e) {			//if there are letters
				System.out.println("Invalid input. Numbers only. Please enter a new answer.");
				return false;
			} catch(StringIndexOutOfBoundsException e) {			//if input is not in the right format
				System.out.println("Invalid input format. Please enter a new answer.");
				return false;
			}
		}
		return true;
	}

	//method to check if the submitted sudoku puzzle is correct and mark the incorrect answers; called by getAnswer() method
	private static boolean checkAns(String[][] ansKey, String[][] sudoku) {
		int wrong = 0;			//integer variable to keep track of how many wrong answers there are
		
		for(int i = 2; i <= sudoku[0].length - 2; i += 2) {
			for(int j = 2; j <= sudoku[0].length - 2; j += 2) {			//traverse through the sudoku and compare it with the answer
				if(!sudoku[i][j].equals(ansKey[i][j])) {				//key, if their is a difference, mark it with a "<>" and
					sudoku[i][j] = "<>";								//increment the wrong variable counter
					wrong++;
				}
			}
		}
		
		System.out.println("Here is the result of the puzzle:");
		printArray(sudoku);			//show user the result
		if(wrong > 0) {
			System.out.println("Wrong answers are marked with with a \"<>\"");		
			return false;			//return false if there is at least one wrong answer

		}
		else {
			System.out.println("Congratulations!!! It is all right!");
			return true;			//retrun true is the sudoku is all correct
		}
	}
	
	//method to print out a 2D array
	private static void printArray(String[][] array) {
		for(int i = 0; i < array[0].length; i++) {			//simply traverse through the whole array and print out each element
			for(int j = 0; j < array[0].length; j++) {
				System.out.print(array[i][j]);
			}
			System.out.println();
		}
	}
}			//481 LINES OF PURE CODE (ONES NEEDED FOR THIS PROGRAM TO RUN). REST ARE EITHER COMMENTs, DEBUG CODES, OR SPACES