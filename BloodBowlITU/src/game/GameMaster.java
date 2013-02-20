package game;

import java.util.ArrayList;
import models.BlockSum;
import models.GameStage;
import models.GameState;
import models.Player;
import models.PlayerTurn;
import models.Square;
import models.Standing;
import models.Team;
import models.Weather;
import models.actions.Block;
import models.actions.Catch;
import models.actions.Dodge;
import models.actions.GoingForIt;
import models.actions.PickUp;
import models.dice.BB;
import models.dice.D3;
import models.dice.D6;
import models.dice.DiceFace;
import models.dice.DiceRoll;

public class GameMaster {
	
	private GameState state;
	private Player selectedPlayer;
	//private PlayerAgent homeAgent;
	//private PlayerAgent awayAgent;
	
	public GameMaster(GameState gameState, PlayerAgent homeAgent, PlayerAgent awayAgent) {
		super();
		this.state = gameState;
		
		// For debugging
		//this.state.setGameStage(GameStage.KICKING_SETUP);
		//this.state.setKickingTeam(this.state.getHomeTeam());
	}
	
	public void update(){
		/*
		switch(state.getGameStage()){
		case KICK_PLACEMENT : getKickingAgent().takeAction(this, state);	
		case HOME_TURN : homeAgent.takeAction(this, state);	
		case AWAY_TURN : awayAgent.takeAction(this, state);	
		}
		*/
		
		String msg = GameLog.poll();
		if (msg != "" && msg != null){
			System.out.println(msg);
		}
		
	}
	
	/**
	 * Ends the current phase.
	 * Should only be called during the following phases:
	 * - KICKING_SETUP
	 * - RECEIVING_SETUP
	 * - HOME_TURN
	 * - AWAY_TURN
	 * - KICK_PLACEMENT
	 * - QUICK_SNAP
	 */
	public void endPhase(){
		
		switch(state.getGameStage()){
			case KICKING_SETUP : endSetup(); break;
			case RECEIVING_SETUP : endSetup(); break;
			case HOME_TURN : endTurn(); break;
			case AWAY_TURN : endTurn(); break;
			case KICK_PLACEMENT : kickBall(); break;
			case BLITZ : endTurn(); break;
			case QUICK_SNAP : endTurn(); break;
			case HIGH_KICK : endTurn(); break;
			case PERFECT_DEFENSE : endTurn(); break;
		default:
			break;
		}
		
	}
	
	/**
	 * A square was clicked.
	 * @param square
	 */
	public void squareClicked(Square square) {
		
		// Kick placement?
		if (state.getGameStage() == GameStage.KICK_PLACEMENT){
			placeBall(square);
			return;
		}
		
		Player player = state.getPitch().getPlayerArr()[square.getY()][square.getX()];
		
		// Player?
		if (player != null){
			
			if (state.getGameStage() == GameStage.PLACE_BALL_ON_PLAYER){
				placeBallOnPlayer(square);
				return;
			}
			
			// Selected player?
			if (player == selectedPlayer){
				
				// Remove selection
				selectedPlayer = null;
				
			} else {
				
				// Select player
				selectedPlayer = player;
				
			}
			
		} else {
			
			// Clicked on square
			emptySquareClicked(square.getX(),square.getY());
			
		}
		
	}
	
	private void placeBallOnPlayer(Square square) {
		
		Player player = state.getPitch().getPlayerArr()[square.getY()][square.getX()];
		
		// Correct team?
		if (playerOwner(player) == state.getReceivingTeam()){
			
			// Place ball
			state.getPitch().getBall().setSquare(square);
			state.getPitch().getBall().setOnGround(true);
			state.getPitch().getBall().setUnderControl(true);
			
			// End phase
			endTurn();
			
		}
		
	}

	/**
	 * An empty square on the pitch was clicked.
	 * @param x
	 * @param y
	 */
	public void emptySquareClicked(int x, int y) {
		
		Square square = new Square(x, y);
		//Player clickedPlayer = state.getPitch().getPlayerArr()[square.getY()][square.getX()];
		
		if (state.getGameStage() == GameStage.KICKING_SETUP){
			
			if (selectedPlayer != null && getPlayerOwner(selectedPlayer) == state.getKickingTeam()){
				
				// Places player if allowed to
				placePlayerIfAllowed(selectedPlayer, square);
				
			}
			
		} else if (state.getGameStage() == GameStage.RECEIVING_SETUP){
		
			if (selectedPlayer != null && getPlayerOwner(selectedPlayer) == state.getReceivingTeam()){
				
				// Places player if allowed to
				placePlayerIfAllowed(selectedPlayer, square);
				
			}
			
		} else if (state.getGameStage() == GameStage.HOME_TURN || 
				state.getGameStage() == GameStage.AWAY_TURN){
			
			if (selectedPlayer != null && getPlayerOwner(selectedPlayer) == getMovingTeam()){
				
				// Moves player if allowed to
				movePlayerIfAllowed(selectedPlayer, square);
				
			}
			
		} else if (state.getGameStage() == GameStage.BLITZ){
			
			if (selectedPlayer != null && getPlayerOwner(selectedPlayer) == state.getKickingTeam()){
				
				// Moves player if allowed to
				movePlayerIfAllowed(selectedPlayer, square);
				
			}
			
		} else if (state.getGameStage() == GameStage.QUICK_SNAP){
			
			if (selectedPlayer != null && getPlayerOwner(selectedPlayer) == state.getReceivingTeam()){
				
				// Moves player if allowed to
				movePlayerIfAllowed(selectedPlayer, square);
			}
			
		} else if (state.getGameStage() == GameStage.PERFECT_DEFENSE){
			
			if (selectedPlayer != null && getPlayerOwner(selectedPlayer) == state.getKickingTeam()){
				
				// Moves player if allowed to
				movePlayerIfAllowed(selectedPlayer, square);
				
			}
			
		}
		
	}
	
	public void selectAwayReserve(int reserve) {
		
		if (reserve >= state.getPitch().getAwayDogout().getReserves().size()){
			
			if (selectedPlayer != null){
				
				movePlayerToReserves(selectedPlayer, false);
				
			}
			
		} else if (selectedPlayer != state.getPitch().getAwayDogout().getReserves().get(reserve)){
			
			selectedPlayer = state.getPitch().getAwayDogout().getReserves().get(reserve);
		
		}
		
	}

	public void selectHomeReserve(int reserve) {
		
		if (reserve >= state.getPitch().getHomeDogout().getReserves().size()){
			
			if (selectedPlayer != null){
				
				movePlayerToReserves(selectedPlayer, true);
				
			}
			
		} else if (selectedPlayer != state.getPitch().getHomeDogout().getReserves().get(reserve)){
			
			selectedPlayer = state.getPitch().getHomeDogout().getReserves().get(reserve);
		
		}
		
	}
	
