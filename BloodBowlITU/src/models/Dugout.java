package models;

import java.util.ArrayList;

public class Dugout {
	protected Team team;
	protected ArrayList<Player> reserves;
	protected ArrayList<Player> knockedOut;
	protected ArrayList<Player> deadAndInjured;
	
	public Dugout(Team team, ArrayList<Player> reserves,
			ArrayList<Player> knockedOut, ArrayList<Player> deadAndInjured) {
		super();
		this.team = team;
		this.reserves = reserves;
		this.knockedOut = knockedOut;
		this.deadAndInjured = deadAndInjured;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public ArrayList<Player> getReserves() {
		return reserves;
	}

	public void setReserves(ArrayList<Player> reserves) {
		this.reserves = reserves;
	}

	public ArrayList<Player> getKnockedOut() {
		return knockedOut;
	}

	public void setKnockedOut(ArrayList<Player> knockedOut) {
		this.knockedOut = knockedOut;
	}

	public ArrayList<Player> getDeadAndInjured() {
		return deadAndInjured;
	}

	public void setDeadAndInjured(ArrayList<Player> deadAndInjured) {
		this.deadAndInjured = deadAndInjured;
	}
	
}
