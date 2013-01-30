package models.orcs;

import models.Player;
import models.Skill;
import models.Standing;
import models.Turn;

public class Lineman extends Player{

	public Lineman(){
		super("Orcs", "Lineman");
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

