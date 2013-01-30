package Models;

import java.util.ArrayList;

public class Dugout {
	Team team;
	ArrayList<BBPlayer> reserves;
	ArrayList<BBPlayer> knockedOut;
	ArrayList<BBPlayer> deadAndInjured;
	
	public Dugout(Team team, ArrayList<BBPlayer> reserves,
			ArrayList<BBPlayer> knockedOut, ArrayList<BBPlayer> deadAndInjured) {
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
	
	public void movePlayerToReserves(BBPlayer player) throws BloodBowlException{
		if (team.getPlayers().contains(player)){
			reserves.add(player);
		} else {
			throw new BloodBowlException();
		}
	}

	public ArrayList<BBPlayer> getReserves() {
		return reserves;
	}

	public void setReserves(ArrayList<BBPlayer> reserves) {
		this.reserves = reserves;
	}

	public ArrayList<BBPlayer> getKnockedOut() {
		return knockedOut;
	}

	public void setKnockedOut(ArrayList<BBPlayer> knockedOut) {
		this.knockedOut = knockedOut;
	}

	public ArrayList<BBPlayer> getDeadAndInjured() {
		return deadAndInjured;
	}

	public void setDeadAndInjured(ArrayList<BBPlayer> deadAndInjured) {
		this.deadAndInjured = deadAndInjured;
	}
	
}
