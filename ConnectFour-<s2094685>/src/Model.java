import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This file is to be completed by you.
 *
 * @author <Please enter your matriculation number, not your name>
 */
public final class Model
{
	// ===========================================================================
	// ================================ CONSTANTS ================================
	// ===========================================================================
	// The most common version of Connect Four has 7 rows and 6 columns.
	public static final int DEFAULT_NR_ROWS = 6;
	public static final int DEFAULT_NR_COLS = 7;
	public static final int DEFAULT_CONNECT_N = 4;

	public static final int MIN_RANGE = Integer.MIN_VALUE;
	public static final int MAX_RANGE = Integer.MAX_VALUE;

	// ========================================================================
	// ================================ FIELDS ================================
	// ========================================================================
	// The size of the board.
	private int nrRows;
	private int nrCols;
	private boolean player1;
	private boolean boardFull;
	private int[][] pieceLocation;
	private int connectN;
	private boolean playAgainstAI;
	private static int[] lookupTable;

	private boolean running;




	// =============================================================================
	// ================================ CONSTRUCTOR ================================
	// =============================================================================



	public Model()
	{
		// Initialise the board size to its default values.
		//Check size and values

		this.nrRows = DEFAULT_NR_ROWS;
		this.nrCols = DEFAULT_NR_COLS;
		this.connectN = DEFAULT_CONNECT_N;
		//Make enhanced Input Validation 
		this.player1 = true;
		this.running = true;

	}


	public void initFromSaveFile(boolean playerOne, int nrCols2, int nrRows2, int[][] arraySave, int connectN,boolean playAgainstAI, boolean boardFull) {
		this.setBoardFull(boardFull);
		this.setPlayAgainstAI(playAgainstAI);
		this.setPlayer1(playerOne);
		this.setNrCols(nrCols2);
		this.setNrRows(nrRows2);
		this.setPieceLocation(arraySave);
		this.setConnectN(connectN);
		setLookUptable(this.isPlayAgainstAI(), this.getConnectN());

	}	

	private static  void setLookUptable(boolean playAgainstAI, int connectN) {
		if(playAgainstAI) {
			int[] lookUp = new int [connectN-1];
			for(int i = 0 ; i<lookUp.length;i++) {
				lookUp[i] =(int) Math.pow(10, (double) i );
			}
			lookupTable = lookUp;
		}

	}


	// ====================================================================================
	// ================================ MODEL INTERACTIONS ================================
	// ====================================================================================
	public boolean isMoveValid(int[][] board,int move)
	{
		if( move <= this.getNrCols() && move >= 1) {
			if(board[0][move-1] == 0) {
				return true;
			}
		} 
		return false; 
	}
	/*
	 * NOTE: change in player is handled here too. This is one for two player games.
	 */



	public void makeMove(int move)
	{		
		if(this.isMoveValid( this.getPieceLocation(),move )) {
			int i = this.findRowToPlacePiece(move-1, this.getPieceLocation());
			if(this.isPlayer1()) {
				pieceLocation[i-1][move-1] = 1;
				if (this.checkWin(this.getPieceLocation())) {  this.quitGame(); return; };
				this.setPlayer1(false);
			}else {
				pieceLocation[i-1][move-1] = -1;
				if (this.checkWin(this.getPieceLocation())) {  this.quitGame(); return; };
				this.setPlayer1(true);
			}

		}
	}

	public int findRowToPlacePiece(int move, int[][] board) {
		int i = 0;
		do {
			if(board[i] [move] == 0) {
				i++;
			}else {
				break;
			}

		}while(i< nrRows);
		return i;
	}
	public void aiMakesMove() {

		int copy[][] = new int [this.getNrRows()] [this.getNrCols()];
		for(int k = 0; k< this.getNrRows(); k++) {
			copy[k] = this.getPieceLocation()[k].clone();
		}
		int copy2[][] = new int [this.getNrRows()] [this.getNrCols()];
		for(int k = 0; k< this.getNrRows(); k++) {
			copy2[k] = this.getPieceLocation()[k].clone();
		}
		int eval[]= minimax(new int[] {1,0},copy,0,true);

		int i = this.findRowToPlacePiece(eval[0]-1, this.getPieceLocation());
		pieceLocation[i-1][eval[0]-1] = -1;
		if (this.checkWin(this.getPieceLocation())) {  this.quitGame(); return; };
		this.setPlayer1(true);
	}

