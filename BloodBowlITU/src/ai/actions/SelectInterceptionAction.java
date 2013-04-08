package ai.actions;

import models.Player;

public class SelectInterceptionAction implements Action {

	Player intercepter;

	public SelectInterceptionAction(Player intercepter) {
		super();
		this.intercepter = intercepter;
	}

	public Player getIntercepter() {
		return intercepter;
	}

	public void setIntercepter(Player intercepter) {
		this.intercepter = intercepter;
	}
	
}
