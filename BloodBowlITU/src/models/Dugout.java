package models;

import java.util.ArrayList;

public class Dugout {
	protected Team team;
	protected ArrayList<Player> reserves;
	protected ArrayList<Player> knockedOut;
	protected ArrayList<Player> deadAndInjured;
	
	public Dugout(Team team) {
		super();
		this.team = team;
		this.reserves = new ArrayList<Player>();
		this.knockedOut = new ArrayList<Player>();
		this.deadAndInjured = new ArrayList<Player>();
		//putPlayersInReserves();
	}

	public void putPlayersInReserves() {
		
		for(Player p : team.getPlayers()){
			reserves.add(p);
		}
		
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
