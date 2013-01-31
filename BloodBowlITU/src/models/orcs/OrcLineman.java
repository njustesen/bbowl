package models.orcs;

import models.Player;
import models.Race;
import models.Skill;

public class OrcLineman extends Player{

	public OrcLineman(int number){
		super(Race.ORCS, "Lineman", number);
		this.cost = 50000;
		this.MA = 5;
		this.ST = 3;
		this.AG = 3;
		this.AV = 9;
		this.skills.add(Skill.NONE);
	}
}

