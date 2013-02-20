package models;

import java.util.ArrayList;

public class Team {
	
	protected ArrayList<Player> players;
	protected int rerolls;
	protected int fanFactor;
	protected int assistantCoaches;
	protected int cheerleaders;
	protected TeamStatus teamStatus;
	protected String teamName;
	
	public Team(ArrayList<Player> players, int rerolls,
			int fanFactor, int assistantCoaches, String teamName) {
		super();
		this.players = players;
		this.rerolls = rerolls;
		this.fanFactor = fanFactor;
		this.assistantCoaches = assistantCoaches;
		this.teamStatus = new TeamStatus(4);
		this.teamName = teamName;
		this.cheerleaders = 0;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public int getRerolls() {
		return rerolls;
	}

	public String getTeamName(){
		return teamName;
	}
	public void setRerolls(int rerolls) {
		this.rerolls = rerolls;
	}

	public int getFanFactor() {
		return fanFactor;
	}

	public void setFanFactor(int fanFactor) {
		this.fanFactor = fanFactor;
	}

	public int getAssistantCoaches() {
		return assistantCoaches;
	}

	public void setAssistantCoaches(int assistantCoaches) {
		this.assistantCoaches = assistantCoaches;
	}

	public int getCheerleaders() {
		return cheerleaders;
	}

	public void setCheerleaders(int cheerleaders) {
		this.cheerleaders = cheerleaders;
	}

	public TeamStatus getTeamStatus() {
		return teamStatus;
	}

	public void setTeamStatus(TeamStatus gameStatus) {
		this.teamStatus = gameStatus;
	}

	public void useReroll() {
		this.rerolls--;
		
	}
	
}
