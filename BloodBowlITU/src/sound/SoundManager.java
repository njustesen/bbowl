package sound;

public class SoundManager {

	BBSound buttonClick;
	BBSound beginTurn;
	BBSound knockedDown;
	BBSound cheer;
	BBSound diceRoll;
	BBSound whistle;
	
	public SoundManager(){
		buttonClick.setSound("buttonClick.wav");
		beginTurn.setSound("beginTurn.wav");
		knockedDown.setSound("knockedDown.wav");
		cheer.setSound("cheer.wav"); //from http://www.mediacollege.com/downloads/sound-effects/audience/
		diceRoll.setSound("diceRoll.wav");//from http://soundbible.com/181-Roll-Dice-2.html
		whistle.setSound("whistle.wav");
	}
	
	public void playSound(Sound sound){
		
		switch(sound){
			case BUTTONCLICK: buttonClick.start();
			case BEGINTURN: beginTurn.start();
			case KNOCKEDDOWN: knockedDown.start();
			case CHEER: cheer.start();
			case DICEROLL: diceRoll.start();
			case WHISTLE: whistle.start();
		}
	}
}
