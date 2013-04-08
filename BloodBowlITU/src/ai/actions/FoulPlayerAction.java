package ai.actions;

import models.Player;

public class FoulPlayerAction implements Action {

	Player fouler;
	Player enemy;
	
	public FoulPlayerAction(Player fouler, Player enemy) {
		super();
		this.fouler = fouler;
		this.enemy = enemy;
	}

	public Player getFouler() {
		return fouler;
	}

	public void setFouler(Player fouler) {
		this.fouler = fouler;
	}

	public Player getEnemy() {
		return enemy;
	}

	public void setEnemy(Player enemy) {
		this.enemy = enemy;
	}
	
}
