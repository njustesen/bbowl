package ai;

import ai.actions.Action;

public class MCTSNode {
	
	private MCTSNode parent;
	private Action action;
	
	public MCTSNode(MCTSNode parent, Action action) {
		super();
		this.parent = parent;
		this.action = action;
	}

	public MCTSNode getParent() {
		return parent;
	}

	public void setParent(MCTSNode parent) {
		this.parent = parent;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
	public boolean isRoot(){
		if (parent == null)
			return true;
		else
			return false;
	}
	
}
