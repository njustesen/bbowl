package ai;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import javax.swing.JPanel;

import Statistics.StatisticManager;
import ai.actions.Action;
import game.GameMaster;
import models.GameStage;
import models.GameState;
import models.Pitch;
import models.Player;
import models.Square;
import models.Team;

public abstract class AIAgent {
	
	protected boolean homeTeam;
	
	public AIAgent(boolean homeTeam) {
		this.homeTeam = homeTeam;
	}

	public Action takeAction(GameMaster master, GameState state) {
		
		StatisticManager.actions++;
		
		if (state.getGameStage() == GameStage.HOME_TURN || 
				state.getGameStage() == GameStage.AWAY_TURN){

			if (state.isAwaitingReroll() && state.getCurrentDiceRoll() != null)
				return decideReroll(state);
			
			if (state.isAwaitingPush())
				return decidePush(state);
			
			if (state.isAwaitingFollowUp())
				return decideFollowUp(state);
			
			if (state.getCurrentPass() != null &&
				state.getCurrentPass().getInterceptionPlayers() != null &&
				state.getCurrentPass().getInterceptionPlayers().size() > 0){
				
				return pickIntercepter(state);
				
			}
			
			return turn(state);
			
		} else if (state.getGameStage() == GameStage.COIN_TOSS && !homeTeam){
			return pickCoinSide(state);
		} else if (state.getGameStage() == GameStage.PICK_COIN_TOSS_EFFECT){
			
			//if (state.getCoinToss().hasAwayPickedHeads() == state.getCoinToss().isResultHeads() && !homeTeam){
				return pickCoinSideEffect(state);
			//} else if (state.getCoinToss().hasAwayPickedHeads() != state.getCoinToss().isResultHeads() && homeTeam){
				//return pickCoinSideEffect(state);
			//}
			
		} else if (state.getGameStage() == GameStage.KICKING_SETUP){
			
			//if (state.getKickingTeam() == myTeam(state))
				return setup(state);
			
		} else if (state.getGameStage() == GameStage.RECEIVING_SETUP){
			
			//if (state.getReceivingTeam() == myTeam(state))
				return setup(state);
			
		} else if (state.getGameStage() == GameStage.KICK_PLACEMENT){
			
			//if (state.getKickingTeam() == myTeam(state))
				return placeKick(state);
			
		} else if (state.getGameStage() == GameStage.PLACE_BALL_ON_PLAYER){
			
			//if (state.getReceivingTeam() == myTeam(state))
				return placeBallOnPlayer(state);
			
		} else if (state.getGameStage() == GameStage.BLITZ){
			
			//if (state.getKickingTeam() == myTeam(state)){
				
				if (state.isAwaitingReroll() && state.getCurrentDiceRoll() != null)
					return decideReroll(state);
				
				if (state.isAwaitingPush())
					return decidePush(state);
				
				if (state.isAwaitingFollowUp())
					return decideFollowUp(state);
				
				return blitz(state);
			//}
			
		} else if (state.getGameStage() == GameStage.QUICK_SNAP){
			
			//if (state.getReceivingTeam() == myTeam(state))
				return quickSnap(state);
			
		} else if (state.getGameStage() == GameStage.HIGH_KICK){
			
			//if (state.getReceivingTeam() == myTeam(state))
				return highKick(state);
			
		} else if (state.getGameStage() == GameStage.PERFECT_DEFENSE){
			
			//if (state.getKickingTeam() == myTeam(state))
				return perfectDefense(state);
			
		}
		
		return null;
		
	}

	protected Team myTeam(GameState state){
		
		if (homeTeam){
			return state.getHomeTeam();
		}
		return state.getAwayTeam();
		
	}

	protected abstract Action turn(GameState state);
	protected abstract Action decideReroll(GameState state);
	protected abstract Action pickIntercepter(GameState state);
	protected abstract Action decidePush(GameState state);
	protected abstract Action decideFollowUp(GameState state);
	protected abstract Action pickCoinSide(GameState state);
	protected abstract Action pickCoinSideEffect(GameState state);
	protected abstract Action setup(GameState state);
	protected abstract Action placeKick(GameState state);
	protected abstract Action placeBallOnPlayer(GameState state);
	protected abstract Action blitz(GameState state);
	protected abstract Action quickSnap(GameState state);
	protected abstract Action highKick(GameState state);
	protected abstract Action perfectDefense(GameState state);
	
	//inner class
	private class AStar{ 
		
		Pitch pitch;
		Player player;
		int goalX;
		int goalY;
		int pitchWidth;
		int pitchHeight;
		
		private Queue <Mover> pq = new PriorityQueue <Mover>();
		private Set <String> aStarVisited = new HashSet <String>();
		private Mover curMover;
		Square playerPos;
		
		public AStar(Pitch pitch, Player player, int goalX, int goalY, GameState state){
			this.pitch = pitch;
			this.player = player;
			this.goalX = goalX;
			this.goalY = goalY;
			this.pitchWidth = 28;
			this.pitchHeight = 17;
			this.playerPos = state.getPitch().getPlayerPosition(player);
			curMover = new Mover(playerPos.getX(), playerPos.getY(),  goalX, goalY, 0, null, pitch);
		}
		
