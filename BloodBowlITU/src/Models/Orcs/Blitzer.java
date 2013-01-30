package Models.Orcs;

import Models.Skill;
import Models.Standing;
import Models.Turn;

public class Blitzer extends BBOrcPlayer{

	public Blitzer(){
		this.title = "Blitzer";
		this.cost = 80000;
		this.MA = 6;
		this.ST = 3;
		this.AG = 3;
		this.AV = 9;
		this.skills.add(Skill.BLOCK);
		this.standing = Standing.UP;
		this.turn = Turn.UNUSED;
	}
}
