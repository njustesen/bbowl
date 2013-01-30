package models;

import java.util.ArrayList;

public class Team {
	Coach coach;
	ArrayList<Player> players;
	int rerolls;
	int fanFactor;
	int assistantCoaches;
	TeamStatus gameStatus;
	
	public Team(Coach coach, ArrayList<Player> players, int rerolls,
			int fanFactor, int assistantCoaches, TeamStatus gameStatus) {
		super();
		this.coach = coach;
		this.players = players;
		this.rerolls = rerolls;
		this.fanFactor = fanFactor;
		this.assistantCoaches = assistantCoaches;
		this.gameStatus = gameStatus;
	}

	public Coach getCoach() {
		return coach;
	}

	public void setCoach(Coach coach) {
		this.coach = coach;
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

	public TeamStatus getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(TeamStatus gameStatus) {
		this.gameStatus = gameStatus;
	}
	
}
