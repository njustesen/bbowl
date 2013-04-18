package ai;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sound.FakeSoundManager;

import models.GameStage;
import models.GameState;
import models.PassRange;
import models.Player;
import models.PlayerTurn;
import models.RangeRuler;
import models.Square;
import models.Standing;
import models.Weather;
import Statistics.StatisticManager;
import ai.actions.Action;
import ai.actions.BlockPlayerAction;
import ai.actions.EndPhaseAction;
import ai.actions.EndPlayerTurnAction;
import ai.actions.EndSetupAction;
import ai.actions.FollowUpAction;
import ai.actions.FoulPlayerAction;
import ai.actions.HandOffPlayerAction;
import ai.actions.MovePlayerAction;
import ai.actions.PassPlayerAction;
import ai.actions.PlaceBallAction;
import ai.actions.PlaceBallOnPlayerAction;
import ai.actions.PlacePlayerAction;
import ai.actions.RerollAction;
import ai.actions.SelectCoinSideAction;
import ai.actions.SelectCoinTossEffectAction;
import ai.actions.SelectDieAction;
import ai.actions.SelectInterceptionAction;
import ai.actions.SelectPlayerAction;
import ai.actions.SelectPlayerTurnAction;
import ai.actions.SelectPushSquareAction;
import ai.util.GameStateCloner;
import game.GameMaster;

public class MCRandom extends AIAgent{
	
	private static long time;

	private static final int simulations = 20;
	public MCRandom(boolean homeTeam) {
		super(homeTeam);
	}
	
	private Action search(List<Action> possibleActions, GameState state){
		
		//MCTSNode root = new MCTSNode(null, null);
		int bestSum = 0;
		Action best = null;
		GameStateCloner cloner = new GameStateCloner();
		
		for(Action a : possibleActions){
			
			int sum = 0;
			
			for(int i = 0; i < simulations; i++){
				Date now = new Date();
				
				GameState as = cloner.clone(state);
				GameMaster gameMaster = new GameMaster(as, new RandomAI(true), new RandomAI(false), true, false);
				gameMaster.setSoundManager(new FakeSoundManager());
				gameMaster.performAIAction(a);
				
				while(as.getGameStage() != GameStage.GAME_ENDED){
					gameMaster.update();
				}
				
				if (state.getHomeTeam().getTeamStatus().getScore() > state.getAwayTeam().getTeamStatus().getScore()){
					sum++;
				} else if (state.getHomeTeam().getTeamStatus().getScore() < state.getAwayTeam().getTeamStatus().getScore()){
					sum--;
				}
				
				Date newNow = new Date();
				
				System.out.println("simtime: " + (newNow.getTime() - now.getTime()));
				
			}
			
			if (best == null || 
					(sum > bestSum && homeTeam) || 
					(sum < bestSum && !homeTeam)){
				bestSum = sum;
				best = a;
			}
		}
		
		return best;
	}	

	@Override
	protected Action decideReroll(GameState state) {
		
		int r = (int) (Math.random() * 2);
		if (r == 0){
			return new RerollAction();
		}
		
		r = (int) (Math.random() * state.getCurrentDiceRoll().getDices().size());
		
		return new SelectDieAction(r);
		
	}

	@Override
	protected Action decidePush(GameState state) {

		int r = (int) (Math.random() * state.getCurrentBlock().getCurrentPushSquares().size());
		
		return new SelectPushSquareAction(state.getCurrentBlock().getCurrentPushSquares().get(r));
		
	}

	@Override
	protected Action decideFollowUp(GameState state) {
		
		double f = Math.random() * 2;
		
		if (f > 1.0)
			return new FollowUpAction(true);
		
		return new FollowUpAction(false);
		
	}
	
