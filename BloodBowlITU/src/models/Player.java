package models;

import java.util.ArrayList;

public abstract class Player {

	protected Race race;
	protected String title;
	protected int cost;
	protected int MA;
	protected int ST;
	protected int AG;
	protected int AV;
	protected ArrayList<Skill> skills = new ArrayList<Skill>();
	protected Standing standing;
	protected Turn turn;
	
	public Player(Race race, String title) {
		super();
		this.race = race;
		this.title = title;
	}

	public Race getRace() {
		return race;
	}

	public String getTitle() {
		return title;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getMA() {
		return MA;
	}

	public void setMA(int mA) {
		MA = mA;
	}

	public int getST() {
		return ST;
	}

	public void setST(int sT) {
		ST = sT;
	}

	public int getAG() {
		return AG;
	}

	public void setAG(int aG) {
		AG = aG;
	}

	public int getAV() {
		return AV;
	}

	public void setAV(int aV) {
		AV = aV;
	}

	public ArrayList<Skill> getSkills() {
		return skills;
	}

	public void setSkills(ArrayList<Skill> skills) {
		this.skills = skills;
	}

	public Standing getStanding() {
		return standing;
	}

	public void setStanding(Standing standing) {
		this.standing = standing;
	}

	public Turn getTurn() {
		return turn;
	}

	public void setTurn(Turn turn) {
		this.turn = turn;
	}

}
