package models;

import view.InputManager;

public class Pitch {
	Player[][] playerArr = new Player[17][28];
	Ball ball;
	Dugout homeDogout;
	Dugout awayDogout;
	private InputManager inputManager;
	
	public Pitch(){
	}
	
	public InputManager getInputManager() {
		return inputManager;
	}

	public Player[][] getPlayerArr() {
		return playerArr;
	}
	public void setPlayerArr(Player[][] playerArr) {
		this.playerArr = playerArr;
	}
	public Ball getBall() {
		return ball;
	}
	public void setBall(Ball ball) {
		this.ball = ball;
	}
	public Dugout getHomeDogout() {
		return homeDogout;
	}
	public void setHomeDogout(Dugout homeDogout) {
		this.homeDogout = homeDogout;
	}
	public Dugout getAwayDogout() {
		return awayDogout;
	}
	public void setAwayDogout(Dugout awayDogout) {
		this.awayDogout = awayDogout;
	}
	
}