	@Override
	protected Action placeBallOnPlayer(GameState state) {
		
		int rand = (int) (Math.random() * state.getPitch().playersOnPitch(myTeam(state)));
		Player player = state.getPitch().getPlayersOnPitch(myTeam(state)).get(rand);
		
		return new PlaceBallOnPlayerAction(player);
		
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	protected Action blitz(GameState state) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		// Pick non used player
		ArrayList<Player> usable = new ArrayList<Player>();
		for(Player p : state.getPitch().getPlayersOnPitch(myTeam(state))){
			if (p.getPlayerStatus().getTurn() != PlayerTurn.USED && 
					p.getPlayerStatus().getStanding() != Standing.STUNNED){
				usable.add(p);
			}
		}
		for(Player p : usable){
			switch(p.getPlayerStatus().getTurn()){
			case UNUSED : actions.addAll( startPlayerActions(p, state, false, false) ); break;
			case MOVE_ACTION : actions.addAll( continuedMoveActions(p, state) ); break;
			case BLITZ_ACTION : actions.addAll( continuedBlitzActions(p, state) ); break;
			case USED : break;
			}
		}
		
		actions.add( new EndPhaseAction() );
		
		return search(actions, state);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	protected Action quickSnap(GameState state) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		// Pick non used player
		ArrayList<Player> usable = new ArrayList<Player>();
		for(Player p : state.getPitch().getPlayersOnPitch(myTeam(state))){
			if (p.getPlayerStatus().getTurn() != PlayerTurn.USED && 
					p.getPlayerStatus().getStanding() != Standing.STUNNED){
				usable.add(p);
			}
		}
		for(Player p : usable){
			switch(p.getPlayerStatus().getTurn()){
			case UNUSED : actions.addAll( startPlayerActions(p, state, false, true) ); break;
			case MOVE_ACTION : actions.addAll( continuedMoveActions(p, state) ); break;
			case USED : break;
			}
		}
		
		actions.add( new EndPhaseAction() );
		
		return search(actions, state);
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
			
			if (state.getPitch().isSetupLegal(myTeam(state), state.getHalf())){
				StatisticManager.timeSpendByRandomAI += System.nanoTime() - time;
				return new EndSetupAction();
			}
			
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
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		// Pick non used player
		ArrayList<Player> usable = new ArrayList<Player>();
		Player active = null;
		for(Player p : state.getPitch().getPlayersOnPitch(myTeam(state))){
			if (p.getPlayerStatus().getTurn() != PlayerTurn.USED && 
					p.getPlayerStatus().getStanding() != Standing.STUNNED){
				if (p.getPlayerStatus().getTurn() != PlayerTurn.UNUSED){
					active = p;
					break;
				}
				usable.add(p);
			}
		}
		
		if (active == null && !usable.isEmpty()){
			if (usable.size() == 1){
				active = usable.get(0);
			} else {
				active = findActivePlayer(state, usable);
			}
		}
		
		if (active != null){
			switch(active.getPlayerStatus().getTurn()){
			case UNUSED : actions.addAll( startPlayerActions(active, state, false, false) ); break;
			case MOVE_ACTION : actions.addAll( continuedMoveActions(active, state) ); break;
			case BLOCK_ACTION : actions.addAll( continuedBlockActions(active, state) ); break;
			case BLITZ_ACTION : actions.addAll( continuedBlitzActions(active, state) ); break;
			case PASS_ACTION : actions.addAll( continuedPassActions(active, state) ); break;
			case HAND_OFF_ACTION : actions.addAll( continuedHandOffActions(active, state) ); break;
			case FOUL_ACTION : actions.addAll( continuedFoulActions(active, state) ); break;
			case USED : break;
			}
		}
		
		actions.add( new EndPhaseAction() );
		
		return search(actions, state);
		
	}
	
