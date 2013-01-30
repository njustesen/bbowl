package models;

public class Pitch {
	IPlayer[][] playerArr = new IPlayer[17][28];
	Ball ball;
	Dugout homeDogout;
	Dugout awayDogout;
	public IPlayer[][] getPlayerArr() {
		return playerArr;
	}
	public void setPlayerArr(IPlayer[][] playerArr) {
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
