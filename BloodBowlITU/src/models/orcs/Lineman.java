package models.orcs;

import models.Player;
import models.Race;
import models.Skill;

public class Lineman extends Player{

	public Lineman(){
		super(Race.ORCS, "Lineman");
		this.cost = 50000;
		this.MA = 5;
		this.ST = 3;
		this.AG = 3;
		this.AV = 9;
		this.skills.add(Skill.NONE);
	}
}

