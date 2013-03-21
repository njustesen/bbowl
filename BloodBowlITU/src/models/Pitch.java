package models;

import java.util.ArrayList;

import models.actions.Pass;

import game.GameLog;
import view.InputManager;

public class Pitch {
	private Player[][] playerArr = new Player[17][28];
	private Ball ball;
	private Dugout homeDogout;
	private Dugout awayDogout;
	private Team homeTeam;
	private Team awayTeam;
	private InputManager inputManager;
	private ArrayList<Player> dungeon;
	
	public Pitch(Team home, Team away){
		homeDogout = new Dugout(home);
		awayDogout = new Dugout(away);
		this.homeTeam = home;
		this.awayTeam = away;
		ball = new Ball();
		this.dungeon = new ArrayList<Player>();
	}

	public boolean isSetupLegal(Team team, int half) {
		
		if (!requiredNumberOnPitch(team)){
			GameLog.push("Illegal number of players on the field!");
			return false;
		}

		if (!onlyTeamPlayersOnTeamHalf(team, half)){
			GameLog.push("Some players are on the wrong side of the field!");
			return false;
		}
			
		if (!legalWideZones(team)){
			GameLog.push("A maximum of two players are allowed in each flank zone.");
			return false;
		}
			
		if (!legalScrimmage(team)){
			GameLog.push("A minimum of three players are required on the line of scrimmage.");
			return false;
		}
		
		return true;
	}
	
	public boolean ballCorreclyPlaced(Team kickingTeam) {
		if (kickingTeam == homeTeam && ball.getSquare().getX() >= 14){
			return true;
		} if (kickingTeam == awayTeam && ball.getSquare().getX() <= 13){
			return true;
		}
		return false;
	}
	
	public int playersOnPitch(Team team) {
		
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
		
		return playersOnPitch;
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
			for(int x = 14; x < 28; x++){
				
				// If the found player is on the team
				if (playerArr[y][x] != null && team.getPlayers().contains(playerArr[y][x])){
					
					playersOnRightSide = true;
					
				}
			}
			
		}
		
