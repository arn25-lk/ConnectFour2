/**
 * This file is to be completed by you.
 *
 * @author <Please enter your matriculation number, not your name>
 */
public final class TextView
{
	
	String nameOfPlayer1;
	String nameOfPlayer2;
	public TextView()
	{
	
	}
	public void declareWinner(Model model, boolean quitAndSave) {
		if(!quitAndSave) {
			if(!model.isBoardFull()) {
				if(model.isPlayAgainstAI() && !model.isPlayer1()) {
					System.out.println("AI Wins!");
					return;
				}
				if(model.isPlayer1()) {
					System.out.println("Player 1 wins!");
				}else {
					System.out.println("Player 2 wins!");
				}
			}else {
				this.displayGameOver();
			}
		}
		
	}
	public final int columnSize() {
		System.out.println("Number of Columns (3-10): ");
		int nrCols = InputUtil.readIntFromUser();
		while( nrCols < 3 || nrCols > 10) {
			System.out.printf("Can only play connectN in range");
			System.out.println();
			System.out.println("Number of Columns: ");
			nrCols= InputUtil.readIntFromUser();
		} 
		return nrCols;
	}
	public final int rowSize() {
		System.out.println("Number of Rows (3-10): ");
		int nrRows= InputUtil.readIntFromUser();
		while( nrRows < 3 || nrRows > 10) {
			System.out.printf("Can only play connectN in range");
			System.out.println();
			System.out.println("Number of Rows: ");
			nrRows= InputUtil.readIntFromUser();
		}
		return nrRows;
	}
	
	public final int pauseMenuChoice() {
		System.out.println("Press:");
		System.out.println("1. Start new game");
		System.out.println("2. Quit and Save game");
		System.out.println("3. Forfeit and Quit game");
		System.out.println("4. Continue Game");
		int startPrompt = InputUtil.readIntFromUser();
		while(startPrompt != 1 && startPrompt != 2 && startPrompt != 3 && startPrompt != 4) {
			startPrompt = InputUtil.readIntFromUser();
		}
		return startPrompt;
	}
	
	public final int gameOverChoices() {
		System.out.println("Press:");
		System.out.println("1. Start new game");
		System.out.println("2. Load from save file");
		System.out.println("3. Quit game");
		int startPrompt = InputUtil.readIntFromUser();
		while(startPrompt != 1 && startPrompt != 2 && startPrompt != 3) {
			startPrompt = InputUtil.readIntFromUser();
		}
		return startPrompt;
	}
	
	public final int connectFourChoice(int nrRows, int nrCols) {
		System.out.println("Play connect: ");
		int connectN= InputUtil.readIntFromUser();
		while(connectN > nrCols || connectN > nrRows || connectN <= 2) {
			System.out.printf("Can only play connectN in range");
			connectN= InputUtil.readIntFromUser();
		}
		return connectN;
	}
	public final int displayCustomBoardPreference() {
		System.out.println("=========CUSTOMIZE GAME BOARD=========");
		System.out.println("1. Choose customized board size");
		System.out.println("2. Play Connect-Four");
		int startPrompt = InputUtil.readIntFromUser();
		while(startPrompt != 1 && startPrompt != 2 ) {
			startPrompt = InputUtil.readIntFromUser();
		}
		return startPrompt;
	}
	public final int displayPlayAgainstMessage() {
		System.out.println("============CHOOSE GAME MODE============");
		System.out.println("1. Human vs. Human");
		System.out.println("2. Human vs. AI");
		int startPrompt = InputUtil.readIntFromUser();
		while(startPrompt != 1 && startPrompt != 2 ) {
			startPrompt = InputUtil.readIntFromUser();
		}
		return startPrompt;
	}
	public final void displayNewGameMessage()
	{
		System.out.println("---- NEW GAME STARTED ----");
		System.out.println("Press 0 to pause game");
	
		
	}

	public final int askForMove()
	{
		System.out.println("Press 0 to pause game");
		System.out.print("Select a free column: ");
		return InputUtil.readIntFromUser();
	}

	public final void displayGameOver() {
		InputUtil.readCharFromUser();

		System.out.println("---- GAME OVER ----");
	}


	public final void displayBoard(Model model)
	{
		int nrRows = model.getNrRows();
		int nrCols = model.getNrCols();
		int [][] pieceLocation = model.getPieceLocation();
		// Get your board representation.

		// This can be used to print a line between each row.
		// You may need to vary the length to fit your representation.
		String rowDivider = "-".repeat(4 * nrCols + 1);

		// A StringBuilder is used to assemble longer Strings more efficiently.
		StringBuilder sb = new StringBuilder();
		//Fix 
		sb.append(" ");
		for(int i= 1; i <= nrCols;i++) {
			sb.append(i);
			sb.append("   ");
		}
		sb.append("\n");


		for(int i = 0; i< nrRows;i++) {
			for(int j = 0; j<nrCols; j++) {
				switch(pieceLocation[i][j]) {
				case (0):
					sb.append("   ");
				break;
				case (1): 
					sb.append(" O ");
				break;
				case (-1):
					sb.append(" + ");
				break;
				}

				sb.append("|");

			}
			sb.append("\n");
			sb.append(rowDivider);
			sb.append("\n");


		}
		// You can add to the String using its append method.

		// Then print out the assembled String.
		System.out.println(sb);
	}
	public void whoseTurnIsIt(Model model) {
		if(model.isPlayer1()) {
			System.out.println("Player 1's Turn");
		}else {
			System.out.println("Player 2's Turn");
		}
		
	}


}
