package MiniCheckers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Class SquareListener:
 * Implements the listener interface to receive the mouse event,
 * that is, the human player click a square to take a move 
 * 
 * @author Shuangshuang Shen
 *
 */
public class SquareListener implements MouseListener{
	private Square sq;
	private DrawBoard db;
	
	public SquareListener(Square square, DrawBoard drawboard){
		super();
		this.sq = square;
		this.db = drawboard;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		/**
		 * Execute the human move:
		 *  Before execute, check if the human player take a jump(jump must be taken).
		 *  if there exists jump, but human player wants to make a regular move, throw out the "Wrong Move" message
		 */
		if(sq.isSelected()){
			Checkers c = new Checkers();
			boolean jumpmove;	
			int[][] destb = new int[6][6];
			int[] startpos = db.getIndexofSquare(db.getActiveSquare());
			int[] destpos = db.getIndexofSquare(sq);
			
			if(Math.abs(startpos[0]-destpos[0]) != 2){//human want to take a regular move
				jumpmove = false;
				int[][] movablepieces = c.movablePieces(db.getBoard(), false);
				ArrayList<int[]> acts = new ArrayList<int[]>();
				
				for(int i = 0; i < 6; i++){
					if(movablepieces[i][0] == -1){
						break;
					}
					c.possibleActs(db.getBoard(), false, i, movablepieces[i][0], movablepieces[i][1], acts);
				}
				
				for(int i = acts.size()-1; i >= 0; i--){
					int[] temp = acts.get(i);
					if(temp[3] == 1){
						jumpmove = true;
						break;
					}
				}
				if(jumpmove){ // but there exist jump, and jump must be taken
					JLabel msgwrongmove = new JLabel();
					JOptionPane.showMessageDialog(msgwrongmove,"Wrong Move: Jump must be taken!", "Warning", JOptionPane.PLAIN_MESSAGE);
				}
				else{
					destb = c.Move(db.getBoard(), Checkerboard.HUMAN, startpos, destpos);
					db.toMove(destb,true);
				}
			}
			else{
				// take a move
				destb = c.Move(db.getBoard(), Checkerboard.HUMAN, startpos, destpos);
				db.toMove(destb,true);
			}

		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
