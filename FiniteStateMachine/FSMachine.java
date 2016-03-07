//Michael Both
//mjb3299

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

/* A basic Acceptor Finite State Machine.  This program will take in a single text input file consisting
 * of lines of strings to be tested.  Then the program will take in one or more user created '*.fsm' files
 * which will be used to build the finite state machines and each one will be tested on every line of 
 * strings from the input file.
 *
 * The Finite State Machine works like a graph of connected nodes (states), beginning at a root (start
 * state), it uses input to determine which state to move to next and continues to do this until while
 * there is still input to read through. 
*/
public class FSMachine {
	public static void main(String[] args) throws Exception {
		
		File in;
		Scanner sc;
		Scanner fileSC;

		//Get the input file
		try {
			if(args.length > 0) {
				in = new File(args[0]);
				sc = new Scanner(in);
			}
			else {
				fileSC = new Scanner(System.in);
				System.out.print("Enter an input file to test: ");
				in = new File(fileSC.next());
				sc = new Scanner(in);
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Input File Not Found");
			return;
		}


		try{
			//Read in the "*.fsm" files and store them into an ArrayList
			ArrayList<File> fsmList = new ArrayList<File>();

			//If the *.fsm files were given upon executing the program
			if(args.length > 1)
				for(int i = 1; i < args.length; i++)
					fsmList.add(new File(args[i]));

			//Otherwise, prompt the user to enter the file(s)
			else {
				fileSC = new Scanner(System.in);
				System.out.print("Enter the \"*.fsm\" files to use. Hit enter when done: ");
				String machines[] = fileSC.nextLine().split(" ");

				for(String machine : machines)
					fsmList.add(new File(machine));
			}

			//Separate the lines of the input file into an ArrayList
			ArrayList<String> input = new ArrayList<String>();
			while(sc.hasNextLine())
				input.add(sc.nextLine());

			//Tests each Finite State Machine entered
			for(File state : fsmList) {
				Scanner fsmSC = new Scanner(state);
				HashMap<Integer, State> fsmStates = new HashMap<Integer, State>();  //key - state ID number; value - state

				//Create all the states from the current FSM file
				while(fsmSC.hasNextLine()) {
						State s = new State(fsmSC.nextLine());
						fsmStates.put(s.getID(), s);
				}

				for(String line : input) {
					//Tests the current FSM against the current input line, beginning at state 1
					if(Read(line, fsmStates, 1))
						System.out.println(state.getName() + " Accepted: \"" + line + "\"");
				}
			}
		}
		catch(FileNotFoundException e) {
			System.out.println("One Or More \"*.fsm\" Files Not Found");
			return;
		}	
	}


	/* Recursivly runs the given input line through the FSM one character at a time
	   to see if the FSM accepts the given input string */
	public static boolean Read(String input, HashMap<Integer, State> fsm, int state) {
		//Find the state we are at
		State currentState = fsm.get(state);

		//Base case
		if(input.length() <= 0)
			return currentState.isAccepting();

		//Recursive call
		int nextState = currentState.getNextState(input.charAt(0));

		if(nextState == -1)
			return false;

		return Read(input.substring(1), fsm, nextState);
	}
}