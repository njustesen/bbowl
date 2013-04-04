package ai;

import ai.actions.Action;
import game.GameMaster;
import models.Coach;
import models.GameStage;
import models.GameState;
import models.Team;

public abstract class AIAgent {
	
	boolean homeTeam;
	
	public AIAgent(boolean homeTeam) {
		this.homeTeam = homeTeam;
	}

	public Action takeAction(GameMaster master, GameState state) {
		
		if (state.getGameStage() == GameStage.HOME_TURN && homeTeam){
			return turn(state);
		} else if (state.getGameStage() == GameStage.AWAY_TURN && !homeTeam){
			return turn(state);
		} else if (state.getGameStage() == GameStage.COIN_TOSS && !homeTeam){
			return pickCoinSide(state);
		} else if (state.getGameStage() == GameStage.PICK_COIN_TOSS_EFFECT){
			
			if (state.getCoinToss().hasAwayPickedHeads() == state.getCoinToss().isResultHeads() && !homeTeam){
				return pickCoinSideEffect(state);
			} else if (state.getCoinToss().hasAwayPickedHeads() != state.getCoinToss().isResultHeads() && homeTeam){
				return pickCoinSideEffect(state);
			}
			
		} else if (state.getGameStage() == GameStage.KICKING_SETUP){
			
			if (state.getKickingTeam() == myTeam(state))
				return setup(state);
			
		} else if (state.getGameStage() == GameStage.RECEIVING_SETUP){
			
			if (state.getReceivingTeam() == myTeam(state))
				return setup(state);
			
		} else if (state.getGameStage() == GameStage.KICK_PLACEMENT){
			
			if (state.getKickingTeam() == myTeam(state))
				return placeKick(state);
			
		} else if (state.getGameStage() == GameStage.PLACE_BALL_ON_PLAYER){
			
			if (state.getReceivingTeam() == myTeam(state))
				return placeBallOnPlayer(state);
			
		} else if (state.getGameStage() == GameStage.BLITZ){
			
			if (state.getKickingTeam() == myTeam(state))
				return blitz(state);
			
		} else if (state.getGameStage() == GameStage.QUICK_SNAP){
			
			if (state.getReceivingTeam() == myTeam(state))
				return quickSnap(state);
			
		} else if (state.getGameStage() == GameStage.HIGH_KICK){
			
			if (state.getReceivingTeam() == myTeam(state))
				return highKick(state);
			
		} else if (state.getGameStage() == GameStage.PERFECT_DEFENSE){
			
			if (state.getKickingTeam() == myTeam(state))
				return perfectDefense(state);
			
		}
		
		return null;
		
	}

	protected Team myTeam(GameState state){
		
		if (homeTeam){
			return state.getHomeTeam();
		}
		return state.getAwayTeam();
		
	}

	protected abstract Action turn(GameState state);
	protected abstract Action pickCoinSide(GameState state);
	protected abstract Action pickCoinSideEffect(GameState state);
	protected abstract Action setup(GameState state);
	protected abstract Action placeKick(GameState state);
	protected abstract Action placeBallOnPlayer(GameState state);
	protected abstract Action blitz(GameState state);
	protected abstract Action quickSnap(GameState state);
	protected abstract Action highKick(GameState state);
	protected abstract Action perfectDefense(GameState state);
	
}