	private Player findActivePlayer(GameState state, ArrayList<Player> players) {
		
		int bestSum = 0;
		Player bestPlayer = null;
		GameStateCloner cloner = new GameStateCloner();
		
		for(Player p : players){
			
			int sum = 0;
			
			for(int i = 0; i < simulations; i++){
				Date now = new Date();
				
				GameState as = cloner.clone(state);
				GameMaster gameMaster = new GameMaster(as, new RandomAI(true), new RandomAI(false), true, false);
				gameMaster.setSoundManager(new FakeSoundManager());
				
				Action randAction = new SelectPlayerTurnAction(getRandomPlayerTurn(state), p);
				gameMaster.performAIAction(randAction);
				
				while(as.getGameStage() != GameStage.GAME_ENDED){
					gameMaster.update();
				}
				
				if (state.getHomeTeam().getTeamStatus().getScore() > state.getAwayTeam().getTeamStatus().getScore()){
					sum++;
				} else if (state.getHomeTeam().getTeamStatus().getScore() < state.getAwayTeam().getTeamStatus().getScore()){
					sum--;
				}
				
				Date newNow = new Date();
				
				System.out.println("simtime: " + (newNow.getTime() - now.getTime()));
				
			}
			
			if (bestPlayer == null || 
					(sum > bestSum && homeTeam) || 
					(sum < bestSum && !homeTeam)){
				bestSum = sum;
				bestPlayer = p;
			}
		}
		
		return bestPlayer;
		
	}

	private PlayerTurn getRandomPlayerTurn(GameState state) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<Action> continuedFoulActions(Player player, GameState state) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		actions.add(new EndPlayerTurnAction(player));

		// Enemies
		Square playerPos = player.getPosition();
			
		if (!myTeam(state).getTeamStatus().hasFouled()){
			for(int y = -1; y <= 1; y++){
				for(int x = -1; x <= 1; x++){
					Square sq = new Square(playerPos.getX() + x, playerPos.getY() + y);
					Player enemy = state.getPitch().getPlayerAt(sq);
					if (enemy != null && !myTeam(state).getPlayers().contains(enemy) && enemy.getPlayerStatus().getStanding() != Standing.UP){
						actions.add(new FoulPlayerAction(player, enemy));
					}
				}
			}
		}
		
