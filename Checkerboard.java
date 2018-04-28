package MiniCheckers;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This file defines two classes: Frame and Checkerboard.
 * 
 * Class Frame is a Swing-based GUI, which help show the Checker board and other visible components in it.
 * Class Checkerboard is used to define and initial the checker board.
 * 
 * @author Shuangshuang Shen
 *
 */
class Frame extends JFrame{

	private static final long serialVersionUID = -4928994797889836707L;
	
	final static public int FSIZE = 520;
	
	public JButton btn1 = new JButton("Easy");
	public JButton btn2 = new JButton("Medium");
	public JButton btn3 = new JButton("Hard");
	
	public Frame(DrawBoard b){
		this.setSize(FSIZE, FSIZE);
		
		//Bottom Panel for button
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(btn1);
		bottomPanel.add(btn2);
		bottomPanel.add(btn3);
		
		// instantiate the the buttons
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(b); // add checkerboard panel b to mainPanle
        mainPanel.add(bottomPanel,BorderLayout.SOUTH);  // add bottomPanel to mainPanel
        getContentPane().add(mainPanel);  // display all panels

		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setTitle("Mini-Checkers");
		this.setVisible(true);
		
		int c = moveChoice();	
		if(c == 1){ //human move first
			b.setPlayed(false);
		} 
		else{ // computer move first
			b.setPlayed(true);
		}
		
		JLabel msgdifficult = new JLabel("Please choose the level of difficulty (default easy).");
		JOptionPane.showMessageDialog(this, msgdifficult,"Welcome",JOptionPane.PLAIN_MESSAGE);
			
	}
	
	//At start of the game, the human player can choose to move first or second
	public int moveChoice(){
		JLabel msgLabel = new JLabel("Welcome to Mini-Checkers Game!");
		JOptionPane.showMessageDialog(this, msgLabel,"Welcome",JOptionPane.PLAIN_MESSAGE);
		JLabel message = new JLabel("Do you want to move first or second?");
		Object[] options = {"Move second", "Move first"};
		int choice = JOptionPane.showOptionDialog(this, message, "WELCOME", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

		return choice;
	}
}

/**
 * Class Checkerboard defines the different 5 kinds of pieces when players playing the game.
 * HUMAN pieces(1); 
 * COMPUTER pieces(-1); 
 * EMPTY(0) : means no any pieces in this square;
 * HTOEND(3) : indicates that the human piece reach to the opposite end and it has no any legal move to take
 * CTOEND(-3) : indicates that the computer piece reach to the opposite end and it has no any legal move to take  
 */

public class Checkerboard {
	
	final static public int HUMAN = 1;
	final static public int COMPUTER = -1;
	final static public int EMPTY = 0;
	final static public int HTOEND = 3; //Reach the end, human piece no move to take
	final static public int CTOEND = -3; // Reach the end, computer piece no move to take
	
	int[][] board = new int[6][6];
	
	Checkerboard(){
		super();
	}
	
	public Checkerboard(int[][] board){
		super();
		this.board = board;
	}
	
	public int[][] getBoard(){
		return this.board;
	}
	
	public void init(){ 
		int[][] tempboard = this.board;
		for(int row = 0; row < 6; row++){
			for(int col = 0; col < 6; col++){
				if(row % 2 != col % 2){
					if(row < 2){
						tempboard[row][col] = Checkerboard.COMPUTER; 
					} 
					else if(row > 3){
						tempboard[row][col] = Checkerboard.HUMAN;
					}
					else{
						tempboard[row][col] = Checkerboard.EMPTY;
					}
				}
				else{
					tempboard[row][col] = Checkerboard.EMPTY;
				}
			}
		}
		this.board = tempboard;
	}
}
