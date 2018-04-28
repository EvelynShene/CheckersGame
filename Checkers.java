package MiniCheckers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * 
 *  This is a Mini-Checkers game for a human to play against a computer. The board size is 6x6 squares 
 *  and there are only two types of moves: regular moves and capture moves(jump); only forward moves 
 *  are allowed and every opportunity to jump must be taken. 
 *  
 *  This Mini-Checkers game is implemented using the Alpha-Beta search algorithm.   
 *  This file defines the main class Checkers. 
 *  
 *  @author Shuangshuang Shen
 *
 */

public class Checkers {
	/**
	 * Define utility values of terminal state(node) is: 
	 * 		computer win = 1000
	 * 		human win = -1000
	 * 		draw = 0
	 */  
	final static public int DRAW = 0;
	final static public int COMPUTERWIN = 1000;
	final static public int HUMANWIN = -1000;
	
	/**
	 * Define the constant below:
	 * NONTERMINAL : indicates this node is not a terminal node
	 * INF/NINF : use 10000 and -10000 to indicates the positive infinite and negative infinite 
	 * CHANGETURN : indicates the player(human or computer) has no any legal move in current turn
	 * CUTOFF : set cutoff level to be 23 to make sure the Alpha-Beta Search Algorithm can be finished in 15 seconds
	 */
	final static public int NONTERMINAL = 4;
	final static public int INF = 10000;
	final static public int NINF = -10000;
	final static public int CHANGETURN = -50;
	final static public int CUTOFF = 23;
	
	//Array bestnextmove[][] stores the next best move : (bestnextmove[0][0],bestnextmove[0][1]) -> (bestnextmove[1][0],bestnextmove[1][1])	 
	int[][] bestnextmove = {{-1,-1},{-1,-1}};
	
	// difficultylevel indicates the level of difficulty that human player choose, default is 1(easy).
	int difficultylevel = 1;
	
	static int maxdepth = 0;
	static int nodes = 0;
	static int maxprun = 0;
	static int minprun = 0;
	
	/**
	 * Function printCheckerboard(): print the current checkerboard
	 * @param board: the current board
	 * The definition of the printed letter:
	 *     H means the white pieces for the computer
	 *     C means the black pieces for the human
	 *     E means the piece that has no legal move to take  
	 */
	public static void printCheckerboard(int[][] board){
		System.out.print("Checkers Position:");		
		for(int row = 0; row < 6; row++){
			System.out.println();
			for(int col = 0; col < 6; col++){
				if(board[row][col] == Checkerboard.HUMAN){
					System.out.print("H");
				}
				else if(board[row][col] == Checkerboard.COMPUTER){
					System.out.print("C");
				}
				else if(board[row][col] == Checkerboard.HTOEND){
					System.out.print("E");
				}
				else if(board[row][col] == Checkerboard.CTOEND){
					System.out.print("L");
				}
				else{
					System.out.print("-");
				}
				
			}
		}
		System.out.println();
	}
	
	/**
	 * Function Move():  the transition model
	 * @param startb : the current state(checkerboard)
	 * @param player : two possible values: Checkerboard.HUMAN and Checkerboard.COMPUTER 
	 * @param startpos, destpos : indicates the action: the piece in (startpos[0],startpos[1]) move to (destpos[0],destpos[1])  
	 * @return the next state(checkerboard) given the current state and action to be applied
	 */	
	public int[][] Move(int[][] startb, int player, int[] startpos, int[] destpos) {

		int[][] m = new int[6][6];
		copyarray(startb,m);
		
		if(destpos[0] == 0){ // Human piece reach to the end
			m[destpos[0]][destpos[1]] = Checkerboard.HTOEND;
		}
		else if(destpos[0] == 5){ // Computer piece reach to the end
			m[destpos[0]][destpos[1]] = Checkerboard.CTOEND;
		}
		else{
			m[destpos[0]][destpos[1]] = m[startpos[0]][startpos[1]];
		}
		
		if(Math.abs(startpos[0] - destpos[0]) == 2){ //check if there is a capture move(jump)
			if(player == Checkerboard.HUMAN){ //Human jump
				if(startpos[1] > destpos[1]){
					m[startpos[0]-1][startpos[1]-1] = Checkerboard.EMPTY;
				}
				else{
					m[startpos[0]-1][startpos[1]+1] = Checkerboard.EMPTY;
				}
			}
			else{// Computer jump
				if(startpos[1] > destpos[1]){
					m[startpos[0]+1][startpos[1]-1] = Checkerboard.EMPTY;
				}
				else{
					m[startpos[0]+1][startpos[1]+1] = Checkerboard.EMPTY;
				}
			}
		}		
		m[startpos[0]][startpos[1]] = Checkerboard.EMPTY;		
			
		return m;
	}

