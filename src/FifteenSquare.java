
//EECS 1510
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
	public final int TOTAL_ROWS = 4; // Number of rows on the board.
	public final int TOTAL_COLUMNS = 4; // Number of columns on the board.
	public final int BUTTON_SIDE = 70; // Size of the square button side.
	public final int SPACE = 1; // Size of the space between buttons.
	public final int FRAME_MARGIN = 5; // Size of the gap between board and the frame.
	public final int BOARD_SIDE = (3 * SPACE) + (4 * BUTTON_SIDE); // Size of the game board.
	public final int OPTIONS_BUTTON_HEIGHT = 40; // Height of the options buttons.
	public final int MOVE_LABEL_HEIGHT = 10; // Height of the move label.
	public final int OPTIONS_PANEL_HEIGHT = 2 * OPTIONS_BUTTON_HEIGHT + 3 * SPACE + MOVE_LABEL_HEIGHT; // Total height of the options panel.
	public final int OPTIONS_BUTTON_WIDTH = 93; // Width of each of the option buttons.
	public int[] invisibleLocation = new int[2]; // The 0 position is the row and 1 is the column location of the 0 square.
	public boolean isWinner; // This lets the program know when the user has won.
	public int numberOfMoves = 0; // Counts the number of moves the player makes.
	public JLabel moveCount; // The GUI component that displays the number of moves.
	public JLabel gameCount; // Total number of games played label.
	public JButton[] jbtOptions = new JButton[6]; // The array of options buttons.
	public JButton[][] jbtBoardSquares = new JButton[TOTAL_ROWS][TOTAL_COLUMNS]; // 4x4 array of buttons that represent the tiles. 
	public int totalGames = 0; // Total number of games played.
	public int[] clickLocation = new int[2]; // This is like invisible location except that it is the location of the click.
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
				JPanel squarePanel = new JPanel(); // Panel for the square tiles
				squarePanel.setLayout(null); // Makes the panel have no set layout.
				squarePanel.setBounds(FRAME_MARGIN, FRAME_MARGIN, BOARD_SIDE + 10, BOARD_SIDE + 10); // Adjusts size and position of the panel.
				squarePanel.setBackground(Color.BLACK);
				squarePanel.setBorder(BorderFactory.createLineBorder(Color.black));
				
				// The array of buttons needs to have the numbers 0-15 on them. To do that the rows and 
				// columns must be incremented. Then the buttons need to be added to the panel.
				for (int boardRows = 0; boardRows < TOTAL_ROWS; boardRows++)
				{
					for (int boardColumns = 0; boardColumns < TOTAL_COLUMNS; boardColumns++)
					{
						// Each button is created and the text is set to 1-16
						jbtBoardSquares[boardRows][boardColumns] = new JButton(((boardRows * 4) + boardColumns + 1) + "");
						
						// The created squares are not formatted correctly because when a double digit occurs the
						// squares become wider and the buttons are overlapping. To fix this the set bounds function 
						// will be used on the squares to adjust the size and location.
						jbtBoardSquares[boardRows][boardColumns].setBounds
						(
							(boardColumns * BUTTON_SIDE) + (SPACE * boardColumns) + FRAME_MARGIN, 	// X position of the button.
							(boardRows * BUTTON_SIDE) + (SPACE * boardRows) + FRAME_MARGIN, 		// Y position of the button.
							BUTTON_SIDE, BUTTON_SIDE												// Width and height of the button.
						);
						
						// Action listeners need to be added to the buttons now to detect clicks.
						jbtBoardSquares[boardRows][boardColumns].addActionListener(new moveListener());
						
						// The buttons have a dotted line around the text. This next piece of code gets rid of this line.
						jbtBoardSquares[boardRows][boardColumns].setFocusable(false);
						
						// This makes the buttons and fonts of the buttons look nice.
						jbtBoardSquares[boardRows][boardColumns].setFont(new Font("arialblack", Font.BOLD, 25));
						jbtBoardSquares[boardRows][boardColumns].setBorder(BorderFactory.createRaisedBevelBorder());
						
						// Next the buttons are added to the board panel.
						squarePanel.add(jbtBoardSquares[boardRows][boardColumns]);		
					}
				}
				// Now the 16 square will be set to 0.
				jbtBoardSquares[3][3].setText("0");
				
				// This creates the options panel buttons and disables the ones that are not yet implemented. Then 
				// the dotted line is disabled.
				jbtOptions[0] = new JButton("Save");
				jbtOptions[0].setFocusable(false);
				jbtOptions[1] = new JButton("Load");
				jbtOptions[1].setFocusable(false);
				jbtOptions[2] = new JButton("Help");
				jbtOptions[2].setFocusable(false);
				jbtOptions[3] = new JButton("Hints");
				jbtOptions[3].setEnabled(false);
				jbtOptions[3].setFocusable(false);
				jbtOptions[4] = new JButton("Exit");
				jbtOptions[4].setFocusable(false);
				jbtOptions[5] = new JButton("New");
				jbtOptions[5].setFocusable(false);
				
				// The size and position of the options buttons are formatted in the loop.
				for (int i = 0; i < 6; i++)
				{
					if (i < 3)
					{
						jbtOptions[i].setBounds
						(
							(i * OPTIONS_BUTTON_WIDTH) + (i * SPACE) + FRAME_MARGIN + 7,				// X position of the button.
							BOARD_SIDE + FRAME_MARGIN + SPACE + 20,										// Y position of the button.
							OPTIONS_BUTTON_WIDTH, OPTIONS_BUTTON_HEIGHT 							// Width and height of the button.
						);
						jbtOptions[i].setBorder(BorderFactory.createRaisedBevelBorder());
						jbtOptions[i].setFont(new Font("arialblack", Font.BOLD, 18));
					}
					
					else if (i >= 3)
					{
						jbtOptions[i].setBounds
						(
							((i - 3) * OPTIONS_BUTTON_WIDTH) + ((i - 3) * SPACE) + FRAME_MARGIN + 7,    // X position of the button.
							BOARD_SIDE + FRAME_MARGIN + OPTIONS_BUTTON_HEIGHT + 28,						// Y position of the button.
							OPTIONS_BUTTON_WIDTH, OPTIONS_BUTTON_HEIGHT								// Width and height of the button.
						);
						jbtOptions[i].setBorder(BorderFactory.createRaisedBevelBorder());
						jbtOptions[i].setFont(new Font("arialblack", Font.BOLD, 18));
					}
				}
				
				// Finally the listener for clicks is added to the option buttons.
				for(int i = 0; i < 6; i++)
				{
					jbtOptions[i].addActionListener(new optionsListener());
				}
				
				// There also needs to be a label for the games played and number of turns.
				gameCount = new JLabel("Games : " + totalGames);
				gameCount.setFont(new Font("arialblack", Font.BOLD, 12));
				moveCount = new JLabel("Moves : " + numberOfMoves);
				moveCount.setFont(new Font("arialblack", Font.BOLD, 12));
				
				// The labels needs to be formatted.
				gameCount.setBounds(FRAME_MARGIN + 225, BOARD_SIDE + OPTIONS_PANEL_HEIGHT - MOVE_LABEL_HEIGHT + 33, 100, MOVE_LABEL_HEIGHT);
				moveCount.setBounds(FRAME_MARGIN + 7, BOARD_SIDE + OPTIONS_PANEL_HEIGHT - MOVE_LABEL_HEIGHT + 33, 141, MOVE_LABEL_HEIGHT);
				
				// This creates the options panel,sets the layout, sets the size, and then the options buttons and
				// move count are added to it.
				JPanel optionsPanel = new JPanel();
				optionsPanel.setLayout(null);
				optionsPanel.setBounds(FRAME_MARGIN, BOARD_SIDE + FRAME_MARGIN + 28, BOARD_SIDE + 28, OPTIONS_PANEL_HEIGHT + 28);
				
				optionsPanel.add(jbtOptions[0]);
				optionsPanel.add(jbtOptions[1]);
				optionsPanel.add(jbtOptions[2]);
				optionsPanel.add(jbtOptions[3]);
				optionsPanel.add(jbtOptions[4]);
				optionsPanel.add(jbtOptions[5]);
				optionsPanel.add(gameCount);
				optionsPanel.add(moveCount);
				
				// The frame needs the panels to be added to it, formatted to fit the game board, located correctly on the screen, 
				// and close when the x is clicked.
				add(squarePanel);
				add(optionsPanel);
				setTitle("Fifteen Square");
				setSize(BOARD_SIDE + (2 * FRAME_MARGIN) + 26, BOARD_SIDE + (2 * FRAME_MARGIN) + OPTIONS_PANEL_HEIGHT + 65);
				setLocationRelativeTo(null);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setVisible(true);
				setLayout(null);
				
				visibilityToFalse(); // Makes the button with 0 on it not visible before the game starts. 
				shuffle(); // Once the board is shuffled the game is ready to play.
				
	}
	
	// This method will find the location of the 0 square.
	public void findInvisibleSquare()
	{
		// The rows and columns are incremented and the text for each slot is searched. If that text is 0 the row and column 
		// of the location are stored in invisibleLocation.
		for (int boardRows = 0; boardRows < TOTAL_ROWS; boardRows++)
		{
			for (int boardColumns = 0; boardColumns < TOTAL_COLUMNS; boardColumns++)
			{
				if (jbtBoardSquares[boardRows][boardColumns].getText().equals("0"))
				{
					invisibleLocation[0] = boardRows;		// This is the row of the invisible button.
					invisibleLocation[1] = boardColumns;	// This is the column of the invisible button.
				}			
			}
		}
	}
	
	// This method sets the visibility of the 0 button to false.
	public void visibilityToFalse()
	{
		findInvisibleSquare(); // Gets the location of the 0 button.
		jbtBoardSquares[invisibleLocation[0]][invisibleLocation[1]].setVisible(false); // Then sets the location to not visible.		
	}
	
	// This method resets all the buttons to visible.
	public void visibilityToTrue()
	{
		// The rows and columns are incremented and then each of the buttons are set to visible.
		for (int boardRows = 0; boardRows < TOTAL_ROWS; boardRows++)
		{
			for (int boardColumns = 0; boardColumns < TOTAL_COLUMNS; boardColumns++)
			{
				jbtBoardSquares[boardRows][boardColumns].setVisible(true);				
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
		for (int boardRows = 0; boardRows < TOTAL_ROWS; boardRows++)
		{
			for (int boardColumns = 0; boardColumns < TOTAL_COLUMNS; boardColumns++)
			{
				
				// If the random number equals the number on the button the location of that button is stored.
				if (jbtBoardSquares[boardRows][boardColumns].getText().equals(randomNumber))
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
			if (clickLocation[0] == invisibleLocation[0])
			{
				slideColumn(); // This will slide the buttons toward the blank column.
			}
			
			// If the column of the clicked button is the same as the column of the 0 button the rows in that column
			// will slide from the clicked location to the 0 location.
			else if (clickLocation[1] == invisibleLocation[1])
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
		gameCount.setText("Games : " + totalGames);
	}
		
	// This method will slide the column and place the invisible square at the end of the slide.
	public void slideColumn()
	{
		// The rows buttons need to be moved left or right and the 0 needs to be moved to the random location.
		
		int column;
		
		// When the column of the 0 is greater than the column of the clicked numbers location the if statement will execute.
		if (invisibleLocation[1] > clickLocation[1])
		{
			// starting from the 0 button the column is decremented.
			for (column = invisibleLocation[1]; column >= clickLocation[1]; column--)
			{
				// When the column is greater than the column of the clicked buttons column the if statement will execute.
				// This sets the text of the button to be the same as the one to the left of it until the clicked 
				// number is reached.
				if (column > clickLocation[1])	
					jbtBoardSquares[invisibleLocation[0]][column].setText(jbtBoardSquares[invisibleLocation[0]][column - 1].getText());
				
				// This is the location of the clicked number. It will be set to zero and the slide will be finished.
				else
					jbtBoardSquares[invisibleLocation[0]][column].setText("0");
			}
		} 
		// If the invisible squares column is the same as the clicked column the if statement will execute. 
		else if (invisibleLocation[1] < clickLocation[1])
		{
			for (column = invisibleLocation[1]; column <= clickLocation[1]; column++)
			{
				// When the column is less than the clicked locations column the if statement will execute.
				// This sets the text of the button to be the same as the one to the right of it until the clicked 
				// number is reached.
				if (column < clickLocation[1])
					jbtBoardSquares[invisibleLocation[0]][column].setText(jbtBoardSquares[invisibleLocation[0]][column + 1].getText());
				
				// This is the location of the clicked number. It will be set to zero and the slide will be finished.
				else
					jbtBoardSquares[invisibleLocation[0]][column].setText("0");
			}	
		}
	}
	
	// This method will slide the row and place the invisible square at the end of the slide.
	public void slideRow()
	{
		// The columns buttons need to be moved up or down and the 0 needs to be moved to the clicked numbers location.
		
		int row;
		
		// When the row of the 0 is greater than the row of the clicked location the if statement will execute.
		if (invisibleLocation[0] > clickLocation[0])
		{
			// starting from the 0 button the row is decremented.
			for (row = invisibleLocation[0]; row >= clickLocation[0]; row--)
			{
				// When the row is greater than the row of the clicked buttons column the if statement will execute.
				if (row > clickLocation[0])	
					jbtBoardSquares[row][invisibleLocation[1]].setText(jbtBoardSquares[row - 1][invisibleLocation[1]].getText());
				
				// This is the location of the clicked number. It will be set to zero and the slide will be finished.
				else
					jbtBoardSquares[row][invisibleLocation[1]].setText("0");
			}
		} 
		// When the row of the 0 is less than the clicked locations row the if statement will execute.
		else if (invisibleLocation[0] < clickLocation[0])
		{
			// The row is incremented and the and the button will be set to the one beneath it.
			for (row = invisibleLocation[0]; row <= clickLocation[0]; row++)
			{
				if (row < clickLocation[0])
					jbtBoardSquares[row][invisibleLocation[1]].setText(jbtBoardSquares[row + 1][invisibleLocation[1]].getText());
				
				// This is the location of the clicked number. It will be set to zero and the slide will be finished.
				else
					jbtBoardSquares[row][invisibleLocation[1]].setText("0");
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
			for (int boardRows = 0; boardRows < TOTAL_ROWS; boardRows++)
			{
				for (int boardColumns = 0; boardColumns < TOTAL_COLUMNS; boardColumns++)
				{
					if (squareClicked.getSource() == jbtBoardSquares[boardRows][boardColumns])
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
			moveCount.setText("Moves : " + (numberOfMoves));
			
			isWinner = false;
			
			// When the bottom right square is invisible the victory condition is checked.
			if (jbtBoardSquares[3][3].getText().equals("0"))
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
			if (optionClicked.getSource() == jbtOptions[0])
			{
				try
				{
					// When the game save button is clicked a new save will be created and the numbers on
					// each of the buttons will be saved.
					DataOutputStream gameSave = new DataOutputStream(new FileOutputStream("savedGame"));
					
					for (int boardRows = 0; boardRows < TOTAL_ROWS; boardRows++)
					{
						for (int boardColumns = 0; boardColumns < TOTAL_COLUMNS; boardColumns++)
						{
							gameSave.writeUTF(jbtBoardSquares[boardRows][boardColumns].getText());	
						}	
					}
					gameSave.close();
					
					// Also the number of moves needs to be saved.
					DataOutputStream savedMoves = new DataOutputStream(new FileOutputStream("savedMoves"));
					savedMoves.writeInt(numberOfMoves);
					savedMoves.close();
				} catch(IOException ioe)
				{
				}			
							
			}
			// If the source of the click is the load button the if statement will execute.
			if (optionClicked.getSource() == jbtOptions[1])
			{
				try
				{
					// When the load button is clicked the board will read the save file and put the 
					// saved numbers and their locations on the buttons.
					DataInputStream savedGame = new DataInputStream(new FileInputStream("savedGame"));
					  
					for (int boardRows = 0; boardRows < TOTAL_ROWS; boardRows++)
					{
						for (int boardColumns = 0; boardColumns < TOTAL_COLUMNS; boardColumns++)
						{
							jbtBoardSquares[boardRows][boardColumns].setText(savedGame.readUTF());	
						}	
					}
					savedGame.close();
					
					// Then the board needs to be updated so that the zero tile is set to not visible.
					visibilityToTrue();
					visibilityToFalse();
					
					// The moves also need to be updated to the saved number of moves.
					DataInputStream savedMoves = new DataInputStream(new FileInputStream("savedMoves"));
					numberOfMoves = savedMoves.readInt();
					savedMoves.close();
					moveCount.setText("Moves : " + (numberOfMoves));
					
				} catch(IOException ioe)
				{
				}
				
			}
			// If the source of the click is the help button the if statement will execute.
			if (optionClicked.getSource() == jbtOptions[2])
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
				help.setSize(BOARD_SIDE * 2, BOARD_SIDE - 80);
				help.setLocationRelativeTo(null);
				help.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				help.setVisible(true);
				help.setLayout(null);
			}
				
			
			// If the exit button is clicked the game will end.
			else if (optionClicked.getSource() == jbtOptions[4])
			{
				System.exit(EXIT_ON_CLOSE);
			}
			
			// If the new button is clicked the board will be reshuffled and move count is reset
			else if (optionClicked.getSource() == jbtOptions[5])
			{
				numberOfMoves = 0;
				moveCount.setText("Moves : " + (numberOfMoves/2));
				shuffle();
			}
		}
		
	}
		
	public void userMove()
	{
		findInvisibleSquare();
		visibilityToFalse();   // The 0 square needs to be reset to invisible.
		if (clickLocation[0] == invisibleLocation[0])
		{
			slideColumn(); // This will slide the buttons toward the blank column.
			numberOfMoves++;
		}
		else if (clickLocation[1] == invisibleLocation[1])
		{
			slideRow(); // This will slide the the buttons toward the blank row.
			numberOfMoves++;
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
		
		for (int boardRows = 0; boardRows < TOTAL_ROWS; boardRows++)
		{
			for (int boardColumns = 0; boardColumns < TOTAL_COLUMNS; boardColumns++)
			{
				if (jbtBoardSquares[boardRows][boardColumns].getText().equals((boardRows * 4) + boardColumns + 1 + ""))
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
