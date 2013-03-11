package models.actions;

import java.util.ArrayList;

import models.Player;
import models.Square;

public class Push {
	
	Player pushedPlayer;
	ArrayList<Square> pushSquares;
	Push followingPush;
	Square from;
	Square to;
	
	public Push(Player pushedPlayer, Square from, Square to) {
		
		super();
		this.pushedPlayer = pushedPlayer;
		this.pushSquares = new ArrayList<Square>();
		this.followingPush = null;
		this.from = from;
		this.to = to;
		setPushSquares();
		
	}
	
	public void setPushSquares() {
		
		ArrayList<Square> squares = new ArrayList<Square>();
		
		int x = 0;
		int y = 0;
		
		// Get direction
		if (from.getX() > to.getX()){
			x = -1;
		} else if (from.getX() < to.getX()){
			x = 1;
		}
		
		if (from.getY() > to.getY()){
			y = -1;
		} else if (from.getY() < to.getY()){
			y = 1;
		}
		
		// Get squares
		if (x == -1 || x == 1){
			
			if (y == -1 || y == 1){
				
				// Up/down left/right
				squares.add(new Square(to.getX(), to.getY() + y));
				squares.add(new Square(to.getX() + x, to.getY() + y));
				squares.add(new Square(to.getX() + x, to.getY()));
				
			} else if (y == 0){
				
				// Left/right
				squares.add(new Square(to.getX() + x, to.getY() - 1));
				squares.add(new Square(to.getX() + x, to.getY()));
				squares.add(new Square(to.getX() + x, to.getY() + 1));
				
			}
			
		} else {

			// Up/down
			squares.add(new Square(to.getX() - 1, to.getY() + y));
			squares.add(new Square(to.getX(), to.getY() + y));
			squares.add(new Square(to.getX() + 1, to.getY() + y));
			
		}
		
		pushSquares = squares;
		
		
	}public boolean isAmongSquares(Square square) {
		
		for (Square s : pushSquares){
			
			if (s.getX() == square.getX() && 
					s.getY() == square.getY()){
				
				return true;
				
			}
			
		}
		
		return false;
	}

	public Player getPushedPlayer() {
		return pushedPlayer;
	}

	public void setPushedPlayer(Player pushedPlayer) {
		this.pushedPlayer = pushedPlayer;
	}

	public ArrayList<Square> getPushSquares() {
		return pushSquares;
	}

	public void setPushSquares(ArrayList<Square> pushSquares) {
		this.pushSquares = pushSquares;
	}

	public Push getFollowingPush() {
		return followingPush;
	}

	public void setFollowingPush(Push followingPush) {
		this.followingPush = followingPush;
	}

	public Square getFrom() {
		return from;
	}

	public void setFrom(Square from) {
		this.from = from;
	}

	public Square getTo() {
		return to;
	}

	public void setTo(Square to) {
		this.to = to;
	}
	
}
