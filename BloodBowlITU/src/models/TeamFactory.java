package models;

import java.util.ArrayList;
import java.util.Arrays;

import models.humans.HumanBlitzer;
import models.humans.HumanCatcher;
import models.humans.HumanLineman;
import models.humans.HumanThrower;
import models.orcs.OrcBlackOrc;
import models.orcs.OrcBlitzer;
import models.orcs.OrcLineman;
import models.orcs.OrcThrower;

public class TeamFactory {

	public static Team getHumanTeam() {
		Player p1 = new HumanThrower(1); 
		Player p2 = new HumanBlitzer(2); 
		Player p3 = new HumanBlitzer(3); 
		Player p4 = new HumanBlitzer(4); 
		Player p5 = new HumanBlitzer(5); 
		Player p6 = new HumanCatcher(6); 
		Player p7 = new HumanCatcher(7); 
		Player p8 = new HumanLineman(8); 
		Player p9 = new HumanLineman(9); 
		Player p10 = new HumanLineman(10); 
		Player p11 = new HumanLineman(11); 
		Player p12 = new HumanLineman(12); 
		ArrayList<Player> players = new ArrayList<Player>(
				Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12)
			);
		
		Team team = new Team(players, 4, 5, 0, "Humans");
		
		for (Player p : players){
			p.setTeam(team);
		}
		
		return team;
		
	}

	public static Team getHumanOrc() {
		Player p1 = new OrcThrower(1); 
		Player p2 = new OrcBlackOrc(2); 
		Player p3 = new OrcBlackOrc(3); 
		Player p4 = new OrcBlitzer(4); 
		Player p5 = new OrcBlitzer(5); 
		Player p6 = new OrcLineman(6); 
		Player p7 = new OrcLineman(7); 
		Player p8 = new OrcLineman(8); 
		Player p9 = new OrcLineman(9); 
		Player p10 = new OrcLineman(10); 
		Player p11 = new OrcLineman(11); 
		Player p12 = new OrcLineman(12); 
		ArrayList<Player> players = new ArrayList<Player>(
				Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12)
			);
		
		Team team = new Team(players, 3, 6, 0, "Orcses");
		
		for (Player p : players){
			p.setTeam(team);
		}
		
		return team;
	}
	
	
	
}