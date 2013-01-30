package models.orcs;

import models.Skill;
import models.Standing;
import models.Turn;

public class Thrower extends OrcPlayer{

	public Thrower(){
		this.title = "Thrower";
		this.cost = 70000;
		this.MA = 5;
		this.ST = 3;
		this.AG = 3;
		this.AV = 8;
		this.skills.add(Skill.SURE_HANDS);
		this.skills.add(Skill.PASS);
		this.standing = Standing.UP;
		this.turn = Turn.UNUSED;
	}
}
