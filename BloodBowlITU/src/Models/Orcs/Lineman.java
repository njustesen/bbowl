package Models.Orcs;

import Models.Skill;
import Models.Standing;
import Models.Turn;

public class Lineman extends BBOrcPlayer{

	public Lineman(){
		this.title = "lineman";
		this.cost = 50000;
		this.MA = 5;
		this.ST = 3;
		this.AG = 3;
		this.AV = 9;
		this.skills.add(Skill.NONE);
		this.standing = Standing.UP;
		this.turn = Turn.UNUSED;
	}
}

