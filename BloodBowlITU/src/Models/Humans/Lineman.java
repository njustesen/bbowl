package models.humans;

import models.Skill;
import models.Standing;
import models.Turn;

public class Lineman extends HumanPlayer{

	public Lineman(){
		this.title = "lineman";
		this.cost = 50000;
		this.MA = 6;
		this.ST = 3;
		this.AG = 3;
		this.AV = 8;
		this.skills.add(Skill.NONE);
		this.standing = Standing.UP;
		this.turn = Turn.UNUSED;
	}
}
