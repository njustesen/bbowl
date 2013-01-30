package Models.Humans;

import Models.Skill;
import Models.Standing;
import Models.Turn;

public class Lineman extends BBHumanPlayer{

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
