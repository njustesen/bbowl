package models.humans;

import models.Player;
import models.Race;
import models.Skill;

public class Catcher extends Player{

	public Catcher(){
		super(Race.HUMANS, "Catcher");
		this.cost = 70000;
		this.MA = 8;
		this.ST = 2;
		this.AG = 3;
		this.AV = 7;
		this.skills.add(Skill.CATCH);
		this.skills.add(Skill.DODGE);
	}
}
