package ai.monteCarlo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.GameStage;

public class MctsAbstractNode {

	protected int visits;
	protected double value;
	protected MctsAbstractNode parent;
	protected List<MctsAbstractNode> children;
	
	public MctsAbstractNode(MctsAbstractNode parent) {
		this.visits = 0;
		this.value = 0.0;
		this.children = new ArrayList<MctsAbstractNode>();
		this.parent = parent;
	}
	
	public MctsAbstractNode getParent() {
		return parent;
	}

	public void setParent(MctsAbstractNode parent) {
		this.parent = parent;
	}
	
	public List<MctsAbstractNode> getChildren() {
		return children;
	}

	public void setChildren(List<MctsAbstractNode> children) {
		this.children = children;
	}

	public int getVisits() {
		return visits;
	}
	public void setVisits(int visits) {
		this.visits = visits;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}

	public MctsAbstractNode randomChild() {

		List<MctsAbstractNode> nonTerminal = new ArrayList<MctsAbstractNode>();
		
		for (MctsAbstractNode child : children){
			
			if (child instanceof MctsStateNode){
				if (((MctsStateNode)child).getState().getGameStage() != GameStage.GAME_ENDED){
					nonTerminal.add(child);
					continue;
				}
			}
			
			nonTerminal.add(child);
		}
		
		int i = new Random().nextInt(nonTerminal.size());
		
		return nonTerminal.get(i);
		
	}
	
	public double UTC(double C) {
		double x = value;
		double n = 1;
		if (parent != null)
			n = parent.getVisits();
		double nj = visits;
		if (nj == 0.0)
			nj = 0.001;
		
		return x + 2 * C * Math.sqrt( (2 * Math.log(n)) / nj );
	}
	
}
