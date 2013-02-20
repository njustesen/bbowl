package models;

public class TeamStatus {
	int score;
	int rerolls;
	boolean rerolledThisTurn;
	int fans;
	int FAME;
	int babes;
	
	public TeamStatus(int rerolls) {
		super();
		this.score = 0;
		this.rerolls = rerolls;
		this.rerolledThisTurn = false;
		this.babes = 0;
		this.fans = 0;
		this.FAME = 0;
	}
	
	public TeamStatus(int score, int rerolls, int babes) {
		super();
		this.score = score;
		this.rerolls = rerolls;
		this.babes = babes;
		this.rerolledThisTurn = false;
		this.fans = 0;
		this.FAME = 0;
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

	public int getFans() {
		return fans;
	}

	public void setFans(int fans) {
		this.fans = fans;
	}

	public int getFAME() {
		return FAME;
	}

	public void setFAME(int fAME) {
		FAME = fAME;
	}

	public boolean isRerolledThisTurn() {
		return rerolledThisTurn;
	}
	
}