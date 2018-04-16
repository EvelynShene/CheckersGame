package MiniCheckers;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * This file contains three classes -- Square, Pieces and DrawBoard.
 * All these classes are extends JPanel class to help implement a GUI Checkers game.
 *  
 * @author Shuangshuang Shen
 *
 */


/**
 * Class Square: defined to show the 36 squares in the checker board.
 */
class Square extends JPanel{

	private static final long serialVersionUID = -2206825743729300418L;
	
	private boolean active; // indicate whether a square is a legal move
	private boolean selected; // indicate whether a square can be selected to move 
	
	public Square(boolean active){
		setLayout(new GridLayout(1,0));
		this.setActive(active);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isSelected() {
		return selected;
	}
	
	// Set the color(light gray) of the square which is a possible move
	public void setSelected(boolean selected) {
		this.selected = selected;
		Color c = new Color(211,211,211);
		
		if(selected){
			setBackground(new Color(211,211,211));
			setVisible(true);
		}
		else{
			if(getBackground().equals(c)){
				setVisible(false);
			}
		}
	}
	
	public void paintComponent(Graphics g){
		Paint paint;
		Graphics2D g2;
		
		if(g instanceof Graphics2D){
			g2 = (Graphics2D) g;
		}
		else{
			System.out.print("Error! Fail to paint square!");
			return;
		}
		paint = new GradientPaint(0, 0, getBackground(), getWidth(), getHeight(), getForeground());
		g2.setPaint(paint);
		g.fillRect(0, 0, getWidth(), getHeight());
		
	}
	
	
}

/**
 * Class Pieces: define to show the pieces on the checker board.
 *   White pieces for the computer and black pieces for the human;
 *   Once a piece reaching to the opposite end and has no legal move anymore, it becomes end piece
 */
class Pieces extends JPanel{

	private static final long serialVersionUID = -8065584826738938142L;
	
	private int pieceStatus;
	private String imgPath;
	
	public Pieces(int pieceStatus){
		this.pieceStatus = pieceStatus;
		
		switch(pieceStatus){
			case Checkerboard.HUMAN:
				imgPath = "/Users/evelynshen/Documents/workspace/ai/CheckersGame/src/blackpiece.png";
				break;
			case Checkerboard.COMPUTER:
				imgPath = "/Users/evelynshen/Documents/workspace/ai/CheckersGame/src/whitepiece.png";
				break;
			case Checkerboard.HTOEND:
				imgPath = "/Users/evelynshen/Documents/workspace/ai/CheckersGame/src/blacktoend.png";
				break;
			case Checkerboard.CTOEND:
				imgPath = "/Users/evelynshen/Documents/workspace/ai/CheckersGame/src/whitetoend.png";
				break;
			default:
				imgPath = "";
				break;							
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
		try{
			Image img = ImageIO.read(new File(imgPath));
			g.drawImage(img, 5, 5, this);
		} catch (IOException e){
			e.toString();
		}
	}
	
	public int getSquaretatus(){
		return pieceStatus;
	}
}

/**
 * Class DrawBoard: defines a container as checker board for all lightweight components Square and Pieces
 */

public class DrawBoard extends JPanel{

	private static final long serialVersionUID = -6156384728427151635L;
	
	private static final int SIZE = 6;
	private int[][] board = new int[6][6];
	/**
	 * variable played: indicates whether this turn is for human or computer;
	 *         		     played == false: it's human's turn, waiting for human to play
	 *        			 played == true: human finished his/her move; waiting for computer to play
	 */	
	private boolean played = false;
	private Square activeSquare;

	public Square getActiveSquare() {
		return activeSquare;
	}
	public void setActiveSquare(Square activeSquare) {
		this.activeSquare = activeSquare;
	}
	
	public void setPlayed(boolean played){
		this.played = played;
	}
	public boolean isPlayed(){
		return played;
	}	
	
	public void setBoard(int[][] b){
		this.board = b;
		deleteAllpieces();
		setBoardByMatrice(b);
	}
	
	public int[][] getBoard(){
		return board;
	}
	
	public void paintComponent(Graphics g){
		try{
			Image img = ImageIO.read(new File("/Users/evelynshen/Documents/workspace/ai/CheckersGame/src/boardbg.png"));
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		} catch (IOException e){
			e.toString();
		}
	}
	
	//initial the checker board 
	public DrawBoard(int[][] b){
		setLayout(new GridLayout(6,6));
		applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				if(j % 2 != i % 2){
					addSquare(true);
				}
				else{
					addSquare(false);
				}
			}
		}		
		board = b;
		setBoardByMatrice(b);
	}
	
	//add mouseListener for the squares
	public void addSquare(boolean active){
		Square s = new Square(active);
		s.addMouseListener(new SquareListener(s, this));
		add(s);
	}
	
	//add mouseListener for the pieces
	private Pieces createPiece(int pieceStatus){
		Pieces p = new Pieces(pieceStatus);
		p.addMouseListener(new PieceListener(p, this));
		return p;
	}
	
	public void deleteAllpieces(){
		for(int i = 0; i < 36; i++){
			getSquare(i).removeAll();
		}
	}
	
	public Square getSquare(int i) {
		// TODO Auto-generated method stub
		return (Square) getComponent(i);
	}
	
	public Square getSquare(int i, int j){
		return (Square) getComponent(i*6+j);
	}
	
