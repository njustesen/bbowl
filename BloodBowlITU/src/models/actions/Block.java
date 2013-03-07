package models.actions;

import models.Player;
import models.Team;


public class Block {
	
	Player attacker;
	Player defender;
	Team selectTeam;
	
	public Block(Player attacker, Player defender, Team selectTeam) {
		super();
		this.attacker = attacker;
		this.defender = defender;
		this.selectTeam = selectTeam;
	}

	public Player getAttacker() {
		return attacker;
	}

	public void setAttacker(Player attacker) {
		this.attacker = attacker;
	}

	public Player getDefender() {
		return defender;
	}

	public void setDefender(Player defender) {
		this.defender = defender;
	}
	
	public Team getSelectTeam(){
		return selectTeam;
	}
	
}
