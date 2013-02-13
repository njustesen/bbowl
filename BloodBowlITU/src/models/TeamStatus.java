package models;

public class TeamStatus {
	int score;
	int rerolls;
	boolean rerolledThisTurn;
	int babes;
	
	public TeamStatus(int rerolls) {
		super();
		this.score = 0;
		this.rerolls = rerolls;
		this.rerolledThisTurn = false;
		this.babes = 0;
	}
	
	public TeamStatus(int score, int rerolls, int babes) {
		super();
		this.score = score;
		this.rerolls = rerolls;
		this.babes = babes;
		this.rerolledThisTurn = false;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRerollsDif() {
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

	public int getRerolls() {
		return rerolls;
	}

	public boolean rerolledThisTurn() {
		return rerolledThisTurn;
	}
	
	public void setRerolledThisTurn(boolean b){
		rerolledThisTurn = b;
	}
	
}
