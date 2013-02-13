package models;

import game.Catch;
import game.Dodge;
import game.GoingForIt;
import game.PickUp;

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
	private Dodge currentDodge;
	private GoingForIt currentGoingForIt;
	private boolean awaitReroll;
	private PickUp currentPickUp;
	private Catch currentCatch;
	
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
		this.currentDodge = null;
		this.awaitReroll = false;
		this.currentGoingForIt = null;
		this.currentPickUp = null;
		this.currentCatch = null;
		// Only for testing purposes - should be null
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

	public void setCurrentDiceRoll(DiceRoll diceRoll) {
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

	public void setCurrentDodge(Dodge dodge) {
		this.currentDodge = dodge;
		
	}
	
	public Dodge getCurrentDodge(){
		
		return this.currentDodge;
	}

	public void setAwaitReroll(boolean b) {
		this.awaitReroll = b;
		
	}
	
	public boolean isAwaitingReroll(){
		return this.awaitReroll;
	}

	public void setCurrentGoingForIt(GoingForIt goingForIt) {
		this.currentGoingForIt = goingForIt;
	}
	
	public GoingForIt getCurrentGoingForIt(){
		
		return currentGoingForIt;
	}

	public void setCurrentPickUp(PickUp pickUp) {
		this.currentPickUp = pickUp;
		
	}
	
	public PickUp getCurrentPickUp(){
		return currentPickUp;
	}

	public Team getMovingTeam() {
		
		if (isHomeTurn){
			return homeTeam;
		} else {
			return awayTeam;
		}
	}

	public void setCurrentCatch(Catch c) {
		this.currentCatch = c;
		
	}
	
	public Catch getCurrentCatch(){
		
		return currentCatch;
	}
	
}
