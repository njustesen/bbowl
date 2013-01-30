package models.orcs;

import models.Player;
import models.Race;
import models.Skill;

public class Blitzer extends Player{

	public Blitzer(){
		super(Race.ORCS, "Blitzer");
		this.title = "Blitzer";
		this.cost = 80000;
		this.MA = 6;
		this.ST = 3;
		this.AG = 3;
		this.AV = 9;
		this.skills.add(Skill.BLOCK);
	}
}