	public void setBoardByMatrice(int[][] b) {
		// TODO Auto-generated method stub
		for(int row = 0; row < 6; row++){
			for(int col = 0; col < 6; col++){
				if(row % 2 != col % 2){ //pieces can only be placed on the square where row%2 != col%2 
					if(b[row][col] == Checkerboard.HUMAN){
						getSquare(row,col).add(createPiece(Checkerboard.HUMAN)); //Human - dark piece
					}
					else if(b[row][col] == Checkerboard.COMPUTER){
						getSquare(row,col).add(createPiece(Checkerboard.COMPUTER)); //Computer - white piece
					}
					else if(b[row][col] == Checkerboard.HTOEND){
						getSquare(row,col).add(createPiece(Checkerboard.HTOEND)); //Human piece reach to the opposite end
					}
					else if(b[row][col] == Checkerboard.CTOEND){
						getSquare(row,col).add(createPiece(Checkerboard.CTOEND)); //Computer piece reach to the opposite end
					}
					else{
						getSquare(row,col).add(createPiece(Checkerboard.EMPTY)); //Empty - no piece
					}			
				}
				else{
					getSquare(row,col).add(createPiece(Checkerboard.EMPTY)); //Empty - no piece
				}
			}
		}
		
		//refresh the checkerboard
		for(int row = 0; row < 6; row++){
			for(int col = 0; col < 6; col++){
				getSquare(row,col).setVisible(false);
				getSquare(row,col).setVisible(true);
			}
		}
	}
	
	
	/**
	 *  MovableHumanPiecePosition(): 
	 *  this function will return the position of the selected human piece
	 *  @param p: the selected human piece  
	 */
	
	public int[] MovableHumanPiecePosition(Pieces p){
		int[] respos = {6,6};
		if(p.getSquaretatus() == 1){ //Human
			for(int i = 0; i < 36; i++){
				getSquare(i).setSelected(false);
				
				if(getSquare(i).getComponentCount() != 0 && getSquare(i).getComponent(0).equals(p)){
					activeSquare = getSquare(i);
					respos[0] = i / 6;
					respos[1] = i % 6;
				}
				if(getSquare(i).getComponentCount() != 0){
					getSquare(i).setVisible(true);
				}
			}
		}		
		return respos;
	}
	
	/**
	 *  displayPossibleMoveSquare(): 
	 *  this function will return the number of possible movable squares of the selected human piece
	 *  @param s: the position of the selected human piece
	 *  @return : the number of possible movable squares
	 */
	public int displayPossibleMoveSquare(int[] s){
		int movesnum = 0;
		int row = s[0];
		int col = s[1];
		boolean jump = false;
		
		if(row - 2 >= 0){ //Check Jump move 
			if((col - 2 >= 0) && (board[row-1][col-1] == Checkerboard.COMPUTER) && (board[row-2][col-2] == Checkerboard.EMPTY)){
				getSquare(row-2,col-2).setSelected(true);
				getSquare(row-2,col-2).removeAll();
				jump = true;
				movesnum++;
			}
			if((col + 2 <= 5) && (board[row-1][col+1] == Checkerboard.COMPUTER) && (board[row-2][col+2] == Checkerboard.EMPTY)){
				getSquare(row-2,col+2).setSelected(true);
				getSquare(row-2,col+2).removeAll();
				jump = true;
				movesnum++;
			}
		}
		if(jump == false){ // Check regular move
			if((col - 1 >= 0) && (board[row-1][col-1] == Checkerboard.EMPTY)){
				getSquare(row-1,col-1).setSelected(true);
				getSquare(row-1,col-1).removeAll();
				movesnum++;
			}
			if((col + 1 <= 5) && board[row-1][col+1] == Checkerboard.EMPTY){
				getSquare(row-1,col+1).setSelected(true);
				getSquare(row-1,col+1).removeAll();
				movesnum++;
			}
		}

		return movesnum;
		
	}

	public int[] getIndexofSquare(Square s){
		int[] ind = new int[2];
		
		for(int i = 0; i < 36; i++){
			if(getSquare(i).equals(s)){
				ind[0] = i / 6;
				ind[1] = i % 6;
			}
		}
		return ind;
	}
	
	/**
	 * toMove(): this function is used to make the move for the player and then show the result checker board in GUI
	 * @param newBoard : the result checker board after taking a move
	 * @param played : true means human player has already taken his/her move;
	 * 				   false means computer player has taken its move, waiting for human player to take a move 
	 */
	public void toMove(int[][] newBoard, boolean played){	
		
		Checkers c = new Checkers();
		//check if have any legal move
		int[][] movablepieces = c.movablePieces(newBoard, false);
		ArrayList<int[]> acts = new ArrayList<int[]>();
		for(int i = 0; i < 6; i++){
			if(movablepieces[i][0] == -1){
				break;
			}
			c.possibleActs(newBoard, false, i, movablepieces[i][0], movablepieces[i][1], acts);
		}
		
		if(acts.size() != 0){
			setPlayed(played);
		}
		else{//no legal move
			for(int i = 0; i < 6; i++){
				if(movablepieces[i][0] == -1){
					break;
				}

				newBoard[movablepieces[i][0]][movablepieces[i][1]] = Checkerboard.HTOEND;
			}	
			System.out.println("No legal move in my turn!");
			setPlayed(true);
		}
		
		if(!Checkers.equalarray(newBoard, this.board)){
			setBoard(newBoard);	
		}
		
	}
	
	
}

