package game;

import models.CoinToss;
import models.GameStage;
import models.GameState;
import models.Weather;
import models.dice.D6;

public class GameMaster {
	
	protected GameState gameState;
	protected Event event;
	
	public GameMaster(GameState gameState) {
		super();
		this.gameState = gameState;
		this.event = Event.START_GAME;
	}
	
	public void update(){
		
		switch(gameState.getGameStage()){
			case START_UP: resolveStartUp(); break;
			case COIN_TOSS: resolveCoinToss(); break;
		default:
			break;
		}
		
	}

	private void resolveStartUp() {
		if (event == Event.START_GAME){
			rollForWeather();
			gameState.setGameStage(GameStage.COIN_TOSS);
			event = Event.NONE;
		}
	}

	private void resolveCoinToss() {
		
		boolean awayHeads = false;
		
		// Read event
		if (event == Event.AWAY_PICKS_HEADS){
			awayHeads = true;
		} else if (event == Event.AWAY_PICKS_TAILS){
			awayHeads = false;
		} else if (event == Event.PICKS_RECEIVE){
			if (gameState.getCoinToss().isTossed()){
				boolean heads = gameState.getCoinToss().isResult();
				boolean pickedHeads = gameState.getCoinToss().isHomePicked();
				boolean homeWon = (heads == pickedHeads);
				gameState.getCoinToss().setHomeReceives(homeWon);
			}
		} else if (event == Event.PICKS_KICK){
			if (gameState.getCoinToss().isTossed()){
				boolean heads = gameState.getCoinToss().isResult();
				boolean pickedHeads = gameState.getCoinToss().isHomePicked();
				boolean homeWon = (heads == pickedHeads);
				gameState.getCoinToss().setHomeReceives(!homeWon);
			}
		} else {
			return;
		}
		
		// Toss coin
		D6 d = new D6();
		d.roll();
		CoinToss toss = new CoinToss();
		toss.setHomePicked(awayHeads);
		if(d.getResultAsInt() > 3){
			toss.setResult(true);
		} else {
			toss.setResult(false);
		}
		
		event = Event.NONE;
		
	}
	
	private void rollForWeather() {
		D6 da = new D6();
		D6 db = new D6();
		da.roll(); 
		db.roll();
		int roll = da.getResultAsInt() + db.getResultAsInt();
		switch(roll){
			case 2: gameState.setWeather(Weather.SWELTERING_HEAT); break;
			case 3: gameState.setWeather(Weather.VERY_SUNNY); break;
			case 4: gameState.setWeather(Weather.NICE); break;
			case 5: gameState.setWeather(Weather.NICE); break;
			case 6: gameState.setWeather(Weather.NICE); break;
			case 7: gameState.setWeather(Weather.NICE); break;
			case 8: gameState.setWeather(Weather.NICE); break;
			case 9: gameState.setWeather(Weather.NICE); break;
			case 10: gameState.setWeather(Weather.NICE); break;
			case 11: gameState.setWeather(Weather.POURING_RAIN); break;
			case 12: gameState.setWeather(Weather.BLIZZARD); break;
		}
	}
	
	public void setEvent(Event e){
		if (event == Event.NONE){
			event = e;
		}
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	
	
}
