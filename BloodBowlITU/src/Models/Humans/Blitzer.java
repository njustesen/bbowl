package models.humans;

import models.Skill;
import models.Standing;
import models.Turn;

public class Blitzer extends HumanPlayer{

	public Blitzer(){
		this.title = "Blitzer";
		this.cost = 90000;
		this.MA = 7;
		this.ST = 3;
		this.AG = 3;
		this.AV = 8;
		this.skills.add(Skill.BLOCK);
		this.standing = Standing.UP;
		this.turn = Turn.UNUSED;
	}
}