	/**
	 * Function alphaBetaSearch(): Alpha-Beta Search Algorithm to find the best next move for the computer
	 * @param b: current board (state)
	 * @return : return the new board after the computer executing the best next move
	 * initially:
	 * 		beta = maximum_utility = 12 and alpha = minimum_utility = -12
	 * 		the root node be level 0
	 */
	public int[][] alphaBetaSearch(int[][] b) {
		int level = 0;
		
		int v = maxValue(b, Checkers.HUMANWIN, Checkers.COMPUTERWIN, level);
		
		System.out.println("bestnextmove:");
		System.out.println("["+bestnextmove[0][0]+","+bestnextmove[0][1]+"] -> ["+bestnextmove[1][0]+","+bestnextmove[1][1]+"]");
		
		if(v != Checkers.CHANGETURN){
			int[][] nextstate = Move(b, Checkerboard.COMPUTER, bestnextmove[0], bestnextmove[1]);
			return nextstate;
		}
		else{
			return b;
		}
	}

	public int maxValue(int[][] b, int alpha, int beta, int level) {//computer
		nodes++;
		boolean myturn = true;
		
		int utility = terminalTest(b);
		if(utility != Checkers.NONTERMINAL){
			maxdepth = Max(maxdepth,level);
			return utility;
		}
		if(level == Checkers.CUTOFF){ // Cut off
			return evaluation(b,true);
		}		

		int v = Checkers.NINF;
		int tempv = v;
		int[][] newb = new int[6][6];
		int[][] movablepieces = movablePieces(b, true);
		boolean jumpmove = false;
		ArrayList<int[]> acts = new ArrayList<int[]>();			
		
		// get all the computer's actions in the current state
		for(int i = 0; i < 6; i++){
			if(movablepieces[i][0] == -1){
				break;	
			}
			possibleActs(b, true, i, movablepieces[i][0], movablepieces[i][1], acts);
		}
				
		// Checker if the computer has no any legal move to take
		if(acts.size() == 0){
			for(int i = 0; i < 6; i++){
				if(movablepieces[i][0] == -1){
					break;
				}
				b[movablepieces[i][0]][movablepieces[i][1]] = Checkerboard.CTOEND;
			}
			myturn = false;
		}
		// current board for this player has no any legal move, need to change turn.
		if(!myturn){
			if(level == 0){ 
				return Checkers.CHANGETURN;
			}
			v = minValue(b, alpha, beta, level+1);
			return v;

		}
			
		//Check if have jump actions, because jump move must be taken	
		for(int i = acts.size()-1; i >= 0; i--){
			int[] temp = acts.get(i);
			if(temp[3] == 1){
				jumpmove = true;
				break;
			}
		}
		if(jumpmove){ // jump must be taken, so remove all the regular move in acts
			for(int i = acts.size()-1; i >= 0; i--){
				if(acts.get(i)[3] == 0){
					acts.remove(i);
				}
			}
		}
			
		int[] destpos = new int[2];
		int a = 0;
		for(a = 0; a < acts.size(); a++){
			destpos[0] = acts.get(a)[1];
			destpos[1] = acts.get(a)[2];
			newb = Move(b, Checkerboard.COMPUTER, movablepieces[acts.get(a)[0]], destpos);
			tempv = minValue(newb, alpha, beta, level+1);
			if(level == 0){
				if(tempv > v){
					bestnextmove[0][0] = movablepieces[acts.get(a)[0]][0];
					bestnextmove[0][1] = movablepieces[acts.get(a)[0]][1];
					bestnextmove[1][0] = destpos[0];
					bestnextmove[1][1] = destpos[1];
				}
			}
			v = Max(v, tempv);
			if(v >= beta){
				maxprun++;
				return v;
			}
			alpha = Max(alpha, v);
		}
		
		if(level == 0){
			System.out.println();
		}		
		return v;	
	}


