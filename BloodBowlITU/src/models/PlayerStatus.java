package models;

public class PlayerStatus {
	
	private Standing standing;
	private PlayerTurn turn;
	private boolean isBlitzing;
	private int movementUsed;
	private boolean movedToBlock;
	
	public PlayerStatus() {
		super();
		this.standing = Standing.UP;
		this.turn = PlayerTurn.USED;
		this.movementUsed = 0;
		this.isBlitzing = false;
		this.movedToBlock = false;
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

	public boolean isBlitzing() {
		return isBlitzing;
	}

	public void setBlitzing(boolean isBlitzing) {
		this.isBlitzing = isBlitzing;
	}

	public int getMovementUsed() {
		return movementUsed;
	}

	public void setMovementUsed(int movementUsed) {
		this.movementUsed = movementUsed;
	}

	public void moveOneSquare() {
		movementUsed++;
	}

	public boolean hasMovedToBlock() {
		return movedToBlock;
	}

	public void setMovedToBlock(boolean movementUsedToBlock) {
		this.movedToBlock = movementUsedToBlock;
	}

	public void useMovement(int i) {
		movementUsed += i;
		
	}
	
	
	
}