	private void movePlayerToReserves(Player player, boolean home) {
		
		if (playerOwner(player) == state.getHomeTeam() && home){
			
			removePlayerFromCurrentSquare(player);
			
			removePlayerFromReserves(player);
			
			state.getPitch().getHomeDogout().getReserves().add(selectedPlayer);
			
			
		} else if (playerOwner(player) == state.getAwayTeam() && !home){
			
			removePlayerFromCurrentSquare(player);
			
			removePlayerFromReserves(player);
			
			state.getPitch().getAwayDogout().getReserves().add(selectedPlayer);
			
		}
		
	}
	
	/**
	 * Start the game!
	 * This will initiate the coin toss game stage.
	 */
	public void startGame(){
		
		// Legal action?
		if (state.getGameStage() == GameStage.START_UP){
			
			// Roll for fans and FAME
			rollForFans();
			
			// Roll for weather
			rollForWeather();
			
			// Go to coin toss
			state.setGameStage(GameStage.COIN_TOSS);
			
			// Move all players to reserve
			state.getPitch().getHomeDogout().putPlayersInReserves();
			state.getPitch().getAwayDogout().putPlayersInReserves();
			
			GameLog.push("The game has started!");
			
		}
		
	}
	
	private void rollForFans() {
		
		// Fans
		D6 a = new D6();
		D6 b = new D6();
		a.roll();
		b.roll();
		int homeFans = 1000 * (a.getResultAsInt() + b.getResultAsInt() + state.getHomeTeam().getFanFactor());
		a.roll();
		b.roll();
		int awayFans = 1000 * (a.getResultAsInt() + b.getResultAsInt() + state.getAwayTeam().getFanFactor());
		
		state.getHomeTeam().getTeamStatus().setFans(homeFans);
		state.getAwayTeam().getTeamStatus().setFans(awayFans);
		
		// FAME
		if (homeFans <= awayFans){
			state.getHomeTeam().getTeamStatus().setFAME(0);
		}
		if (awayFans <= homeFans){
			state.getAwayTeam().getTeamStatus().setFAME(0);
		}
		
		if (homeFans > awayFans){
			state.getHomeTeam().getTeamStatus().setFAME(1);
		}
		if (awayFans > homeFans){
			state.getAwayTeam().getTeamStatus().setFAME(1);
		}
		
		if (homeFans > awayFans){
			state.getHomeTeam().getTeamStatus().setFAME(1);
		}
		if (awayFans > homeFans){
			state.getAwayTeam().getTeamStatus().setFAME(1);
		}
		
		if (homeFans >= awayFans * 2){
			state.getHomeTeam().getTeamStatus().setFAME(2);
		}
		if (awayFans >= homeFans * 2){
			state.getAwayTeam().getTeamStatus().setFAME(2);
		}
		
	}

	/**
	 * Home team picks a coin side.
	 * @param heads
	 * 		True if heads, false if tails.
	 */
	public void pickCoinSide(boolean heads){
		
		// Legal action?
		if (state.getGameStage() == GameStage.COIN_TOSS){
			
			// Set coin side pick
			state.getCoinToss().setHomePicked(heads);
			
			if (heads){
				GameLog.push("Home team picked heads.");
			} else {
				GameLog.push("Home team picked tails.");
			}
			
			// Toss the coin
			state.getCoinToss().Toss();
			
			if (state.getCoinToss().isResult() == heads){
				GameLog.push("Home team won the coin toss and will select to kick or receive.");
			} else {
				GameLog.push("Away team won the coin toss and will select to kick or receive.");
			}
			
			// Go to coin toss
			state.setGameStage(GameStage.PICK_COIN_TOSS_EFFECT);
			
		}
		
	}
	
	/**
	 * Coin toss winner picks coin toss effect.
	 * @param receive
	 * 		True if winner receives, false if winner kicks.
	 */
	public void pickCoinTossEffect(boolean receive){
		
		// Legal action?
		if (state.getGameStage() == GameStage.PICK_COIN_TOSS_EFFECT){
			
			// If home picked correct
			if (state.getCoinToss().isHomePicked() == state.getCoinToss().isResult()){
				
				// Home chooses to receive or kick
				state.getCoinToss().setHomeReceives(receive);
				if (receive){
					state.setReceivingTeam(state.getHomeTeam());
					state.setKickingTeam(state.getAwayTeam());
					GameLog.push("Home team selected to recieve the ball.");
					GameLog.push("Away team sets up first.");
				} else {
					state.setKickingTeam(state.getHomeTeam());
					state.setReceivingTeam(state.getAwayTeam());
					GameLog.push("Home team selected to kick the ball.");
					GameLog.push("Home team sets up first.");
				}
				
			} else {
				
				// Away chooses to receive or kick
				state.getCoinToss().setHomeReceives(!receive);
				if (!receive){
					state.setReceivingTeam(state.getHomeTeam());
					state.setKickingTeam(state.getAwayTeam());
					GameLog.push("Home team selected to recieve the ball.");
					GameLog.push("Away team sets up first.");
				} else {
					state.setKickingTeam(state.getHomeTeam());
					state.setReceivingTeam(state.getAwayTeam());
					GameLog.push("Home team selected to kick the ball.");
					GameLog.push("Home team sets up first.");
				}
				
			}
			
			state.setGameStage(GameStage.KICKING_SETUP);
			
		}
		
	}
	
	/**
	 * A team is done setting up.
	 * If the team is the kicking team, the next game stage will be receiving setup.
	 * If the team is the receiving team, the next game stage will be kick placement.
	 */
	public void endSetup(){
		
		if (state.getGameStage() == GameStage.KICKING_SETUP){
			
			// Kicking team	
			if (state.getPitch().isSetupLegal(state.getKickingTeam(), state.getHalf())){
				
				state.setGameStage(GameStage.RECEIVING_SETUP);
				selectedPlayer = null;
				
				GameLog.push("Kicking team is done setting up.");
				GameLog.push("Receiving team now has to setup.");
				
			}
			
		} else if (state.getGameStage() == GameStage.RECEIVING_SETUP){
			
			// Receiving team	
			if (state.getPitch().isSetupLegal(state.getReceivingTeam(), state.getHalf())){
					
				state.setGameStage(GameStage.KICK_PLACEMENT);
				selectedPlayer = null;
				
				GameLog.push("Receiving team is done setting up.");
				GameLog.push("Kicking team now has to place the ball.");
				
			}
			
		}
		
	}
	
