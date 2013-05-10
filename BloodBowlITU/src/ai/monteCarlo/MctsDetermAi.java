package ai.monteCarlo;

import game.GameMaster;

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
import models.Team;
import models.Weather;
import ai.AIAgent;
import ai.RandomAI;
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

public class MctsDetermAi extends AIAgent {

	public static final double C = 1f;
	
	private static final int MULTIPLIER = 50;
	private static final int FEW = 1 * MULTIPLIER;
	private static final int MEDIUM = 2 * MULTIPLIER;
	private static final int MANY = 4 * MULTIPLIER;
	private static final int INSANE = 10 * MULTIPLIER;

	private static final int samples = 20;
	
	public MctsDetermAi(boolean homeTeam) {
		super(homeTeam);
	}
	
	protected Action MctsSearch(GameState state, long ms){
		
		MctsStateNode root = new MctsStateNode(state, null, getPossibleActions(state, getTurn(state)));
		
		long startTime = new Date().getTime();
		
		boolean first = true;
		while(new Date().getTime() < startTime + ms || first){
			
			// Tree stats
			printTreeStats(root);
			
			// Selection
			MctsStateNode v1 = select(root);
			
			if (v1 == null)
				return new EndPhaseAction();
			
			// Expansion
			MctsStateNode v2 = expand(v1);
			
			// Simulation
			int result = simulatedResult(v2);
			
			// Backpropagate
			backpropagate(v2, result);
			
			first = false;
			
		}
		
		return bestChild(root).getAction();
	}

	private void printTreeStats(MctsStateNode root) {

		int nodes = countNodes(root);
		
		System.out.println(nodes + " nodes in the tree with " + root.visits + " root visits.");
		
	}

	private int countNodes(MctsAbstractNode node) {
		
		int count = 1;
		
		for(MctsAbstractNode child : node.getChildren()){
			count += countNodes(child);
		}
		
		return count;
	}

	private MctsStateNode select(MctsStateNode node) {
		
		if (node.getChildren().isEmpty())
			return node;
			
		// UTC
		double bestUTC = node.UTC(C);
		MctsAbstractNode best = node;
		
		for(MctsAbstractNode child : node.getChildren()){
			
			double utc = child.UTC(C);
			
			if (utc >= bestUTC && !allChildStatesTerminal(((MctsIntermediateNode)child))){
				bestUTC = utc;
				best = child;
			}
			
		}
		
		// Skip intermediate level
		if(best instanceof MctsIntermediateNode){
			best = best.randomChild();
		}
		
		// If no child was selected
		if (best == node){
			return node;
		}
		
		return select((MctsStateNode) best);
	}
	
	private boolean allChildStatesTerminal(MctsIntermediateNode node) {

		for(MctsAbstractNode child : node.getChildren()){
			
			if (((MctsStateNode)child).getState().getGameStage() != GameStage.GAME_ENDED)
				return false;
			
		}
		
		return true;
		
	}

	private MctsStateNode expand(MctsStateNode node) {
		
		// Breed intermediate node
		MctsIntermediateNode child = node.breedChild();
		if (child == null)
			return node;
		
		// Sample
		List<MctsStateNode> outcomes = new ArrayList<MctsStateNode>();
		for(int i = 0; i < samples; i++){
			GameState state = new GameStateCloner().clone(node.getState());
			GameMaster master = new GameMaster(state, new RandomAI(true), new RandomAI(false), true, false);
			master.setSoundManager(new FakeSoundManager());
			master.performAIAction(child.getAction());
			boolean home = getTurn(state);
			List<Action> actions = getPossibleActions(state, home);
			outcomes.add(new MctsStateNode(state, node, actions));
		}
		
		// If all states are equal shrink down to one child TODO
		
		child.getChildren().addAll(outcomes);
		
		return (MctsStateNode) child.randomChild();
		
	}

