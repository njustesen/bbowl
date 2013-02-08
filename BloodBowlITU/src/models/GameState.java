package models;

import java.util.ArrayList;

import models.dice.BB;
import models.dice.DiceRoll;

public class GameState {
	
	private Team homeTeam;
	private Team awayTeam;
	private Pitch pitch;
	private int half;
	private int awayTurn;
	private int homeTurn;
	private boolean isHomeTurn;
	private boolean refAgainstHomeTeam;
	private boolean refAgainstAwayTeam;
	private Weather weather;
	private DiceRoll currentDiceRoll;
	private CoinToss coinToss;
	private Team kickingTeam;
	private Team receivingTeam;
	private GameStage gameStage;
	private Block currentBlock;
	
	public GameState(Team homeTeam, Team awayTeam, Pitch pitch) {
		super();
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.pitch = pitch;
		this.half = 1;
		this.awayTurn = 0;
		this.homeTurn = 0;
		this.isHomeTurn = false;
		this.refAgainstAwayTeam = true;
		this.refAgainstHomeTeam = true;
		this.gameStage = GameStage.START_UP;
		this.weather = Weather.NICE;
		this.coinToss = new CoinToss();
		this.currentDiceRoll = new DiceRoll();
		BB a = new BB();
		a.roll();
		BB b = new BB();
		b.roll();
		BB c = new BB();
		c.roll();
		this.currentDiceRoll.addDice(a);
		this.currentDiceRoll.addDice(b);
		this.currentDiceRoll.addDice(c);
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

	public DiceRoll getCurrentDiceRoll() {
		return currentDiceRoll;
	}

	public Block getCurrentBlock() {
		return currentBlock;
	}

	public void setCurrentBlock(Block currentBlock) {
		this.currentBlock = currentBlock;
	}

	public void setCurrnetDiceRoll(DiceRoll diceRoll) {
		this.currentDiceRoll = diceRoll;
	}

	public boolean isHomeTurn() {
		return isHomeTurn;
	}

	public void setHomeTurn(boolean isHomeTurn) {
		this.isHomeTurn = isHomeTurn;
	}

	public void incAwayTurn() {
		awayTurn++;
		
	}
	
	public void incHomeTurn() {
		homeTurn++;
		
	}
	
	
	
}
