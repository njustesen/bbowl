package ai.actions;

import models.Player;

public class HandOffPlayerAction implements Action {

	private Player ballCarrier;
	private Player catcher;
	
	public HandOffPlayerAction(Player ballCarrier, Player catcher) {
		super();
		this.ballCarrier = ballCarrier;
		this.catcher = catcher;
	}

	public Player getBallCarrier() {
		return ballCarrier;
	}

	public void setBallCarrier(Player ballCarrier) {
		this.ballCarrier = ballCarrier;
	}

	public Player getCatcher() {
		return catcher;
	}

	public void setCatcher(Player catcher) {
		this.catcher = catcher;
	}
	
}
