package MiniCheckers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Class PieceListener:
 * Implements the listener interface to receive the mouse event,
 * that is, the human player click a piece to see its possible move or take a move 
 * If the selected piece has legal move, show possible moves in checker board
 * 
 * @author Shuangshuang Shen
 *
 */
public class PieceListener implements MouseListener{
	private DrawBoard db;
	private Pieces p;
	
	public PieceListener(Pieces p, DrawBoard b){
		super();
		this.db = b;
		this.p = p;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		int[] position = new int[2];
		position = db.MovableHumanPiecePosition(p);
		
		// show possible moves of the selected piece in the board
		if(position[0] < 6){
			try{
				if(db.isPlayed() == false){
					db.displayPossibleMoveSquare(position);
			}
			}catch(Exception e1){
				e1.toString();
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
