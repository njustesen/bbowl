package Models.Humans;

import Models.Skill;
import Models.Standing;
import Models.Turn;

public class Thrower extends BBHumanPlayer{

	public Thrower(){
		this.title = "Thrower";
		this.cost = 70000;
		this.MA = 6;
		this.ST = 3;
		this.AG = 3;
		this.AV = 8;
		this.skills.add(Skill.SURE_HANDS);
		this.skills.add(Skill.PASS);
		this.standing = Standing.UP;
		this.turn = Turn.UNUSED;
	}
}
