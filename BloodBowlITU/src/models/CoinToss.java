package models;

import models.dice.D6;

public class CoinToss {
	
	boolean tossed;
	boolean awayPickedHeads;
	boolean resultHeads;
	boolean homeReceives;
	
	public CoinToss() {
		this.tossed = false;
		this.awayPickedHeads = false;
		this.resultHeads = false;
		this.homeReceives = false;
	}

	public boolean isTossed() {
		return tossed;
	}

	public void setTossed(boolean tossed) {
		this.tossed = tossed;
	}

	public boolean hasAwayPickedHeads() {
		return awayPickedHeads;
	}

	public void setAwayPickedHeads(boolean pick) {
		this.awayPickedHeads = pick;
	}

	public boolean isResultHeads() {
		return resultHeads;
	}

	public void setResultHeads(boolean result) {
		this.resultHeads = result;
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
			setResultHeads(true);
		} else {
			setResultHeads(false);
		}
		
		tossed = true;
		
	}
	
}
