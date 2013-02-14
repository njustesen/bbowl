package game;

import models.Block;
import models.BlockSum;
import models.GameStage;
import models.GameState;
import models.Player;
import models.PlayerTurn;
import models.Square;
import models.Standing;
import models.Team;
import models.Weather;
import models.dice.BB;
import models.dice.D6;
import models.dice.DiceFace;
import models.dice.DiceRoll;

public class GameMaster {
	
	private GameState state;
	private Player selectedPlayer;
	private PlayerAgent homeAgent;
	private PlayerAgent awayAgent;
	
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
		default:
			break;
		}
		
	}
	
	/**
	 * A square on the pitch was clicked.
	 * @param x
	 * @param y
	 */
	public void squareClicked(int x, int y) {
		
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
			
			if (selectedPlayer != null && getPlayerOwner(selectedPlayer) == state.getMovingTeam()){
				
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
			
			// Roll for weather
			rollForWeather();
			
			// Go to coin toss
			state.setGameStage(GameStage.COIN_TOSS);
			
			// Move all players to reserve
			state.getPitch().getHomeDogout().putPlayersInReserves();
			state.getPitch().getAwayDogout().putPlayersInReserves();
			
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
			
			// Toss the coin
			state.getCoinToss().Toss();
			
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
			if (state.getCoinToss().isHomePicked() && state.getCoinToss().isResult()){
				
				// Home chooses to receive or kick
				state.getCoinToss().setHomeReceives(receive);
				if (receive){
					state.setReceivingTeam(state.getHomeTeam());
					state.setKickingTeam(state.getAwayTeam());
				} else {
					state.setKickingTeam(state.getHomeTeam());
					state.setReceivingTeam(state.getAwayTeam());
				}
				
			} else {
				
				// Away chooses to receive or kick
				state.getCoinToss().setHomeReceives(!receive);
				if (!receive){
					state.setReceivingTeam(state.getHomeTeam());
					state.setKickingTeam(state.getAwayTeam());
				} else {
					state.setKickingTeam(state.getHomeTeam());
					state.setReceivingTeam(state.getAwayTeam());
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
				
			}
			
		} else if (state.getGameStage() == GameStage.RECEIVING_SETUP){
			
			// Receiving team	
			if (state.getPitch().isSetupLegal(state.getReceivingTeam(), state.getHalf())){
					
				state.setGameStage(GameStage.KICK_PLACEMENT);
				selectedPlayer = null;
				
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
			
		}
		
		//For debugging
		//moveAllowed = true;
		
		// Square occupied?
		if (state.getPitch().getPlayerArr()[square.getY()][square.getX()] != null){
			
			moveAllowed = false;
			
		}
		
		if (moveAllowed){
			removePlayerFromReserves(player);
			removePlayerFromCurrentSquare(player);
			placePlayerAt(player, square);
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
			state.getMovingTeam().useReroll();
			
			DiceRoll roll = new DiceRoll();
			D6 d = new D6();
			d.roll();
			roll.addDice(d);
			state.setCurrentDiceRoll(roll);
			
			continueDodge(d.getResultAsInt());
			
		} else if (state.getCurrentGoingForIt() != null){
			
			state.setAwaitReroll(false);
			state.getMovingTeam().useReroll();
			
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
		
		dodgeToMovePlayer(player, square);
		
	}
	
	private void continueGoingForIt(int result) {
		
		if (result > 1){
			
			dodgeToMovePlayer(state.getCurrentDodge().getPlayer(), state.getCurrentGoingForIt().getSquare());
			
		} else {
			
			movePlayer(state.getCurrentDodge().getPlayer(), state.getCurrentGoingForIt().getSquare());
			knockDown(state.getCurrentDodge().getPlayer());
			
		}
		
	}

	private void continueDodge(int result) {
		
		performDodge(state.getCurrentDodge().getPlayer(), 
				state.getCurrentDodge().getSquare(), 
				result, 
				state.getCurrentDodge().getSuccess());
		
	}

	
	
	private void dodgeToMovePlayer(Player player, Square square) {

		// Dodge
		int zones = 0;
		if (isInTackleZone(player)){
			
			zones = numberOfTackleZones(player, square);
			
		}
		
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
				knockDown(player);
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
			if (getPlayerOwner(player) == state.getMovingTeam()){
				
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
		// TODO 
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
			// TODO : gamestage placeballonplayer
			
		}

		Player player = state.getPitch().getPlayerArr()[ballOn.getY()][ballOn.getX()];
		
		// Land on player
		if (player != null){
			
			catchBall();
			
		} else {
			
			scatterBall();
			
		}
		
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
			if (getPlayerOwner(player) == state.getMovingTeam()){
				
				endTurn();
				
			}
			
		}
		
	}

	private void movePlayerToSquare(Player player, Square square) {
		
		state.getPitch().getPlayerArr()[square.getY()][square.getX()] = player;
		
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
		
		knockDown(block.getDefender());
		
		// ball?
		
		attackerDown(block);
		
	}

	private void defenderPushed(Block block) {
		// TODO Auto-generated method stub
		
	}

	private void attackerDown(Block block) {
		
		knockDown( block.getAttacker() );
		
		// ball?
		
		endTurn();
		
	}

	private void endTurn() {
		
		// Any turns left?
		if (state.isHomeTurn()){
			
			if (state.getAwayTurn() < 8){
				
				// Away turn
				state.incAwayTurn();
				state.setHomeTurn(false);
				startNewTurn();
				
			} else {
				
				if (state.getHalf() == 1){
					
					startNextHalf();
					
				} else {
					
					endGame();
					
				}
				
			}
			
		} else {
			
			if (state.getHomeTurn() < 8){
				
				// Away turn
				state.incHomeTurn();
				state.setHomeTurn(true);
				startNewTurn();
				
			} else {
				
				if (state.getHalf() == 1){
					
					startNextHalf();
					
				} else {
					
					endGame();
					
				}
				
			}
			
		}
		
		// TODO: blitz
		
		// TODO: quick snap
		
	}
	
	private PlayerAgent getKickingAgent() {
		PlayerAgent kickingAgent = awayAgent;
		
		if (state.getKickingTeam() == state.getHomeTeam()){
			kickingAgent = homeAgent;
		} 
		
		return kickingAgent;
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
		
	}

	private void knockDown(Player player) {
		
		// Armour roll
		D6 da = new D6();
		D6 db = new D6();
		da.roll();
		db.roll();
		
		int result = da.getResultAsInt() + db.getResultAsInt();
		
		if (result > player.getAV()){
			
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
			
		} else if (player.getPlayerStatus().getTurn() == PlayerTurn.USING_NOW && 
				player.getPlayerStatus().isBlitzing() &&
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
		// TODO Auto-generated method stub
		state.setGameStage(GameStage.KICK_OFF);
		
		rollForKickOff();
		
		state.getPitch().getBall().setOnGround(true);
		
		scatterKickedBall();
		
		// If no other phase started
		if (state.getGameStage() == GameStage.KICK_OFF){
			endTurn();
		}
		
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

	private void pitchInvasion() {
		// TODO Auto-generated method stub
		
	}

	private void throwARock() {
		// TODO Auto-generated method stub
		
	}

	private void blitz() {
		// TODO Auto-generated method stub
		state.setGameStage(GameStage.BLITZ);
	}

	private void quickSnap() {
		// TODO Auto-generated method stub
		state.setGameStage(GameStage.QUICK_SNAP);
	}

	private void brilliantCoaching() {
		// TODO Auto-generated method stub
		
	}

	private void changingWeather() {
		rollForWeather();
	}

	private void cheeringFans() {
		// TODO Auto-generated method stub
		
	}

	private void highKick() {
		// TODO Auto-generated method stub
		state.setGameStage(GameStage.HIGH_KICK);
	}

	private void perfectDefense() {
		// TODO Auto-generated method stub
		state.setGameStage(GameStage.PERFECT_DEFENSE);
	}

	private void riot() {
		// TODO Auto-generated method stub
		
	}

	private void getTheRef() {
		// TODO Auto-generated method stub
		
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