package game;

import models.Player;
import models.Square;

public class PickUp {

	private Player player;
	private Square square;
	private int success;

	public PickUp(Player player, Square square, int success) {
		this.player = player;
		this.square = square;
		this.success = success;
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

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

}
