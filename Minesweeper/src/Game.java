import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.Timer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
//import javax.swing.Timer;

public class Game extends JPanel implements MouseListener{
	// there are 10 mines in our game every time, so we need to keep track of this.
	private int mines_left = 10;
	//private int mine_exists = 1;
	private int timeElapsed = 0;
	private Grid gameGrid = new Grid();
	private JFrame frame;
	private final int rows    = 10;
	private final int columns = 10;
	private String username = "";
	private File file = new File("gameScores.txt");
	
	private ImageIcon one = new ImageIcon("button_1.gif");
	private ImageIcon two = new ImageIcon("button_2.gif");
	private ImageIcon three = new ImageIcon("button_3.gif");
	private ImageIcon four = new ImageIcon("button_4.gif");
	private ImageIcon five = new ImageIcon("button_5.gif");
	private ImageIcon six = new ImageIcon("button_6.gif");
	private ImageIcon seven = new ImageIcon("button_7.gif");
	private ImageIcon eight = new ImageIcon("button_8.gif");
	private ImageIcon bomb = new ImageIcon("button_bomb_blown.gif");
	private ImageIcon flag = new ImageIcon("button_flag.gif");
	private ImageIcon question = new ImageIcon("button_question.gif");
	private ImageIcon smile = new ImageIcon("smile_button.gif");
	private ImageIcon dead = new ImageIcon("head_dead.gif");
	private Timer timerTimer = new Timer();// = new Timer(1000, actionTimeListener);
	
	
	private Grid gridIntBoard;
	// increments to 90. If it doesnt hit a mine, the player should win.
	private int movesMade = 0;
	JLabel minesGui = new JLabel(String.valueOf("Mines: " + mines_left));
    JButton resetButton = new JButton();
    JLabel timer = new JLabel(String.valueOf("Time: " + timeElapsed));
    
	// Declaring a 2d array for our JButtons.
	private JButton grid[][] = new JButton[rows][columns];

	// Constructor for our Game class.
	public Game() 
	{
		resetButton.setSize(1, 1);
		gridIntBoard = new Grid();
		JPanel gameBoard = new JPanel();
		gameBoard.setLayout(new GridLayout(10, 10));
		// Panel for the scoreboard.
		JPanel scoreBoard = new JPanel();
		//scoreBoard.setLayout(new BoxLayout(scoreBoard, BoxLayout.Y_AXIS));
		scoreBoard.setLayout(new GridLayout(1, 3));
		scoreBoard.add(minesGui);
		scoreBoard.add(resetButton);
		scoreBoard.add(timer);
		//resetButton.setText("-1");
		resetButton.addMouseListener(this);
		resetButton.setIcon(smile);
		grid = new JButton[rows][columns];
		for(int i = 0 ; i < rows; i++)
		{
			for(int j = 0; j < columns; j++)
			{
				grid[i][j] = new JButton();
				grid[i][j].addMouseListener(this);
				gameBoard.add(grid[i][j]);
			}
		}

		// must be initialized first
		JMenuBar menuBar = new JMenuBar();
		// the keyword that the user sees
		JMenu gameMenu = new JMenu("Game");
		// VK_G pretty much means alt + G
		gameMenu.setMnemonic(KeyEvent.VK_G);

		// extension of the "Game" Menu tab
		JMenuItem exitMenuItem = new JMenuItem(new MenuItemListener("Exit", KeyEvent.VK_X));
		JMenuItem aboutMenuItem = new JMenuItem(new MenuItemListener("About", KeyEvent.VK_A));
		JMenuItem topTenMenuItem = new JMenuItem(new MenuItemListener("Top Ten", KeyEvent.VK_T));
		JMenuItem resetMenuItem = new JMenuItem(new MenuItemListener("Reset", KeyEvent.VK_R));
		JMenuItem helpMenuItem = new JMenuItem(new MenuItemListener("Help", KeyEvent.VK_L));

		// reset, top ten, exit
		// help, about
		// Another option at the top for "Help"
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_L);


		/*
		 * I actually tried to separate the menuBar from the constructor and call it from a method
		 * but it was giving me problems. I primarily wanted to keep the constructor clean.
		 */
		menuBar.add(gameMenu);
		gameMenu.add(resetMenuItem);
		gameMenu.add(topTenMenuItem);
		gameMenu.add(exitMenuItem);
		helpMenu.add(helpMenuItem);
		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		// The "window" at the very top
		frame = new JFrame("Minesweeper");
		// controls size of entire grid.
		frame.setSize(500,  500);
		// allows the program to respond when user wants to close the frame.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);	
		frame.add(scoreBoard, BorderLayout.NORTH);
		//frame.add(scoreBoard); //, BorderLayout.NORTH
		frame.setJMenuBar(menuBar);
		frame.add(gameBoard, BorderLayout.CENTER);
		//frame.add(guiLabel);

