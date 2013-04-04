package ai.actions;

import models.Player;

public class EndPlayerTurnAction implements Action {

	private Player player;

	public EndPlayerTurnAction(Player player) {
		super();
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
}
