package ai;

import ai.actions.Action;
import ai.actions.SelectCoinSideAction;
import ai.actions.SelectCoinTossEffectAction;
import models.GameState;

public class MontiCarlos extends AIAgent {
	
	public MontiCarlos(boolean homeTeam) {
		super(homeTeam);
	}

	protected Action placeBallOnPlayer(GameState state) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Action blitz(GameState state) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Action quickSnap(GameState state) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Action highKick(GameState state) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Action placeKick(GameState state) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Action setup(GameState state) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Action pickCoinSideEffect(GameState state) {
		return new SelectCoinTossEffectAction(true);
	}

	protected Action pickCoinSide(GameState state) {
		return new SelectCoinSideAction(true);
	}

	protected Action turn(GameState state) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(boolean homeTeam) {
		this.homeTeam = homeTeam;
	}
	
}