	/**
	 * Place a player on a square.
	 * 
	 * @param player
	 * @param square
	 */
	public void placePlayerIfAllowed(Player player, Square square){
		
		Team team = getPlayerOwner(player);
		boolean moveAllowed = false;
		
		// Setting up?
		if (state.getGameStage() == GameStage.KICKING_SETUP &&
				state.getKickingTeam() == team){
			
			moveAllowed = true;
			
		} else if (state.getGameStage() == GameStage.RECEIVING_SETUP &&
				state.getReceivingTeam() == team){
			
			moveAllowed = true;
			
		} else if (state.getGameStage() == GameStage.PERFECT_DEFENSE &&
				state.getKickingTeam() == team){
			
			moveAllowed = true;
			
		} else if (state.getGameStage() == GameStage.HIGH_KICK &&
				!state.isPlayerPlaced() && 
				state.getReceivingTeam() == team){
			
			moveAllowed = true;
			
		}
		
		// Square occupied?
		if (state.getPitch().getPlayerArr()[square.getY()][square.getX()] != null){
			
			moveAllowed = false;
			
		}
		
		if (moveAllowed){
			removePlayerFromReserves(player);
			removePlayerFromCurrentSquare(player);
			placePlayerAt(player, square);
			
			if (state.getGameStage() == GameStage.HIGH_KICK){
				state.setPlayerPlaced(true);
			}
		}
		
	}
	
	/**
	 * Performs a block roll.
	 * 
	 * @param attacker
	 * @param defender
	 */
	public void performBlock(Player attacker, Player defender){
		
		// Legal action?
		if (allowedToBlock(attacker) && 
				nextToEachOther(attacker, defender) &&
				state.getCurrentBlock() == null){
			
			DiceRoll roll = new DiceRoll();
			
			BlockSum sum = CalculateBlockSum(attacker, defender);
			
			if (sum == BlockSum.EQUAL){
				
				BB ba = new BB();
				ba.roll();
				roll.addDice(ba);
				
			} else if(sum == BlockSum.ATTACKER_STRONGER){
				
				BB ba = new BB();
				BB bb = new BB();
				ba.roll();
				bb.roll();
				roll.addDice(ba);
				roll.addDice(bb);
				
			}  else if(sum == BlockSum.DEFENDER_STRONGER){
				
				BB ba = new BB();
				BB bb = new BB();
				ba.roll();
				bb.roll();
				roll.addDice(ba);
				roll.addDice(bb);
				
			} else if(sum == BlockSum.ATTACKER_DOUBLE_STRONG){
				
				BB ba = new BB();
				BB bb = new BB();
				BB bc = new BB();
				ba.roll();
				bb.roll();
				bc.roll();
				roll.addDice(ba);
				roll.addDice(bb);
				roll.addDice(bc);
				
			} else if(sum == BlockSum.DEFENDER_DOUBLE_STRONG){
				
				BB ba = new BB();
				BB bb = new BB();
				BB bc = new BB();
				ba.roll();
				bb.roll();
				bc.roll();
				roll.addDice(ba);
				roll.addDice(bb);
				roll.addDice(bc);
				
			}
			
			state.setCurrentBlock(new Block(attacker, defender));
			state.setAwaitReroll(true);
			
		}
		
	}
	
	/**
	 * Selects a rolled die.
	 * 
	 * @param i
	 * 		The index of the die in the dice roll.
	 */
	public void selectDie(int i){
		
		if (state.getCurrentDiceRoll() != null){
			
			// Select face
			DiceFace face = state.getCurrentDiceRoll().getFaces().get(i);
			
			// Continue block/dodge/going
			if (state.getCurrentBlock() != null){
				
				state.setAwaitReroll(false);
				continueBlock(face);
				
			}
		}
		
	}
	
	/**
	 * Rerolls the current dice roll.
	 */
	public void reroll(){
		
		if (!state.isAwaitingReroll()){
			return;
		}
		
		// Dodge/going/block
	 	if (state.getCurrentDodge() != null){
			
			state.setAwaitReroll(false);
			getMovingTeam().useReroll();
			
			DiceRoll roll = new DiceRoll();
			D6 d = new D6();
			d.roll();
			roll.addDice(d);
			state.setCurrentDiceRoll(roll);
			
			continueDodge(d.getResultAsInt());
			
		} else if (state.getCurrentGoingForIt() != null){
			
			state.setAwaitReroll(false);
			getMovingTeam().useReroll();
			
			DiceRoll roll = new DiceRoll();
			D6 d = new D6();
			d.roll();
			roll.addDice(d);
			state.setCurrentDiceRoll(roll);
			
			continueGoingForIt(d.getResultAsInt());
			
		} else if (state.getCurrentBlock() != null){

			// TODO
			
		} else if (state.getCurrentPickUp() != null){

			// TODO
			
		} else if (state.getCurrentCatch() != null){

			// TODO
			
		}
		
	}
	
	/**
	 * Moves a player to a square. 
	 * 
	 * @param player
	 * @param square
	 */
	public void movePlayerIfAllowed(Player player, Square square){
		
		boolean moveAllowed = moveAllowed(player, square);
		
		if (!moveAllowed){
			return;
		}
		
		// Dodge
		if (isInTackleZone(player)){
			
			dodgeToMovePlayer(player, square);
			
		} else {
			
			// Move
			movePlayer(player, square);
			
		}
		
	}
	
	/**
	 * Selects an action to the selected player.
	 * @param action
	 */
	public void selectAction(PlayerTurn action){
		
		// Only if allowed
		if (selectedPlayer != null && 
				playerOwner(selectedPlayer) == getMovingTeam() &&
				selectedPlayer.getPlayerStatus().getTurn() == PlayerTurn.UNUSED){
			
			selectedPlayer.getPlayerStatus().setTurn(action);
			
		}
		
	}
	
	private void continueGoingForIt(int result) {
		
		if (result > 1){
			
			dodgeToMovePlayer(state.getCurrentDodge().getPlayer(), state.getCurrentGoingForIt().getSquare());
			
		} else {
			
			movePlayer(state.getCurrentDodge().getPlayer(), state.getCurrentGoingForIt().getSquare());
			knockDown(state.getCurrentDodge().getPlayer(), true);
			
		}
		
	}

	private void continueDodge(int result) {
		
		performDodge(state.getCurrentDodge().getPlayer(), 
				state.getCurrentDodge().getSquare(), 
				result, 
				state.getCurrentDodge().getSuccess());
		
	}

	private void dodgeToMovePlayer(Player player, Square square) {

		int zones = numberOfTackleZones(player, square);
		
		int success = getDodgeSuccesRoll(player, zones);
		
		DiceRoll roll = new DiceRoll();
		D6 d = new D6();
		roll.addDice(d);
		d.roll();
		int result = d.getResultAsInt();
		
		state.setCurrentDiceRoll(roll);
		
		performDodge(player, square, result, success);
		
	}

