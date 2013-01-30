package models.orcs;

import models.Player;
import models.Race;
import models.Skill;

public class Thrower extends Player{

	public Thrower(){
		super(Race.ORCS, "Thrower");
		this.cost = 70000;
		this.MA = 5;
		this.ST = 3;
		this.AG = 3;
		this.AV = 8;
		this.skills.add(Skill.SURE_HANDS);
		this.skills.add(Skill.PASS);
	}
}
