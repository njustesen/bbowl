package models;

public class TeamStatus {
	int score;
	int rerollsDif;
	int babes;
	
	public TeamStatus() {
		super();
		this.score = 0;
		this.rerollsDif = 0;
		this.babes = 0;
	}
	
	public TeamStatus(int score, int rerollsDif, int babes) {
		super();
		this.score = score;
		this.rerollsDif = rerollsDif;
		this.babes = babes;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRerollsDif() {
		return rerollsDif;
	}

	public void setRerolls(int rerollsDif) {
		this.rerollsDif = rerollsDif;
	}

	public int getBabes() {
		return babes;
	}

	public void setBabes(int babes) {
		this.babes = babes;
	}
	
}
