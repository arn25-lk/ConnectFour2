/**
 * This file is to be completed by you.
 *
 * @author <s2094685>
 */
public final class Controller
{
	private final Model model;
	private final TextView view;


	public Controller(Model model, TextView view)
	{
		this.model = model;
		this.view = view;
		this.initPlayAgainst();
	}
	boolean quitAndSaveGame;
	public void startSession()
	{	
		view.displayNewGameMessage();

		// TODO Complete this method. The following bits of code should be useful:

		view.displayBoard(model);
		while(model.isRunning()) {
			// Tell the user that the game has started.
		
			if(!model.checkBoardFull()) {
				view.whoseTurnIsIt(model);
				if(model.isPlayAgainstAI()&& !model.isPlayer1()) {
					model.aiMakesMove();
				}else {
					int move =  view.askForMove();
					if(move==0) {
						this.pauseMenu();
					}else {
						model.makeMove(move); 
					}
				}
			}else {
				model.setBoardFull(true);
				model.quitGame();
				view.declareWinner(model, this.isQuitAndSaveGame());
				break;
			}
			
			view.displayBoard(model);
		}
		view.declareWinner(model, this.isQuitAndSaveGame());
		this.setQuitAndSaveGame(false);
		if(!this.userDecidesIfGameOver()) {
			this.startSession();
		}





		// Repeat...
	}
	public boolean userDecidesIfGameOver()  {
		int startPrompt = view.gameOverChoices();
		switch(startPrompt) {
		case 1:
			model.startNewGame();
			this.initPlayAgainst();
			model.setRunning(true);
			return false;


		case 2: 
			if(model.loadGameData()) {
				if(model.isPlayer1()) {
					System.out.println("PLAYER 1 TO PLAY");
				}else {
					System.out.println("PLAYER 2 TO PLAY");
				}
			}else {
				
				this.userDecidesIfGameOver();
				
			}
			model.setRunning(true);
			return false;

		case 3:
			break;
		}
		return true;
	}
	public void initPlayAgainst() {
		int startPrompt = view.displayPlayAgainstMessage();
		switch(startPrompt) {
		case 1: 
			this.initDimensions(false);
			break;
		case 2: 
			this.initDimensions(true);
			break;
		}

	}
	public void initDimensions( boolean playAgainstAI) {

		
			int startPrompt = view.displayCustomBoardPreference();
			switch(startPrompt) {
			case 1: 
				
				int nrRows = view.rowSize();
				int nrCols = view.columnSize();
				int connectN = view.connectFourChoice(nrRows, nrCols);
				model.initFromSaveFile(true, nrCols, nrRows, new int[nrRows][nrCols], connectN, playAgainstAI,false);
				break;
			case 2: 
				model.initFromSaveFile(
						true, 
						Model.DEFAULT_NR_COLS, 
						Model.DEFAULT_NR_ROWS, 
						new int [Model.DEFAULT_NR_ROWS][Model.DEFAULT_NR_COLS] , 
						Model.DEFAULT_CONNECT_N, 
						playAgainstAI,false);
				break;
			}


	
	}
	public void pauseMenu() {
		int startPrompt = view.pauseMenuChoice();
		switch(startPrompt) {
		case 1: 
			model.startNewGame();
			this.initPlayAgainst();
			break;
		case 2: 
			if(model.saveGameData()) {
				System.out.println("Saved Game Successfully!");
			}
			model.quitGame();
			this.setQuitAndSaveGame(true);
			break;
		case 3: 
			model.quitGame();
			break;

		default: break;
		}
	}
	public boolean isQuitAndSaveGame() {
		return quitAndSaveGame;
	}
	public void setQuitAndSaveGame(boolean quitAndSaveGame) {
		this.quitAndSaveGame = quitAndSaveGame;
	}

}
