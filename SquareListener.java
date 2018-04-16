package MiniCheckers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
		if(sq.isSelected()){
			Checkers c = new Checkers();
			
			int[][] destb = new int[6][6];
			
			int[] startpos = db.getIndexofSquare(db.getActiveSquare());
			int[] destpos = db.getIndexofSquare(sq);
			
			// take a move
			destb = c.Move(db.getBoard(), Checkerboard.HUMAN, startpos, destpos);
			db.toMove(destb,true);
			
//			//Print to debug
//			System.out.println("This is mousePressed in SquareListener!");
//			
//			System.out.print("startpos:");
//			System.out.print("["+startpos[0]+","+startpos[1]+"]");
//			System.out.println();
//			System.out.print("destpos:");
//			System.out.print("["+destpos[0]+","+destpos[1]+"]");
//			System.out.println();
//			
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
