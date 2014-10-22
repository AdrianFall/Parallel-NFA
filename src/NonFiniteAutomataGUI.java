import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class NonFiniteAutomataGUI {
	
	private JButton testWordButton;
	private JButton cleanButton;
	private static NonFiniteAutomata machine;
	private JTextArea inputWordArea;
	private JTextArea superConfigSequenceArea;
	private JLabel wordAcceptationLabel;
	
	/**
	 * Constructor of this class, to set up the GUI layout and its functionality.
	 */
	public NonFiniteAutomataGUI() {
		//Instantiate the JFrame object
		JFrame window = new JFrame("Non Finite Automata - Parallel");
		
		//Set the default operation when user closes the window (JFrame)
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Set the size of the window
		window.setSize(600, 600);
		//Do not allow resizing of the window
		window.setResizable(false);
		//Set the position of the window to be in middle of the screen when program is started
		window.setLocationRelativeTo(null);
		
		//Call the setUpWindow method for setting up all the components needed in the window
		window = setUpWindow(window);
		
		//Set the window to be visible
		window.setVisible(true);
	}
	
	/**
	 * The main method of this class
	 * @param args - For example for the command line arguments when running the java class
	 */
	public static void main(String[] args) {
		
		//Initialise the NFA
		machine = new NonFiniteAutomata(); 
		//Initialise the GUI
		new NonFiniteAutomataGUI();
	}
	
	/**
	 * Creates a method for setting up the window, i.e. its layout, components and their positions
	 * @param window - The JFrame of the NFA window
	 * @return - returns a JFrame
	 */
	private JFrame setUpWindow(JFrame window) {
		//Create an instance of the JPanel object
    	JPanel panel = new JPanel();
    	//Set the panel's layout manager to null
    	panel.setLayout(null);
    	
    	//Set the bounds of the window
    	panel.setBounds(0, 0, 600, 600);
    	
    	//Create an instance of the JLabel object for showing the author label
    	JLabel authorLabel = new JLabel("By Adrian Fall");
    	//Set the position of authorLabel
    	authorLabel.setBounds(470, 540, 130, 25);
    	//Add the authorLabel to the panel
    	panel.add(authorLabel);
    	
    	//Create an instance of the JLabel for inputtin word information
    	JLabel inputWordLabel = new JLabel("Please input the word:");
    	//Set the position of inputWordLabel
    	inputWordLabel.setBounds(50, 45, 150, 25);
    	//Add the inputWordLabel to the panel
    	panel.add(inputWordLabel);
    	
    	//Create an instance of the JTextField object
    	inputWordArea = new JTextArea();
    	//Allow the inputWordArea to be line wrapped, for long inputs.
    	inputWordArea.setLineWrap(true);
    	//Add a scroll panel for the inputWordArea
    	JScrollPane inputWordScroll = new JScrollPane(inputWordArea);
    	//Set the position of inputWordScroll
    	inputWordScroll.setBounds(50, 70, 240, 50);
    	//Add the inputWordScroll to the panel
    	panel.add(inputWordScroll);
    	
    	//Create an instance of the JButton object for testing the inputted word
    	testWordButton = new JButton("Test Word.");
    	//Set the position of the testWordButton
    	testWordButton.setBounds(295, 70, 120, 50);
    	//Add the testWordButton to the panel
    	panel.add(testWordButton);
    	
    	//Create an instance of the JButton object for clearing the inputted word
    	cleanButton = new JButton("Clean.");
    	//Set the position of the cleanWord
    	cleanButton.setBounds(420, 70, 80, 50);
    	//Add the cleanWordButton to the panel
    	panel.add(cleanButton);
    	
    	//Create an instance of the JLabel for showing whether the word has been accepted
    	wordAcceptationLabel = new JLabel("");
    	//Set the position of the wordAcceptationLabel
    	wordAcceptationLabel.setBounds(50, 140, 250, 25);
    	//Add the wordAcceptationLabel to the panel
    	panel.add(wordAcceptationLabel);
    	
    	//Create an instance of the JLabel for showing the super configuration information
    	JLabel superConfigLabel = new JLabel("Tested word super configuration sequence: ");
    	//Set the position of the superConfigLabel
    	superConfigLabel.setBounds(50, 185, 280, 25);
    	//Add the superConfigLabel to the panel
    	panel.add(superConfigLabel);
    	
    	//Create an instance of the JTextArea for showing the super configuration sequence
    	superConfigSequenceArea = new JTextArea();
    	//Set the superConfigSequenceArea to be not editable, so user can't input anything into it
    	superConfigSequenceArea.setEditable(false);
    	//Allow superConfigSequenceArea to be line wrapped, for long outputs.
    	superConfigSequenceArea.setLineWrap(true);
    	//Add a scroll panel for the superConfigSequenceArea
    	JScrollPane superConfigSequenceScroll = new JScrollPane(superConfigSequenceArea);
    	//Set the position of superConfigSequenceScroll
    	superConfigSequenceScroll.setBounds(50, 215, 455, 250);
    	//Add the superConfigSequenceScroll to the panel
    	panel.add(superConfigSequenceScroll);
    	
    	addActionListeners();
    	
    	//Add the panel to the window
    	window.add(panel);
		return window;
	}//End of setUpWindow method

	/**
	 * Adds the action listeners for the buttons, i.e. the test button and clean button.
	 */
	private void addActionListeners() {
		//Add the action listener for the testWordButton
		testWordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				machine.reset();
				machine.process(inputWordArea.getText());
				//If the word has been accepted by the machine
				if (machine.accepted()) {
					wordAcceptationLabel.setText("<html>The word has been <font color=green>accepted.</font></html>");
				} else { //The word has been rejected by the machine
					wordAcceptationLabel.setText("<html>The word has been <font color=red>rejected.</font></html>");
				}
				superConfigSequenceArea.setText(machine.getSuperConfigurationSequence());
			}
		});//End of action listener for testWordButton
		
		//Add the action listener for the cleanButton
		cleanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inputWordArea.setText("");
				wordAcceptationLabel.setText("");
				superConfigSequenceArea.setText("");
			}
		});//End of action listener for the cleanButton
		
	}//End of addActionListeners method
}