	private void performDodge(Player player, Square square, int result, int success) {
		
		// Success?
		if (result == 6 || (result != 1 && result > success)){
			
			// Move
			movePlayer(player, square);
			
		} else {
			
			// Reroll?
			if (ableToReroll(getPlayerOwner(player))){
				
				// Prepare for reroll usage
				state.setCurrentDodge(new Dodge(player, square, success));
				state.setAwaitReroll(true);
				
			} else {

				// Player fall
				movePlayer(player, square);
				knockDown(player, true);
				endTurn();
				
			}
		}
		
	}

	private boolean ableToReroll(Team team) {
		if (team.getTeamStatus().getRerolls() > 0 &&
				!team.getTeamStatus().rerolledThisTurn()){
			return true;
		}
		return false;
	}

	private int getDodgeSuccesRoll(Player player, int zones) {
		
		int roll = 6 - player.getAG() + zones;
		
		return Math.max( 1, Math.min(6, roll) );
	}

	private int numberOfTackleZones(Player player, Square square) {
		
		int num = 0;
		
		for(int y = -1; y <= 1; y++){
			for(int x = -1; x <= 1; x++){
				
				Square test = new Square(square.getX() + x, square.getY() + y);
				
				Player p = state.getPitch().getPlayerArr()[test.getY()][test.getX()]; 
				
				// Opposite team?
				if (p != null &&
						getPlayerOwner(p) != getPlayerOwner(player)){
					
					num++;
					
				}
			}
		}
		
		return num;
		
	}

	private boolean isInTackleZone(Player player) {
		
		Square square = state.getPitch().getPlayerPosition(player);
		
		for(int y = -1; y <= 1; y++){
			for(int x = -1; x <= 1; x++){
				
				Square test = new Square(square.getX() + x, square.getY() + y);
				
				Player p = state.getPitch().getPlayerArr()[test.getY()][test.getX()]; 
				
				// Opposite team?
				if (p != null &&
						getPlayerOwner(p) != getPlayerOwner(player)){
					
					return true;
					
				}
			}
		}
		
		return false;
		
	}

	private void movePlayer(Player player, Square square) {

		// Use movement
		if (player.getPlayerStatus().getStanding() == Standing.DOWN){
			
			player.getPlayerStatus().useMovement(3 + 1);
			
		} else {
			
			player.getPlayerStatus().useMovement(3 + 1);

		}
		
		// Move player
		removePlayerFromCurrentSquare(player);
		movePlayerToSquare(player, square);
		
		// Pick up ball
		Square ballOn = state.getPitch().getBall().getSquare();
		if (ballOn.getX() == square.getX() && ballOn.getY() == square.getY()){
			
			pickUpBall();
			
		}
		
	}

	private void pickUpBall() {
		
		Square square = state.getPitch().getBall().getSquare();
		
		Player player = state.getPitch().getPlayerArr()[square.getY()][square.getX()];
		
		if (player == null || square == null){	
			return;
		}
		
		int zones = numberOfTackleZones(player, square);
		int success = 6 - player.getAG() + zones;
		success = Math.max( 1, Math.min(6, success) );
		
		// Roll
		DiceRoll roll = new DiceRoll();
		D6 d = new D6();
		roll.addDice(d);
		d.roll();
		state.setCurrentDiceRoll(roll);
		
		if (d.getResultAsInt() == 6 || 
				(d.getResultAsInt() != 1 && d.getResultAsInt() > success)){
			
			state.getPitch().getBall().setUnderControl(true);
			
		} else if (ableToReroll(getPlayerOwner(player))) {
			
			state.setCurrentPickUp(new PickUp(player, square, success));
			state.setAwaitReroll(true);
			
		} else {
			
			scatterBall();
			
			// End turn?
			// TODO: check to see if ball was thrown or scattered, maybe?
			if (getPlayerOwner(player) == getMovingTeam()){
				
				endTurn();
				
			}
			
		}
		
	}

	private void scatterBall() {
		
		int result = (int) (Math.random() * 8 + 1);
		Square ballOn = state.getPitch().getBall().getSquare();
		
		switch (result){
		case 1 : ballOn = new Square(ballOn.getX() - 1, ballOn.getY() - 1);
		case 2 : ballOn = new Square(ballOn.getX(), ballOn.getY() - 1);
		case 3 : ballOn = new Square(ballOn.getX() + 1, ballOn.getY() - 1);
		case 4 : ballOn = new Square(ballOn.getX() - 1, ballOn.getY());
		case 5 : ballOn = new Square(ballOn.getX() + 1, ballOn.getY());
		case 6 : ballOn = new Square(ballOn.getX() + 1, ballOn.getY() + 1);
		case 7 : ballOn = new Square(ballOn.getX() + 1, ballOn.getY() + 1);
		case 8 : ballOn = new Square(ballOn.getX() + 1, ballOn.getY() + 1);
		}
		
		state.getPitch().getBall().setSquare(ballOn);
		
		Player player = state.getPitch().getPlayerArr()[ballOn.getY()][ballOn.getX()];
		
		// Land on player
		if (player != null){
			
			catchBall();
			
		}
		
		// Outside pitch
		if (!state.getPitch().isBallInsidePitch()){
			
			if (state.getGameStage() == GameStage.KICK_OFF){
				state.setGameStage(GameStage.PLACE_BALL_ON_PLAYER);
			}
			
			trowInBall();
			
		}
	}
	
	private void trowInBall() {
		// TODO Auto-generated method stub
	}

	private void scatterKickedBall() {
		int d8 = (int) (Math.random() * 8 + 1);
		int d6 = (int) (Math.random() * 6 + 1);
		
		Square ballOn = state.getPitch().getBall().getSquare();
		
		while(d6 > 0){
			ballOn = state.getPitch().getBall().getSquare();
			
			switch (d8){
				case 1 : ballOn = new Square(ballOn.getX() - 1, ballOn.getY() - 1);
				case 2 : ballOn = new Square(ballOn.getX(), ballOn.getY() - 1);
				case 3 : ballOn = new Square(ballOn.getX() + 1, ballOn.getY() - 1);
				case 4 : ballOn = new Square(ballOn.getX() - 1, ballOn.getY());
				case 5 : ballOn = new Square(ballOn.getX() + 1, ballOn.getY());
				case 6 : ballOn = new Square(ballOn.getX() + 1, ballOn.getY() + 1);
				case 7 : ballOn = new Square(ballOn.getX() + 1, ballOn.getY() + 1);
				case 8 : ballOn = new Square(ballOn.getX() + 1, ballOn.getY() + 1);
			}
			
			state.getPitch().getBall().setSquare(ballOn);
			
			d6--;
			
			// Outside pitch
			if (!state.getPitch().isBallInsidePitch()){
				
				state.setGameStage(GameStage.PLACE_BALL_ON_PLAYER);
				
			}
			
		}

		// Land on player
		Player player = state.getPitch().getPlayerArr()[ballOn.getY()][ballOn.getX()];
		
		if (player != null){
			
			catchBall();
			
		} else {
			
			scatterBall();
			
		}
		
		// Gust of wind
		if (state.isGust()){
			
			scatterBall();
			
		}
		
		state.getPitch().getBall().setOnGround(true);
		
	}