	public int minValue(int[][] b, int alpha, int beta, int level) {//Human
		nodes++;
		boolean myturn = true;
		
		int utility = terminalTest(b);
		if(utility != Checkers.NONTERMINAL){
			maxdepth = Max(maxdepth,level);
			return utility;
		}
		if(level == Checkers.CUTOFF){ // Cut off
			return evaluation(b,false);		
		}

		int v = Checkers.INF;
		int tempv = v;
		int[][] newb = new int[6][6];
		int[][] movablepieces = movablePieces(b, false);
		boolean jumpmove = false;
		ArrayList<int[]> acts = new ArrayList<int[]>();		
			
		// get all the human's actions in the current state
		for(int i = 0; i < 6; i++){
			if(movablepieces[i][0] == -1){
				break;
			}
			possibleActs(b, false, i, movablepieces[i][0], movablepieces[i][1], acts);
		}
					
		// Checker if the human has no legal move to take
		if(acts.size() == 0){
			for(int i = 0; i < 6; i++){
				if(movablepieces[i][0] == -1){
					break;
				}
				b[movablepieces[i][0]][movablepieces[i][1]] = Checkerboard.HTOEND;
			}	
			myturn = false;
		}
		// no any legal move, change the turn
		if(!myturn){
			v = maxValue(b, alpha, beta, level+1);
			return v;
		}
			
		//Check if have jump actions, because jump move must be taken	
		for(int i = acts.size()-1; i >= 0; i--){
			int[] temp = acts.get(i);
			if(temp[3] == 1){
				jumpmove = true;
				break;
			}
		}
		if(jumpmove){ // jump must be taken, so remove all the regular move in acts
			for(int i = acts.size()-1; i >= 0; i--){
				if(acts.get(i)[3] == 0){
					acts.remove(i);
				}
			}
		}
				
		int[] destpos = new int[2];
		for(int a = 0; a < acts.size(); a++){
			destpos[0] = acts.get(a)[1];
			destpos[1] = acts.get(a)[2];
			newb = Move(b, Checkerboard.HUMAN, movablepieces[acts.get(a)[0]], destpos);
			tempv = maxValue(newb, alpha, beta, level+1);
				
			v = Min(v, tempv);
			if(v <= alpha){
				minprun++;
				return v;
			}
			beta = Min(beta, v);
		}
		return v;
	
	}

