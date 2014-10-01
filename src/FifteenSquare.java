//David Carek

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class FifteenSquare extends JFrame 
{
	// Fifteen square is a game in which the user tries to reorder a set of 15 tiles from least to greatest 
	// in a 4x4 playing grid. This program will allow the user to play the game in a GUI. They will have the
	// option of saving and loading games. They will also be able to create a new game and ask how to play. 
	//The number of moves will also be displayed and an unclickable hints button will be displayed.
	
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
	
	public final int ROW = 0;
	
	public final int COLUMN = 1;
	
	public boolean isWinner; 
	
	public int numberOfMovesCounter = 0; 
	
	public JLabel moveCountLabel; 
	
	public JLabel gameCountLabel; 
	
	public JButton[] jbtOptionsButtonsArray = new JButton[6]; 
	
	public JButton[][] jbtBoardTilesArray = new JButton[TOTAL_BOARD_ROWS][TOTAL_BOARD_COLUMNS];
	
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
	
	public final int HELP_WINDOW_WIDTH = 500;
	
	public final int HELP_WINDOW_HEIGHT = 200;
	
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
						
						jbtBoardTilesArray[currentBoardRow][currentBoardColumn] = new JButton();
						
						jbtBoardTilesArray[currentBoardRow][currentBoardColumn].setText(buttonTextAlgorithm + "");
						
						jbtBoardTilesArray[currentBoardRow][currentBoardColumn].setBounds
						(
			/*X Position*/	(currentBoardColumn * SQUARE_BUTTON_SIDE_LENGTH) + (SPACE_BETWEEN_BUTTONS * currentBoardColumn) + FRAME_MARGIN, 
			/*Y Position*/	(currentBoardRow * SQUARE_BUTTON_SIDE_LENGTH) + (SPACE_BETWEEN_BUTTONS * currentBoardRow) + FRAME_MARGIN, 		
			/*Width*/		(SQUARE_BUTTON_SIDE_LENGTH),
			/*Height*/		(SQUARE_BUTTON_SIDE_LENGTH)												
						);
						
						jbtBoardTilesArray[currentBoardRow][currentBoardColumn].addActionListener(new moveListener());
						
						jbtBoardTilesArray[currentBoardRow][currentBoardColumn].setFocusable(false);
						
						jbtBoardTilesArray[currentBoardRow][currentBoardColumn].setFont(new Font("arialblack", Font.BOLD, 25));
						
						jbtBoardTilesArray[currentBoardRow][currentBoardColumn].setBorder(BorderFactory.createRaisedBevelBorder());
						
						mainGameTilesPanel.add(jbtBoardTilesArray[currentBoardRow][currentBoardColumn]);		
					}
				}
				
				int invisibleButtonStartingRow = 3;
				int invisibleButtonStartingColumn = 3;
				jbtBoardTilesArray[invisibleButtonStartingRow][invisibleButtonStartingColumn].setText("0");
				
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
					jbtOptionsButtonsArray[currentOptionsButton].addActionListener(new optionsButtonListener());
					
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
				
				shuffleTiles(); // Once the board is shuffled the game is ready to play.
				
	}
	
	public void findZeroTile()
	{
		for (int tileRow = 0; tileRow < TOTAL_BOARD_ROWS; tileRow++)
		{
			for (int tileColumn = 0; tileColumn < TOTAL_BOARD_COLUMNS; tileColumn++)
			{
				if (jbtBoardTilesArray[tileRow][tileColumn].getText().equals("0"))
				{
					invisibleButtonLocation[ROW] = tileRow;		
					invisibleButtonLocation[COLUMN] = tileColumn;	 
				}			
			}
		}
	}
	
	public void setZeroTileInvisible()
	{
		findZeroTile(); 
		jbtBoardTilesArray[invisibleButtonLocation[ROW]]
		                  [invisibleButtonLocation[COLUMN]].setVisible(false); 		
	}
	
	public void allTilesVisible()
	{
		for (int boardRow = 0; boardRow < TOTAL_BOARD_ROWS; boardRow++)
		{
			for (int boardColumn = 0; boardColumn < TOTAL_BOARD_COLUMNS; boardColumn++)
			{
				jbtBoardTilesArray[boardRow][boardColumn].setVisible(true);				
			}	
		}
	}
	
	public void simulateRandomTileClick()
	{
		String randomNumber = "" + ((int) (Math.random() * 15 + 1)); 
		
		for (int tileRow = 0; tileRow < TOTAL_BOARD_ROWS; tileRow++)
		{
			for (int tileColumn = 0; tileColumn < TOTAL_BOARD_COLUMNS; tileColumn++)
			{
				// If the random number equals the number on the button the location of that button is stored.
				if (jbtBoardTilesArray[tileRow][tileColumn].getText().equals(randomNumber))
				{
					clickLocation[ROW] = tileRow;		
					clickLocation[COLUMN] = tileColumn;	
				}			
			}
		}
	}
	
	public void shuffleTiles()
	{
		
		for (int shuffleMoveCount = 0; shuffleMoveCount < 1000; shuffleMoveCount++)
		{
			
			simulateRandomTileClick(); 
		
			findZeroTile();
			
			if (clickLocation[ROW] == invisibleButtonLocation[ROW])
			{
				slideTilesHorizontal();
			}
			
			else if (clickLocation[COLUMN] == invisibleButtonLocation[COLUMN])
			{
				slideTilesVertical();
			}
		}
		
		allTilesVisible();
		findZeroTile();
		setZeroTileInvisible();
		
		// Every time the board is shuffled another game needs to be added to the total number of 
		// games and the text on the label redisplayed. 
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
		
	public void slideTilesHorizontal()
	{
		if (invisibleButtonLocation[COLUMN] > clickLocation[COLUMN])
		{
			shiftTextRight();
		} 
		
		else if (invisibleButtonLocation[COLUMN] < clickLocation[COLUMN])
		{
			shiftTextLeft();
		}
	}
	
	public void shiftTextRight()
	{
		int column;
		for (column = invisibleButtonLocation[COLUMN]; column >= clickLocation[COLUMN]; column--)
		{
			if (column > clickLocation[COLUMN])	
				jbtBoardTilesArray[invisibleButtonLocation[ROW]][column].setText
				(jbtBoardTilesArray[invisibleButtonLocation[ROW]][column - 1].getText());
			
			// The tile clicked is set to 0 to finish the slide.
			else
				jbtBoardTilesArray[invisibleButtonLocation[ROW]][column].setText("0");
		}
	}
	
	public void shiftTextLeft()
	{
		int column;
		for (column = invisibleButtonLocation[COLUMN]; column <= clickLocation[COLUMN]; column++)
		{
			if (column < clickLocation[COLUMN])
				jbtBoardTilesArray[invisibleButtonLocation[ROW]][column].setText
				(jbtBoardTilesArray[invisibleButtonLocation[ROW]][column + 1].getText());
			
			// The tile clicked is set to 0 to finish the slide.
			else
				jbtBoardTilesArray[invisibleButtonLocation[ROW]][column].setText("0");
		}	
	}
	
	public void slideTilesVertical()
	{	
		if (invisibleButtonLocation[ROW] > clickLocation[ROW])
		{
			shiftTextUp();
		} 
		
		else if (invisibleButtonLocation[ROW] < clickLocation[ROW])
		{
			shiftTextDown();
		}
	}
	
	public void shiftTextUp()
	{
		int row;
		for (row = invisibleButtonLocation[ROW]; row >= clickLocation[ROW]; row--)
		{
			if (row > clickLocation[ROW])	
				jbtBoardTilesArray[row][invisibleButtonLocation[COLUMN]].setText
				(jbtBoardTilesArray[row - 1][invisibleButtonLocation[COLUMN]].getText());
						
			// The tile clicked is set to 0 to finish the slide.
			else
				jbtBoardTilesArray[row][invisibleButtonLocation[COLUMN]].setText("0");
		}
	}
	
	public void shiftTextDown()
	{
		int row;
		for (row = invisibleButtonLocation[ROW]; row <= clickLocation[ROW]; row++)
		{
			if (row < clickLocation[ROW])
				jbtBoardTilesArray[row][invisibleButtonLocation[COLUMN]].setText
				(jbtBoardTilesArray[row + 1][invisibleButtonLocation[COLUMN]].getText());
						
			// The tile clicked is set to 0 to finish the slide.
			else
				jbtBoardTilesArray[row][invisibleButtonLocation[COLUMN]].setText("0");
		}	
	}
	
	class moveListener implements ActionListener
	{
		// This class handles the action listeners on the buttons on the playing board.

		@Override
		public void actionPerformed(ActionEvent squareClicked)
		{
			for (int boardRow = 0; boardRow < TOTAL_BOARD_ROWS; boardRow++)
			{
				for (int boardColumn = 0; boardColumn < TOTAL_BOARD_COLUMNS; boardColumn++)
				{
					if (squareClicked.getSource() == jbtBoardTilesArray[boardRow][boardColumn])
					{
						clickLocation[ROW] = boardRow;
						clickLocation[COLUMN] = boardColumn;
					}
				}
			}
			
			userMove();
			
			moveCountLabel.setText("Moves : " + (numberOfMovesCounter));
			
			isWinner = false;
			
			int bottom = 3;
			int right = 3;
			if (jbtBoardTilesArray[bottom][right].getText().equals("0"))
				isWinner = victoryCondition();
			
			if (isWinner)
				JOptionPane.showMessageDialog(null, "You Won!");
		}
	}
	
	class optionsButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent optionButtonClicked)
		{
			if (optionButtonClicked.getSource() == jbtOptionsButtonsArray[SAVE_BUTTON_NUMBER])
			{
				try
				{
					DataOutputStream gameSave = new DataOutputStream(new FileOutputStream("savedGame"));
					
					for (int boardRow = 0; boardRow < TOTAL_BOARD_ROWS; boardRow++)
					{
						for (int boardColumn = 0; boardColumn < TOTAL_BOARD_COLUMNS; boardColumn++)
						{
							gameSave.writeUTF(jbtBoardTilesArray[boardRow][boardColumn].getText());	
						}	
					}
					gameSave.close();

					DataOutputStream savedMoves = new DataOutputStream(new FileOutputStream("savedMoves"));
					savedMoves.writeInt(numberOfMovesCounter);
					savedMoves.close();
				} catch(IOException ioe)
				{
				}			
							
			}
			
			if (optionButtonClicked.getSource() == jbtOptionsButtonsArray[LOAD_BUTTON_NUMBER])
			{
				try
				{
					DataInputStream savedGame = new DataInputStream(new FileInputStream("savedGame"));
					  
					for (int boardRow = 0; boardRow < TOTAL_BOARD_ROWS; boardRow++)
					{
						for (int boardColumn = 0; boardColumn < TOTAL_BOARD_COLUMNS; boardColumn++)
						{
							jbtBoardTilesArray[boardRow][boardColumn].setText(savedGame.readUTF());	
						}	
					}
					savedGame.close();
					
					allTilesVisible();
					setZeroTileInvisible();
					
					DataInputStream savedMoves = new DataInputStream(new FileInputStream("savedMoves"));
					numberOfMovesCounter = savedMoves.readInt();
					savedMoves.close();
					moveCountLabel.setText("Moves : " + (numberOfMovesCounter));
					
				} catch(IOException ioe)
				{
				}
				
			}
			
			if (optionButtonClicked.getSource() == jbtOptionsButtonsArray[HELP_BUTTON_NUMBER])
			{
					JTextPane instructions = new JTextPane();
					instructions.setLayout(null);
					instructions.setText(
							"Welcome To The Fifteen Square Help Page\n"
							+ "How to play : \n"
							+ "Click the buttons to slide them to the empty square.\n"
							+ "Objective : \n"
							+ "Reorder the tiles from one to fifteen with the blank square "
							+ "in the bottom right corner.\n"
										);
				
				instructions.setFont(new Font("arialblack", Font.PLAIN, 20));
				instructions.setEditable(false);
				JFrame help = new JFrame();
				help.add(instructions);
				
				
				help.setTitle("Help");
				help.setSize(HELP_WINDOW_WIDTH, HELP_WINDOW_HEIGHT);
				help.setLocationRelativeTo(null);
				help.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				help.setVisible(true);
				help.setLayout(null);
			}
				
			if (optionButtonClicked.getSource() == jbtOptionsButtonsArray[EXIT_BUTTON_NUMBER])
				System.exit(EXIT_ON_CLOSE);
			
			if (optionButtonClicked.getSource() == jbtOptionsButtonsArray[NEW_GAME_BUTTON_NUMBER])
			{
				numberOfMovesCounter = 0;
				moveCountLabel.setText("Moves : " + (numberOfMovesCounter));
				shuffleTiles();
			}
		}
		
	}
		
	public void userMove()
	{
		findZeroTile();
		
		if (clickLocation[ROW] == invisibleButtonLocation[ROW])
		{
			slideTilesHorizontal();
			numberOfMovesCounter++;
		}
		else if (clickLocation[COLUMN] == invisibleButtonLocation[COLUMN])
		{
			slideTilesVertical();
			numberOfMovesCounter++;
		}

		allTilesVisible();
		findZeroTile(); 
		setZeroTileInvisible();  
	}
	
	public boolean victoryCondition()
	{
		int correctTileOrderCount = 0;
		
		for (int boardRow = 0; boardRow < TOTAL_BOARD_ROWS; boardRow++)
		{
			for (int boardColumn = 0; boardColumn < TOTAL_BOARD_COLUMNS; boardColumn++)
			{
				if (jbtBoardTilesArray[boardRow][boardColumn].getText()
						.equals((boardRow * 4) + boardColumn + 1 + ""))
				{
					correctTileOrderCount++;
				}
			}
		}
		
		if (correctTileOrderCount == 15)
			isWinner = true;
		
		return isWinner;
	}
}