	private void catchBall() {
		
		Square square = state.getPitch().getBall().getSquare();
		
		Player player = state.getPitch().getPlayerArr()[square.getY()][square.getX()];
		
		if (player == null || square == null){	
			return;
		}
		
		int zones = numberOfTackleZones(player, square);
		int success = 6 - player.getAG() + zones;
		success = Math.max( 1, Math.min(6, success) );
		
		// Roll
		DiceRoll roll = new DiceRoll();
		D6 d = new D6();
		roll.addDice(d);
		d.roll();
		state.setCurrentDiceRoll(roll);
		
		if (d.getResultAsInt() == 6 || 
				(d.getResultAsInt() != 1 && d.getResultAsInt() > success)){
			
			state.getPitch().getBall().setUnderControl(true);
			
		} else if (ableToReroll(getPlayerOwner(player))) {
			
			state.setCurrentCatch(new Catch(player, square, success));
			state.setAwaitReroll(true);
			
		} else {
			
			scatterBall();
			
			// End turn? 
			// TODO: check to see if ball was thrown or scattered
			if (getPlayerOwner(player) == getMovingTeam()){
				
				endTurn();
				
			}
			
		}
		
	}

	private Team getMovingTeam() {
		if (state.getGameStage() == GameStage.HOME_TURN){
			return state.getHomeTeam();
		} else if (state.getGameStage() == GameStage.HOME_TURN){
			return state.getAwayTeam();
		}
		return null;
	}

	private void movePlayerToSquare(Player player, Square square) {
		
		state.getPitch().getPlayerArr()[square.getY()][square.getX()] = player;
		
		player.getPlayerStatus().setTurn(PlayerTurn.MOVE_ACTION);
		
	}

	private boolean moveAllowed(Player player, Square square) {
		
		boolean moveAllowed = false;
		
		if (!nextToEachOther(selectedPlayer, square)){
			return false;
		}
		
		// Turn
		if (state.getGameStage() == GameStage.HOME_TURN && 
				state.getHomeTeam() == playerOwner(player)){
			
			moveAllowed = true;
			
		} else if (state.getGameStage() == GameStage.AWAY_TURN && 
				state.getAwayTeam() == playerOwner(player)){
			
			moveAllowed = true;
			
		} else if (state.getGameStage() == GameStage.BLITZ && 
				state.getKickingTeam() == playerOwner(player)){
			
			moveAllowed = true;
			
		} else if (state.getGameStage() == GameStage.QUICK_SNAP && 
				state.getReceivingTeam() == playerOwner(player)){
			
			if (player.getPlayerStatus().getMovementUsed() < 1){
				moveAllowed = true;
			}
			
		} else if (state.getGameStage() == GameStage.PERFECT_DEFENSE &&
				state.getKickingTeam() == playerOwner(player)){
					
			moveAllowed = true;
			
		}
		
		// Player turn
		if (player.getPlayerStatus().getTurn() == PlayerTurn.USED){
			return false;
		}

		// Movement left
		if (player.getPlayerStatus().getStanding() == Standing.UP){
			
			if (player.getPlayerStatus().getMovementUsed() < player.getMA()){
				
				// Move
				moveAllowed = true;
				
			} else if (player.getPlayerStatus().getMovementUsed() < player.getMA() + 2){
			
				// Going for it
				goingForIt(player, square);
				
			}
			
		} else if (player.getPlayerStatus().getStanding() == Standing.DOWN){
			
			if (player.getPlayerStatus().getMovementUsed() + 3 < player.getMA()){
				
				// Move
				moveAllowed = true;
				
			}
			
		} else if (player.getPlayerStatus().getStanding() == Standing.STUNNED){
			
			return false;
			
		}
		
		// Square occupied?
		if (state.getPitch().getPlayerArr()[square.getY()][square.getX()] != null){
			
			return false;
			
		}
		
		return moveAllowed;
	}
	
	private void goingForIt(Player player, Square square) {
		
		DiceRoll roll = new DiceRoll();
		D6 d = new D6();
		roll.addDice(d);
		d.roll();
		
		if (d.getResultAsInt() > 1){
			dodgeToMovePlayer(player, square);
		} else {
			state.setCurrentGoingForIt(new GoingForIt(player, square));
			state.setAwaitReroll(true);
		}
		
	}

	private void continueBlock(DiceFace face) {
		
		switch(face){
		case SKULL : attackerDown(state.getCurrentBlock());
		case PUSH : defenderPushed(state.getCurrentBlock());
		case BOTH_DOWN : bothDown(state.getCurrentBlock());
		case DEFENDER_STUMBLES : defenderStumples(state.getCurrentBlock());
		case DEFENDER_KNOCKED_DOWN : defenderKnockedDown(state.getCurrentBlock());
		default:
			break;
		}
	}

	private void defenderKnockedDown(Block block) {
		// TODO Auto-generated method stub
		
	}

	private void defenderStumples(Block block) {
		// TODO Auto-generated method stub

	}

	private void bothDown(Block block) {
		
		knockDown(block.getDefender(), true);
		
		// ball?
		
		attackerDown(block);
		
	}

	private void defenderPushed(Block block) {
		// TODO Auto-generated method stub
		
	}

	private void attackerDown(Block block) {
		
		knockDown( block.getAttacker(), true );
		
		// ball?
		
		endTurn();
		
	}

	private void endTurn() {
		
		// Any turns left?
		if (state.getGameStage() == GameStage.HOME_TURN){
			
			if (state.getAwayTurn() < 8){
				
				// Away turn
				state.incAwayTurn();
				startNewTurn();
				
			} else {
				
				endHalf();
				
			}
			
		} else if (state.getGameStage() == GameStage.AWAY_TURN){
			
			if (state.getHomeTurn() < 8){
				
				// Away turn
				state.incHomeTurn();
				startNewTurn();
				
			} else {
				
				if (state.getHalf() == 1){
					
					startNextHalf();
					
				} else {
					
					endGame();
					
				}
				
			}
			
		} else if (state.getGameStage() == GameStage.BLITZ || 
				state.getGameStage() == GameStage.QUICK_SNAP){
			
			endKickOffPhase();
		
		} else if (state.getGameStage() == GameStage.KICK_OFF || 
				state.getGameStage() == GameStage.PLACE_BALL_ON_PLAYER){
			
			startNewTurn();
		
		} 
		
	}

	private void endHalf() {
		
		if (state.getHalf() == 1){
			
			startNextHalf();
			
		} else {
			
			endGame();
			
		}
		
	}

