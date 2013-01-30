package models;

import java.util.ArrayList;

public class Dugout {
	Team team;
	ArrayList<IPlayer> reserves;
	ArrayList<IPlayer> knockedOut;
	ArrayList<IPlayer> deadAndInjured;
	
	public Dugout(Team team, ArrayList<IPlayer> reserves,
			ArrayList<IPlayer> knockedOut, ArrayList<IPlayer> deadAndInjured) {
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
	
	public void movePlayerToReserves(IPlayer player) throws BloodBowlException{
		if (team.getPlayers().contains(player)){
			reserves.add(player);
		} else {
			throw new BloodBowlException();
		}
	}

	public ArrayList<IPlayer> getReserves() {
		return reserves;
	}

	public void setReserves(ArrayList<IPlayer> reserves) {
		this.reserves = reserves;
	}

	public ArrayList<IPlayer> getKnockedOut() {
		return knockedOut;
	}

	public void setKnockedOut(ArrayList<IPlayer> knockedOut) {
		this.knockedOut = knockedOut;
	}

	public ArrayList<IPlayer> getDeadAndInjured() {
		return deadAndInjured;
	}

	public void setDeadAndInjured(ArrayList<IPlayer> deadAndInjured) {
		this.deadAndInjured = deadAndInjured;
	}
	
}
