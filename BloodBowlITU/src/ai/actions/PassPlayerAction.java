package ai.actions;

import models.Player;

public class PassPlayerAction implements Action {

	Player passer;
	Player catcher;
	
	public PassPlayerAction(Player passer, Player catcher) {
		super();
		this.passer = passer;
		this.catcher = catcher;
	}

	public Player getPasser() {
		return passer;
	}

	public void setPasser(Player passer) {
		this.passer = passer;
	}

	public Player getCatcher() {
		return catcher;
	}

	public void setCatcher(Player catcher) {
		this.catcher = catcher;
	}
	
	
	
}
