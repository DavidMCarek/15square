//David Carek

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class FifteenSquare extends JFrame 
{
	// Fifteen square is a game in which the user tries to reorder a set of 15 tiles from least to greatest in a 4x4 playing grid. 
	// This program will allow the user to play the game in a GUI. They will have the option of saving and loading games. They will 
	// also be able to create a new game and ask how to play. The number of moves will also be displayed and an unclickable hints 
	// button will be displayed.
	
	// Steps
	// 1.  Create GUI.
	// 2.  Find and set the 0 tile to not visible.
	// 3.  Shuffle board.
	// 4.  Reset tiles to visible.
	// 5.  Find and set the 0 tile to not visible.
	// 6.  Allow for a turn to be taken and text in tiles to be swapped or "slid".
	// 7.  Add 1 to move count.
	// 8.  Reset tiles to visible.
	// 9.  Find and set the 0 tile to not visible.
	// 10. Check victory condition.
	// 11. Implement options buttons.
	
	// NOTE: When referring to buttons moving the text is moving not the actual buttons.
	
	
	// These are some of the values that will be used throughout the program.
	public final int TOTAL_BOARD_ROWS = 4; 
	
	public final int TOTAL_BOARD_COLUMNS = 4; 
	
	public final int SQUARE_BUTTON_SIDE_LENGTH = 70; 
	
	public final int SPACE_BETWEEN_BUTTONS = 1; 
	
	public final int FRAME_MARGIN = 5; 

	public final int GAME_BOARD_SIDE_LENGTH = (3 * SPACE_BETWEEN_BUTTONS) + (4 * SQUARE_BUTTON_SIDE_LENGTH);
	
	public final int OPTIONS_BUTTON_HEIGHT = 40; 
	
	public final int MOVE_COUNTER_LABEL_HEIGHT = 10; 
	
	public final int OPTIONS_PANEL_HEIGHT = (2 * OPTIONS_BUTTON_HEIGHT) + SPACE_BETWEEN_BUTTONS + MOVE_COUNTER_LABEL_HEIGHT + (2*FRAME_MARGIN);
	
	public final int OPTIONS_BUTTON_WIDTH = 97; 
	
	public int[] invisibleButtonLocation = new int[2]; 
	
	public final int INVISIBLE_ROW = 0;
	
	public final int INVISIBLE_COLUMN = 1;
	
	public boolean isWinner; 
	
	public int numberOfMovesCounter = 0; 
	
	public JLabel moveCountLabel; 
	
	public JLabel gameCountLabel; 
	
	public JButton[] jbtOptionsButtonsArray = new JButton[6]; 
	
	public JButton[][] jbtBoardButtonTilesArray = new JButton[TOTAL_BOARD_ROWS][TOTAL_BOARD_COLUMNS];
	
	public int totalGames = 0; 
	
	public int[] clickLocation = new int[2]; 
	
	public final int CLICKED_ROW = 0;
	
	public final int CLICKED_COLUMN = 1;
	
	public final int SAVE_BUTTON_NUMBER = 0;
	
	public final int LOAD_BUTTON_NUMBER = 1;
	
	public final int HELP_BUTTON_NUMBER = 2;
	
	public final int HINTS_BUTTON_NUMBER = 3;
	
	public final int EXIT_BUTTON_NUMBER = 4;
	
	public final int NEW_GAME_BUTTON_NUMBER = 5;
	
	public DataOutputStream outGamesPlayed; // Used to write the total games save.
	
	public DataInputStream inGamesPlayed; // Used to load the total games save.
	
	FifteenSquare()
	{	
		// If there is a previous save of the number of games played that number will be loaded and the number of total games will be set to it.
		// If that save is not found an exception will be thrown. Since the catch statement is empty the program will just continue.
		try
		{
			inGamesPlayed = new DataInputStream(new FileInputStream("gamesPlayed"));
			totalGames = inGamesPlayed.readInt();
		} catch (Exception totalGamesException)
		{
		}
				JPanel mainGameTilesPanel = new JPanel(); 
				mainGameTilesPanel.setLayout(null); 
				mainGameTilesPanel.setBounds(FRAME_MARGIN, FRAME_MARGIN, GAME_BOARD_SIDE_LENGTH + (2 * FRAME_MARGIN),
									         GAME_BOARD_SIDE_LENGTH + (2 * FRAME_MARGIN));
				mainGameTilesPanel.setBackground(Color.black);
				mainGameTilesPanel.setBorder(BorderFactory.createLineBorder(Color.black));
				
				int buttonTextAlgorithm;
				
				for (int currentBoardRow = 0; currentBoardRow < TOTAL_BOARD_ROWS; currentBoardRow++)
				{
					for (int currentBoardColumn = 0; currentBoardColumn < TOTAL_BOARD_COLUMNS; currentBoardColumn++)
					{
						buttonTextAlgorithm = ((currentBoardRow * 4) + currentBoardColumn + 1);
						
						jbtBoardButtonTilesArray[currentBoardRow][currentBoardColumn] = new JButton();
						
						jbtBoardButtonTilesArray[currentBoardRow][currentBoardColumn].setText(buttonTextAlgorithm + "");
						
						jbtBoardButtonTilesArray[currentBoardRow][currentBoardColumn].setBounds
						(
			/*X Position*/	(currentBoardColumn * SQUARE_BUTTON_SIDE_LENGTH) + (SPACE_BETWEEN_BUTTONS * currentBoardColumn) + FRAME_MARGIN, 
			/*Y Position*/	(currentBoardRow * SQUARE_BUTTON_SIDE_LENGTH) + (SPACE_BETWEEN_BUTTONS * currentBoardRow) + FRAME_MARGIN, 		
			/*Width*/		(SQUARE_BUTTON_SIDE_LENGTH),
			/*Height*/		(SQUARE_BUTTON_SIDE_LENGTH)												
						);
						
						jbtBoardButtonTilesArray[currentBoardRow][currentBoardColumn].addActionListener(new moveListener());
						
						jbtBoardButtonTilesArray[currentBoardRow][currentBoardColumn].setFocusable(false);
						
						jbtBoardButtonTilesArray[currentBoardRow][currentBoardColumn].setFont(new Font("arialblack", Font.BOLD, 25));
						
						jbtBoardButtonTilesArray[currentBoardRow][currentBoardColumn].setBorder(BorderFactory.createRaisedBevelBorder());
						
						mainGameTilesPanel.add(jbtBoardButtonTilesArray[currentBoardRow][currentBoardColumn]);		
					}
				}
				
				int invisibleButtonStartingRow = 3;
				int invisibleButtonStartingColumn = 3;
				jbtBoardButtonTilesArray[invisibleButtonStartingRow][invisibleButtonStartingColumn].setText("0");
				
				// This creates the options panel buttons and disables the ones that are not yet implemented. Then 
				// the dotted line is disabled.
				jbtOptionsButtonsArray[SAVE_BUTTON_NUMBER] = new JButton("Save");
				jbtOptionsButtonsArray[SAVE_BUTTON_NUMBER].setFocusable(false);
				
				jbtOptionsButtonsArray[LOAD_BUTTON_NUMBER] = new JButton("Load");
				jbtOptionsButtonsArray[LOAD_BUTTON_NUMBER].setFocusable(false);
				
				jbtOptionsButtonsArray[HELP_BUTTON_NUMBER] = new JButton("Help");
				jbtOptionsButtonsArray[HELP_BUTTON_NUMBER].setFocusable(false);
				
				jbtOptionsButtonsArray[HINTS_BUTTON_NUMBER] = new JButton("Hints");
				jbtOptionsButtonsArray[HINTS_BUTTON_NUMBER].setEnabled(false);
				jbtOptionsButtonsArray[HINTS_BUTTON_NUMBER].setFocusable(false);
				
				jbtOptionsButtonsArray[EXIT_BUTTON_NUMBER] = new JButton("Exit");
				jbtOptionsButtonsArray[EXIT_BUTTON_NUMBER].setFocusable(false);
				
				jbtOptionsButtonsArray[NEW_GAME_BUTTON_NUMBER] = new JButton("New");
				jbtOptionsButtonsArray[NEW_GAME_BUTTON_NUMBER].setFocusable(false);
				
				// The options buttons are formatted in the loop and action listeners are added.
				for (int currentOptionsButton = 0; currentOptionsButton < 6; currentOptionsButton++)
				{
					if (currentOptionsButton < 3) // Top row
					{
						jbtOptionsButtonsArray[currentOptionsButton].setBounds
						(
			/*X Position*/		(currentOptionsButton * OPTIONS_BUTTON_WIDTH) + (currentOptionsButton * SPACE_BETWEEN_BUTTONS) + FRAME_MARGIN,				
			/*Y Position*/		(GAME_BOARD_SIDE_LENGTH + (4 * FRAME_MARGIN) + SPACE_BETWEEN_BUTTONS),										
			/*Width*/			(OPTIONS_BUTTON_WIDTH), 
			/*Height*/			(OPTIONS_BUTTON_HEIGHT) 							
						);
					}
					
					else if (currentOptionsButton >= 3) // Bottom row
					{
						jbtOptionsButtonsArray[currentOptionsButton].setBounds
						(
			/*X Position*/		((currentOptionsButton - 3) * OPTIONS_BUTTON_WIDTH) + ((currentOptionsButton - 3) * SPACE_BETWEEN_BUTTONS) + FRAME_MARGIN,  
			/*Y Position*/		(GAME_BOARD_SIDE_LENGTH + (5 * FRAME_MARGIN) + OPTIONS_BUTTON_HEIGHT),						
			/*Width*/			(OPTIONS_BUTTON_WIDTH),
			/*Height*/			(OPTIONS_BUTTON_HEIGHT)								
						);
					}
					
					jbtOptionsButtonsArray[currentOptionsButton].setBorder(BorderFactory.createRaisedBevelBorder());
					jbtOptionsButtonsArray[currentOptionsButton].setFont(new Font("arialblack", Font.BOLD, 18));
					jbtOptionsButtonsArray[currentOptionsButton].addActionListener(new optionsListener());
					
				}
				
				gameCountLabel = new JLabel("Games : " + totalGames);
				gameCountLabel.setFont(new Font("arialblack", Font.BOLD, 12));
				
				moveCountLabel = new JLabel("Moves : " + numberOfMovesCounter);
				moveCountLabel.setFont(new Font("arialblack", Font.BOLD, 12));
			
				gameCountLabel.setBounds(
						/*X Position*/	(FRAME_MARGIN + (2 * OPTIONS_BUTTON_WIDTH) + SPACE_BETWEEN_BUTTONS), 
						/*Y Position*/	(GAME_BOARD_SIDE_LENGTH + OPTIONS_PANEL_HEIGHT + FRAME_MARGIN),
						/*Width*/		(GAME_BOARD_SIDE_LENGTH / 2),
						/*Height*/		(MOVE_COUNTER_LABEL_HEIGHT)
										);
				
				moveCountLabel.setBounds(
						/*X Position*/	(FRAME_MARGIN),
						/*Y Position*/	(GAME_BOARD_SIDE_LENGTH + OPTIONS_PANEL_HEIGHT + FRAME_MARGIN),
						/*Width*/		(GAME_BOARD_SIDE_LENGTH / 2),
						/*Height*/		(MOVE_COUNTER_LABEL_HEIGHT)
										);
				
				// This creates the options panel,sets the layout, sets the size, and then the options buttons and
				// move count are added to it.
				JPanel optionsPanel = new JPanel();
				optionsPanel.setLayout(null);
				optionsPanel.setBounds(
					/*X Position*/	  (FRAME_MARGIN),
					/*Y Position*/	  (GAME_BOARD_SIDE_LENGTH + (2 * FRAME_MARGIN)),
					/*Width*/		  (GAME_BOARD_SIDE_LENGTH + (2 * FRAME_MARGIN)), 
					/*Height*/		  (OPTIONS_PANEL_HEIGHT)
									  );
				
				optionsPanel.add(jbtOptionsButtonsArray[SAVE_BUTTON_NUMBER]);
				optionsPanel.add(jbtOptionsButtonsArray[LOAD_BUTTON_NUMBER]);
				optionsPanel.add(jbtOptionsButtonsArray[HELP_BUTTON_NUMBER]);
				optionsPanel.add(jbtOptionsButtonsArray[HINTS_BUTTON_NUMBER]);
				optionsPanel.add(jbtOptionsButtonsArray[EXIT_BUTTON_NUMBER]);
				optionsPanel.add(jbtOptionsButtonsArray[NEW_GAME_BUTTON_NUMBER]);
				optionsPanel.add(gameCountLabel);
				optionsPanel.add(moveCountLabel);
				
	
				add(mainGameTilesPanel);
				add(optionsPanel);
				setTitle("Fifteen Square");
				setSize(
		/*Width*/	   (GAME_BOARD_SIDE_LENGTH + (7 * FRAME_MARGIN) + SPACE_BETWEEN_BUTTONS),
		/*Height*/	   (GAME_BOARD_SIDE_LENGTH + (11 * FRAME_MARGIN) + OPTIONS_PANEL_HEIGHT + SPACE_BETWEEN_BUTTONS)
					   );
				setLocationRelativeTo(null);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setVisible(true);
				setLayout(null);
				
				shuffle(); // Once the board is shuffled the game is ready to play.
				
	}
	
	// This method will find the location of the 0 square.
	public void findInvisibleSquare()
	{
		// The rows and columns are incremented and the text for each slot is searched. If that text is 0 the row and column 
		// of the location are stored in invisibleLocation.
		for (int boardRows = 0; boardRows < TOTAL_BOARD_ROWS; boardRows++)
		{
			for (int boardColumns = 0; boardColumns < TOTAL_BOARD_COLUMNS; boardColumns++)
			{
				if (jbtBoardButtonTilesArray[boardRows][boardColumns].getText().equals("0"))
				{
					invisibleButtonLocation[0] = boardRows;		// This is the row of the invisible button.
					invisibleButtonLocation[1] = boardColumns;	// This is the column of the invisible button.
				}			
			}
		}
	}
	
	// This method sets the visibility of the 0 button to false.
	public void visibilityToFalse()
	{
		findInvisibleSquare(); // Gets the location of the 0 button.
		jbtBoardButtonTilesArray[invisibleButtonLocation[0]][invisibleButtonLocation[1]].setVisible(false); // Then sets the location to not visible.		
	}
	
	// This method resets all the buttons to visible.
	public void visibilityToTrue()
	{
		// The rows and columns are incremented and then each of the buttons are set to visible.
		for (int boardRows = 0; boardRows < TOTAL_BOARD_ROWS; boardRows++)
		{
			for (int boardColumns = 0; boardColumns < TOTAL_BOARD_COLUMNS; boardColumns++)
			{
				jbtBoardButtonTilesArray[boardRows][boardColumns].setVisible(true);				
			}	
		}
	}
	
	// This method "clicks" on a random button and the location of the click is stored in clickLocation.
	// In other words the method in combination with the shuffle method simulate a turn.
	public void findRandomSquare()
	{
		// A random integer between 1 and 15 is created.
		String randomNumber = "" + ((int) (Math.random() * 15 + 1)); 
		
		// The rows and columns are then incremented to find the random numbers location on the board.
		for (int boardRows = 0; boardRows < TOTAL_BOARD_ROWS; boardRows++)
		{
			for (int boardColumns = 0; boardColumns < TOTAL_BOARD_COLUMNS; boardColumns++)
			{
				
				// If the random number equals the number on the button the location of that button is stored.
				if (jbtBoardButtonTilesArray[boardRows][boardColumns].getText().equals(randomNumber))
				{
					clickLocation[0] = boardRows;		// This is the row of a random button.
					clickLocation[1] = boardColumns;	// This is the column of a random button.
				}			
			}
		}
	}
	
	// This method will shuffle the already displayed board.
	public void shuffle()
	{
		for (int shuffleMoves = 0; shuffleMoves < 1000; shuffleMoves++)
		{
			// If the number generated matches the row or column that the invisible button is in, the invisible button will be
			// moved to that location and the other buttons will slide over.
			
			findRandomSquare(); // Gets the location of a random "clicked" button.
			
			// The buttons need to be reset to visible so that the previous 0 button is not left invisible.
			visibilityToTrue();
			
			// If the row of the clicked button is the same as the row of the 0 button the columns in that row
			// will slide from the clicked location to the 0 location.
			if (clickLocation[0] == invisibleButtonLocation[0])
			{
				slideColumn(); // This will slide the buttons toward the blank column.
			}
			
			// If the column of the clicked button is the same as the column of the 0 button the rows in that column
			// will slide from the clicked location to the 0 location.
			else if (clickLocation[1] == invisibleButtonLocation[1])
			{
				slideRow(); // This will slide the the buttons toward the blank row.
			}
			
			// If the randomly "clicked" button is not in the row or column of the 0 nothing will happen.
			
			findInvisibleSquare(); // The 0 square needs to be found again.
			visibilityToFalse();   // The 0 square needs to be reset to invisible.	
		}
		// Every time the board is shuffled another game needs to be added to the total number of games and the text on the label
		// redisplayed. 
		try
		{
			outGamesPlayed = new DataOutputStream(new FileOutputStream("gamesPlayed"));
			totalGames++;
			outGamesPlayed.writeInt(totalGames);
		} catch (Exception totalGamesException)
		{
		}
		gameCountLabel.setText("Games : " + totalGames);
	}
		
	// This method will slide the column and place the invisible square at the end of the slide.
	public void slideColumn()
	{
		// The rows buttons need to be moved left or right and the 0 needs to be moved to the random location.
		
		int column;
		
		// When the column of the 0 is greater than the column of the clicked numbers location the if statement will execute.
		if (invisibleButtonLocation[1] > clickLocation[1])
		{
			// starting from the 0 button the column is decremented.
			for (column = invisibleButtonLocation[1]; column >= clickLocation[1]; column--)
			{
				// When the column is greater than the column of the clicked buttons column the if statement will execute.
				// This sets the text of the button to be the same as the one to the left of it until the clicked 
				// number is reached.
				if (column > clickLocation[1])	
					jbtBoardButtonTilesArray[invisibleButtonLocation[0]][column].setText(jbtBoardButtonTilesArray[invisibleButtonLocation[0]][column - 1].getText());
				
				// This is the location of the clicked number. It will be set to zero and the slide will be finished.
				else
					jbtBoardButtonTilesArray[invisibleButtonLocation[0]][column].setText("0");
			}
		} 
		// If the invisible squares column is the same as the clicked column the if statement will execute. 
		else if (invisibleButtonLocation[1] < clickLocation[1])
		{
			for (column = invisibleButtonLocation[1]; column <= clickLocation[1]; column++)
			{
				// When the column is less than the clicked locations column the if statement will execute.
				// This sets the text of the button to be the same as the one to the right of it until the clicked 
				// number is reached.
				if (column < clickLocation[1])
					jbtBoardButtonTilesArray[invisibleButtonLocation[0]][column].setText(jbtBoardButtonTilesArray[invisibleButtonLocation[0]][column + 1].getText());
				
				// This is the location of the clicked number. It will be set to zero and the slide will be finished.
				else
					jbtBoardButtonTilesArray[invisibleButtonLocation[0]][column].setText("0");
			}	
		}
	}
	
	// This method will slide the row and place the invisible square at the end of the slide.
	public void slideRow()
	{
		// The columns buttons need to be moved up or down and the 0 needs to be moved to the clicked numbers location.
		
		int row;
		
		// When the row of the 0 is greater than the row of the clicked location the if statement will execute.
		if (invisibleButtonLocation[0] > clickLocation[0])
		{
			// starting from the 0 button the row is decremented.
			for (row = invisibleButtonLocation[0]; row >= clickLocation[0]; row--)
			{
				// When the row is greater than the row of the clicked buttons column the if statement will execute.
				if (row > clickLocation[0])	
					jbtBoardButtonTilesArray[row][invisibleButtonLocation[1]].setText(jbtBoardButtonTilesArray[row - 1][invisibleButtonLocation[1]].getText());
				
				// This is the location of the clicked number. It will be set to zero and the slide will be finished.
				else
					jbtBoardButtonTilesArray[row][invisibleButtonLocation[1]].setText("0");
			}
		} 
		// When the row of the 0 is less than the clicked locations row the if statement will execute.
		else if (invisibleButtonLocation[0] < clickLocation[0])
		{
			// The row is incremented and the and the button will be set to the one beneath it.
			for (row = invisibleButtonLocation[0]; row <= clickLocation[0]; row++)
			{
				if (row < clickLocation[0])
					jbtBoardButtonTilesArray[row][invisibleButtonLocation[1]].setText(jbtBoardButtonTilesArray[row + 1][invisibleButtonLocation[1]].getText());
				
				// This is the location of the clicked number. It will be set to zero and the slide will be finished.
				else
					jbtBoardButtonTilesArray[row][invisibleButtonLocation[1]].setText("0");
			}	
		}
	}
	
	class moveListener implements ActionListener
	{
		// This class handles the action listeners on the buttons on the playing board.

		@Override
		public void actionPerformed(ActionEvent squareClicked)
		{
			// The rows and columns of the board are checked to find where the click came from.
			for (int boardRows = 0; boardRows < TOTAL_BOARD_ROWS; boardRows++)
			{
				for (int boardColumns = 0; boardColumns < TOTAL_BOARD_COLUMNS; boardColumns++)
				{
					if (squareClicked.getSource() == jbtBoardButtonTilesArray[boardRows][boardColumns])
					{
						clickLocation[0] = boardRows;
						clickLocation[1] = boardColumns;
					}
				}
			}
			// Now the move method is called.
			userMove();
			
			// The next part checks to see if any square has been moved. If a move has been made the move count is incremented.
			
			// The move text is updated.
			moveCountLabel.setText("Moves : " + (numberOfMovesCounter));
			
			isWinner = false;
			
			// When the bottom right square is invisible the victory condition is checked.
			if (jbtBoardButtonTilesArray[3][3].getText().equals("0"))
			isWinner = victoryCondition();
			
			// If the win condition has been met a message displays the you won.
			if (isWinner)
			{
				JOptionPane.showMessageDialog(null, "You Won!");
			}
		}
	}
	
	class optionsListener implements ActionListener
	{
		// This class implements the action listener for the buttons on the options panel.
		@Override
		public void actionPerformed(ActionEvent optionClicked)
		{
			// If the source of the click is the save button the if statement will execute.
			if (optionClicked.getSource() == jbtOptionsButtonsArray[0])
			{
				try
				{
					// When the game save button is clicked a new save will be created and the numbers on
					// each of the buttons will be saved.
					DataOutputStream gameSave = new DataOutputStream(new FileOutputStream("savedGame"));
					
					for (int boardRows = 0; boardRows < TOTAL_BOARD_ROWS; boardRows++)
					{
						for (int boardColumns = 0; boardColumns < TOTAL_BOARD_COLUMNS; boardColumns++)
						{
							gameSave.writeUTF(jbtBoardButtonTilesArray[boardRows][boardColumns].getText());	
						}	
					}
					gameSave.close();
					
					// Also the number of moves needs to be saved.
					DataOutputStream savedMoves = new DataOutputStream(new FileOutputStream("savedMoves"));
					savedMoves.writeInt(numberOfMovesCounter);
					savedMoves.close();
				} catch(IOException ioe)
				{
				}			
							
			}
			// If the source of the click is the load button the if statement will execute.
			if (optionClicked.getSource() == jbtOptionsButtonsArray[1])
			{
				try
				{
					// When the load button is clicked the board will read the save file and put the 
					// saved numbers and their locations on the buttons.
					DataInputStream savedGame = new DataInputStream(new FileInputStream("savedGame"));
					  
					for (int boardRows = 0; boardRows < TOTAL_BOARD_ROWS; boardRows++)
					{
						for (int boardColumns = 0; boardColumns < TOTAL_BOARD_COLUMNS; boardColumns++)
						{
							jbtBoardButtonTilesArray[boardRows][boardColumns].setText(savedGame.readUTF());	
						}	
					}
					savedGame.close();
					
					// Then the board needs to be updated so that the zero tile is set to not visible.
					visibilityToTrue();
					visibilityToFalse();
					
					// The moves also need to be updated to the saved number of moves.
					DataInputStream savedMoves = new DataInputStream(new FileInputStream("savedMoves"));
					numberOfMovesCounter = savedMoves.readInt();
					savedMoves.close();
					moveCountLabel.setText("Moves : " + (numberOfMovesCounter));
					
				} catch(IOException ioe)
				{
				}
				
			}
			// If the source of the click is the help button the if statement will execute.
			if (optionClicked.getSource() == jbtOptionsButtonsArray[2])
			{
					// The help frame and instructions are created.
					JTextPane instructions = new JTextPane();
					instructions.setLayout(null);
					instructions.setText(
							"Welcome To The Fifteen Square Help Page\n"
							+ "How to play : \n"
							+ "Click the buttons to slide them to the invisible square.\n"
							+ "Objective : \n"
							+ "Reorder the tiles from one to fifteen with the blank square in the bottom right corner.\n"
										);
				
				instructions.setFont(new Font("arialblack", Font.PLAIN, 20));
				instructions.setEditable(false);
				JFrame help = new JFrame();
				help.add(instructions);
				
				// Some sizing and and basic settings are changed here.
				help.setTitle("Help");
				help.setSize(GAME_BOARD_SIDE_LENGTH * 2, GAME_BOARD_SIDE_LENGTH - 80);
				help.setLocationRelativeTo(null);
				help.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				help.setVisible(true);
				help.setLayout(null);
			}
				
			
			// If the exit button is clicked the game will end.
			else if (optionClicked.getSource() == jbtOptionsButtonsArray[4])
			{
				System.exit(EXIT_ON_CLOSE);
			}
			
			// If the new button is clicked the board will be reshuffled and move count is reset
			else if (optionClicked.getSource() == jbtOptionsButtonsArray[5])
			{
				numberOfMovesCounter = 0;
				moveCountLabel.setText("Moves : " + (numberOfMovesCounter/2));
				shuffle();
			}
		}
		
	}
		
	public void userMove()
	{
		findInvisibleSquare();
		visibilityToFalse();   // The 0 square needs to be reset to invisible.
		if (clickLocation[0] == invisibleButtonLocation[0])
		{
			slideColumn(); // This will slide the buttons toward the blank column.
			numberOfMovesCounter++;
		}
		else if (clickLocation[1] == invisibleButtonLocation[1])
		{
			slideRow(); // This will slide the the buttons toward the blank row.
			numberOfMovesCounter++;
		}
		// The buttons need to be reset to visible so that button is not left not visible.
		visibilityToTrue();
							
		findInvisibleSquare(); // The 0 square needs to be found again.
		visibilityToFalse();   // The 0 square needs to be reset to invisible.
		
	}
	
	public boolean victoryCondition()
	{
		// If the board is in order and the 0 is in the bottom right the victory condition is set to true and
		// returned to the action move listener class.
		int correctCount = 0;
		
		for (int boardRows = 0; boardRows < TOTAL_BOARD_ROWS; boardRows++)
		{
			for (int boardColumns = 0; boardColumns < TOTAL_BOARD_COLUMNS; boardColumns++)
			{
				if (jbtBoardButtonTilesArray[boardRows][boardColumns].getText().equals((boardRows * 4) + boardColumns + 1 + ""))
				{
					correctCount++;
				}
			}
		}
		
		if (correctCount == 15)
			isWinner = true;
		
		return isWinner;
	}
}