	private void endGame() {
		
		state.setGameStage(GameStage.GAME_ENDED);
		
	}

	private void startNextHalf() {
		
		clearField();
		rollForKnockedOut();
		
		state.setHalf(state.getHalf() + 1);
		
		// Who kicks?
		if ( state.getCoinToss().isHomeReceives() ){
			
			state.setKickingTeam(state.getHomeTeam());
			state.setReceivingTeam(state.getAwayTeam());
			
		} else {
			
			state.setKickingTeam(state.getHomeTeam());
			state.setReceivingTeam(state.getAwayTeam());
			
		}
		
		state.setGameStage(GameStage.KICKING_SETUP);
		
	}

	private void rollForKnockedOut() {
		
		D6 da = new D6();
		
		// Home team
		for(Player player : state.getPitch().getHomeDogout().getKnockedOut()){
			
			da.roll();
			
			if (da.getResultAsInt()> 3){
				
				state.getPitch().getHomeDogout().getKnockedOut().remove(player);
				state.getPitch().getHomeDogout().getReserves().add(player);
				
			}
			
		}
		
		// Away team
		for(Player player : state.getPitch().getAwayDogout().getKnockedOut()){
			
			da.roll();
			
			if (da.getResultAsInt() > 3){
				
				state.getPitch().getAwayDogout().getKnockedOut().remove(player);
				state.getPitch().getAwayDogout().getReserves().add(player);
				
			}
			
		}
		
	}

	private void clearField() {
		
		// Home team
		for(Player player : state.getHomeTeam().getPlayers()){
			
			if (state.getPitch().isOnPitch(player)){
				
				state.getPitch().removePlayer(player);
				state.getPitch().getHomeDogout().getReserves().add(player);
				
			}
			
		}
		
		// Away team
		for(Player player : state.getAwayTeam().getPlayers()){
			
			if (state.getPitch().isOnPitch(player)){
				
				state.getPitch().removePlayer(player);
				state.getPitch().getAwayDogout().getReserves().add(player);
				
			}
			
		}
		
	}

	private void startNewTurn() {
		
		selectedPlayer = null;
		
		if (state.getGameStage() == GameStage.KICK_OFF){
			
			if (state.getHomeTeam() == state.getReceivingTeam()){
				
				state.setGameStage(GameStage.HOME_TURN);
				state.incHomeTurn();
				fixStunnedPlayers(state.getHomeTeam());
				resetStatii(state.getAwayTeam());
				resetStatii(state.getHomeTeam());
				
			} else {
				
				state.setGameStage(GameStage.AWAY_TURN);
				state.incAwayTurn();
				fixStunnedPlayers(state.getAwayTeam());
				resetStatii(state.getAwayTeam());
				resetStatii(state.getHomeTeam());
				
			}
			
		} else if (state.getGameStage() == GameStage.HOME_TURN){
			
			state.setGameStage(GameStage.AWAY_TURN);
			state.incAwayTurn();
			fixStunnedPlayers(state.getAwayTeam());
			resetStatii(state.getHomeTeam());
			
		} else if (state.getGameStage() == GameStage.AWAY_TURN){
				
			state.setGameStage(GameStage.HOME_TURN);
			state.incHomeTurn();
			fixStunnedPlayers(state.getHomeTeam());
			resetStatii(state.getAwayTeam());
				
		}
		
	}

	private void resetStatii(Team team) {
		
		team.getTeamStatus().reset();
		
		for(Player p : team.getPlayers()){
			
			p.getPlayerStatus().reset();
			
		}
		
	}

	private void fixStunnedPlayers(Team team) {
		
		for(Player p : team.getPlayers()){
			
			if (p.getPlayerStatus().getStanding() == Standing.STUNNED){
				
				p.getPlayerStatus().setStanding(Standing.DOWN);
				
			}
			
		}
		
	}

	private void knockDown(Player player, boolean armourRoll) {
		
		// Armour roll
		D6 da = new D6();
		D6 db = new D6();
		da.roll();
		db.roll();
		
		int result = da.getResultAsInt() + db.getResultAsInt();
		
		if (result > player.getAV() || !armourRoll){
			
			// Injury roll
			da.roll();
			db.roll();
			
			result = da.getResultAsInt() + db.getResultAsInt();
			
			if (result < 8){
				
				// Stunned
				player.getPlayerStatus().setStanding(Standing.STUNNED);
				
			} else if (result < 10){
				
				// Knocked out
				state.getPitch().removePlayer(player);
				state.getPitch().getDogout(getPlayerOwner(player)).getKnockedOut().add(player);
				
			} else {
				
				// Dead and injured
				state.getPitch().removePlayer(player);
				state.getPitch().getDogout(getPlayerOwner(player)).getDeadAndInjured().add(player);
				
			}
			
		} else {
		
			player.getPlayerStatus().setStanding(Standing.DOWN);
			
		}
		
	}

	private BlockSum CalculateBlockSum(Player attacker, Player defender) {
		// TODO Auto-generated method stub
		return BlockSum.EQUAL;
	}

	private boolean nextToEachOther(Player a, Player b) {
		
		if (state.getPitch().isOnPitch(a) &&
				state.getPitch().isOnPitch(b)){
			
			Square aPos = state.getPitch().getPlayerPosition(a);
			Square bPos = state.getPitch().getPlayerPosition(b);
			
			// Not equal
			if (aPos.getX() == bPos.getX() && aPos.getY() == bPos.getY()){
				return false;
			}
			
			// At most one away
			if (Math.abs( aPos.getX() - bPos.getX() ) <= 1 ){
				if (Math.abs( aPos.getY() - bPos.getY() ) <= 1 ){
					return true;
				}
			}
			
		}
		
		return false;
	}
	
	private void placeBall(Square square) {
		
		state.getPitch().getBall().setSquare(square);
		
	}

	private boolean allowedToBlock(Player player) {
		
		boolean allowed = false;
		
		// Home turn
		if (state.getGameStage() == GameStage.HOME_TURN && 
				state.getHomeTeam() == playerOwner(player)){
			
			allowed = true;
			
		}
		
		// Away turn
		if (state.getGameStage() == GameStage.AWAY_TURN && 
				state.getAwayTeam() == playerOwner(player)){
			
			allowed = true;
			
		}
		
		// Blitz phase
		if (state.getGameStage() == GameStage.BLITZ && 
				state.getKickingTeam() == playerOwner(player)){
			
			allowed = true;
			
		}
		
		if (!allowed)
			return false;
		
		allowed = false;
		
		// Player have had turn?
		if (player.getPlayerStatus().getTurn() == PlayerTurn.USED){
			
			return false;
			
		} else if (player.getPlayerStatus().getTurn() == PlayerTurn.BLITZ_ACTION && 
				player.getPlayerStatus().hasMovedToBlock()){
			
			// Blitz
			allowed = true;
			
		} else if (player.getPlayerStatus().getTurn() == PlayerTurn.UNUSED){
			
			allowed = true;
			
		}
		
		// Standing
		if (allowed && player.getPlayerStatus().getStanding() == Standing.UP){
			
			return true;
			
		}
			
		return false;
		
	}
	