	private int simulatedResult(MctsStateNode node) {
		
		int result = 0;
		
		GameState state = new GameStateCloner().clone(node.getState());
		
		GameMaster master = new GameMaster(state, new RandomAI(true), new RandomAI(false), true, false);
		master.setSoundManager(new FakeSoundManager());
		
		while(state.getGameStage() != GameStage.GAME_ENDED){
			master.update();
		}
		
		if (state.getHomeTeam().getTeamStatus().getScore() > state.getAwayTeam().getTeamStatus().getScore()){
			if (myTeam(state) == state.getHomeTeam()){
				result++;
			} else {
				result--;
			}
		} else if (state.getHomeTeam().getTeamStatus().getScore() < state.getAwayTeam().getTeamStatus().getScore()){
			if (myTeam(state) == state.getHomeTeam()){
				result--;
			} else {
				result++;
			}
		}
		
		return result;
		
	}

	private void backpropagate(MctsStateNode node, int result) {
		
		MctsAbstractNode current = node;
		
		while(current != null){
			
			double sum = current.getValue() * current.getVisits() + result;
			current.setVisits(current.getVisits() + 1);
			current.setValue(sum / current.getVisits());
			
			current = current.getParent();
			
		}
		
	}

	private MctsIntermediateNode bestChild(MctsAbstractNode node) {
		
		double bestValue = -100.0;
		MctsIntermediateNode bestNode = null;
		
		for(MctsAbstractNode child : node.getChildren() ){
			
			if (child.getValue() > bestValue){
				
				bestValue = child.getValue();
				bestNode = (MctsIntermediateNode) child;
				
			}
		
		}
		
		return bestNode;
		
	}
	
	private List<Action> getPossibleActions(GameState state, boolean home) {

		if (state.getGameStage() == GameStage.HOME_TURN || 
				state.getGameStage() == GameStage.AWAY_TURN){

			if (state.isAwaitingReroll() && state.getCurrentDiceRoll() != null)
				return decideRerollActions(state, home);
			
			if (state.isAwaitingPush())
				return decidePushActions(state, home);
			
			if (state.isAwaitingFollowUp())
				return decideFollowUpActions(state, home);
			
			if (state.getCurrentPass() != null &&
				state.getCurrentPass().getInterceptionPlayers() != null &&
				state.getCurrentPass().getInterceptionPlayers().size() > 0){
				
				return pickIntercepterActions(state);
				
			}
			
			return turnActions(state, home);
			
		} else if (state.getGameStage() == GameStage.COIN_TOSS && !homeTeam){
			return pickCoinSideActions(state, home);
		} else if (state.getGameStage() == GameStage.PICK_COIN_TOSS_EFFECT){
			
			return pickCoinSideEffectActions(state, home);

		} else if (state.getGameStage() == GameStage.KICKING_SETUP){
			
			return setupActions(state, home);
			
		} else if (state.getGameStage() == GameStage.RECEIVING_SETUP){
			
			return setupActions(state, home);
			
		} else if (state.getGameStage() == GameStage.KICK_PLACEMENT){
			
			return placeKickActions(state, home);
			
		} else if (state.getGameStage() == GameStage.PLACE_BALL_ON_PLAYER){
			
			return placeBallOnPlayerActions(state, home);
			
		} else if (state.getGameStage() == GameStage.BLITZ){

			if (state.isAwaitingReroll() && state.getCurrentDiceRoll() != null)
				return decideRerollActions(state, home);
			
			if (state.isAwaitingPush())
				return decidePushActions(state, home);
			
			if (state.isAwaitingFollowUp())
				return decideFollowUpActions(state, home);
			
			return blitzActions(state, home);
			
		} else if (state.getGameStage() == GameStage.QUICK_SNAP){
			
			return quickSnapActions(state, home);
			
		} else if (state.getGameStage() == GameStage.HIGH_KICK){
			
			return highKickActions(state, home);
			
		} else if (state.getGameStage() == GameStage.PERFECT_DEFENSE){
			
			return perfectDefenseActions(state, home);
			
		}
		
		return null;
		
	}

