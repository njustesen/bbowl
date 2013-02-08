package game;

import models.Coach;
import models.GameState;

public abstract class PlayerAgent {

	Coach coach;
	
	public PlayerAgent(Coach coach) {
		super();
		this.coach = coach;
	}

	public abstract void takeAction(GameMaster master, GameState state);

	public Coach getCoach() {
		return coach;
	}

	public void setCoach(Coach coach) {
		this.coach = coach;
	}
	
}
