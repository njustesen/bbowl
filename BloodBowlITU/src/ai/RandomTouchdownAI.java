package ai;

import java.util.ArrayList;

import models.GameState;
import models.Player;
import models.PlayerTurn;
import models.Skill;
import models.Square;
import models.Standing;
import models.dice.DiceFace;
import models.dice.IDice;
import Statistics.StatisticManager;
import ai.actions.Action;
import ai.actions.SelectDieAction;
import ai.actions.SelectPlayerTurnAction;

public class RandomTouchdownAI extends RandomAI{

	public RandomTouchdownAI(boolean homeTeam) {
		super(homeTeam);
		// TODO Auto-generated constructor stub
	}

	
private Action startPlayerAction(Player player, GameState state) {
		
		PlayerTurn action = null;
		Square playerPos = player.getPosition();
		Square ballPos = state.getPitch().getBall().getSquare();
		
		while(true){
			int i = (int) (Math.random() * 6);
			//int i = 3;
			
			if(playerPos.getX() == ballPos.getX() && playerPos.getY() == ballPos.getY()){
				if(myTeam(state) == state.getAwayTeam()){
					if( (player.getMA() - player.getPlayerStatus().getMovementUsed())  >= playerPos.getX() ){
						i = 0;
					}
				}else{
					if( (player.getMA() - player.getPlayerStatus().getMovementUsed()) + playerPos.getX() >= 26 ){
						i = 0;
					}
				}
			}
			
			switch(i){
			case 0: action = PlayerTurn.MOVE_ACTION; break;
			case 1: action = PlayerTurn.BLOCK_ACTION; break;
			case 2: action = PlayerTurn.PASS_ACTION; break;
			case 3: action = PlayerTurn.HAND_OFF_ACTION; break;
			case 4: action = PlayerTurn.BLITZ_ACTION; break;
			case 5: action = PlayerTurn.FOUL_ACTION; break;
			}
			
			if(action == PlayerTurn.BLOCK_ACTION && player.getPlayerStatus().getStanding() != Standing.UP){
				action = PlayerTurn.MOVE_ACTION;
			}
			if (action == PlayerTurn.HAND_OFF_ACTION && myTeam(state).getTeamStatus().hasHandedOf()){
				action = PlayerTurn.MOVE_ACTION;
			}
			if (action == PlayerTurn.PASS_ACTION && myTeam(state).getTeamStatus().hasPassed()){
				action = PlayerTurn.MOVE_ACTION;
			}
			if (action == PlayerTurn.BLITZ_ACTION && myTeam(state).getTeamStatus().hasBlitzed()){
				action = PlayerTurn.MOVE_ACTION;
			}
			if (action == PlayerTurn.FOUL_ACTION && myTeam(state).getTeamStatus().hasFouled()){
				action = PlayerTurn.MOVE_ACTION;
			}
			
			break;
		}
		
		return new SelectPlayerTurnAction(action, player);
	}

	@Override
	protected Action decideReroll(GameState state) {
	
		ArrayList<IDice> dices = state.getCurrentDiceRoll().getDices();
		
		int selected = 0;
		int bestValue = 0;
		
		
		if(state.getCurrentBlock() != null){
			
			Player defender = state.getCurrentBlock().getDefender();
			Player attacker = state.getCurrentBlock().getAttacker();
			
			if(attacker.getTeamName() == myTeam(state).getTeamName()){
				
				for(int i = 0; i < dices.size(); i++){
					//DEFENDER_KNOCKED_DOWN
					if(dices.get(i).getResult() == DiceFace.DEFENDER_KNOCKED_DOWN && bestValue < 5){
						selected = i;
						bestValue = 5;
					//PUSH
					}else if(dices.get(i).getResult() == DiceFace.PUSH && bestValue < 4){
						selected = i;
						bestValue = 4;
					//DEFENDER_STUMBLES
					}else if(dices.get(i).getResult() == DiceFace.DEFENDER_STUMBLES){
						//defender has dodge
						if(defender.getSkills().contains(Skill.DODGE) && bestValue < 4){
							selected = i;
							bestValue = 4;
						//defender does NOT have dodge
						}else if(!defender.getSkills().contains(Skill.DODGE) && bestValue < 5){
							selected = i;
							bestValue = 5;
						}
					//BOTH_DOWN
					}else if(dices.get(i).getResult() == DiceFace.BOTH_DOWN){
						//defender has block
						if(defender.getSkills().contains(Skill.BLOCK)){
							//both have block
							if(attacker.getSkills().contains(Skill.BLOCK) && bestValue < 3){
								selected = i;
								bestValue = 3;
							//only defender has block
							}else if(!attacker.getSkills().contains(Skill.BLOCK) && bestValue < 1){
								selected = i;
								bestValue = 1;
							}
						//defender does NOT have block
						}else{
							//only i have block
							if(attacker.getSkills().contains(Skill.BLOCK) && bestValue < 5){
								selected = i;
								bestValue = 5;
								
							//none of us have block
							}else if(!attacker.getSkills().contains(Skill.BLOCK) && bestValue < 2){
								selected = i;
								bestValue = 2;
							}
						}
					//SKULL
					}else if(dices.get(i).getResult() == DiceFace.SKULL && bestValue < 1){
						selected = i;
						bestValue = 1;
					}
					
				}
				
			}else if(defender.getTeamName() == myTeam(state).getTeamName()){
			
				for(int i = 0; i < dices.size(); i++){
					//DEFENDER_KNOCKED_DOWN
					if(dices.get(i).getResult() == DiceFace.DEFENDER_KNOCKED_DOWN && bestValue < 1){
						selected = i;
						bestValue = 1;
					//PUSH
					}else if(dices.get(i).getResult() == DiceFace.PUSH && bestValue < 2){
						selected = i;
						bestValue = 2;
					//DEFENDER_STUMBLES
					}else if(dices.get(i).getResult() == DiceFace.DEFENDER_STUMBLES){
						//defender has dodge
						if(defender.getSkills().contains(Skill.DODGE) && bestValue < 4){
							selected = i;
							bestValue = 4;
						//defender does NOT have dodge
						}else if(!defender.getSkills().contains(Skill.DODGE) && bestValue < 5){
							selected = i;
							bestValue = 5;
						}
					//BOTH_DOWN
					}else if(dices.get(i).getResult() == DiceFace.BOTH_DOWN){
						//defender has block
						if(defender.getSkills().contains(Skill.BLOCK)){
							//both have block
							if(attacker.getSkills().contains(Skill.BLOCK) && bestValue < 3){
								selected = i;
								bestValue = 3;
							//only defender has block
							}else if(!attacker.getSkills().contains(Skill.BLOCK) && bestValue < 5){
								selected = i;
								bestValue = 5;
							}
						//defender does NOT have block
						}else{
							//only attacker have block
							if(attacker.getSkills().contains(Skill.BLOCK) && bestValue < 1){
								selected = i;
								bestValue = 1;
								
							//none of us have block
							}else if(!attacker.getSkills().contains(Skill.BLOCK) && bestValue < 4){
								selected = i;
								bestValue = 4;
							}
						}
					//SKULL
					}else if(dices.get(i).getResult() == DiceFace.SKULL && bestValue < 5){
						selected = i;
						bestValue = 5;
					}
					
				}
			}
		}
		
		return new SelectDieAction(selected);	
	}
}