	private boolean getTurn(GameState state) {

		boolean home = false;
		
		if (state.getGameStage() == GameStage.HOME_TURN){
			home = true;
			if (state.getCurrentPass() != null &&
					state.getCurrentPass().getInterceptionPlayers() != null &&
					state.getCurrentPass().getInterceptionPlayers().size() > 0){
				home = false;
			}
			if (state.isAwaitingReroll() && state.getCurrentDiceRoll() != null && state.getCurrentBlock() != null && state.getCurrentGoingForIt() == null){
				if (!state.isAwaitingFollowUp() && !state.isAwaitingPush()){
					home = (state.getHomeTeam() == state.getCurrentBlock().getSelectTeam());
				}
			}
		} else if (state.getGameStage() == GameStage.AWAY_TURN){
			home = false;
			if (state.getCurrentPass() != null &&
					state.getCurrentPass().getInterceptionPlayers() != null &&
					state.getCurrentPass().getInterceptionPlayers().size() > 0){
				home = true;
			}
			if (state.isAwaitingReroll() && state.getCurrentDiceRoll() != null && state.getCurrentBlock() != null && state.getCurrentGoingForIt() == null){
				if (!state.isAwaitingFollowUp() && !state.isAwaitingPush()){
					home = (state.getHomeTeam() == state.getCurrentBlock().getSelectTeam());
				}
			}
		} else if (state.getGameStage() == GameStage.COIN_TOSS){
			home = false;
		} else if (state.getGameStage() == GameStage.PICK_COIN_TOSS_EFFECT){
			if (state.getCoinToss().hasAwayPickedHeads() == state.getCoinToss().isResultHeads()){
				home = false;
			} else {
				home = true;
			}
		} else if (state.getGameStage() == GameStage.KICKING_SETUP){
			if (state.getKickingTeam() == state.getHomeTeam()){
				home = true;
			} else if (state.getKickingTeam() == state.getAwayTeam()){
				home = false;
			}
		} else if (state.getGameStage() == GameStage.RECEIVING_SETUP){
			if (state.getReceivingTeam() == state.getHomeTeam()){
				home = true;
			} else if (state.getReceivingTeam() == state.getAwayTeam()){
				home = false;
			}
		} else if (state.getGameStage() == GameStage.KICK_PLACEMENT){
			if (state.getKickingTeam() == state.getHomeTeam()){
				home = true;
			} else if (state.getKickingTeam() == state.getAwayTeam()){
				home = false;
			}
		} else if (state.getGameStage() == GameStage.PLACE_BALL_ON_PLAYER){
			if (state.getReceivingTeam() == state.getHomeTeam()){
				home = true;
			} else if (state.getReceivingTeam() == state.getAwayTeam()){
				home = false;
			}
		} else if (state.getGameStage() == GameStage.BLITZ){
			if (state.getKickingTeam() == state.getHomeTeam()){
				home = true;
			} else if (state.getKickingTeam() == state.getAwayTeam()){
				home = false;
			}
		} else if (state.getGameStage() == GameStage.QUICK_SNAP){
			if (state.getReceivingTeam() == state.getHomeTeam()){
				home = true;
			} else if (state.getReceivingTeam() == state.getAwayTeam()){
				home = false;
			}
		} else if (state.getGameStage() == GameStage.HIGH_KICK){
			if (state.getReceivingTeam() == state.getHomeTeam()){
				home = true;
			} else if (state.getReceivingTeam() == state.getAwayTeam()){
				home = false;
			}
		} else if (state.getGameStage() == GameStage.PERFECT_DEFENSE){
			if (state.getKickingTeam() == state.getHomeTeam()){
				home = true;
			} else if (state.getKickingTeam() == state.getAwayTeam()){
				home = false;
			}
		}
		return home;
	}

	@Override
	protected Action turn(GameState state) {
		return MctsSearch(state, INSANE);
	}

	@Override
	protected Action decideReroll(GameState state) {
		return MctsSearch(state, INSANE);
	}

	@Override
	protected Action pickIntercepter(GameState state) {
		return MctsSearch(state, INSANE);
	}

	@Override
	protected Action decidePush(GameState state) {
		return MctsSearch(state, INSANE);
	}

	@Override
	protected Action decideFollowUp(GameState state) {
		return MctsSearch(state, INSANE);
	}

	@Override
	protected Action pickCoinSide(GameState state) {
		return new SelectCoinSideAction(true);
	}

	@Override
	protected Action pickCoinSideEffect(GameState state) {
		return MctsSearch(state, INSANE);
	}

	@Override
	protected Action setup(GameState state) {
		return MctsSearch(state, FEW);
	}

	@Override
	protected Action placeKick(GameState state) {
		return MctsSearch(state, MEDIUM);
	}

	@Override
	protected Action placeBallOnPlayer(GameState state) {
		return MctsSearch(state, INSANE);
	}

	@Override
	protected Action blitz(GameState state) {
		return MctsSearch(state, MANY);
	}

