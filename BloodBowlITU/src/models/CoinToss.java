package models;

import models.dice.D6;

public class CoinToss {
	
	boolean tossed;
	boolean homePicked;
	boolean result;
	boolean homeReceives;
	
	public CoinToss() {
		this.tossed = false;
		this.homePicked = false;
		this.result = false;
		this.homeReceives = false;
	}

	public boolean isTossed() {
		return tossed;
	}

	public void setTossed(boolean tossed) {
		this.tossed = tossed;
	}

	public boolean isHomePicked() {
		return homePicked;
	}

	public void setHomePicked(boolean homePicked) {
		this.homePicked = homePicked;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public boolean isHomeReceives() {
		return homeReceives;
	}

	public void setHomeReceives(boolean homeReceives) {
		this.homeReceives = homeReceives;
	}

	public void Toss() {
		
		// Toss coin
		D6 d = new D6();
		d.roll();

		if(d.getResultAsInt() > 3){
			setResult(true);
		} else {
			setResult(false);
		}
		
		tossed = true;
		
	}
	
}
