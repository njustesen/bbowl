package models;

public class PlayerStatus {
	
	protected Standing standing;
	protected PlayerTurn turn;
	protected int sprints;
	
	public PlayerStatus() {
		super();
		this.standing = Standing.UP;
		this.turn = PlayerTurn.USED;
		this.sprints = 0;
	}

	public Standing getStanding() {
		return standing;
	}

	public void setStanding(Standing standing) {
		this.standing = standing;
	}

	public PlayerTurn getTurn() {
		return turn;
	}

	public void setTurn(PlayerTurn turn) {
		this.turn = turn;
	}

	public int getSprints() {
		return sprints;
	}

	public void setSprints(int sprints) {
		this.sprints = sprints;
	}
	
}