		// Home players on the left side
		if (playersOnRightSide && isTeamHome(team)){
			return false;
		}
		// Away players on the right
		if (playersOnLeftSide && !isTeamHome(team)){
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
					
				}
			}
		}
		
	}

	public boolean isOnPitch(Player player) {
		
		for(int y = 1; y < playerArr.length - 1; y++){
			for(int x = 1; x < playerArr[0].length - 1; x++){
				
				if (playerArr[y][x] == player)
					return true;
				
			}
		}
		
		return false;
		
	}
	
	public boolean isOnPitch(Square square) {
		
		if (square.getX() > 0 && square.getX() < 27 && square.getY() > 0 && square.getY() < 16){
			return true;
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
	
	public int teamPlayersOnPitch(Team team) {

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
		
		return playersOnPitch;
		
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

	public boolean isBallInsidePitch() {
		if (ball.getSquare().getX() >=1 &&
				ball.getSquare().getX() < playerArr[0].length-1){
			
			if (ball.getSquare().getY() >=1 &&
					ball.getSquare().getY() < playerArr.length-1){
				
				return true;
				
			}
			
		}
		return false;
	}

	public boolean isBallOnTeamSide(Team team) {
		
		// Ball on left side
		if (ball.getSquare().getX() <= 13){
			
			if (isTeamHome(team)){
				return true;
			} else {
				return false;
			}
			
		} else {
			
			if (isTeamHome(team)){
				return false;
			} else {
				return true;
			}
			
		}
		
	}

	public int playersOnScrimmage(Team team) {

		int playersOnScrimmage = 0;
		int scrimmageLine = 13;
		
		if (!isTeamHome(team)){
			scrimmageLine = 14;
		}

		// Count players
		for(int y = 5; y <= 11; y++){
				
			// If the found player is on the team
			if (playerArr[y][scrimmageLine] != null && team.getPlayers().contains(playerArr[y][scrimmageLine])){
				
				playersOnScrimmage++;
				
			}
		}
		
		return playersOnScrimmage;
		
	}

	public int playersOnTopWideZones(Team team) {
		
		int playersInTopWideZones = 0;

		// Count players in top
		for(int y = 1; y <= 4; y++){
			for(int x = 2; x <= 25; x++){
				
				// If the found player is on the team
				if (playerArr[y][x] != null && team.getPlayers().contains(playerArr[y][x])){
					
					playersInTopWideZones++;
					
				}
			}
		}
		
		return playersInTopWideZones;
		
	}
	
	public int playersOnBottomWideZones(Team team) {
		
		int playersInBottomWideZones = 0;

		for(int y = 12; y <= 15; y++){
			for(int x = 2; x <= 25; x++){
				
				// If the found player is on the team
				if (playerArr[y][x] != null && team.getPlayers().contains(playerArr[y][x])){
					
					playersInBottomWideZones++;
					
				}
			}
		}
		
		return playersInBottomWideZones;
		
	}

	public void placePlayerInTopWideZone(Player p, Team team) {
		
		int start;
		int end;
		
		if (!isTeamHome(team)){
			start = 14;
			end = 25;
		} else {
			end = 13;
			start = 2;
		}

		for(int y = 1; y <= 4; y++){
			for(int x = start; x <= end; x++){
				
				if (playerArr[y][x] == null){
					
					playerArr[y][x] = p;
					
					getDogout(team).getReserves().remove(p);
					return;
				}
			}
		}
		
	}
	
	public void placePlayerOnScrimmage(Player p, Team team) {
		
		int scrimmageLine = 13;
		
		if (!isTeamHome(team)){
			scrimmageLine = 14;
		}

		// Count players
		for(int y = 5; y <= 11; y++){
				
			// If empty place player
			if (playerArr[y][scrimmageLine] == null){
				
				playerArr[y][scrimmageLine] = p;
				
				getDogout(team).getReserves().remove(p);
				return;
			}
		}
		
	}
	
	public void placePlayerInBottomWideZone(Player p, Team team) {

		int start;
		int end;
		
		if (!isTeamHome(team)){
			start = 14;
			end = 25;
		} else {
			end = 13;
			start = 2;
		}
		
		for(int y = 12; y <= 15; y++){
			for(int x = start; x <= end; x++){
				
				if (playerArr[y][x] == null){
					
					playerArr[y][x] = p;
					getDogout(team).getReserves().remove(p);
					return;
					
				}
			}
		}
		
	}

	public void placePlayerInMidfield(Player p, Team team) {
		
		int start;
		int end;
		
		if (!isTeamHome(team)){
			start = 15;
			end = 25;
		} else {
			end = 12;
			start = 2;
		}
		
		for(int y = 5; y <= 11; y++){
			for(int x = start; x <= end; x++){
				
				if (playerArr[y][x] == null){
					
					playerArr[y][x] = p;
					getDogout(team).getReserves().remove(p);
					return;
					
				}
			}
		}
		
	}

	public boolean isBallInEndzone(Team team) {
		
		if (isTeamHome(team) && 
				ball.getSquare().getX() == 1){
			
			return true;
			
		} else if (!isTeamHome(team) && 
				ball.getSquare().getX() == 26){
			
			return true;
			
		}
		
		return false;
	}

	public Player getPlayerAt(Square sq) {
		return playerArr[sq.getY()][sq.getX()];
	}

	public ArrayList<Player> getDungeoun() {
		return dungeon;
	}

	public ArrayList<Player> interceptionPlayers(Pass pass) {
		
		Square from = getPlayerPosition(pass.getPasser());
		Square to = getPlayerPosition(pass.getCatcher());
		
		Team catcherTeam = playerOwner(pass.getCatcher());;
		
		ArrayList<Square> line = line(from, to);
		line = includeManhattanNeighbors(line);
		
		ArrayList<Player> players = new ArrayList<Player>();
		for(Square s : line){
			Player p = getPlayerAt(s);
			if (p != null && catcherTeam == playerOwner(p)){
				players.add(p);
			}
		}
		
		return players;
		
	}
	
	private Team playerOwner(Player player) {
		if (homeTeam.players.contains(player)){
			return homeTeam;
		} else if (awayTeam.players.contains(player)){
			return awayTeam;
		}
		return null;
	}

	private ArrayList<Square> includeManhattanNeighbors(ArrayList<Square> line) {
		
		ArrayList<Square> withNeighbors = new ArrayList<Square>();
		
		for (Square s : line){
			withNeighbors.add(s);
			Square right = new Square(s.getX() + 1, s.getY());
			Square left = new Square(s.getX() - 1, s.getY());
			Square up = new Square(s.getX() + 1, s.getY() - 1);
			Square down = new Square(s.getX() + 1, s.getY() + 1);
			if (isOnPitch(right) && !line.contains(right) && !withNeighbors.contains(right)){
				withNeighbors.add(right);
			}
			if (isOnPitch(left) && !line.contains(left) && !withNeighbors.contains(left)){
				withNeighbors.add(left);
			}
			if (isOnPitch(up) && !line.contains(up) && !withNeighbors.contains(up)){
				withNeighbors.add(up);
			}
			if (isOnPitch(down) && !line.contains(down) && !withNeighbors.contains(down)){
				withNeighbors.add(down);
			}
		}
		
		return withNeighbors;
		
	}

	private ArrayList<Square> line(Square a, Square b){
		
		ArrayList<Square> line = new ArrayList<Square>();
		
		int x0 = a.getX();
		int y0 = a.getY();
		int x1 = b.getX();
		int y1 = b.getY();
		
		int deltaX = x1 - x0;
		int deltaY = y1 - y0;
		double error = 0;
		double deltaError = Math.abs(deltaY / deltaX);
		
		int y = y0;
		for(int x = x0; x <= x1; x++){
			line.add(new Square(x, y));
			error = error + deltaError;
			if (error >= 0.5){
				y++;
				error--;
			}
		}
		
		return line;
		
	}
    
}


	