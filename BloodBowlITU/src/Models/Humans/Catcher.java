package Models.Humans;

import Models.Skill;
import Models.Standing;
import Models.Turn;

public class Catcher extends BBHumanPlayer{

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