	private  int[][] generateChild(int[][] newBoard,int move, boolean isAI) {
		int i = this.findRowToPlacePiece(move-1, newBoard);
		if(isAI) {
			newBoard[i-1][move-1] = -1;
		}else {
			newBoard[i-1][move-1] = 1;
		}

		return newBoard;
	}
	private boolean columnIsEmpty(int[][]board, int move) {
		if(board[this.getNrRows()-1][move-1] == 0) {
			return true;
		}else {
			return false;
		}
	}



	private int[] minimax(int moveSet[], int[][] board, int depth, boolean maximizingPlayer) {

		//MOVESET[0] = column;
		//MOVESET[1] = score;
		if(this.columnIsEmpty(board, this.getNrCols()/2+1)) {
			moveSet[0] = this.getNrCols()/2+1;
			moveSet[1] = (int)Math.pow(10.0,this.getConnectN());
			return moveSet;
		}

		boolean checkWin = this.checkWin(board);
		boolean boardFull = this.checkBoardFull();
		if(boardFull && !checkWin) {
			if(maximizingPlayer){
				moveSet[1] = (int)-(Math.pow(10.0,this.getConnectN()))/depth;
				return moveSet;
			}else {
				moveSet[1] = (int)(Math.pow(10.0,this.getConnectN()))/depth;
				return moveSet;

			}
		}
		if(checkWin) {
			if(maximizingPlayer){
				moveSet[1] = (int)(Math.pow(10.0,this.getConnectN()+1));
				return moveSet;
			}else {
				moveSet[1] = (int)-(Math.pow(10.0,this.getConnectN()+1));
				return moveSet;

			}
		}
			if(depth==6) {
				return staticEval(moveSet,board);
			}

			if(maximizingPlayer) {
				moveSet[1] = MIN_RANGE;

				for(int i=1;i<=this.getNrCols();i++) {
					if(this.isMoveValid(board,i)) {
						int copy[][] = new int [this.getNrRows()] [this.getNrCols()];
						for(int k = 0; k< board.length; k++) {
							copy[k] = board[k].clone();
						}
						int eval [] = new int[2];
						int [] moveSetCopy = new int [2];
						moveSetCopy = moveSet.clone();
						eval = minimax(moveSetCopy,this.generateChild(copy,i,maximizingPlayer), depth+1, false);
						if(moveSet[1]<eval[1]) {
							moveSet[1] = eval[1];
							moveSet[0] = i;
						}
					}

				}
				return moveSet;
			}else { //minimiizing player i.e. the player. Takes advantage of the losses of the player.
				moveSet[1] = MAX_RANGE;

				for(int i=1;i<=this.getNrCols();i++) {
					if(this.isMoveValid(board,i)) {
						int copy[][] = new int [this.getNrRows()] [this.getNrCols()];
						for(int k = 0; k< board.length; k++) {
							copy[k] = board[k].clone();
						}
						int eval [] = new int[2];
						int[] moveSetCopy = new int [2] ;
						moveSetCopy = moveSet.clone();
						eval= minimax(moveSetCopy,this.generateChild(copy,i,maximizingPlayer), depth+1, true);
						if(moveSet[1]>eval[1]) {
							moveSet[1] = eval[1];
							moveSet[0] = i;
						}
					}

				}
				return moveSet;
			}
		

	}
	private int[]staticEval(int eval[],int[][] board) {

		//we use an array as a look-up table, a hash-table would have been preferable.



		eval[1] = this.positionSum(board);

		return eval;

	}

	public int positionSum(int [][] board) {
		int sum = 0;
		for (int i = 2; i<=this.getConnectN(); i++ ) {
			sum +=  lookupTable[i-2]*(countDiagonals((board), i, -1) + countVertical(board,i,-1)+ countDiagonals(this.reverseArray(board), i, -1) + countHorizontal(board, i, -1));
			sum +=  -lookupTable[i-2]*(countDiagonals((board), i, 1) + countVertical(board,i, 1)+ countDiagonals(this.reverseArray(board), i, 1) + countHorizontal(board,i,1));

		}
		return sum;
	}
	public int countDiagonals(int [][] pieceLocation, int countThreshold, int c) {
		int sum = 0;
		int startSum = connectN - 1;
		int maxBound = nrRows+nrCols-connectN-1 ;
		for (int k = startSum; k <= maxBound; ++k) {
			int count = 0;
			int temp = MIN_RANGE;
			int sumAlongDiag = 1;
			int yMin = Math.max(0, k - nrCols+ 1);
			int yMax = Math.min(nrRows - 1, k);
			for (int i = yMin; i <= yMax; i++) {
				int j = k - i;
				int currentPlayer = pieceLocation[i][j];

				if(currentPlayer!=0 && currentPlayer == c) {

					if(currentPlayer == temp) {
						sumAlongDiag++;
					}else {
						sumAlongDiag = 1;
					}
					temp = currentPlayer;
				}
			}
			if(sumAlongDiag == countThreshold) {
				count++;
			}
			sum += count;
		}
		return sum;
	}

