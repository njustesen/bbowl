package Models;

public class Ball {
	Square square;
	boolean inGame;
	boolean underControl;
	
	public Ball(Square square, boolean inGame, boolean underControl) {
		super();
		this.square = square;
		this.inGame = inGame;
		this.underControl = underControl;
	}

	public Square getSquare() {
		return square;
	}

	public void setSquare(Square square) {
		this.square = square;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public boolean isUnderControl() {
		return underControl;
	}

	public void setUnderControl(boolean underControl) {
		this.underControl = underControl;
	}
	
	
	
}

