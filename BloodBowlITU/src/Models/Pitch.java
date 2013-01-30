package Models;

public class Pitch {
	BBPlayer[][] playerArr = new BBPlayer[17][28];
	Ball ball;
	Dugout homeDogout;
	Dugout awayDogout;
	public BBPlayer[][] getPlayerArr() {
		return playerArr;
	}
	public void setPlayerArr(BBPlayer[][] playerArr) {
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