		gridIntBoard.buildGrid();
		System.out.println(gridIntBoard.getGrid());

	}
	public void gameStart()
	{
		Grid gridBoard = new Grid();

		gridBoard.buildGrid();
	}

	private void getUsername() throws FileNotFoundException
	{
		System.out.println("Please enter in a username: ");
		Scanner sc = new Scanner(System.in);
		
		username = sc.toString();
		File file = new File("gameScores.txt");
		final PrintWriter writer = new PrintWriter(file);	
		writer.write(username);
		
	}
	private ActionListener actionTimeListener = new ActionListener()
	{
	public void actionPerformed(ActionEvent actionEvent)
	{
		timer.setText("Timer: " + timeElapsed);
	}
	};
	private void gameWinner()
	{
		JOptionPane.showMessageDialog(null, "You win!");
		//}
	}

	private class timerTask extends TimerTask {
        public void run() {
            EventQueue.invokeLater(new Runnable() {
            	public void run() {
                    timer.setText(String.valueOf("Time: "+ timeElapsed++));
                }
            });
        }
    }
	private void gameOver() throws FileNotFoundException{
		/*
		 * Need to display all mines to the user once the game is 
		 * won or lost.
		 */
		final PrintWriter writer = new PrintWriter(file);	
		int gridNewIntBoard[][] = gridIntBoard.getGrid();
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < columns; j++)
			{
				if(gridNewIntBoard[i][j] == 50)
				{
					grid[i][j].setIcon(bomb);
					resetButton.setIcon(dead);
					timerTimer.cancel();
					writer.write(timeElapsed);
				}			
			}
		}
		JOptionPane.showMessageDialog(null, "Game Over");
	}

/* 
 * LIST OF SETTERS AND GETTERS FOR THE GAME CLASS
 */
	public int getMines_left() {
		return mines_left;
	}


	public void setMines_left(int mines_left) {
		this.mines_left = mines_left;
	}


	public int getTimer() {
		return timeElapsed;
	}


	public void setTimer(int timer) {
		this.timeElapsed = timeElapsed;
	}


	public Grid getGameGrid() {
		return gameGrid;
	}


	public void setGameGrid(Grid gameGrid) {
		this.gameGrid = gameGrid;
	}


	public JFrame getFrame() {
		return frame;
	}


	public void setFrame(JFrame frame) {
		this.frame = frame;
	}


	public Grid getGridIntBoard() {
		return gridIntBoard;
	}


	public void setGridIntBoard(Grid gridIntBoard) {
		this.gridIntBoard = gridIntBoard;
	}


	public JButton[][] getGrid() {
		return grid;
	}


	public void setGrid(JButton[][] grid) {
		this.grid = grid;
	}


	public int getRows() {
		return rows;
	}


	public int getColumns() {
		return columns;
	}

