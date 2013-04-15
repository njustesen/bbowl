package models;

public class Ball {
	Square square;
	boolean inGame;
	boolean underControl;
	private boolean onGround;
	
	public Ball() {
		
	}
	
	public Ball(Square square, boolean inGame, boolean underControl) {
		this.square = square;
		this.inGame = inGame;
		this.underControl = false;
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
	
	public boolean isOnGround(){
		
		return this.onGround;
	}

	public void setOnGround(boolean b) {
		this.onGround = true;
		
	}
	
}