	@Override
	protected Action quickSnap(GameState state) {
		return MctsSearch(state, MANY);
	}

	@Override
	protected Action highKick(GameState state) {
		return MctsSearch(state, INSANE);
	}

	@Override
	protected Action perfectDefense(GameState state) {
		return MctsSearch(state, INSANE);
	}
	
	
	protected ArrayList<Action> decideRerollActions(GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		if ((state.getGameStage() == GameStage.HOME_TURN && home) || 
				(state.getGameStage() == GameStage.AWAY_TURN && !home)){
			
			if (!teamFromState(state, home).getTeamStatus().rerolledThisTurn())
				actions.add(new RerollAction());
			
		}
		
		for(int i = 0; i < state.getCurrentDiceRoll().getDices().size(); i++){
			actions.add(new SelectDieAction(i));
		}
		
		return actions;
		
	}

	protected ArrayList<Action> decidePushActions(GameState state, boolean home) {

		ArrayList<Action> actions = new ArrayList<Action>();
		
		for(int i = 0; i < state.getCurrentBlock().getCurrentPushSquares().size(); i++){
			actions.add(new SelectPushSquareAction(state.getCurrentBlock().getCurrentPushSquares().get(i)));
		}
		
		return actions;
		
	}

	protected ArrayList<Action> decideFollowUpActions(GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		actions.add( new FollowUpAction(true) );
		actions.add( new FollowUpAction(false) );
		
		return actions;
		
	}
	
	protected ArrayList<Action> placeBallOnPlayerActions(GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		for(Player p : state.getPitch().getPlayersOnPitch(teamFromState(state, home)) ){
			
			actions.add(new PlaceBallOnPlayerAction(p));
			
		}
		
		return actions;
		
	}

	

	@SuppressWarnings("incomplete-switch")
	protected ArrayList<Action> blitzActions(GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		// Pick non used player
		ArrayList<Player> usable = new ArrayList<Player>();
		Player active = null;
		for(Player p : state.getPitch().getPlayersOnPitch(teamFromState(state, home))){
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
				
				for(Player player : usable){
					
					actions.add(new SelectPlayerTurnAction(PlayerTurn.MOVE_ACTION, player));
					
					if (!teamFromState(state, home).getTeamStatus().hasBlitzed())
						actions.add(new SelectPlayerTurnAction(PlayerTurn.BLITZ_ACTION, player));
					
				}
				
				return actions;
				
			}
		}
		
		if (active != null){
			switch(active.getPlayerStatus().getTurn()){
			case UNUSED : actions.addAll( startPlayerActions(active, state, true, false, home) ); break;
			case MOVE_ACTION : actions.addAll( continuedMoveActions(active, state, home) ); break;
			case BLITZ_ACTION : actions.addAll( continuedBlitzActions(active, state, home) ); break;
			case USED : break;
			}
		}
		
		actions.add( new EndPhaseAction() );
		