/* END SETTERS AND GETTERS *
 * 
 */
	

	/*
	 * probably didnt' need to override this method, to be honest, but I did anyways. 
	 * This method handles all the mouse clicks for the whole game while I have a separate one for the menu.
	 * For each JButton, including the reset button, this method handles each click if it's the "source"
	 * gridIntBoard was something implemented in Grid. It made it very easy to place the mines this way.
	 * Unique value for a mine was 50.
	 * I would compare a grid of Jbuttons to a grid of integers and they basically mapped to one another.
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// BUTTON1 = left click
		//MouseEvent arg1;
		//MenuItemListener resetTheGame = new MenuItemListener(null, KeyEvent.VK_Q);
		if(resetButton == arg0.getSource())
		{
			System.out.println("TESTTTT");
			reset();
		}
		if(arg0.getButton() == arg0.BUTTON1)
		{
			for(int i = 0; i< rows; i++)
			{
				for(int j = 0; j < columns; j++)
				{
					if(grid[i][j] == arg0.getSource())
					{
						if(timeElapsed == 0 )
						{
							timerTimer = new Timer();
	        				timerTimer.schedule(new timerTask(), 0, 1000);
						}
						
						int gridNewIntBoard[][] = gridIntBoard.getGrid();

						/* checks to see if button is ? or M */
						if(grid[i][j].getText() == "M")
						{
							continue;
						}

						if(grid[i][j].getText() == "?")
						{
							continue;
						}
				

						if(gridNewIntBoard[i][j] == 50)
						{
							//System.out.println("EYYYYY");
							//grid[i][j].setText("M");
							grid[i][j].setIcon(bomb);
							try {
								gameOver();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(gridNewIntBoard[i][j] == 0)
						{
							movesMade++;
						}
						if(gridNewIntBoard[i][j] == 1)
						{
							// setIcon for images
							movesMade++;
							System.out.println("One");
							grid[i][j].setIcon(one);
							//grid[i][j].setText("1");
							//grid[i][j].setBackground("GRAY");
						}
						if(gridNewIntBoard[i][j] == 2)
						{
							// setIcon for images
							movesMade++;
							System.out.println("Two");
							grid[i][j].setIcon(two);
							//grid[i][j].setText("2");
						}
						if(gridNewIntBoard[i][j] == 3)
						{
							// setIcon for images
							movesMade++;
							System.out.println("Three");
							grid[i][j].setIcon(three);
							//grid[i][j].setText("3");						
						}
						if(gridNewIntBoard[i][j] == 4)
						{
							// setIcon for images
							movesMade++;
							System.out.println("Four");
							grid[i][j].setIcon(four);
							//grid[i][j].setText("4");
						}
						if(gridNewIntBoard[i][j] == 5)
						{
							// setIcon for images
							movesMade++;
							System.out.println("Five");
							grid[i][j].setIcon(five);
							//grid[i][j].setText("5");

						}
						if(gridNewIntBoard[i][j] == 6)
						{
							// setIcon for images
							movesMade++;
							System.out.println("Six");
							grid[i][j].setIcon(six);
							//grid[i][j].setText("6");

						}
						if(gridNewIntBoard[i][j] == 7)
						{
							// setIcon for images
							movesMade++;
							System.out.println("Seven");
							grid[i][j].setIcon(seven);
							//grid[i][j].setText("7");

						}
						if(gridNewIntBoard[i][j] == 8)
						{
							// setIcon for images
							movesMade++;
							System.out.println("Eight");
							grid[i][j].setIcon(eight);
							//grid[i][j].setText("8");

						}
						/*		if(gridNewIntBoard[i][j] == 9)
						{
							// setIcon for images
							System.out.println("Nine");
							grid[i][j].setText("9");

						}*/
						if(movesMade == 90)
						{
							gameWinner();
						}

					}
				}
			}		
		}	

		/* 
		 * This handles clicks for the RIGHT mouse clicks. SImply setting flags and question marks.
		 */
		if(arg0.getButton() == arg0.BUTTON3)
		{
			for(int i = 0; i< rows; i++)
			{
				for(int j = 0; j < columns; j++)
				{
					if(mines_left == 0)
					{
						//System.out.println("You are out of mines!!!");
						continue;
					}
					if(grid[i][j] == arg0.getSource() && grid[i][j].getText() == ""){
						grid[i][j].setText("M");
						grid[i][j].setIcon(flag);
						mines_left--;
						minesGui.setText("Mines: " + mines_left);
						System.out.println("These are the mines remaining: " + mines_left);
					}
					else if(grid[i][j].getText() == "M" && grid[i][j] == arg0.getSource())
					{
						System.out.println("FFF");
						mines_left++;
						grid[i][j].setText("?");
						grid[i][j].setIcon(question);
					}
					else if(grid[i][j].getText() == "?" && grid[i][j] == arg0.getSource())
					{
						grid[i][j].setText("");
						grid[i][j].setIcon(null);
					}
					
				}
			}
			if(arg0.getButton() == arg0.BUTTON1)
			{
				if(resetButton.getText() == "-1")
				{
					reset();
				}
			//if(arg1.getButton() == arg1.BUTTON3){
			/*for(int i = 0; i< rows; i++)
			{
				for(int j = 0; j < columns; j++)
				{
					if(grid[i][j] == arg0.getSource()){
					if(grid[i][j].getText() == "F")
					{
						System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFF");
						grid[i][j].setText("?");
					}
					}
				}
				
			}*/
			}
		}
	}
	
	/* reset the game if called.
	 * 
	 */

	private void reset()
	{
		MenuItemListener resetButton = new MenuItemListener(null, KeyEvent.VK_Q);
		resetButton.reset();
		//gameStart();
	}
	

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
