/* This projects takes x amount of schedules times from x amount of user. The program will then take in an event time and
 * calculate whether or not the event will fit into the schedule(s), and if it does, it will print out the availible times.
 * 
 * This project was inspried by a Google coding interview for an internship position
 * 		- the interview had two given 2D array of times instead of user input
 * https://www.youtube.com/watch?v=3Q_oYDQ2whs
*/

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AvailableTime {
//============================================================================================INSTANCE VARIABLES===========//
	//a list of arraylist of string arrays that hold the schedules of each user
			//each string array stores a start and end time
			//each arraylist represents a schedule of an user
			//the list contains all the schedule arraylists
	private static List<ArrayList <String[]>> schedules = new ArrayList<ArrayList<String[]>>();	
	
	//a arraylist of float arrays that stores all the unavailible times in float (for easy calculations)
	private static ArrayList<float[]> masterSchedule = new ArrayList<float[]>();
	
	//universal scanner so that it can be used throughout methods instead of only in the main method
	private static Scanner keyboard = new Scanner (System.in);
	
	//used instance variables, because multiple methods needs to have access to each of them
	private static String earliest, latest, eventTime;
//=========================================================================================================================//
	public static void main(String[] args) {			
		System.out.println("This program takes in a 24-hour schedule from user(s) and then a duration of an event. \n" + 
						   "It will then produce a list of time periods where the event will fit into everyone's schedule.");
		
		//comment out following do-while loop (and one in getOpenTimes()) and getTimes() surpressed warning
//		test();
		
		//variable to hold the condition whether or not a new schedule will be added
		String cont = null;
		do {
			//get the schedules of an user
			getTimes();
			
			//check if there is more schedules to be inserted
			System.out.println("Is there another schedule that you would like to add? (Y/N)");
			cont = keyboard.nextLine();
			//check if response was valid
			while (!cont.trim().equalsIgnoreCase("y") && !cont.trim().equalsIgnoreCase("n")) {
				System.out.println("Invalid decision. PLease choose \"y\" or \"n\".\n"
						         + "Is there another schedule that you would like to add? (Y/N)" );
				cont = keyboard.nextLine();
			}
		} while(cont.equalsIgnoreCase("y")); //reloop if there is another schedule to be added
		
		System.out.println("Here are your schedules:");
		printList(schedules);
		
		System.out.println("Here are the earliest and latest available times: \n"
						 + "From: \t" + earliest + "\n"
						 + "To: \t" + latest);
		
		System.out.println("How long will the event be (hour:minute)?");
		getOpenTime();
		
		keyboard.close();
	}
	
	//a method that checks whether inputed time was a valid time
	private static boolean check (String a) {
		try {
			int hour = Integer.parseInt(a.substring(0, a.indexOf(':')).trim());			//get the hour of the time
			int minute = Integer.parseInt(a.substring((a.indexOf(":") + 1)).trim());	//get the minute of the time
			
			//invalid if hour is not between 0 and 24, minute is not between 0 and 60, and minutes does not have 2 chars
			if((hour < 0 || hour > 24) || (minute < 0 || minute > 59) ||
				a.substring(a.indexOf(":") + 1).trim().length() != 2 ) {
				System.out.println("\tInvalid time format. Please try again (hous:minutes).");
				return false;
			}
			return true;		//valid input of time
		} catch (NumberFormatException nfe) {
			System.out.println("\tInvalid input. Please try again (only numbers allowed).");
			return false;
		} catch (StringIndexOutOfBoundsException exception) {
			System.out.println("\tPlease also provide minutes (hous:minutes)");
			return false;
		}
	}
	
	//a method that returns a list of avaliable times in the schedules of every user that the event can take place
	private static ArrayList<float[]> findOpenTimes(ArrayList<float[]> list) {
		ArrayList<float[]> openTimes = new ArrayList<float[]>();	//a "schedule" to stroed the open times
		float[] time = null;										//a array to store times
		int pointer = 0;											//pointer to keep track of unavailible time
		float currentTime = toFloatTime(earliest);					//the time
		
		//dependng on the machine running this program, we could declare and initiate a variable to store 
		//toFloatTime(variable) to reduce the number of cycle and time needed to execute. Current version uses more
		//time, but less memory space as it call the method each time
		
		while (currentTime <= toFloatTime(latest)) {
			//if current time is at/in between unavailable time, jump current time to the end of the unavailable time
			if((currentTime >= list.get(pointer)[0]) && (currentTime <= list.get(pointer)[1])) {
				currentTime = list.get(pointer)[1];
				if (pointer + 1 != list.size()) {
					pointer++;		//move the pointer to point at next available time
				}
			}
						
			//if there is time in between the current time and the next unavailable or the latest time, 
			//add the time to the list of availible times
			//the +0.05 at the end is to loosen the restriction on the time for super small event times (1 min)
			if(((currentTime + toFloatTime(eventTime)) <= (list.get(pointer)[0] + 0.05)) ||
			  ((currentTime + toFloatTime(eventTime)) >= list.get(pointer)[1]) && 
			  ((currentTime + toFloatTime(eventTime)) <= (toFloatTime(latest) + 0.05))) {
				time = new float[2];		//must create a new time array each loop so that no overrides happen
				time[0] = currentTime;
				time[1] = (currentTime + toFloatTime(eventTime));
//				System.out.print("[" + toStringTime(time[0]) + ", "); 			//for debugging use
//				System.out.println(toStringTime(time[1]) + "]"); 				//for debugging use
				openTimes.add(time);
			}	
			
			currentTime += toFloatTime(eventTime);		//only if current time is in between unavailable times
		}
		
		return openTimes;		//return a list of available times
	}
	
	//"master" method that calls other methods to find the free times
	private static void getOpenTime() {
//		eventTime = "1:30";		//uncomment if testing and comment out following do-while loop
		
		do {
			System.out.println("Event Time:");
			eventTime = keyboard.nextLine();
		} while (!check(eventTime));
		
//		printArrayListFloat(masterSchedule); 					//for debugging use
		simplify(masterSchedule);
//		printArrayListFloat(masterSchedule);					//for debugging use
//		findOpenTimes(masterSchedule);							//for debugging use
//		printArrayListFloat(findOpenTimes(masterSchedule));		//for debugging use
		
		//'copy' must not be altered altering it will alter the findOpenTimes() array
		ArrayList<float[]> copy = findOpenTimes(masterSchedule);
		
		if (copy.size() != 0) {
			System.out.println("Here is the open space(s) in everyone's schedule for the event:");
			printArrayListFloatToString(copy);	
		}
		else {
			System.out.println("Sorry, no open spaces found in the schedules for the event!");
		}
	}
	
	//method that gets the time for the schedule of an user (long, but simple)
//	@SuppressWarnings("unused")
	private static void getTimes() {
		String start, end, early, late;		//to hold time and schedule information
		ArrayList <String []> userSchedule = new ArrayList<>();		//the "schedule"
		boolean redo;		//for when user inputs a invalid time
		
		//find the earliest to the latest time that the user is available and check the inputs
		System.out.println("When are you available?");
		do {
			redo = false;		//reset the redo
			
			do {		//find the earliest hour and see if input is valid
				System.out.println("From (earliest time):");
				early = keyboard.nextLine();
			} while(!check(early));
			
			do {		//find the latest hour and see if input is valid
				System.out.println("To (latest time):");
				late = keyboard.nextLine();
			} while (!check(late));
			
			//is the the tie range valid (is earliest time before latest time). Get new times if not
			if (toFloatTime(early) > toFloatTime(late)) {
					System.out.println("\tInvalid start and end times. Please try again.");
					redo = true;
			}
		} while (redo);		//get new available times if needed
		
		updateOverallAvailability(early, late);		//see if earliest or latest time needs to be changed
		
		System.out.println("Please enter your schedule below in 24-hour format (hous:minutes): ");
		System.out.println("Once you are done, leave the \"start\" field blank, and press \"ENTER\".");
		
		outerloop:		//loop label
		do {
			String [] time = new String [2];		//an array that stores the start and end string times
			
			do {
				redo = false;		//reset the redo 
				
				innerLoop1:
				do {
					System.out.print("Start Time: ");		//get start time
					start = keyboard.nextLine();
					
					if (start.isEmpty()) {		//break to outerloop (user has finished enter their schedule)
						break outerloop;		//as per given direction
					}
					
					//check the validity of the input start time
					if (check(start)) {
						if (toFloatTime(start) < toFloatTime(early)) {
							System.out.println("This time is too early for you! Please try another time!");
							redo = true;
							continue innerLoop1;	//skip rest of the loop to get new time if start time
						}							//is earlier than user's earliest available time
						
						redo = false;		//start is valid, go on to innerLoop2
						break innerLoop1;
					}
					else {
						redo = true;		//the start input contains an error
					}
				} while (redo);		//get a new start time if needed
			
				innerLoop2:
				do {
					System.out.print("End Time: ");		//get end time
					end = keyboard.nextLine();

					if (end.isEmpty()) {		//skip the rest of the code and get an end time if empty
						System.out.println("\tPlease provide an end time:");
						redo = true;
						continue innerLoop2;
					}
					
					//check the validity of the input end time
					if (check(end)) {
						if (toFloatTime(end) > toFloatTime(late)) {
							System.out.println("This time is too late for you! Please try another time!");
							redo = true;
							continue innerLoop2;		//skip rest of the loop to get a new time if end time
						}								//is later than user's latest available time
						
						redo = false;		//end time is valid, get out of the while loop
						break innerLoop2;
					}
					else {
						redo = true;		//end time is not acceptable
					}
				} while (redo);		//get a new end time if needed
				
				if (toFloatTime(start) > toFloatTime(end)) {		//if the end time earlier than start time
					System.out.println("\tInvalid start and end times. Please try again.");
					redo = true;		//set redo
				}
			} while(redo);		//get a new start and end time
			
			time[0] = start;		//add the start and end time to the time string array
			time[1] = end;			//[start, end]
			userSchedule.add(time);		//add the time to the user's schedule
		} while (!start.isEmpty());		//get new times as long as the start time is not empty
		
		schedules.add(userSchedule);	//add "current" user's schedule to the list of schedules
	}
	
	//part 1 of 2 part method (supposedly to be 3 parts) that prints out the schedules
	private static void printList (List<ArrayList<String[]>> list) {
		for (int i = 0; i < list.size(); i ++) {
			System.out.println("User " + (i + 1) + "'s schedule (occupied times):");		//who the schedule belong's to
			System.out.print("\t{");
			printArrayList(list.get(i));			//get the arraylist and pass it on to the method that prints it
			System.out.println("}");
		}
	}

	//part 2 of 2 part method (supposedly to be 3 parts) that prints out the schedules
	private static void printArrayList(ArrayList<String[]> arrayLists) {
		for (int i = 0; i < arrayLists.size(); i++) {
			if (i == 0) {
				System.out.print("[");
			}
			else {
				System.out.print(", [");
			}
			//printArray(arrayLists.get(i));
			System.out.print(arrayLists.get(i)[0]);		//this is known as chain accessing (if I remembered correctly)
			System.out.print(", ");						//list can be long horizontally
			System.out.print(arrayLists.get(i)[1]);		//need to come up with new print format
			System.out.print("]");
			
			//at the same time, convert times into a numerical value and add it into the schedule that hold all user's time
			//did this so the program does not have to waste time later since it is already accessing each and every time
			float [] temp = new float[2];
			temp[0] = toFloatTime(arrayLists.get(i)[0]);		//this chain accessing is the same concept
			temp[1] = toFloatTime(arrayLists.get(i)[1]);		//as chaining methods
			masterSchedule.add(temp);
		}
	}
	
	//same printing concept as printArrayList() method, but just prints the schedule in numberical format
	//***mostly used for debugging***, hence the @surpresswarnings("unused")
	@SuppressWarnings("unused")		//comment out when using this method in getOpenTime() when debugging
	private static void printArrayListFloat(ArrayList<float[]> arrayLists) {
		System.out.print("{");
		for (int i = 0; i < arrayLists.size(); i++) {
			//System.out.println(combinedTimes.get(i).toString());		//this will produce memory addresses
			if (i == 0) {
				System.out.print("[");
			}
			else {
				System.out.print(", [");
			}
			System.out.print(arrayLists.get(i)[0]);		//this method was made to check if simplify() method
			System.out.print(", ");						//is doing its job correctly
			System.out.print(arrayLists.get(i)[1]);		//also would need a new printing format
			System.out.print("]");
		}
		System.out.println("}");
	}
	
	//same printing concept as printArrayList() method, but just prints from float to String
	private static void printArrayListFloatToString(ArrayList<float[]> arrayLists) {
		System.out.print("{");
		for (int i = 0; i < arrayLists.size(); i++) {
			if (i == 0) {
				System.out.print("[");
			}
			else {
				System.out.print(", [");
			}
			System.out.print(toStringTime(arrayLists.get(i)[0]));
			System.out.print(", ");
			System.out.print(toStringTime(arrayLists.get(i)[1]));		//also would need a new printing format
			System.out.print("]");
		}
		System.out.println("}");
	}
	
/*
	//This is the part 3 of the original printList() methods
	//possible but less efficient (would increase runtime to O(n^4) with 4 nested for-loops)
	private static void printArray(String [] array) {
//		for (int i = 0; i < array.length; i++) {
//			for (int j = 0; j < array[0].length; j++) {
//				if (j == 1) {								//how to access list of arraylist of arrays with large array size
//					System.out.print(", ");
//				}
//				System.out.print(array[i][j]);
//			}
//		}
		System.out.print(array[0]);		//since array size was small (2), we can just directly access it
		System.out.print(", ");
		System.out.print(array[1]);
	}
*/
	
	//probably the longest and most complicated method in this code
	//this method deletes "smaller" times and combines overlapping times
	private static ArrayList<float[]> simplify(ArrayList<float[]> schedule) {
		//using a nest for-loop so that it can compare each start-end time with every other ones in the master schedule
		for(int i = 0; i < schedule.size(); i++) {		//'i' will point to a start-end time in the schedule
			for(int j = 0; j < schedule.size(); j++) {	//'j' will compare every other start-end time to 'i'
				if (i == j) {		//no need to compare itself with itself, so go on to next loop
					continue;
				}
				
				//*****for the rest of the if-elseIf statements, "A", "B", "C", "D" will be references	*****
				//*****representing [A, B], [C, D]. [A, B] is 'i', and [C, D] is 'j'					*****
				//*****also, A and B will be the main, so it will never be deleted, only altered		*****
				//if A and B falls in between C and D, set A and B equal to C and D, deleted C and D
				if((schedule.get(i)[0] > schedule.get(j)[0]) && (schedule.get(i)[1] < schedule.get(j)[1])) {
//					System.out.print(i + ", " + j + " -->1");				//for debugging use
					schedule.get(i)[0] = schedule.get(j)[0];	//Example: [9:00, 10:00], [8:00, 11:00]
					schedule.get(i)[1] = schedule.get(j)[1];	//				==> [8:00, 11:00], deleted
					schedule.remove(j);
					if (i > 0) {	//to prevent out of bound error
//						System.out.println(i + ", " + j + " -->1.1");		//for debugging use
						i--;
					}
					j--;		//need this -- because each time schedule removes an element, the index changes
					continue;		//technically, the else-if are useless since these 'continue' defines it
				} 
				//if A and B completely covers C and D, delete C and D
				else if((schedule.get(i)[0] < schedule.get(j)[0]) && (schedule.get(i)[1] > schedule.get(j)[1])) {
//					System.out.print(i + ", " + j + " -->2");				//for debugging use
					schedule.remove(j);			//Example: [8:00, 11:00], [9:00, 10:00]
					if (i > 0) {				//				==> [8:00, 11:00], deleted
//						System.out.println(i + ", " + j + " -->2.1");		//for debugging use
						i--;
					}
					j--;
					continue;
				} 
				//if A & B overlaps C & D with A & B begining earlier, and B is in between C & D, set B = D, delete C & D
				else if((schedule.get(i)[0] < schedule.get(j)[0]) && (schedule.get(i)[1] < schedule.get(j)[1]) && 
						  (schedule.get(i)[1] > schedule.get(j)[0])) {
//					System.out.print(i + ", " + j + " -->3");		//for debugging use
					schedule.get(i)[1] = schedule.get(j)[1];
//					float [] temp = new float[2];		//line above does these
//					temp [0] = a.get(i)[0];
//					temp [1] = a.get(j)[1];		//Example: [8:00, 10:00], [9:00, 11:00]
//					a.add(temp);				//				==> [8:00, 11:00], deleted
//					a.remove(i);
//					if ((j - 1) > 0) {
//						a.remove(j-1);
//					}
//					i--;
					schedule.remove(j);
					if (i > 0) {
//						System.out.println(i + ", " + j + " -->3.1");		//for debugging use
						i--;
					}
					j--;
					continue;
				} 
				//if A & B overlaps C & D with A & B begining later, and D is in between A & B, set A = C, delete C & D
				else if((schedule.get(i)[0] > schedule.get(j)[0]) && (schedule.get(i)[1] > schedule.get(j)[1]) && 
						(schedule.get(i)[0] < schedule.get(j)[1])) {
//					System.out.print(i + ", " + j + " -->4");		//for debugging use
					schedule.get(i)[0] = schedule.get(j)[0];
//					float [] temp = new float[2];		//line above does these
//					temp [0] = a.get(j)[0];
//					temp [1] = a.get(i)[1];		//Example: [9:00, 11:00], [8:00, 10:00]
//					a.add(temp);				//				==> [8:00, 11:00], deleted
//					a.remove(i);
//					if ((j - 1) > 0) {
//						a.remove(j-1);
//					}
//					i--;
					schedule.remove(j);
					if (i > 0) {
//						System.out.println(i + ", " + j + " -->4.1");		//for debugging use
						i--;
					}
					j--;
					continue;
				} 
				//if A & C have the same start time and B ends earlier than D, set B = D, delete C and D
				else if(schedule.get(i)[0] == schedule.get(j)[0]) {
//					System.out.print(i + ", " + j + " -->5");				//for debugging use
					if ((schedule.get(i)[1] < schedule.get(j)[1])) {
//						System.out.print(" -->5.1");						//for debugging use
						schedule.get(i)[1] = schedule.get(j)[1];
					}							//Example: [9:00, 10:00], [9:00, 11:00]
					schedule.remove(j);			//				==> [9:00, 11:00], deleted
					if (i > 0) {
//						System.out.println(" -->5.2");						//for debugging use
						i--;
					}
					j--;
					continue;
				} 
				//if B & D have the same end time and A starts later than C, set A = C, delete C and D
				else if(schedule.get(i)[1] == schedule.get(j)[1]) {
//					System.out.print(i + ", " + j + " -->6");				//for debugging use
					if ((schedule.get(i)[0] > schedule.get(j)[0])) {
//						System.out.print(" -->6.1");						//for debugging use
						schedule.get(i)[0] = schedule.get(j)[0];
					}							//Example: [9:00, 10:00], [8:00, 10:00]
					schedule.remove(j);			//				==> [8:00, 10:00], deleted
					if (i > 0) {
//						System.out.println(" -->6.2");						//for debugging use
						i--;
					}
					j--;
					continue;
				} 
				//if A = D, set A = C, delete C and D
				else if(schedule.get(i)[0] == schedule.get(j)[1]) {
//					System.out.print(i + ", " + j + " -->7");				//for debugging use
					schedule.get(i)[0] = schedule.get(j)[0];
					schedule.remove(j);			//Example: [9:00, 10:00], [8:00, 9:00]
					if (i > 0) {				//				==> [8:00, 10:00], deleted
//						System.out.println(" -->7.1");						//for debugging use
						i--;
					}
					j--;
					continue;
				}
			}
			
			//if the start time is earlier than the earliest acceptable time, set it equal to earliest acceptable time
			if(schedule.get(i)[0] < toFloatTime(earliest)) {
//				System.out.println(i + " -->1");							//for debugging use
				schedule.get(i)[0] = toFloatTime(earliest);
			}
			//if the end time is later than the latest acceptable time, set it equal to latest acceptable time
			if(schedule.get(i)[1] > toFloatTime(latest)) {
//				System.out.println(i + " -->2");							//for debugging use
				schedule.get(i)[1] = toFloatTime(latest);
			}
			//if the time is not in the acceptable time range, remove the time
			if((schedule.get(i)[1] < toFloatTime(earliest)) || (schedule.get(i)[0] > toFloatTime(latest))) {
//				System.out.println(i + " -->3");							//for debugging use
				schedule.remove(i);
				i--;
			}
		}		
		return schedule;		//return a simplified schedule
	}
	
	//this method convert a string time to a float time so it is readable to logic statements/calculateable
	private static float toFloatTime(String a) {
		float time = 
				(Float.parseFloat(a.substring(0, a.indexOf(':')).trim())) + 
				(Float.parseFloat(a.substring(a.indexOf(":") + 1).trim()) / 60);	//divide by 60 to get the decimal portion
		
		return time;
	}
	
	//this method converts a float time back to a String time so it is readable to users
	private static String toStringTime(float a) {
		int hour = (int)(a);
		int minutes = (int)((a - hour) * 60);
//		float b = a - hour;
//		float c = b * 60;					//How I got the below formula through debugging
//		float d = Math.abs(minutes - c);
//		float test = (1 -  d);
		String time;
		
		//example of this condition is it we took .5 (30 minutes) * by 60 and .516 (31 minutes) * by 60
		//we will still get 30 minutes in String
		if((1 - Math.abs(minutes - ((a - hour) * 60)) < 0.0001) && (1 - Math.abs(minutes - ((a - hour) * 60)) != 0)) {
			minutes = (int)((a - hour) * 60) + 1;
		}
				
		//if the minute is 0, add a 0 to end of it so it produce hour:00 instead of hour:0
		if(String.valueOf(minutes).length() == 1 && minutes == 0) {
			time = Integer.toString(hour) + ":" + Integer.toString(minutes) + "0";
		} 		
		//if the minute is below 10 and above 0, add a 0 before it so it produce hour:0# instead of hour:#
		else if (String.valueOf(minutes).length() == 1 && minutes > 0 && minutes < 10) {
			time = Integer.toString(hour) + ":" + "0" + Integer.toString(minutes); 
		}
		//just hour:minute
		else {
			time = Integer.toString(hour) + ":" + Integer.toString(minutes);
		}
		
		return time;		//return the String version of the time
	}
	
	//this method update the earliest and latest acceptable times
	private static void updateOverallAvailability(String start, String end) {
		if(earliest != null && latest != null) {
			if(toFloatTime(start) > toFloatTime(earliest)) {
				earliest = start;
			}
			if (toFloatTime(end) < toFloatTime(latest)) {
				latest = end;
			}
		}
		else {
			earliest = start;
			latest = end;
		}
	}
	
/*
   //THIS METHOD IS FOR TESTING ONLY
	private static void test() {
		earliest = "00:00";
		latest = "24:00";
		ArrayList <String []> times = new ArrayList<>();
		
//		String [] exampleTimes = new String [] {"09:15", "10:00", "10:30", "11:15", "11:45", "12:00",
//												"10:30", "11:00", "11:15", "12:15", "12:30", "13:00"};
		String [] exampleTimes = new String [] {"08:00", "09:45", "10:00", "11:30", "12:00", "14:15", "15:00", "16:00", 
												"17:30", "18:00", "18:15", "20:00", "08:45", "10:00", "11:45", "12:00", 
												"13:00", "14:00", "16:00", "17:45", "19:00", "21:30", "08:00", "08:30", 
												"10:00", "11:30", "13:00", "14:45", "16:30", "18:00"};

		for (int i = 0; i < exampleTimes.length - 1; i++) {
			String [] setTimes = new String [2];
			setTimes[0] = exampleTimes[i];
			setTimes[1] = exampleTimes[++i];
			times.add(setTimes);
		}
		
		schedules.add(times);
	}
*/
}			//357 LINES OF PURE CODE (ONES NEEDED FOR THIS PROGRAM TO RUN). REST ARE EITHER COMMENTs, DEBUG CODES, OR SPACES