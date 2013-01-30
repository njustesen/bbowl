package models;

public class TeamStatus {
	int score;
	int rerolls;
	int babes;
	
	public TeamStatus(int score, int rerolls, int babes) {
		super();
		this.score = score;
		this.rerolls = rerolls;
		this.babes = babes;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRerolls() {
		return rerolls;
	}

	public void setRerolls(int rerolls) {
		this.rerolls = rerolls;
	}

	public int getBabes() {
		return babes;
	}

	public void setBabes(int babes) {
		this.babes = babes;
	}
	
}
