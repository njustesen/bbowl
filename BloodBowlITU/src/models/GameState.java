package models;

public class GameState {
	
	Team homeTeam;
	Team awayTeam;
	Pitch pitch;
	int half;
	int turnsInHalf;
	int awayTurn;
	int homeTurn;
	boolean refAgainstHomeTeam;
	boolean refAgainstAwayTeam;
	Weather weather;
	CoinToss coinToss;
	GameStage gameStage;
	
}
