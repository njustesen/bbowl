package models.actions;

import models.Player;

public class Foul {

	Player fouler;
	Player target;
	
	public Foul(Player fouler, Player target) {
		super();
		this.fouler = fouler;
		this.target = target;
	}

	public Player getFouler() {
		return fouler;
	}

	public void setFouler(Player fouler) {
		this.fouler = fouler;
	}

	public Player getTarget() {
		return target;
	}

	public void setTarget(Player target) {
		this.target = target;
	}
	
}
