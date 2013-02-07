package models.dice;

import java.util.ArrayList;

public class DiceRoll {
	
	ArrayList<IDice> dices;

	public DiceRoll() {
		super();
	}

	public ArrayList<IDice> getDices() {
		return dices;
	}

	public void setDices(ArrayList<IDice> dices) {
		this.dices = dices;
	}
	
	public void addDice(IDice dice){
		dices.add(dice);
	}
	
	public ArrayList<DiceFace> getFaces(){
		ArrayList<DiceFace> faces = new ArrayList<DiceFace>();
		for(IDice dice : dices){
			faces.add(dice.getResult());
		}
		return faces;
	}
	
}
