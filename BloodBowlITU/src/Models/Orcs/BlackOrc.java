package models.orcs;

import models.Skill;
import models.Standing;
import models.Turn;

public class BlackOrc extends OrcPlayer{

	public BlackOrc(){
		this.title = "black orc";
		this.cost = 80000;
		this.MA = 4;
		this.ST = 4;
		this.AG = 2;
		this.AV = 9;
		this.skills.add(Skill.NONE);
		this.standing = Standing.UP;
		this.turn = Turn.UNUSED;
	}
}