	private boolean nextToEachOther(Player player, Square square) {
		
		if (state.getPitch().isOnPitch(player)){
			
			Square aPos = state.getPitch().getPlayerPosition(player);
			
			// Not equal
			if (aPos.getX() == square.getX() && aPos.getY() == square.getY()){
				return false;
			}
			
			// At most one away
			if (Math.abs( aPos.getX() - square.getX() ) <= 1 ){
				if (Math.abs( aPos.getY() - square.getY() ) <= 1 ){
					return true;
				}
			}
			
		}
		
		return false;
		
	}

	private Team playerOwner(Player player) {
		if (state.getHomeTeam().getPlayers().contains(player)){
			return state.getHomeTeam();
		} 
		return state.getAwayTeam();
	}

	private void placePlayerAt(Player player, Square square) {
		state.getPitch().getPlayerArr()[square.getY()][square.getX()] = player;
	}

	private void removePlayerFromReserves(Player player) {
		state.getPitch().getDogout(getPlayerOwner(player)).getReserves().remove(player);
	}
	
	private void removePlayerFromCurrentSquare(Player player) {
		state.getPitch().removePlayer(player);
	}

	private Team getPlayerOwner(Player player) {

		// On home team?
		if (state.getHomeTeam().getPlayers().contains(player)){
			return state.getHomeTeam();
		} else {
			return state.getAwayTeam();
		}

	}
	
	private void kickBall() {
		
		// Ball not placed?
		if (state.getPitch().getBall().getSquare() == null){
			GameLog.push("The ball has not been placed!");
			return;
		}
		
		// Ball corectly placed?
		if (!state.getPitch().ballCorreclyPlaced(state.getKickingTeam())){
			GameLog.push("The ball has not been placed correctly!");
			return;
		}
		
		state.setGameStage(GameStage.KICK_OFF);
		
		rollForKickOff();
		
		// If special kick off phase started
		if (state.getGameStage() != GameStage.KICK_OFF){
			
			return;
		}
		
		endKickOffPhase();
		
	}

	private void endKickOffPhase() {
		
		state.setGameStage(GameStage.KICK_OFF);
		
		scatterKickedBall();

		endTurn();
		
	}

	private void rollForKickOff(){
		
		D6 da = new D6();
		D6 db = new D6();
		da.roll(); 
		db.roll();
		int roll = da.getResultAsInt() + db.getResultAsInt();
		switch(roll){
			case 2: getTheRef(); break;
			case 3: riot(); break;
			case 4: perfectDefense(); break;
			case 5: highKick(); break;
			case 6: cheeringFans(); break;
			case 7: changingWeather(); break;
			case 8: brilliantCoaching(); break;
			case 9: quickSnap(); break;
			case 10: blitz(); break;
			case 11: throwARock(); break;
			case 12: pitchInvasion(); break;
		}
		
	}

	/**
	 * Pitch Invasion:  Both coaches roll a D6 for each 
	 * opposing player on the pitch and add their FAME 
	 * (see page 18) to the roll. If a roll is 6 or more after 
	 * modification then the player is Stunned (players with 
	 * the Ball & Chain skill are KO'd). A roll of 1 before 
	 * adding FAME will always have no effect.
	 */
	private void pitchInvasion() {
		
		GameLog.push("Pitch invasion!");
	
		invadeTeam(state.getHomeTeam());
		
		invadeTeam(state.getAwayTeam());
		
	}

	/**
	 * Throw a Rock: An enraged fan hurls a large rock at 
	 * one of the players on the opposing team.  Each 
	 * coach rolls a D6 and adds their FAME (see page 
	 * 18) to the roll. The fans of the team that rolls higher 
	 * are the ones that threw the rock. In the case of a tie 
	 * a rock is thrown at each team! Decide randomly 
	 * which player in the other team was hit (only players 
	 * on the pitch are eligible) and roll for the effects of 
	 * the injury straight away. No Armour roll is required.
	 */
	private void throwARock() {
		
		GameLog.push("Throw a rock!");
	
		D6 home = new D6();
		D6 away = new D6();
		
		home.roll();
		away.roll();
		
		int homeResult = home.getResultAsInt() + 
				state.getHomeTeam().getTeamStatus().getFAME();
		
		int awayResult = away.getResultAsInt() + 
				state.getAwayTeam().getTeamStatus().getFAME();
		
		if (homeResult >= awayResult){
			GameLog.push(state.getHomeTeam().getTeamName() + " threw a rock.");
			
			// Injure random away player
			throwRockAt(state.getAwayTeam());
			
		}
		if (awayResult >= homeResult){
			GameLog.push(state.getAwayTeam().getTeamName() + " threw a rock.");
			
			// Injure random home player
			throwRockAt(state.getHomeTeam());
			
		}
		
	}
	
	private void invadeTeam(Team team) {
		
		for(Player p : team.getPlayers()){
			
			if (state.getPitch().isOnPitch(p)){
				
				D6 d = new D6();
				
				d.roll();
				
				int result = d.getResultAsInt() + 
						state.getHomeTeam().getTeamStatus().getFAME();
				
				if (d.getResultAsInt() == 1){
					
					continue;
					
				} else if (result >= 6){
					
					p.getPlayerStatus().setStanding(Standing.STUNNED);
					
				}
				
			}
			
		}
		
	}

	private void throwRockAt(Team team) {
		
		ArrayList<Player> targets = new ArrayList<Player>();
		
		for(Player p : team.getPlayers()){
			
			if (state.getPitch().isOnPitch(p)){
				targets.add(p);
			}
			
		}
		
		int ran = (int)(Math.random()*targets.size());
		
		Player target = targets.get(ran);
		
		// Knock down player with no armour save
		knockDown(target, false);
		
	}

	private void blitz() {
		state.setGameStage(GameStage.BLITZ);
		GameLog.push("Blitz!");
	}

	private void quickSnap() {
		state.setGameStage(GameStage.QUICK_SNAP);
		GameLog.push("Quck snap!");
	}

