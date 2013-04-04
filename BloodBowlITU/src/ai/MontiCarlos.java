package ai;

import java.util.ArrayList;

import ai.actions.Action;
import ai.actions.EndPhaseAction;
import ai.actions.EndPlayerTurnAction;
import ai.actions.EndSetupAction;
import ai.actions.MovePlayerAction;
import ai.actions.PlaceBallAction;
import ai.actions.PlaceBallOnPlayerAction;
import ai.actions.PlacePlayerAction;
import ai.actions.SelectCoinSideAction;
import ai.actions.SelectCoinTossEffectAction;
import ai.actions.SelectPlayerAction;
import models.GameState;
import models.Player;
import models.PlayerTurn;
import models.Square;

public class MontiCarlos extends AIAgent {
	
	public MontiCarlos(boolean homeTeam) {
		super(homeTeam);
	}

	@Override
	protected Action placeBallOnPlayer(GameState state) {
		
		int rand = (int) (Math.random() * state.getPitch().playersOnPitch(myTeam(state)));
		Player player = state.getPitch().getPlayersOnPitch(myTeam(state)).get(rand);
		
		return new PlaceBallOnPlayerAction(player);
		
	}

	@Override
	protected Action blitz(GameState state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Action quickSnap(GameState state) {
		
		for(Player p : state.getPitch().getPlayersOnPitch(myTeam(state))){
			
			if (p.getPlayerStatus().getTurn() == PlayerTurn.UNUSED){
				
				Square square = state.getPitch().getPlayerPosition(p);
				int i = 1 + (int) (Math.random() * 9);
				switch(i){
				case 1 : square = new Square(square.getX()-1, square.getY()-1); break;
				case 2 : square = new Square(square.getX(), square.getY()-1); break;
				case 3 : square = new Square(square.getX()+1, square.getY()-1); break;
				case 4 : square = new Square(square.getX()-1, square.getY()); break;
				case 5 : return new EndPlayerTurnAction(p);
				case 6 : square = new Square(square.getX()+1, square.getY()); break;
				case 7 : square = new Square(square.getX()-1, square.getY()+1); break;
				case 8 : square = new Square(square.getX(), square.getY()+1); break;
				case 9 : square = new Square(square.getX()+1, square.getY()+1); break;
				}
				
				if (state.getPitch().getPlayerAt(square) == null && state.getPitch().isOnPitch(square)){
					return new MovePlayerAction(p, square);
				} else {
					return new EndPlayerTurnAction(p);
				}
				
			}
			
		}
		
		return new EndPhaseAction();
	}

	@Override
	protected Action highKick(GameState state) {
		
		int rand = (int) (Math.random() * state.getPitch().playersOnPitch(myTeam(state)));
		Player player = state.getPitch().getPlayersOnPitch(myTeam(state)).get(rand);
		
		return new SelectPlayerAction(player);
		
	}
	
	@Override
	protected Action perfectDefense(GameState state) {
		
		return new EndPhaseAction();
		
	}

	@Override
	protected Action placeKick(GameState state) {
		
		Square square = state.getPitch().getRandomOpposingSquare(myTeam(state));
		
		return new PlaceBallAction(square);
		
	}

	@Override
	protected Action setup(GameState state) {
		
		if (state.getPitch().getDogout(myTeam(state)).getReserves().size() == 0 ||  
				state.getPitch().playersOnPitch(myTeam(state)) == 11){
			
			if (state.getPitch().isSetupLegal(myTeam(state), state.getHalf()))
				return new EndSetupAction();
			
		}
		
		return placeRandomPlayer(state);
		
	}

	@Override
	protected Action pickCoinSideEffect(GameState state) {
		return new SelectCoinTossEffectAction(true);
	}

	@Override
	protected Action pickCoinSide(GameState state) {
		return new SelectCoinSideAction(true);
	}

	@Override
	protected Action turn(GameState state) {

		ArrayList<Player> usable = new ArrayList<Player>();
		
		for(Player p : state.getPitch().getPlayersOnPitch(myTeam(state))){
			if (p.getPlayerStatus().getTurn() != PlayerTurn.USED){
				usable.add(p);
			}
		}
		
		if (usable.size() != 0){
		
			int i = (int) (Math.random() * usable.size());
			Player player = usable.get(i);
			
			switch(player.getPlayerStatus().getTurn()){
			case UNUSED : return startPlayerAction(player);
			case MOVE_ACTION : return continueMoveAction(player);
			case BLOCK_ACTION : return continueBlockAction(player);
			case BLITZ_ACTION : return continueBlitzAction(player);
			case PASS_ACTION : return continuePassAction(player);
			case HAND_OFF_ACTION : return continueHandOffAction(player);
			case FOUL_ACTION : return continueFoulAction(player);
			case USED : break;
			}
			
		}
		
		return new EndPhaseAction();
		
	}
	
	private Action continueFoulAction(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	private Action continueHandOffAction(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	private Action continuePassAction(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	private Action continueBlitzAction(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	private Action continueBlockAction(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	private Action continueMoveAction(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	private Action startPlayerAction(Player player) {
		return new EndPlayerTurnAction(player);
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