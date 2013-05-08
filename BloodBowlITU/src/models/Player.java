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
	private int number;
	protected ArrayList<Skill> skills = new ArrayList<Skill>();
	protected PlayerStatus playerStatus;
	private Square position;

	private String teamName;
	private Team team;

	
	public Player(Race race, String title, int number, String teamName) {
		super();
		this.race = race;
		this.title = title;
		this.number = number;
		this.playerStatus = new PlayerStatus(); 
		this.position = null;
		this.teamName = teamName;
		this.team = null;

	}

	public Race getRace() {
		return race;
	}

	public String getTeamName() {
		return teamName;
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

	public PlayerStatus getPlayerStatus() {
		return playerStatus;
	}

	public void setPlayerStatus(PlayerStatus playerStatus) {
		this.playerStatus = playerStatus;
	}

	public int getNumber() {
		return number;
	}

	public Square getPosition() {
		return position;
	}
	
	public void setPosition(Square position){
		
		this.position = position;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

}
