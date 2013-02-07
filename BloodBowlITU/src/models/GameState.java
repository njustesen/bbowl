package models;

import models.dice.DiceRoll;

public class GameState {
	
	Team homeTeam;
	Team awayTeam;
	Pitch pitch;
	int half;
	int awayTurn;
	int homeTurn;
	boolean refAgainstHomeTeam;
	boolean refAgainstAwayTeam;
	Weather weather;
	DiceRoll lastDiceRoll;
	CoinToss coinToss;
	Team kickingTeam;
	Team receivingTeam;
	GameStage gameStage;
	
	public GameState(Team homeTeam, Team awayTeam, Pitch pitch) {
		super();
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.pitch = pitch;
		this.half = 1;
		this.awayTurn = 0;
		this.homeTurn = 0;
		this.refAgainstAwayTeam = true;
		this.refAgainstHomeTeam = true;
		this.gameStage = GameStage.START_UP;
		this.weather = Weather.NICE;
		this.coinToss = new CoinToss();
		
	}

	public Pitch getPitch() {
		return pitch;
	}

	public void setPitch(Pitch pitch) {
		this.pitch = pitch;
	}

	public int getHalf() {
		return half;
	}

	public void setHalf(int half) {
		this.half = half;
	}

	public int getAwayTurn() {
		return awayTurn;
	}

	public void setAwayTurn(int awayTurn) {
		this.awayTurn = awayTurn;
	}

	public int getHomeTurn() {
		return homeTurn;
	}

	public void setHomeTurn(int homeTurn) {
		this.homeTurn = homeTurn;
	}

	public boolean isRefAgainstHomeTeam() {
		return refAgainstHomeTeam;
	}

	public void setRefAgainstHomeTeam(boolean refAgainstHomeTeam) {
		this.refAgainstHomeTeam = refAgainstHomeTeam;
	}

	public boolean isRefAgainstAwayTeam() {
		return refAgainstAwayTeam;
	}

	public void setRefAgainstAwayTeam(boolean refAgainstAwayTeam) {
		this.refAgainstAwayTeam = refAgainstAwayTeam;
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}

	public CoinToss getCoinToss() {
		return coinToss;
	}

	public void setCoinToss(CoinToss coinToss) {
		this.coinToss = coinToss;
	}

	public GameStage getGameStage() {
		return gameStage;
	}

	public void setGameStage(GameStage gameStage) {
		this.gameStage = gameStage;
	}

	public Team getHomeTeam() {
		return homeTeam;
	}

	public Team getAwayTeam() {
		return awayTeam;
	}

	public Team getKickingTeam() {
		return kickingTeam;
	}

	public void setKickingTeam(Team kickingTeam) {
		this.kickingTeam = kickingTeam;
	}

	public Team getReceivingTeam() {
		return receivingTeam;
	}

	public void setReceivingTeam(Team receivingTeam) {
		this.receivingTeam = receivingTeam;
	}

	public DiceRoll getLastDiceRoll() {
		return lastDiceRoll;
	}
	
}
