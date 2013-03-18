package models.actions;

import models.Player;

public class Pass {

	private Player passer;
	private Player catcher;
	private int success;
	private boolean accurate;
	
	public Pass(Player passer, Player catcher, int success) {
		super();
		this.passer = passer;
		this.catcher = catcher;
		this.success = success;
		this.accurate = false;
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

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public void setAccurate(boolean b) {
		this.accurate = b;
		
	}

	public boolean isAccurate() {
		return accurate;
	}
	
}