	public int countHorizontal(int[][] position, int countThreshold, int c ) {
		int count = 0;
		for (int i = 0; i<position.length; i++) {
			int sumAlongHor =1;
			for(int j= 0; j<position[i].length-1; j++) {
				int currentPlayer = position[i][j];
				if(currentPlayer != 0 && currentPlayer == c ) {
					if(currentPlayer == position[i][j+1]) {
						sumAlongHor++;
					}else {
						sumAlongHor = 1;
					}
				}
				if(sumAlongHor == countThreshold) {
					count++;

				}

			}

		}

		return count;
	}

	public int countVertical(int[][] pieces, int countThreshold, int c ) {
		int count = 0;
		for (int j = 0; j<this.getNrCols(); j++) {
			int sumAlongVert=1;
			for(int i= 0; i<this.getNrRows()-1; i++) {
				int currentPlayer = pieces[i][j];
				if(currentPlayer != 0 && currentPlayer == c) {
					if(currentPlayer == pieces[i+1][j]) {
						sumAlongVert++;

					}else{
						sumAlongVert=1;
					}

				}
				if(sumAlongVert==countThreshold) {
					count++ ;
				}
			}
		}
		return count;
	}


	//Change boardfull, only checks the top most row, if this is full, then return
	public boolean checkBoardFull() {
		boolean check = true;
		int i = 0;
		while(check && i< this.getNrCols() ) {
			if(pieceLocation[0][i]==0) {
				check = false;
			}
			i++;
		}
		//Change to better

		return check;
	}
	public boolean checkWin(int [][] piece) {
		if(this.checkVertical(piece)) {
			return true;
		}

		if(this.checkHorizontal(piece)) {
			return true;
		}

		//Leftward diagonal condition
		if(this.checkDiagonal(piece)) {
			return true;
		}
		//Rightward diagonal 
		if(this.checkDiagonal(this.reverseArray(piece))){
			return true;
		}



		return false;
	}





	public void startNewGame() {

		this.clearBoard();
		this.setPlayer1(true);
	}
	public void quitGame() {
		this.setRunning(false); 
	}
	public void clearBoard() {
		for(int i = 0; i<this.getNrRows(); i++) {
			for(int j = 0; j< this.getNrCols(); j++) {
				pieceLocation[i][j] = 0;
			}
		}
	}

