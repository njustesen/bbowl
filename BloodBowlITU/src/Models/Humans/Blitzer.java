package Models.Humans;

import Models.Skill;
import Models.Standing;
import Models.Turn;

public class Blitzer extends BBHumanPlayer{

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
