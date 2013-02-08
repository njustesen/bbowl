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
	}
	
	public void update(){
		
		switch(state.getGameStage()){
		case HOME_TURN : homeAgent.takeAction(this, state);	
		case AWAY_TURN : awayAgent.takeAction(this, state);	
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
			state.getPitch().getHomeDogout().getReserves().addAll(
					state.getHomeTeam().getPlayers()
				);

			state.getPitch().getAwayDogout().getReserves().addAll(
					state.getAwayTeam().getPlayers()
				);
			
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
				
			}
			
		} else if (state.getGameStage() == GameStage.RECEIVING_SETUP){
			
			// Receiving team	
			if (state.getPitch().isSetupLegal(state.getReceivingTeam(), state.getHalf())){
					
				state.setGameStage(GameStage.KICK_PLACEMENT);
				
			}
			
		}
		
	}
	
	/**
	 * Place a player on a square.
	 * 
	 * @param player
	 * @param square
	 */
	public void placePlayer(Player player, Square square){
		
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
			
			// Continue block?
			if (state.getCurrentBlock() != null){
				
				continueBlock(face);
				
			}
			
			
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
		*/
	}

	private void resolveKickOff() {
		KickOffResult kickoff = getKickOffResult();
		// TODO
	}

	private KickOffResult getKickOffResult() {
		// TODO Auto-generated method stub
		return null;
	}

	private void resolveKickPlacement() {
		if (event == Event.BALL_PLACEMENT_DONE){
			gameState.setGameStage(GameStage.KICK_OFF);
		}
	}

	private void resolveReceivingSetup() {
		if (event == Event.SETUP_DONE){
			gameState.setGameStage(GameStage.KICK_PLACING);
		}
	}

	private void resolveKickingSetup() {
		if (event == Event.SETUP_DONE){
			gameState.setGameStage(GameStage.RECEIVING_SETUP);
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
		// TODO Auto-generated method stub
		
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