	public int[][] reverseArray(int arr[][]) {
		int copy[][] = new int [this.getNrRows()] [this.getNrCols()];

		for(int i = 0; i<this.getNrRows(); i++) {
			for(int j = 0; j<this.getNrCols();j++) {
				copy[i][j] = arr[i][this.getNrCols()-j-1];
			}
		}
		return copy;
	}
	public boolean checkVertical(int[][] pieces) {
		for (int j = 0; j<this.getNrCols(); j++) {
			int sumAlongVert=1;
			for(int i= 0; i<this.getNrRows()-1; i++) {
				int currentPlayer = pieceLocation[i][j];
				if(currentPlayer != 0) {
					if(currentPlayer == pieceLocation[i+1][j]) {
						sumAlongVert++;

					}else{
						sumAlongVert=1;
					}

				}
				if(sumAlongVert==this.getConnectN()) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean checkHorizontal(int[][] pieces) {
		for (int i = 0; i<this.getNrRows(); i++) {
			int sumAlongHor =1;
			for(int j= 0; j<this.getNrCols()-1; j++) {
				int currentPlayer = pieceLocation[i][j];
				if(currentPlayer != 0) {
					if(currentPlayer == pieceLocation[i][j+1]) {
						sumAlongHor++;
					}else {
						sumAlongHor = 1;
					}
				}
				if(sumAlongHor == this.getConnectN()) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean checkDiagonal(int[][]pieces) {
		int startSum = connectN - 1;
		int maxBound = nrRows+nrCols-connectN-1 ;

		for (int k = startSum; k <= maxBound; ++k) {
			int temp = MAX_RANGE;
			int sumAlongDiag = 1;
			int yMin = Math.max(0, k - nrCols+ 1);
			int yMax = Math.min(nrRows - 1, k);
			for (int i = yMin; i <= yMax; i++) {
				int j = k - i;
				int currentPlayer = pieces[i][j];
				if(currentPlayer!=0) {
					if(currentPlayer == temp) {
						sumAlongDiag++;

					}else {
						sumAlongDiag = 1;
					}
				}
				temp = currentPlayer;
				if(sumAlongDiag == connectN) {
					return true;
				}
			}

		}
		return false;
	}
	public boolean saveGameData(){
		String dir = "gameData";
		File directory = new File(dir);
		File file = new File("gameData/data.txt");	
		try{
			if(directory.exists() && directory.isDirectory()) {
				if(file.exists() && file.isFile()) {
					try(BufferedWriter writer = new BufferedWriter(new FileWriter(file)); ){
						writer.write(String.valueOf(this.getNrCols()));
						writer.newLine();
						writer.write(String.valueOf(this.getNrRows()));
						writer.newLine();
						writer.write(String.valueOf(this.getConnectN()));
						writer.newLine();
						writer.write(String.valueOf(this.isPlayer1()));
						writer.newLine();
						writer.write(String.valueOf(this.isPlayAgainstAI()));
						writer.newLine();
						writer.write(String.valueOf(this.isBoardFull()));
						writer.newLine();
						for(int i=0;i<nrRows; i++) {
							for(int j= 0; j<nrCols;j++) {
								writer.write(String.valueOf(this.getPieceLocation()[i][j]));
								writer.newLine();
							}
						}

					}catch (FileNotFoundException e) {
						return false;

					} catch (IOException e) {
						return false;

					}

				}else {
					file.createNewFile();
					this.saveGameData();
				}
			}else {
				directory.mkdirs();
				this.saveGameData();
			}

		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;

	}
	public boolean loadGameData() {
		String dir = "gameData";
		File directory = new File(dir);
		File file = new File("gameData/data.txt");	

		if(directory.exists() && directory.isDirectory()) {
			if(file.exists() && file.isFile()) {
				try(BufferedReader reader = new BufferedReader(new FileReader(file)); ){
					this.setNrCols(Integer.parseInt(reader.readLine()));
					this.setNrRows(Integer.parseInt(reader.readLine()));
					this.setConnectN(Integer.parseInt(reader.readLine()));
					this.setPlayer1(Boolean.parseBoolean(reader.readLine()));
					this.setPlayAgainstAI(Boolean.parseBoolean(reader.readLine()));
					if(this.isPlayAgainstAI()) {
						setLookUptable(playAgainstAI, connectN);
					}
					this.setBoardFull(Boolean.parseBoolean(reader.readLine()));

					int piecesArray[][] = new int[this.getNrRows()][this.getNrCols()];
					for(int i = 0; i<this.getNrRows();i++) {
						for(int j = 0; j<this.getNrCols();j++) {
							piecesArray[i][j] = Integer.parseInt(reader.readLine());
						}
					}
					this.setPieceLocation(piecesArray);

				}catch (FileNotFoundException e) {
					System.out.println("Game file not found");
					return false;
				} catch (IOException e) {
					e.printStackTrace();
				} catch(NumberFormatException e) {
					System.out.println("Game file tampered with");
					file.delete();
					return false;

				}

			}else {
				System.out.println("Save file does not exist");
				return false;

			}
		}else {
			System.out.println("Game folder has been tampered with");
			return false; 

		}
		return true;
	}

	// =========================================================================
	// ================================ GETTERS ================================
	// =========================================================================
	public int getNrRows()
	{
		return nrRows;
	}

	public boolean isPlayAgainstAI() {
		return playAgainstAI;
	}
	public int getNrCols()
	{
		return nrCols;
	}
	public boolean isPlayer1() {
		return player1;
	}
	public int[][] getPieceLocation() {
		return pieceLocation;
	}
	public boolean isRunning() {
		return running;
	}
	public boolean isBoardFull() {
		return boardFull;
	}

	public int getConnectN() {
		return connectN;
	}
	void setConnectN(int connectN) {
		this.connectN = connectN;
	}
	private void setNrRows(int nrRows) {
		this.nrRows = nrRows;
	}
	public void setNrCols(int nrCols) {
		this.nrCols = nrCols;
	}
	private void setPlayer1(boolean player1) {
		this.player1 = player1;
	}
	private void setPieceLocation(int[][] pieceLocation) {
		this.pieceLocation = pieceLocation;
	}
	public void setPlayAgainstAI(boolean playAgainstAI) {
		this.playAgainstAI = playAgainstAI;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}

	public void setBoardFull(boolean boardFull) {
		this.boardFull = boardFull;
	}

}