		return actions;
	}

	@SuppressWarnings("incomplete-switch")
	protected ArrayList<Action> quickSnapActions(GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		// Pick non used player
		ArrayList<Player> usable = new ArrayList<Player>();
		Player active = null;
		for(Player p : state.getPitch().getPlayersOnPitch(teamFromState(state, home))){
			if (p.getPlayerStatus().getTurn() != PlayerTurn.USED && 
					p.getPlayerStatus().getStanding() != Standing.STUNNED){
				if (p.getPlayerStatus().getTurn() != PlayerTurn.UNUSED){
					active = p;
					break;
				}
				usable.add(p);
			}
		}
		
		for(Player player : usable){
			
			actions.add(new SelectPlayerTurnAction(PlayerTurn.MOVE_ACTION, player));
			
			if (!teamFromState(state, home).getTeamStatus().hasBlitzed())
				actions.add(new SelectPlayerTurnAction(PlayerTurn.MOVE_ACTION, player));
			
		}
		
		if (active != null){
			switch(active.getPlayerStatus().getTurn()){
			case UNUSED : actions.addAll( startPlayerActions(active, state, false, true, home) ); break;
			case MOVE_ACTION : actions.addAll( continuedMoveActions(active, state, home) ); break;
			case USED : break;
			}
		}
		
		actions.add( new EndPhaseAction() );
		
		return actions;
	}

	protected ArrayList<Action> highKickActions(GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		for(Player p : state.getPitch().getPlayersOnPitch(teamFromState(state, home)) ){
			
			actions.add(new SelectPlayerAction(p));
			
		}
		
		return actions;
		
	}
	
	protected ArrayList<Action> perfectDefenseActions(GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		actions.add(new EndPhaseAction());
		return actions;
		
	}

	protected ArrayList<Action> placeKickActions(GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		for(Square square : state.getPitch().getOpposingSquares(teamFromState(state, home)) ){
			
			actions.add(new PlaceBallAction(square));
			
		}
		
		return actions;
		
	}

	protected ArrayList<Action> setupActions(GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		if (state.getPitch().getDogout(teamFromState(state, home)).getReserves().size() == 0 ||  
				state.getPitch().playersOnPitch(teamFromState(state, home)) == 11){
			
			if (state.getPitch().isSetupLegal(teamFromState(state, home), state.getHalf())){

				actions.add(new EndSetupAction());
				return actions;
				
			}
			
		}
			
		if (state.getPitch().playersOnScrimmage(teamFromState(state, home)) < 3){
			// Scrimmage
			for(Square square : state.getPitch().getScrimmageSquares(teamFromState(state, home))){
				if (state.getPitch().getPlayerAt(square) == null){
					for (Player p : state.getPitch().getDogout(teamFromState(state, home)).getReserves()){
						actions.add(new PlacePlayerAction(p, square));
					}
				}
			}
		} else {
	
			Player player = state.getPitch().getDogout(teamFromState(state, home)).getReserves().get(0);
			
			int top = state.getPitch().playersOnTopWideZones(teamFromState(state, home));
			int bottom = state.getPitch().playersOnBottomWideZones(teamFromState(state, home));
			
			for(Square square : state.getPitch().getTeamSquares(teamFromState(state, home))){
				if (state.getPitch().getPlayerAt(square) == null){
					if (top >= 2 && state.getPitch().isInTopWideZone(square)){
					} else if (bottom >= 2 && state.getPitch().isInBottomWideZone(square)){
					} else {
						actions.add(new PlacePlayerAction(player, square));
					}
				}
			}
			
		}
		
		return actions;
		
	}

	protected ArrayList<Action> pickCoinSideEffectActions(GameState state, boolean home) {
		ArrayList<Action> actions = new ArrayList<Action>();
		actions.add(new SelectCoinTossEffectAction(true));
		return actions;
	}

	protected ArrayList<Action> pickCoinSideActions(GameState state, boolean home) {
		ArrayList<Action> actions = new ArrayList<Action>();
		actions.add(new SelectCoinSideAction(true));
		return actions;
	}

	protected ArrayList<Action> turnActions(GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		// Pick non used player
		ArrayList<Player> usable = new ArrayList<Player>();
		Player active = null;
		for(Player p : state.getPitch().getPlayersOnPitch(teamFromState(state, home))){
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
				
				for(Player player : usable){
					
					actions.add(new SelectPlayerTurnAction(PlayerTurn.MOVE_ACTION, player));
					
					if (player.getPlayerStatus().getStanding() == Standing.UP)
						actions.add(new SelectPlayerTurnAction(PlayerTurn.BLOCK_ACTION, player));
					
					if (!teamFromState(state, home).getTeamStatus().hasBlitzed())
						actions.add(new SelectPlayerTurnAction(PlayerTurn.BLITZ_ACTION, player));
					
					if (!teamFromState(state, home).getTeamStatus().hasFouled())
						actions.add(new SelectPlayerTurnAction(PlayerTurn.FOUL_ACTION, player));
					
					if (!teamFromState(state, home).getTeamStatus().hasHandedOf())
						actions.add(new SelectPlayerTurnAction(PlayerTurn.HAND_OFF_ACTION, player));
					
					if (!teamFromState(state, home).getTeamStatus().hasPassed())
						actions.add(new SelectPlayerTurnAction(PlayerTurn.PASS_ACTION, player));
				}
				
				return actions;
				
			}
		}
		
		if (active != null){
			switch(active.getPlayerStatus().getTurn()){
			case UNUSED : actions.addAll( startPlayerActions(active, state, false, false, home) ); break;
			case MOVE_ACTION : actions.addAll( continuedMoveActions(active, state, home) ); break;
			case BLOCK_ACTION : actions.addAll( continuedBlockActions(active, state, home) ); break;
			case BLITZ_ACTION : actions.addAll( continuedBlitzActions(active, state, home) ); break;
			case PASS_ACTION : actions.addAll( continuedPassActions(active, state, home) ); break;
			case HAND_OFF_ACTION : actions.addAll( continuedHandOffActions(active, state, home) ); break;
			case FOUL_ACTION : actions.addAll( continuedFoulActions(active, state, home) ); break;
			case USED : break;
			}
		}
		
		actions.add( new EndPhaseAction() );
		
		return actions;
		
	}
	
	private Player findActivePlayer(GameState state, ArrayList<Player> players, boolean home) {
		
		int bestSum = 0;
		Player bestPlayer = null;
		GameStateCloner cloner = new GameStateCloner();
		
		for(Player p : players){
			
			int sum = 0;
			
			for(int i = 0; i < MEDIUM; i++){
				Date now = new Date();
				
				GameState as = cloner.clone(state);
				GameMaster gameMaster = new GameMaster(as, new RandomAI(true), new RandomAI(false), true, false);
				gameMaster.setSoundManager(new FakeSoundManager());
				
				Player clonedPlayer = findIdenticalPlayer(p, teamFromState(as, home), state, as);
				Action randAction = new SelectPlayerTurnAction(getRandomPlayerTurn(), clonedPlayer);
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

	private Player findIdenticalPlayer(Player player, Team newTeam, GameState oldState, GameState newState) {

		for(Player p : newTeam.getPlayers()){
			if (p.getNumber() == player.getNumber()){
				return p;
			}
		}
		
		return null;
	}

	private PlayerTurn getRandomPlayerTurn() {

		int i = (int) (Math.random() * 6);
		switch(i){
		case 0: return PlayerTurn.BLITZ_ACTION;
		case 1: return PlayerTurn.BLOCK_ACTION;
		case 2: return PlayerTurn.FOUL_ACTION;
		case 3: return PlayerTurn.HAND_OFF_ACTION;
		case 4: return PlayerTurn.MOVE_ACTION;
		case 5: return PlayerTurn.PASS_ACTION;
		}
		return PlayerTurn.MOVE_ACTION;
	}

	private ArrayList<Action> continuedFoulActions(Player player, GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();

		// Enemies
		Square playerPos = player.getPosition();
			
		if (!teamFromState(state, home).getTeamStatus().hasFouled()){
			for(int y = -1; y <= 1; y++){
				for(int x = -1; x <= 1; x++){
					Square sq = new Square(playerPos.getX() + x, playerPos.getY() + y);
					Player enemy = state.getPitch().getPlayerAt(sq);
					if (enemy != null && !teamFromState(state, home).getPlayers().contains(enemy) && enemy.getPlayerStatus().getStanding() != Standing.UP){
						actions.add(new FoulPlayerAction(player, enemy));
					}
				}
			}
		}
		
		actions.add(new EndPlayerTurnAction(player));
		return continuedMoveActions(player, state, home);
	}

	private ArrayList<Action> continuedHandOffActions(Player player, GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
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
						if (other != null && teamFromState(state, home).getPlayers().contains(other) && other.getPlayerStatus().getStanding() == Standing.UP){
							actions.add(new HandOffPlayerAction(player, other) );
						}
					}
				}
			}
			
			actions.add(new EndPlayerTurnAction(player));
			return actions;
			
		}
		
		actions.addAll( continuedMoveActions(player, state, home) );
		
		actions.add(new EndPlayerTurnAction(player));
		return actions;
		
	}

	private ArrayList<Action> continuedPassActions(Player player, GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		if (state.getPitch().getBall().isUnderControl()){
			
			Player ballCarrier = state.getPitch().getPlayerAt(state.getPitch().getBall().getSquare());
			if (player == ballCarrier){
				
				for(Player p : state.getPitch().getPlayersOnPitch(teamFromState(state, home))){
					if (p != player && isInRange(player, p, state)){
						actions.add(new PassPlayerAction(player, p));
					}
				}
			}
			
		}
		
		actions.addAll( continuedMoveActions(player, state, home) );
		
		actions.add(new EndPlayerTurnAction(player));
		
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

	private ArrayList<Action> continuedBlitzActions(Player player, GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();

		if (!teamFromState(state, home).getTeamStatus().hasBlitzed()){
			for(int y = -1; y <= 1; y++){
				for(int x = -1; x <= 1; x++){
					Square sq = new Square(player.getPosition().getX() + x, player.getPosition().getY() + y);
					Player enemy = state.getPitch().getPlayerAt(sq);
					if (enemy != null && !teamFromState(state, home).getPlayers().contains(enemy) && enemy.getPlayerStatus().getStanding() == Standing.UP){
						actions.add( new BlockPlayerAction(player, enemy) );
					}
				}
			}
		}
		
		actions.addAll(continuedMoveActions(player, state, home));
		
		return actions;
	}

	private ArrayList<Action> continuedBlockActions(Player player, GameState state, boolean home) {
		
		// Enemies
		ArrayList<Action> actions = new ArrayList<Action>();
		
		for(int y = -1; y <= 1; y++){
			for(int x = -1; x <= 1; x++){
				Square sq = new Square(player.getPosition().getX() + x, player.getPosition().getY() + y);
				Player enemy = state.getPitch().getPlayerAt(sq);
				if (enemy != null && !teamFromState(state, home).getPlayers().contains(enemy) && enemy.getPlayerStatus().getStanding() == Standing.UP){
					actions.add(new BlockPlayerAction(player, enemy));
				}
			}
		}
		
		actions.add(new EndPlayerTurnAction(player));
		
		return actions;
		
	}

	private ArrayList<Action> continuedMoveActions(Player player, GameState state, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
	
		if (player.getPlayerStatus().getMovementUsed() == 1 && state.getGameStage() == GameStage.QUICK_SNAP){
			actions.add(new EndPlayerTurnAction(player));
			return actions;
		}
		
		if (player.getPlayerStatus().getMovementUsed() == player.getMA() + 2){
			actions.add(new EndPlayerTurnAction(player));
			return actions;
		}
		
		for(int y = -1; y <= 1; y++){
			for(int x = -1; x <= 1; x++){
				Square sq = new Square(player.getPosition().getX() + x, player.getPosition().getY() + y);
				if (state.getPitch().isOnPitch(sq) && state.getPitch().getPlayerAt(sq) == null){
					actions.add(new MovePlayerAction(player, sq));
				}
			}
		}

		actions.add(new EndPlayerTurnAction(player));
		return actions;
		
	}

	private ArrayList<Action> startPlayerActions(Player player, GameState state, boolean blitzPhase, boolean quickPhase, boolean home) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		actions.add( new SelectPlayerTurnAction(PlayerTurn.MOVE_ACTION, player) );
		
		if (!teamFromState(state, home).getTeamStatus().hasBlitzed() && !quickPhase){
			actions.add( new SelectPlayerTurnAction(PlayerTurn.BLITZ_ACTION, player) );
		}
		
		if (!blitzPhase && !quickPhase){
			if (player.getPlayerStatus().getStanding() == Standing.UP){
				actions.add( new SelectPlayerTurnAction(PlayerTurn.BLOCK_ACTION, player) );
			}
			if (!teamFromState(state, home).getTeamStatus().hasPassed()){
				actions.add( new SelectPlayerTurnAction(PlayerTurn.PASS_ACTION, player) );
			}
			if (!teamFromState(state, home).getTeamStatus().hasHandedOf()){
				actions.add( new SelectPlayerTurnAction(PlayerTurn.HAND_OFF_ACTION, player) );
			}
			if (!teamFromState(state, home).getTeamStatus().hasFouled()){
				actions.add( new SelectPlayerTurnAction(PlayerTurn.FOUL_ACTION, player) );
			}
		}
		
		return actions;
	}
	
	protected ArrayList<Action> pickIntercepterActions(GameState state) {
		
		ArrayList<Action> actions = new ArrayList<Action>();
		
		for(Player p : state.getCurrentPass().getInterceptionPlayers()){
			
			actions.add(new SelectInterceptionAction(p));
			
		}
		
		return actions;
		
	}
	
	private Team teamFromState(GameState state, boolean home) {
		if (home)
			return state.getHomeTeam();
		
		return state.getAwayTeam();
	}

}
