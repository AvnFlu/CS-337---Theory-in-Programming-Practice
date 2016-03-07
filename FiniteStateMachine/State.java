import java.util.HashMap;

/*  Each '*.fsm' file represents a Finite State Machine, and each line of that file represents a single state.
 *  The program will expect each State to be represented by the following tokens:
 *  
 *  [STATE ID #] [NEXT STATE 1] [NEXT STATE 2] ... [NEXT STATE N] [ACCEPTING STATE FLAG (OPTIONAL)]
 *
 *
 *  Tokens:
 *
 *  [STATE ID #]           - [POSITIVE INTEGER] A single integer that identifies the state
 *
 *  [NEXT STATES]          - [POSITIVE INTEGER][':'][POTENTIAL CHARACTERS] Used to identify which state to go to next
 *                           when the next character is read, all characters must either be a number,
 *                           a lower case letter, a valid symbol (listed below),
 *                           or a reserved letter ('A','D','N','S').
 *
 *  [ACCEPTING STATE FLAG] - ['X'] When there is no more input to read, the FSM will check the state
 *                           it is currently in to see if this is an accepting state. While not required,
 *                           all '*.fsm' files should have at least one accepting state to be useful.
 *                           This flag must be the last token in the state.
 *
 *
 *  Reserved Letters:
 *  	Alapbet        - any lower case letter
 *      Digit          - any number 0-9
 *      Non-zero digit - any number 1-9
 *      Symbol         - any valid symbol  ['~' | '!' | '@' | '#' | '%' | '^' | '&' | '*' |
 *                                          '(' | ')' | '-' | '+' | '{' | '}' | '.' | ',']
 *
 *  All tokens must be separated by a whitespace, since a whitespace is a delimiter, it should not be used in the input file 
 *  All States are 1-indexed and should have a Start State with the ID number 1.  This is where the FSM will begin.
 *
 *
 *  Examples:
 *  The following FSM will only accept the stings "ah" or "ch"
 *  
 *  1 2:ac 4:bdefghijklmnopqrstuvwxyzDS
 *  2 3:h 4:abcdefgijklmnopqrstuvwxyzDS
 *  3 4:ADS X
 *  4 4:ADS
 *
 *  If it sees the letters 'a' or 'c' first, it will go to state 2, where it will look for the letter 'h' which will
 *  send it to the accepting state 3, any other input will send it to the infinitely-looping rejecting state 4
 *
 *  This new FSM will just check to see if "ah" or "ch" exist anywhere in the string
 *  
 *  1 2:ac 1:bdefghijklmnopqrstuvwxyzDS
 *  2 3:h 1:abcdefgijklmnopqrstuvwxyzDS
 *  3 3:ADS X
 *  
 *  Once state 3 is hit, the FSM is guaranteed to accept the rest of the input
 */
public class State {
	private int id;
	private HashMap<Character, Integer> nextStates;  //key -  potential characters;  value - the state to go to
	private boolean accepting;

	public State(String line) {
		try {
			//Split all tokens into an array
			String str[] = line.split(" ");

			//Set the state id number as the first token in the line 
			id = Integer.parseInt(str[0]);

			//Read through the rest of the tokens in the line to build the nextStates
			nextStates = new HashMap<Character,Integer>();
			int i = 0;
			while(++i < str.length) {

				//Check if this is the last token in the line and if it is an "accepting state" flag
				if(i == str.length - 1 && str[i].charAt(0) == 'X') {
					accepting = true;
					return;
				}

				//Identify the next state the following characters will lead to
				int j = 0;
				while(str[i].charAt(j) != ':')
					j++;

				int nextState = Integer.parseInt(str[i].substring(0,j));
				
				while(++j < str[i].length())
					nextStates.put(str[i].charAt(j), nextState);
			}
		}
		catch (Exception e) {
			System.out.println("FSM invalid input");
			System.exit(0);	
		}
	}

	public int getID() { return id; }
	public boolean isAccepting() { return accepting; }

	public int getNextState(char c) {
		if(!nextStates.containsKey(c)) {
			//Check if the selected character belongs to a reserved letter
			if(A(c) && nextStates.containsKey('A')) return nextStates.get('A');
			if(D(c) && nextStates.containsKey('D')) return nextStates.get('D');
			if(N(c) && nextStates.containsKey('N')) return nextStates.get('N');
			if(S(c) && nextStates.containsKey('S')) return nextStates.get('S');

			return -1; //character not found
		} 

		return nextStates.get(c);
	}

	/* Checks the reserved letters */
	public boolean A(char c) { return (c >= 'a' && c <= 'z'); }  //checks if the input character is a lowercase alphabet, uppercase letters are reserved 
	public boolean D(char c) { return (c >= '0' && c <= '9'); }  //checks if the input character is a digit
	public boolean N(char c) { return (c >= '1' && c <= '9'); }  //checks if the input character is a nonzero digit
	public boolean S(char c) {                                   //checks if the input character is a valid symbol
		return (c == '~' || c == '!' || c == '@' || c == '#' || c == '%' || c == '^'
			|| c == '&' || c == '*' || c == '(' || c == ')' || c == '-' || c == '+'
			|| c == '{' || c == '}' || c == '.' || c == ',');
	}
}