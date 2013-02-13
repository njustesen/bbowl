package game;

import models.Player;
import models.Square;
import models.dice.DiceRoll;

public class GoingForIt {

	private Player player;
	private Square square;

	public GoingForIt(Player player, Square square) {
		
		this.player = player;
		this.square = square;
		
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Square getSquare() {
		return square;
	}

	public void setSquare(Square square) {
		this.square = square;
	}
}
