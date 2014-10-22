
public class NonFiniteAutomata {
	
	
	/*
	 * The current set of states, encoded bit-wise: 
	 * state n is represented by the bit 1<<n.
	 */
	private int stateSet;
	//Declare a constant integer, and initialise it with the number of states of the NFA machine
	private final int NUM_OF_STATES = 9; 
	//Declare a variable string, for the sequential test of the word (i.e. for showing the super-configuration sequence)
	private String superConfigurationSequence;
	private char sinkStateChar = '0';
	  
	/**
	 * Resets the stateSet to contain just the state q0 (i.e. the start state)
	 */
	public void reset() {
		sinkStateChar = '0';
		stateSet = 1<<0; // {q0}
	}


	/*
	 * The state transition table of delta that
	 * uses two dimensional array to implement the 
	 * bit-mapped parallel search
	 */
	static private int[][] delta = 
			  
		{{1<<0|1<<1, 1<<0},  //delta[q0,0] = {q0,q1}
		  					 //delta [q0,1] = {q0}
		  
		{1<<2, 1<<0},        //delta [q1, 0] = {q2}
		  					 //delta [q1, 1] = {q0}
		  
		{1<<5, 1<<3},		 //delta [q2, 0] = {q5}
		  					 //delta [q2, 1] = {q3}
		  
		{1<<4, 1<<4},		 //delta [q3, 0] = {q4}
		  					 //delta [q3, 1] = {q4}
		  
		{1<<5, 1<<3},        //delta [q4, 0] = {q5}
		  					 //delta [q4, 1] = {q3}
		  
		{1<<8, 1<<6}, 	     //delta [q5, 0] = {q8}
		  					 //delta [q5, 1] = {q6}
		  
		{1<<5, 1<<7},        //delta [q6, 0] = {q5}
		  					 //delta [q6, 1] = {q7}
		  
		{1<<6, 1<<6},        //delta [q7, 0] = {q6}
		  				     //delta [q7, 1] = {q6}
		  
		{1<<8, 1<<8}};       //delta [q8, 0] = {q8}
	                         //delta [q8, 1] == {q8}
			 
	/**
	  * Make one transition from the current state-set  
	  * to the next state-set, for each char in the input string.
	  * @param input - the String to process
	  */
	public void process(String input) {
		//Set the superConfigurationSequence with a start state and the input to be read
		superConfigurationSequence = ("{q0} " + input); 
		  
		for (int i = 0; i < input.length(); i++) { // for each character in the input string
	    	
			char c = input.charAt(i);
			  
			//If the currently read character is not 0 and 1
			if (c != '0' && c != '1') {
				reset();
				//Append the superConfigurationSequence with a sink state
				superConfigurationSequence += "\u22A2 \n {Sink}";
				sinkStateChar = c;
				break;
			  }
			  
			int nextSS = 0; // next state set, initially empty
	    	
			for (int s = 1; s <= NUM_OF_STATES; s++) { // for each state s (number of states from nfa)
				//Declare a local variable for the state, which subtracts one from the currently looped s (this is due to the nature of the loop, hence to include the state q0 and exclude q9 as it doesn't exist in the delta)
				int state = s - 1;
	    		
				if ((stateSet & (1<<s-1)) != 0) { //If the state set contains the currently looped state
					
					try {
						nextSS |= delta[state][c-'0'];
					}
					catch (ArrayIndexOutOfBoundsException ex) {
						// in effect, nextSS |= 0
					}
				}
			}//End of for loop, for each state s
			
			//Append the superConfigurationSequence with an unicode for Turnstile/provable and opening curly bracket
			superConfigurationSequence += " \u22A2 \n{";
			assembleStateSetToSuperConfig(nextSS);
			  
			//If the superConfigurationSequence ends with string pattern of comma and space (i.e. ", ")
			if (superConfigurationSequence.endsWith(", ")) {
				//Obtain the index of end
				int indexOfEnd = superConfigurationSequence.lastIndexOf(",");
				//Declare a temporary String variable for substringing the superConfigurationSequence 
				//(so that the superConfigurationSequence doesn't end with unnecessary space and comma (i.e. " ,")) 
				String tempSuperConfigurationSequence = superConfigurationSequence.substring(0, indexOfEnd);
				//Assign the obtained tempSuperConfigurationSequence to the superConfigurationSequence
				superConfigurationSequence = tempSuperConfigurationSequence;
			}
			//Append the superConfigurationSequence with bracket closure followed by the remaining part of the word to be read
			superConfigurationSequence += ("} " + input.substring(i+1));
			// new state set after character is read
			stateSet = nextSS;
	      
		}//End of for loop, for each character in the input string
		
		//If the machine accepts the word
		if (accepted()) {
			//Append the superConfigurationSequence with the unicode for intersection, F (for final state),
			//unicode for not equal and unicode for empty set
			superConfigurationSequence += " \u2229 F \u2260 \u2205";
		} else { //The machine rejects the word
			//Append the superConfigurationSequence with the unicode for intersection, F (for final state),
			// = (equals sign) and unicode for empty set
			superConfigurationSequence += " \u2229 F = \u2205";
			
			//If the sinkStateChar is not equal to 0, meaning the sink state has been reached.
			if (sinkStateChar != '0') {
				//Append the superConfigurationSequence with the information why the sink state has been reached.
				superConfigurationSequence += "\n Sink state reached, because of character '" +  sinkStateChar  +  "' not belonging to the alphabet.";
			}
		}
		
	}//End of process method


	/**
	 * Test whether the NFA accepted the string.
	 * @return true if the final set includes 
	 *         a final state.
	 */
	public boolean accepted() {
		return (stateSet & (1<<8))!=0; // return true if q8 is in stateSet
	}
	  
	/**
	 * A method for obtaining the states that are contained in the next state set
	 * and appending them to the super configuration sequence
	 * @param nextSS - The next state set
	 */
	private void assembleStateSetToSuperConfig(int nextSS) {
		  
		for (int i = 1; i <= NUM_OF_STATES; i++) { // for each state
			//If currently looped state - 1 (so that it includes state q0 and excludes state q9) is contained in the state set
			if ((nextSS & (1<<(i-1))) != 0) {
				//Append the superConfigurationSequence string with the state name
				superConfigurationSequence += "q" + (i-1) + ", ";
			}
		}//End of for loop for each state
	}//End of configureStateSetSequence method
	
	/**
	 * Getter for the superConfigurationSequence
	 * @return - String with the superConfigurationSequence
	 */
	public String getSuperConfigurationSequence() {
		return superConfigurationSequence;
	}
}