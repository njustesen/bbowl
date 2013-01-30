package models.humans;

import models.Skill;
import models.Standing;
import models.Turn;

public class Catcher extends HumanPlayer{

	public Catcher(){
		this.title = "Catcher";
		this.cost = 70000;
		this.MA = 8;
		this.ST = 2;
		this.AG = 3;
		this.AV = 7;
		this.skills.add(Skill.CATCH);
		this.skills.add(Skill.DODGE);
		this.standing = Standing.UP;
		this.turn = Turn.UNUSED;
	}
}