		return continuedMoveActions(player, state);
	}

	private ArrayList<Action> continuedHandOffActions(Player player, GameState state) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		actions.add(new EndPlayerTurnAction(player));
		
		if (state.getPitch().getBall().isUnderControl()){
			
			Player ballCarrier = state.getPitch().getPlayerAt(state.getPitch().getBall().getSquare());
			if (player == ballCarrier){
				
				// Team members
				Square playerPos = player.getPosition();
				
				for(int y = -1; y <= 1; y++){
					for(int x = -1; x <= 1; x++){
						if (x == 0 && y == 0){
							continue;
						}
						Square sq = new Square(playerPos.getX() + x, playerPos.getY() + y);
						Player other = state.getPitch().getPlayerAt(sq);
						if (other != null && myTeam(state).getPlayers().contains(other) && other.getPlayerStatus().getStanding() == Standing.UP){
							actions.add(new HandOffPlayerAction(player, other) );
						}
					}
				}
			}
			
			return actions;
			
		}
		
		actions.addAll( continuedMoveActions(player, state) );
		
		return actions;
		
	}

	private ArrayList<Action> continuedPassActions(Player player, GameState state) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		actions.add(new EndPlayerTurnAction(player));
		
		if (state.getPitch().getBall().isUnderControl()){
			
			Player ballCarrier = state.getPitch().getPlayerAt(state.getPitch().getBall().getSquare());
			if (player == ballCarrier){
				
				for(Player p : state.getPitch().getPlayersOnPitch(myTeam(state))){
					if (p != player && isInRange(player, p, state)){
						actions.add(new PassPlayerAction(player, p));
					}
				}
			}
			
		}
		
		actions.addAll( continuedMoveActions(player, state) );
		
		return actions;
		
	}

	private boolean isInRange(Player passer, Player catcher, GameState state) {
		Square a = passer.getPosition();
		Square b = catcher.getPosition();
		int x = (a.getX() - b.getX()) * (a.getX() - b.getX());
		int y = (a.getY() - b.getY()) * (a.getY() - b.getY());
		int distance = (int) Math.sqrt(x + y);
		PassRange range = RangeRuler.getPassRange(distance);
		if (state.getWeather() == Weather.BLIZZARD){
			if (range == PassRange.LONG_BOMB || range == PassRange.LONG_PASS){
				return false;
			}
		}
		if (range == PassRange.OUT_OF_RANGE){
			return false;
		}
		return true;
	}

	private ArrayList<Action> continuedBlitzActions(Player player, GameState state) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		actions.add( new EndPlayerTurnAction(player) );

		if (!myTeam(state).getTeamStatus().hasBlitzed()){
			for(int y = -1; y <= 1; y++){
				for(int x = -1; x <= 1; x++){
					Square sq = new Square(player.getPosition().getX() + x, player.getPosition().getY() + y);
					Player enemy = state.getPitch().getPlayerAt(sq);
					if (enemy != null && !myTeam(state).getPlayers().contains(enemy) && enemy.getPlayerStatus().getStanding() == Standing.UP){
						actions.add( new BlockPlayerAction(player, enemy) );
					}
				}
			}
		}
		
		actions.addAll(continuedMoveActions(player, state));
		
		return actions;
	}

	private ArrayList<Action> continuedBlockActions(Player player, GameState state) {
		
		// Enemies
		ArrayList<Action> actions = new ArrayList<Action>();
		actions.add(new EndPlayerTurnAction(player));
		
		for(int y = -1; y <= 1; y++){
			for(int x = -1; x <= 1; x++){
				Square sq = new Square(player.getPosition().getX() + x, player.getPosition().getY() + y);
				Player enemy = state.getPitch().getPlayerAt(sq);
				if (enemy != null && !myTeam(state).getPlayers().contains(enemy) && enemy.getPlayerStatus().getStanding() == Standing.UP){
					actions.add(new BlockPlayerAction(player, enemy));
				}
			}
		}
		
		return actions;
		
	}

	private ArrayList<Action> continuedMoveActions(Player player, GameState state) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		actions.add(new EndPlayerTurnAction(player));
		
		if (player.getPlayerStatus().getMovementUsed() == 1 && state.getGameStage() == GameStage.QUICK_SNAP)
			return actions;
		
		if (player.getPlayerStatus().getMovementUsed() == player.getMA() + 2)
			return actions;
		
		for(int y = -1; y <= 1; y++){
			for(int x = -1; x <= 1; x++){
				Square sq = new Square(player.getPosition().getX() + x, player.getPosition().getY() + y);
				if (state.getPitch().isOnPitch(sq) && state.getPitch().getPlayerAt(sq) == null){
					actions.add(new MovePlayerAction(player, sq));
				}
			}
		}

		return actions;
		
	}

	private ArrayList<Action> startPlayerActions(Player player, GameState state, boolean blitzPhase, boolean quickPhase) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		actions.add( new SelectPlayerTurnAction(PlayerTurn.MOVE_ACTION, player) );
		
		if (!myTeam(state).getTeamStatus().hasBlitzed() && !quickPhase){
			actions.add( new SelectPlayerTurnAction(PlayerTurn.BLITZ_ACTION, player) );
		}
		
		if (!blitzPhase && !quickPhase){
			if (player.getPlayerStatus().getStanding() == Standing.UP){
				actions.add( new SelectPlayerTurnAction(PlayerTurn.BLOCK_ACTION, player) );
			}
			if (!myTeam(state).getTeamStatus().hasPassed()){
				actions.add( new SelectPlayerTurnAction(PlayerTurn.PASS_ACTION, player) );
			}
			if (!myTeam(state).getTeamStatus().hasHandedOf()){
				actions.add( new SelectPlayerTurnAction(PlayerTurn.HAND_OFF_ACTION, player) );
			}
			if (!myTeam(state).getTeamStatus().hasFouled()){
				actions.add( new SelectPlayerTurnAction(PlayerTurn.FOUL_ACTION, player) );
			}
		}
		
		return actions;
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

	@Override
	protected Action pickIntercepter(GameState state) {
		
		int i = (int) (Math.random() * state.getCurrentPass().getInterceptionPlayers().size());
		
		return new SelectInterceptionAction(state.getCurrentPass().getInterceptionPlayers().get(i));
		
	}
	
}
