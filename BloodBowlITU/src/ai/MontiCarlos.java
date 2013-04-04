package ai;

import ai.actions.Action;
import ai.actions.EndSetupAction;
import ai.actions.PlaceBallAction;
import ai.actions.PlacePlayerAction;
import ai.actions.SelectCoinSideAction;
import ai.actions.SelectCoinTossEffectAction;
import models.GameState;
import models.Player;
import models.Square;

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
		
		Square square = state.getPitch().getRandomOpposingSquare(myTeam(state));
		
		return new PlaceBallAction(square);
		
	}

	protected Action setup(GameState state) {
		
		if (state.getPitch().getDogout(myTeam(state)).getReserves().size() == 0 ||  
				state.getPitch().playersOnPitch(myTeam(state)) == 11){
			
			if (state.getPitch().isSetupLegal(myTeam(state), state.getHalf()))
				return new EndSetupAction();
			
		}
		
		return placeRandomPlayer(state);
		
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
	
	private Action placeRandomPlayer(GameState state) {

		int rand = (int) (Math.random() * state.getPitch().getDogout(myTeam(state)).getReserves().size());
		Player player = state.getPitch().getDogout(myTeam(state)).getReserves().get(rand);
		Square square = new Square(1,1);
		
		if (state.getPitch().playersOnScrimmage(myTeam(state)) < 3){
			
			square = state.getPitch().getRandomFreeScrimmageSquare(myTeam(state));
			
		} else if (state.getPitch().playersOnTopWideZones(myTeam(state)) < 2 && 
				state.getPitch().playersOnBottomWideZones(myTeam(state)) < 2){
			
			square = state.getPitch().getRandomFreeSquare(myTeam(state));
			
		} else {
			
			square = state.getPitch().getRandomFreeMiddleSquare(myTeam(state));
			
		}
		
		return new PlacePlayerAction(player, square);
		
	}


	public boolean isHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(boolean homeTeam) {
		this.homeTeam = homeTeam;
	}
	
}