	/**
	 * Brilliant Coaching: Each coach rolls a D3 and adds 
	 * their FAME (see page 18) and the number of 
	 * assistant coaches on their team to the score. The 
	 * team with the highest total gets an extra team re-roll 
	 * this half thanks to the brilliant instruction provided 
	 * by the coaching staff. In case of a tie both teams get 
	 * an extra team re-roll. 
	 */
	private void brilliantCoaching() {
		
		GameLog.push("Brilliant coaching!");
		
		D3 home = new D3();
		D3 away = new D3();
		
		home.roll();
		away.roll();
		
		int homeResult = home.getResultAsInt() + 
				state.getHomeTeam().getTeamStatus().getFAME() + 
				state.getHomeTeam().getAssistantCoaches();
		
		int awayResult = away.getResultAsInt() + 
				state.getAwayTeam().getTeamStatus().getFAME() + 
				state.getAwayTeam().getAssistantCoaches();
		
		if (homeResult >= awayResult){
			int rr = state.getHomeTeam().getTeamStatus().getRerolls() + 1;
			state.getHomeTeam().getTeamStatus().setRerolls(rr);
			GameLog.push(state.getHomeTeam().getTeamName() + " gets an extra reroll.");
		}
		if (awayResult >= homeResult){
			int rr = state.getAwayTeam().getTeamStatus().getRerolls() + 1;
			state.getAwayTeam().getTeamStatus().setRerolls(rr);
			GameLog.push(state.getAwayTeam().getTeamName() + " gets an extra reroll.");
		}
		
	}

	/**
	 * Changing Weather: Make a new roll on the Weather 
	 * table (see page 20). Apply the new Weather roll. If 
	 * the new Weather roll was a ‘Nice’ result, then a 
	 * gentle gust of wind makes the ball scatter one extra 
	 * square in a random direction before landing.  
	 */
	private void changingWeather() {
		GameLog.push("Changing weather!");
		rollForWeather();
		
		// Gentle gust
		if (state.getWeather() == Weather.NICE){
			state.setGust(true);
		}
	}

	/**
	 * Cheering Fans:  Each coach rolls a  D3  and 
	 * adds their team’s FAME (see page 18) 
	 * and the number of cheerleaders on their 
	 * team to the score. 
	 * The team with the highest score is 
	 * inspired by their fans' 
	 * cheering and gets an extra re-roll this half. 
	 * If both teams have the same score, then 
	 * both teams get a reroll.
	 */
	private void cheeringFans() {
		
		GameLog.push("Cheering fans!");
		
		D3 home = new D3();
		D3 away = new D3();
		
		home.roll();
		away.roll();
		
		int homeResult = home.getResultAsInt() + 
				state.getHomeTeam().getTeamStatus().getFAME() + 
				state.getHomeTeam().getCheerleaders();
		
		int awayResult = away.getResultAsInt() + 
				state.getAwayTeam().getTeamStatus().getFAME() + 
				state.getAwayTeam().getCheerleaders();
		
		if (homeResult >= awayResult){
			int rr = state.getHomeTeam().getTeamStatus().getRerolls() + 1;
			state.getHomeTeam().getTeamStatus().setRerolls(rr);
			GameLog.push(state.getHomeTeam().getTeamName() + " gets an extra reroll.");
		}
		if (awayResult >= homeResult){
			int rr = state.getAwayTeam().getTeamStatus().getRerolls() + 1;
			state.getAwayTeam().getTeamStatus().setRerolls(rr);
			GameLog.push(state.getAwayTeam().getTeamName() + " gets an extra reroll.");
		}
		
		
	}

	private void highKick() {
		GameLog.push("High kick!");
		state.setGameStage(GameStage.HIGH_KICK);
	}

	private void perfectDefense() {
		GameLog.push("Perfect defense!");
		state.setGameStage(GameStage.PERFECT_DEFENSE);
	}

	/**
	 * 	The trash talk between two opposing players 
	 *	explodes and rapidly degenerates, involving the rest 
	 *	of the players. Roll a D6. On a 1-3, the referee lets the 
	 *	clock run on during the fight; both teams’ turn markers 
	 *	are moved  forward  along the turn track a number of 
	 *	spaces equal to the D6 roll. If this takes the number of 
	 *	turns to 8 or more for both teams, then the half ends. 
	 *	On a roll of 4-6 the referee resets the clock back to 
	 *	before the fight started, so both teams turn markers 
	 *	are moved one space back along the track. The turn 
	 *	marker may not be moved back before turn 1; if this 
	 *	would happen do not move the Turn marker in either 
	 * 	direction. 
	 */
	private void riot() {
		
		GameLog.push("Riot!");
		
		D6 d = new D6();
		d.roll();
		if (d.getResultAsInt() <= 3){
			
			GameLog.push("The referee lets the clock run on during the fight.");
			
			// End half?
			if (state.getHomeTurn() + d.getResultAsInt() >= 8 && 
					state.getAwayTurn() + d.getResultAsInt() >= 8){
				
				endHalf();
				
			} else {
				
				// Move turn markers forward
				state.setHomeTurn(state.getHomeTurn() + d.getResultAsInt());
				state.setAwayTurn(state.getAwayTurn() + d.getResultAsInt());
				
			}
			
		} else {
			
			GameLog.push("The referee resets the clock back to before the fight started.");
			
			// Move turn makers one backwards
			state.setHomeTurn(Math.max(1, state.getHomeTurn() - 1));
			state.setAwayTurn(Math.max(1, state.getAwayTurn() - 1));
			
		}
		
	}

	/**
	 * Get the Ref: The fans exact gruesome revenge on the 
	 * referee for some of the dubious decisions he has 
	 * made, either during this match or in the past. His 
	 * replacement is so intimidated that for the rest of the 
	 * half he will not send players from either team off for
	 */
	private void getTheRef() {
		state.setRefAgainstHomeTeam(false);
		state.setRefAgainstAwayTeam(false);
		GameLog.push("Get the ref! No players from either team will be send off the field for making a foul nor be banned for using secret weapons.");
	}

	private void rollForWeather() {
		D6 da = new D6();
		D6 db = new D6();
		da.roll(); 
		db.roll();
		int roll = da.getResultAsInt() + db.getResultAsInt();
		switch(roll){
			case 2: state.setWeather(Weather.SWELTERING_HEAT); break;
			case 3: state.setWeather(Weather.VERY_SUNNY); break;
			case 4: state.setWeather(Weather.NICE); break;
			case 5: state.setWeather(Weather.NICE); break;
			case 6: state.setWeather(Weather.NICE); break;
			case 7: state.setWeather(Weather.NICE); break;
			case 8: state.setWeather(Weather.NICE); break;
			case 9: state.setWeather(Weather.NICE); break;
			case 10: state.setWeather(Weather.NICE); break;
			case 11: state.setWeather(Weather.POURING_RAIN); break;
			case 12: state.setWeather(Weather.BLIZZARD); break;
		}
	}

	public Player getSelectedPlayer() {
		return selectedPlayer;
	}

	public void setSelectedPlayer(Player selectedPlayer) {
		this.selectedPlayer = selectedPlayer;
	}

	public GameState getState() {
		return state;
	}

}