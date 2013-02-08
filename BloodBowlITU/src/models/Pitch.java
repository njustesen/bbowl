package models;

import java.util.ArrayList;

import view.InputManager;

public class Pitch {
	Player[][] playerArr = new Player[17][28];
	Ball ball;
	Dugout homeDogout;
	Dugout awayDogout;
	private InputManager inputManager;
	
	public Pitch(Team home, Team away){
		ArrayList<Player> reservesHome = home.getPlayers();
		homeDogout = new Dugout(home, reservesHome, new ArrayList<Player>(), new ArrayList<Player>());
		ArrayList<Player> reservesAway = away.getPlayers();
		awayDogout = new Dugout(away, reservesAway, new ArrayList<Player>(), new ArrayList<Player>());
	}
	

	public boolean isSetupLegal(Team team, int half) {
		
		if (!requiredNumberOnPitch(team))
			return false;

		if (!onlyTeamPlayersOnTeamHalf(team, half))
			return false;
			
		if (!legalWideZones(team))
			return false;
			
		if (!legalScrimmage(team))
			return false;
		
		return true;
	}
	
	private boolean requiredNumberOnPitch(Team team) {
		
		int playersOnPitch = 0;

		// Count players
		for(int y = 0; y < playerArr.length; y++){
			for(int x = 0; x < playerArr[0].length; x++){
				
				// If the found player is on the team
				if (playerArr[y][x] != null && team.getPlayers().contains(playerArr[y][x])){
					
					playersOnPitch++;
					
				}
			}
		}
		
		// Test if legal
		if (playersOnPitch == 11){
			return true;
		} if (playersOnPitch > 11){
			return false;
		} else if (playersOnPitch < 11 && getDogout(team).getReserves().size() == 0){
			return true;
		}
		
		return false;
	}

	private boolean legalScrimmage(Team team) {
		
		int playersOnScrimmage = 0;

		// Count players
		for(int y = 5; y <= 11; y++){
			for(int x = 13; x <= 14; x++){
				
				// If the found player is on the team
				if (playerArr[y][x] != null && team.getPlayers().contains(playerArr[y][x])){
					
					playersOnScrimmage++;
					
				}
			}
		}
		
		if (playersOnScrimmage >= 3){
			return true;
		}
		
		return false;
	}

	private boolean legalWideZones(Team team) {
		
		int playersInTopWideZones = 0;
		int playersInBottomWideZones = 0;

		// Count players in top
		for(int y = 1; y <= 4; y++){
			for(int x = 2; x <= 25; x++){
				
				// If the found player is on the team
				if (playerArr[y][x] != null && team.getPlayers().contains(playerArr[y][x])){
					
					playersInTopWideZones++;
					
				}
			}
		}
		
		// Count players in bottom
		for(int y = 12; y <= 15; y++){
			for(int x = 2; x <= 25; x++){
				
				// If the found player is on the team
				if (playerArr[y][x] != null && team.getPlayers().contains(playerArr[y][x])){
					
					playersInBottomWideZones++;
					
				}
			}
		}
		
		if (playersInTopWideZones > 2 || playersInBottomWideZones > 2){
			return false;
		}
		
		return true;
		
	}

	private boolean onlyTeamPlayersOnTeamHalf(Team team, int half) {
		
		boolean playersOnLeftSide = false;
		boolean playersOnRightSide = false;
		
		for(int y = 0; y < 16; y++){
			
			// Count players on left side
			for(int x = 0; x < 13; x++){
				
				// If the found player is on the team
				if (playerArr[y][x] != null && team.getPlayers().contains(playerArr[y][x])){
					
					playersOnLeftSide = true;
					
				}
			}
			
			// Count players on right side
			for(int x = 14; x <= 28; x++){
				
				// If the found player is on the team
				if (playerArr[y][x] != null && team.getPlayers().contains(playerArr[y][x])){
					
					playersOnRightSide = true;
					
				}
			}
			
		}
		
		// Home players on the left side in half 1
		if (playersOnLeftSide && isTeamHome(team) && half%2==1){
			return false;
		}
		// Home players on the right side in half 2
		if (playersOnRightSide && isTeamHome(team) && half%2==0){
			return false;
		}
		// Away players on the right side in half 1
		if (playersOnRightSide && !isTeamHome(team) && half%2==1){
			return false;
		}
		// Away players on the left side in half 2
		if (playersOnLeftSide && !isTeamHome(team) && half%2==0){
			return false;
		}
		
		return true;
	}

	public void removePlayer(Player player) {
		
		// Count players
		for(int y = 0; y < playerArr.length; y++){
			for(int x = 0; x < playerArr[0].length; x++){
				
				// If the found player is on the team
				if (playerArr[y][x] == player){
					
					playerArr[y][x] = null;
					return;
					
				}
			}
		}
	}

	public boolean isOnPitch(Player player) {
		
		for(int y = 0; y < playerArr.length; y++){
			for(int x = 0; x < playerArr[0].length; x++){
				
				if (playerArr[y][x] == player)
					return true;
				
			}
		}
		
		return false;
	}
	
	public Square getPlayerPosition(Player player) {

		for(int y = 0; y < playerArr.length; y++){
			for(int x = 0; x < playerArr[0].length; x++){
				
				if (playerArr[y][x] == player)
					return new Square(x,y);
				
			}
		}
		
		return null;
	}

	private boolean isTeamHome(Team team) {
		if (getDogout(team) == getHomeDogout())
			return true;
		return false;
	}
	
	public InputManager getInputManager() {
		return inputManager;
	}

	public Player[][] getPlayerArr() {
		return playerArr;
	}
	public void setPlayerArr(Player[][] playerArr) {
		this.playerArr = playerArr;
	}
	public Ball getBall() {
		return ball;
	}
	public void setBall(Ball ball) {
		this.ball = ball;
	}
	public Dugout getHomeDogout() {
		return homeDogout;
	}
	public void setHomeDogout(Dugout homeDogout) {
		this.homeDogout = homeDogout;
		
	}
	public Dugout getAwayDogout() {
		return awayDogout;
	}
	public Dugout getDogout(Team team){
		if (homeDogout.getTeam() == team){
			return homeDogout;
		} 
		return awayDogout;
	}
	
	public void setAwayDogout(Dugout awayDogout) {
		this.awayDogout = awayDogout;
	}
	
}


	
