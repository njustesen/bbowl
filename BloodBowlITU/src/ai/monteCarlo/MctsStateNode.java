package ai.monteCarlo;

import java.util.List;
import java.util.Random;

import ai.actions.Action;

import models.GameState;

public class MctsStateNode extends MctsAbstractNode {

	private GameState state;
	private List<Action> possibleActions;
	
	public MctsStateNode(GameState state, MctsAbstractNode parent, List<Action> possibleActions) {
		super(parent);
		this.state = state;
		this.possibleActions = possibleActions;
	}

	public List<Action> getPossibleActions() {
		return possibleActions;
	}

	public void setPossibleActions(List<Action> possibleActions) {
		this.possibleActions = possibleActions;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public MctsIntermediateNode breedChild() {
		
		if (possibleActions == null || children.size() == possibleActions.size() || possibleActions.isEmpty())
			return null;
			
		while(true){
			
			int i = new Random().nextInt(possibleActions.size());
			Action action = possibleActions.get(i);
			boolean unique = true;
			
			for(MctsAbstractNode node : children){
				if (((MctsIntermediateNode)node).getAction().equals(action))
					unique = false;
			}
			
			if (unique){
				MctsIntermediateNode child = new MctsIntermediateNode(action, this);
				children.add(child);
				return child;
			}
			
		}
		
	}

}
