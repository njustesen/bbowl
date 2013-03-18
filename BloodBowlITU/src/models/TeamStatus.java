package models;

public class TeamStatus {
	private int score;
	private int rerolls;
	private boolean rerolledThisTurn;
	private int fans;
	private int FAME;
	private int babes;
	private boolean hasBlitzed;
	private boolean hasFouled;
	private boolean hasPassed;
	private boolean hasHandedOf;
	
	public TeamStatus(int rerolls) {
		super();
		this.score = 0;
		this.rerolls = rerolls;
		this.rerolledThisTurn = false;
		this.babes = 0;
		this.fans = 0;
		this.FAME = 0;
		this.hasBlitzed = false;
		this.hasFouled = false;
		this.hasPassed = false;
		this.hasHandedOf = false;
	}
	
	public TeamStatus(int score, int rerolls, int babes) {
		super();
		this.score = score;
		this.rerolls = rerolls;
		this.babes = babes;
		this.rerolledThisTurn = false;
		this.fans = 0;
		this.FAME = 0;
		this.hasBlitzed = false;
		this.hasFouled = false;
		this.hasPassed = false;
		this.hasHandedOf = false;
	}
	
	public void reset() {
		this.rerolledThisTurn = false;
		this.hasBlitzed = false;
		this.hasFouled = false;
		this.hasPassed = false;
		this.hasHandedOf = false;
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

	public boolean hasBlitzed() {
		return hasBlitzed;
	}
	
	public void setHasBlitzed(boolean b){
		hasBlitzed = b;
	}

	public void incScore() {
		score++;
		
	}

	public boolean hasFouled() {
		return hasFouled;
	}
	
	public void setHasFouled(boolean b){
		hasFouled = b;
	}

	public boolean hasPassed() {
		return hasPassed;
	}

	public void setHasPassed(boolean hasPassed) {
		this.hasPassed = hasPassed;
	}

	public boolean hasHandedOf() {
		return hasHandedOf;
	}

	public void setHasHandedOf(boolean hasHandedOf) {
		this.hasHandedOf = hasHandedOf;
	}
	
}