		protected Mover findPath(Mover mover){
			
			pq.add(mover);
			
			while(!mover.isGoal()){
				for(int i = 1; i <=8; i++){
					if(mover.isMoveLegal(i, playerPos) && !aStarVisited.contains(mover.cloneMover(i).toString())){
						aStarVisited.add(mover.cloneMover(i).toString());	
						pq.add(mover.cloneMover(i));
						
					}
				}
			mover = pq.poll();
			}
			return mover;
		}
		
		//inner inner class
		protected class Mover implements Comparable{
			
			int currentX;
			int currentY;
			
			int goalX;
			int goalY;
			
			int pitchWidth = 28;
			int pitchHeight = 17;
			
			int cost;
			
			Mover parent;
			
			Pitch p;
			
			public Mover(int x, int y, int goalX, int goalY, int cost, Mover parent,Pitch p){
				this.parent = parent;
				this.cost = cost;
				currentX = x;
				currentY = y;
				this.goalX = goalX;
				this.goalY = goalY;
				this.p = p;
			}
			
			public Mover cloneMover(int i){
				
				switch(i){
					//clone up
					case 1: return new Mover(currentX,currentY+1,goalX,goalY,cost+1,this,p);
					//clone upRight
					case 2: return new Mover(currentX+1,currentY+1,goalX,goalY,cost+1,this,p);
					//clone right
					case 3: return new Mover(currentX+1,currentY,goalX,goalY,cost+1,this,p);
					//clone rightDown
					case 4: return new Mover(currentX+1,currentY-1,goalX,goalY,cost+1,this,p); 
					//clone down
					case 5: return new Mover(currentX,currentY-1,goalX,goalY,cost+1,this,p);
					//clone downLeft
					case 6: return new Mover(currentX-1,currentY-1,goalX,goalY,cost+1,this,p);
					//clone left
					case 7: return new Mover(currentX-1,currentY,goalX,goalY,cost+1,this,p);
					//clone upLeft
					case 8: return new Mover(currentX-1,currentY+1,goalX,goalY,cost+1,this,p); 
					default: System.out.println("clone error");return null;
				}
			}
			
			public int isDodgeSquare(int x, int y){
				
				int counter = 0;
				Player[][] pa = p.getPlayerArr();
				
				if(pa[y+1][x] != null){
					counter++;
				} if(pa[y+1][x+1] != null){
					counter++;
				} if(pa[y][x+1] != null){
					counter++;
				} if(pa[y-1][x+1] != null){
					counter++;
				} if(pa[y-1][x] != null){
					counter++;
				} if(pa[y-1][x-1] != null){
					counter++;
				} if(pa[y][x-1] != null){
					counter++;
				} if(pa[y+1][x-1] != null){
					counter++;
				}
				
				return counter;
			}
			
			public boolean isGoal(){
				if(currentX == goalX && currentY == goalY)
					return true;
				else return false;
			}
			
			public boolean isMoveLegal(int i, Square sq){
				switch(i){
				//check up
				case 1: if(!isGridOccupied(sq.getX(), sq.getY()+1))return true; break;
				//check upRight
				case 2: if(!isGridOccupied(sq.getX()+1, sq.getY()+1))return true; break;
				//check Right
				case 3: if(!isGridOccupied(sq.getX()+1, sq.getY()))return true; break;
				//check downRight
				case 4: if(!isGridOccupied(sq.getX()+1, sq.getY()-1))return true; break;
				//check down
				case 5: if(!isGridOccupied(sq.getX(), sq.getY()-1))return true; break;
				//check downLeft
				case 6: if(!isGridOccupied(sq.getX()-1, sq.getY()-1))return true; break;
				//check left
				case 7: if(!isGridOccupied(sq.getX()-1, sq.getY()))return true; break;
				//check upLeft
				case 8: if(!isGridOccupied(sq.getX()-1, sq.getY()+1))return true; break;
				default: System.out.println("illegal direction"); break;
				}
			return false;
			}
			
			protected boolean isGridOccupied(int x, int y){
				
				if(x > 0 && x < pitchWidth-1 && y > 0 && y < pitchHeight -1){
					Player[][] playerArr = p.getPlayerArr();
				
					if(playerArr[y][x] == null){
						return false;
					}
				}
				return true;
			}
			
			public double manhattanTotalValue(){
				return cost+manhattanHeuristicValue();
			}

			public double manhattanHeuristicValue(){
				
				int difX = (Math.abs(currentX-goalX));
				int difY = (Math.abs(currentY-goalY));
				
				return difX+difY;
			}
			
			@Override
			public int compareTo(Object o) {
				if(manhattanTotalValue() < ((Mover) o).manhattanTotalValue())
					return -1;
				else if(manhattanTotalValue() > ((Mover) o).manhattanTotalValue())
					return 1;
				else return 0;
			}
			
			public String toString(){
				return currentX+" "+currentY;
			}
		}
	}
}
