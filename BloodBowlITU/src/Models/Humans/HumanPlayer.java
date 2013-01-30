package models.humans;

import java.util.ArrayList;

import models.IPlayer;
import models.Skill;
import models.Standing;
import models.Turn;


public class HumanPlayer implements IPlayer{

	protected String race = "human";
	protected String title;
	protected int cost;
	protected int MA;
	protected int ST;
	protected int AG;
	protected int AV;
	protected ArrayList<Skill> skills = new ArrayList<Skill>();
	protected Standing standing;
	protected Turn turn;
	
	public String getRace(){
		return race;
	}
	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}
	
	@Override
	public int getCost() {
		// TODO Auto-generated method stub
		return cost;
	}


	@Override
	public int getMA() {
		// TODO Auto-generated method stub
		return MA;
	}

	@Override
	public int getST() {
		// TODO Auto-generated method stub
		return ST;
	}

	@Override
	public int getAG() {
		// TODO Auto-generated method stub
		return AG;
	}

	@Override
	public int getAV() {
		// TODO Auto-generated method stub
		return AV;
	}

	@Override
	public ArrayList<Skill> getSkills() {
		// TODO Auto-generated method stub
		return skills;
	}
}