	/**
	 * Function movablePieces(): find the pieces which have legal moves in current board m
	 * @param m : current board
	 * @param computer: true means to find movable computer pieces; false means to find movable human pieces
	 * @return : return the location of these movable pieces
	 */
	public int[][] movablePieces(int[][] m, boolean computer){
		int[][] movable = {{-1,-1},{-1,-1},{-1,-1},{-1,-1},{-1,-1},{-1,-1}}; // each player has at most 6 movable pieces
		int n = 0;
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				if(computer){
					if(m[i][j] == Checkerboard.COMPUTER){
						movable[n][0] = i;
						movable[n][1] = j;
						n++;
					}
				}
				else{
					if(m[i][j] == Checkerboard.HUMAN){
						movable[n][0] = i;
						movable[n][1] = j;
						n++;
					}
				}
			}
		}
		return movable;
	}
	
	/**
	 * Function possibleActs(): find all possible human or computer actions in current board m 
	 * @param m: the current board
	 * @param computer: true means to find possible computer actions; otherwise, find all possible human actions
	 * @param n: n indicates that possible actions are the piece movablepieces[n]'s actions
	 * @param row, col : the position of the piece movablepieces[n]
	 * @param acts : to store all the possible human or computer actions; it has four column:
	 *               column 0: the index of the pieces in array moavlepieces[] which it belongs to
	 *               column 1,2: the destination position of a action
	 *               column 3: this column indicates the action is regular move(0) or jump(1).
	 */
	public void possibleActs(int[][] m, boolean computer, int n, int row, int col, ArrayList<int[]> acts) {
		//initial actions
		boolean jump = false;
		int[][] actions = {{n,-1,-1,-1},{n,-1,-1,-1}};
		int index = 0;
		
		if(computer){
			if(row + 2 < 6){
				if((col - 2 >= 0) && ((m[row+1][col-1] == Checkerboard.HUMAN) || m[row+1][col-1] == Checkerboard.HTOEND) && (m[row+2][col-2] == Checkerboard.EMPTY)){
					actions[index][1] = row + 2;
					actions[index][2] = col - 2;
					actions[index][3] = 1;
					acts.add(actions[index]);
					index++;
					jump = true;
				}
				if((col + 2 <= 5) && ((m[row+1][col+1] == Checkerboard.HUMAN) || m[row+1][col+1] == Checkerboard.HTOEND) && (m[row+2][col+2] == Checkerboard.EMPTY)){
					actions[index][1] = row + 2;
					actions[index][2] = col + 2;
					actions[index][3] = 1;
					acts.add(actions[index]);
					index++;
					jump = true;
				}
			}
			if(jump == false){ //Regular move
				if(row + 1 < 6){
					if((col - 1 >= 0) && (m[row+1][col-1] == Checkerboard.EMPTY)){
						actions[index][1] = row + 1;
						actions[index][2] = col - 1;
						actions[index][3] = 0;
						acts.add(actions[index]);
						index++;
					}
					if((col + 1 <= 5) && m[row+1][col+1] == Checkerboard.EMPTY){
						actions[index][1] = row + 1;
						actions[index][2] = col + 1;
						actions[index][3] = 0;
						acts.add(actions[index]);
						index++;
					}
				}
			}
		}
		else{ //Human player
			if(row - 2 >= 0){ //Check Jump move 
				if((col - 2 >= 0) && ((m[row-1][col-1] == Checkerboard.COMPUTER) || (m[row-1][col-1] == Checkerboard.CTOEND)) && (m[row-2][col-2] == Checkerboard.EMPTY)){
					actions[index][1] = row - 2;
					actions[index][2] = col - 2;
					actions[index][3] = 1;
					acts.add(actions[index]);
					index++;
					jump = true;
				}
				if((col + 2 <= 5) && ((m[row-1][col+1] == Checkerboard.COMPUTER) || (m[row-1][col+1] == Checkerboard.CTOEND)) && (m[row-2][col+2] == Checkerboard.EMPTY)){
					actions[index][1] = row - 2;
					actions[index][2] = col + 2;
					actions[index][3] = 1;
					acts.add(actions[index]);
					index++;
					jump = true;
				}
			}
			if(jump == false){ // Check regular move
				if(row - 1 >= 0){
					if((col - 1 >= 0) && (m[row-1][col-1] == Checkerboard.EMPTY)){
						actions[index][1] = row - 1;
						actions[index][2] = col - 1;
						actions[index][3] = 0;
						acts.add(actions[index]);
						index++;
					}
					if((col + 1 <= 5) && m[row-1][col+1] == Checkerboard.EMPTY){
						actions[index][1] = row - 1;
						actions[index][2] = col + 1;
						actions[index][3] = 0;
						acts.add(actions[index]);
						index++;
					}
				}
			}
		}	
	}
		
	/**
	 * Function terminalTest(): to check if the current board is a terminal node
	 * @param b: current board
	 * @return : return NONTERMINAL if the current board not a terminal node;
	 *           otherwise, return the utility of this board
	 */
	public int terminalTest(int[][] b) {
		int whitenum = 0;
		int blacknum = 0;
		int whitemovable = 0;
		int blackmovable = 0;
		
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				switch(b[i][j]){
					case Checkerboard.COMPUTER:
						whitenum++;
						whitemovable++;
						break;
					case Checkerboard.HUMAN:
						blacknum++;
						blackmovable++;
						break;
					case Checkerboard.HTOEND:
						blacknum++;
						break;
					case Checkerboard.CTOEND:
						whitenum++;
						break;
					default:
						break;
				}
			}
		}
		
		if(whitenum == 0){ // Human win
			return Checkers.HUMANWIN;
		}
		else if(blacknum == 0){ //Computer win
			return Checkers.COMPUTERWIN;
		}
		else if((whitemovable == 0) && (blackmovable == 0)){ //both players do not have any legal move to take
			if(whitenum > blacknum){ //Computer win
				return Checkers.COMPUTERWIN; 
			}
			else if(whitenum < blacknum){ //Human win
				return Checkers.HUMANWIN;
			}
			else{
				return Checkers.DRAW;
			}
		}
		
		return Checkers.NONTERMINAL;
	}
	
	/**
	 * Function evaluation(): To evaluate the utility of the current state when cutoff happened
	 * @param b: the current state/board
	 * @param maxvalue: indicates whether cutoff happened in MAX level or Min level. 
	 *                   if maxvalue = true, it's MAX level; otherwise, MIN level.
	 * @return return the evaluated utility
	 */
	public int evaluation(int[][] b, boolean maxvalue) {
		int whitenum = 0;
		int blacknum = 0;
		int whitemovable = 0;
		int blackmovable = 0;
		int wfarthestpos = 5;
		int bfarthestpos = 5;
		
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				switch(b[i][j]){
					case Checkerboard.COMPUTER:
						whitenum++;
						whitemovable++;
						wfarthestpos = Max(wfarthestpos,5-i);
						break;
					case Checkerboard.HUMAN:
						blacknum++;
						blackmovable++;
						bfarthestpos = Max(bfarthestpos,i);
						break;
					case Checkerboard.HTOEND:
						blacknum++;
						bfarthestpos = Max(bfarthestpos,i);
						break;
					case Checkerboard.CTOEND:
						whitenum++;
						wfarthestpos = Max(wfarthestpos,5-i);
						break;
					default:
						break;
				}
			}
		}
		
		if(this.difficultylevel == 1){ //evaluation function for easy level
			return bfarthestpos - wfarthestpos;	
		}
		else if(this.difficultylevel == 2){ //evaluation function for medium level
			return whitenum - blacknum;
		}
		else{ // evaluation function for hard level
			return (whitenum + whitemovable - blacknum - blackmovable);
		}
	}
	
	/**
	 * Function checkBoardStatus(): to activate the pieces if they have legal moves again
	 * @param b: the current board
	 * @return return true if the reactive happens
	 */
	public boolean checkBoardStatus(DrawBoard b){
		int[][] board = b.getBoard();
		boolean reactive = false;
		ArrayList<int[]> temp = new ArrayList<int[]>();
		
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				temp.clear();
				if(board[i][j] == Checkerboard.HTOEND){
					possibleActs(board,false,0,i,j,temp);
					if(temp.size() != 0){
						board[i][j] = Checkerboard.HUMAN; //activate the piece
						reactive = true;
					}
				}
				else if(board[i][j] == Checkerboard.CTOEND){
					possibleActs(board,true,0,i,j,temp);
					if(temp.size() != 0){
						board[i][j] = Checkerboard.COMPUTER; //activate the piece
						reactive = true;
					}
				}
			}
		}
		
		if(reactive){
			b.setBoard(board); // refresh the checkerboard
		}
		
		return reactive;
	}

	public int Max(int a, int b){
		if(a >= b){
			return a;
		}
		else{
			return b;
		}
	}
	
	public int Min(int a, int b){
		if(a <= b){
			return a;
		}
		else{
			return b;
		}
	}
	
	public static void copyarray(int[][] src, int[][] dest){
		for(int i = 0; i < src.length; i++){
			System.arraycopy(src[i], 0, dest[i], 0, src[i].length);
		}
	}
	
	public static boolean equalarray(int[][] a, int[][] b){
		
		if(a == null || b== null || a.length != b.length){
			return false;
		}
		
		for(int l = 0; l < a.length; l++){
			if(!Arrays.equals(a[l], b[l])){
				return false;
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) throws InterruptedException{
		
		Checkerboard checkerboard = new Checkerboard();
		checkerboard.init(); 

		DrawBoard b = new DrawBoard(checkerboard.board);
		Frame f = new Frame(b);
		
		Checkers c = new Checkers();
		int gameresult = 0;
		int treenum = 0;
		
		/**
		 *  addActionListener() on each button to listen which level of difficulty the human player is chosen
		 */
		f.btn1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				c.difficultylevel = 1;
				f.btn2.setEnabled(false);
				f.btn3.setEnabled(false);
			}
			
		});
		
		f.btn2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				c.difficultylevel = 2;
				f.btn1.setEnabled(false);
				f.btn3.setEnabled(false);
			}
			
		});
		
		f.btn3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				c.difficultylevel = 3;
				f.btn1.setEnabled(false);
				f.btn2.setEnabled(false);
			}
			
		});
		
		boolean reactive = false;
		//start game
		while(true){
			gameresult = c.terminalTest(b.getBoard());
			if(gameresult != Checkers.NONTERMINAL){
				break;
			}
			
			boolean computerplayer = b.isPlayed();
			if(computerplayer){ //Computer move
				reactive = false;
				treenum++;
				maxdepth = 0;
				nodes = 0;
				maxprun = 0;
				minprun = 0;
				
				//Implement Alpha-Beta Algorithm to find the best next move
				int [][] nextstate = c.alphaBetaSearch(b.getBoard());
				
				//output the statistics for the game tree
				System.out.println("The statistics for the game tree("+ treenum +"):");
				System.out.println("Maximum depth of tree: "+ maxdepth);
				System.out.println("Total number of nodes generated: " + nodes);
				System.out.println("The number of times pruning occurred in the MAX_VALUE function: " + maxprun);
				System.out.println("The number of times pruning occurred in the MIN_VALUE function: " + minprun);
				
				//Take the best move
				b.toMove(nextstate, false);
				
				// check if the computer move activates some human pieces 
				reactive = c.checkBoardStatus(b);
				if(reactive){
					b.setPlayed(false);
				}
				
				//Check if game over
				gameresult = c.terminalTest(b.getBoard());
				if(gameresult != Checkers.NONTERMINAL){
					break;
				}
			}
			else{ //human move
				b.setPlayed(false);
			}					
			Thread.sleep(2000);
		}
		c.GameEnd(gameresult,f);
		
	}
	
	/**
	 * Function GameEnd(): To show the game result
	 */
	public void GameEnd(int gameresult, Frame f) {
		JLabel HumanWinMsg = new JLabel("Congradulations! You won the game!");
		JLabel ComputerWinMsg = new JLabel("Oh oh, you lost!");
		JLabel DrawMsg = new JLabel("Game over, draw!");
		
		if(gameresult == Checkers.HUMANWIN){
			JOptionPane.showMessageDialog(f, HumanWinMsg, "Game End", JOptionPane.PLAIN_MESSAGE);
		}
		else if(gameresult == Checkers.COMPUTERWIN){
			JOptionPane.showMessageDialog(f, ComputerWinMsg, "Game End", JOptionPane.PLAIN_MESSAGE);
		}
		else{
			JOptionPane.showMessageDialog(f, DrawMsg, "Game End", JOptionPane.PLAIN_MESSAGE);
		}
	}
}
