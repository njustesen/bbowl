package Models.Orcs;

import java.util.ArrayList;

import Models.BBPlayer;
import Models.Skill;
import Models.Standing;
import Models.Turn;

public class BBOrcPlayer implements BBPlayer{

	protected String race = "orc";
	protected String title;
	protected int cost;
	protected int MA;
	protected int ST;
	protected int AG;
	protected int AV;
	protected ArrayList <Skill> skills = new ArrayList<Skill>();
	protected Turn turn;
	protected Standing standing;
	
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
