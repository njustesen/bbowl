package view;

import game.GameMaster;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputManager implements KeyListener, MouseListener, MouseMotionListener{
	
	int mouseX;
	int mouseY;
	boolean mouse1Down;
	boolean [] keysDown = new boolean [256];
	boolean [] keysToggled = new boolean [256];
	private GameMaster gameMaster;
	
	//LAYOUT
	private static int screenWidth = 900;
	private static int screenHeight = 602;
	private static int actionButtonWidth = 66;
	private static int actionButtonHeight = 73;
	private static Point2D diceButtonOrigin = new Point2D(736,517);
	private static Point2D pitchOrigin = new Point2D(60,57);
	private static int pitchSquareSize = 30;
	
	public InputManager(GameMaster gameMaster){
		this.gameMaster = gameMaster;
	}
	
	public static Point2D getPitchOrigin() {
		return pitchOrigin;
	}

	public static int getPitchSquareSize() {
		return pitchSquareSize;
	}

	public boolean isKeyDown(String key){
		char c = key.charAt(0);

		return keysDown[((int)c)];
		
	}
	
	public boolean isKeyToggled(String key){
			char c = key.charAt(0);
			if(keysDown[((int)c)])
				System.out.println(key+" is down");
			return keysToggled[((int)c)];	
		}
	
	public Point2D getMousePosition() {
		return new Point2D(mouseX, mouseY);
	}
	
	public int getMouseX(){return mouseX;}
	
	public int getMouseY(){return mouseY;}
	
	@Override
	public void mouseClicked(MouseEvent e) {	
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		if (button == 1){
			mouse1Down = true;
			screenToArray(e.getX(), e.getY());
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		int button = e.getButton();
		if (button == 1){
			mouse1Down = false;
		}		
	}

	public boolean isMouse1Down() {
		return mouse1Down;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("keycode is "+e.getKeyCode());
		keysDown[e.getKeyCode()]=true;		
	}

	@Override
	public void keyReleased(KeyEvent e) {
	//	System.out.println(e.getKeyCode());
		keysDown[e.getKeyCode()]=false;
		
		if(keysToggled[e.getKeyCode()])
			keysToggled[e.getKeyCode()]=false;
		else{
			keysToggled[e.getKeyCode()]=true;
		}		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public Point2D arrayToScreen(int x, int y){ 
		int screenX = (x*30-30)+getPitchOrigin().getX();
		int screenY = (y*30-30)+getPitchOrigin().getY();
		 return new Point2D(screenX, screenY);	 
	}
	
	public Point2D screenToArray(int x, int y){
		if(getMouseX() > pitchOrigin.getX() && getMouseX() < 26*pitchSquareSize + pitchOrigin.getX() &&
				getMouseY() > pitchOrigin.getY() && getMouseY() < 15*pitchSquareSize + pitchOrigin.getY()){
			int pitchX = getMouseX()-pitchOrigin.getX();
			int pitchY = getMouseY()-pitchOrigin.getY();
			int squareX = pitchX/30+1;
			int squareY = pitchY/30+1;
			selectPlayer(squareX, squareY);
			 return new Point2D(squareX, squareY);	 
		}else if(getMouseX() > 0 && getMouseX() < 2*pitchSquareSize + pitchOrigin.getX() &&
				getMouseY() > pitchOrigin.getY() && getMouseY() < 8*pitchSquareSize + pitchOrigin.getY()){
			int reserveX = getMouseX()/30;
			int reserveY = ((getMouseY()-pitchOrigin.getY())/30)*2;
			int reserve = reserveX + reserveY;
			selectHomeReserve(reserve);
		}else if(getMouseX() > getScreenWidth()-2*pitchSquareSize && getMouseX() < getScreenWidth() &&
				getMouseY() > pitchOrigin.getY() && getMouseY() < 8*pitchSquareSize + pitchOrigin.getY()){
			int reserveX = (getMouseX()-getScreenWidth()+2*pitchSquareSize)/30;
			int reserveY = ((getMouseY()-pitchOrigin.getY())/30)*2;
			int reserve = reserveX + reserveY;
			selectAwayReserve(reserve);
		}else 
			System.out.println("mouse not over pitch");
			return null;
	}
	
	private void selectAwayReserve(int reserve) {
		gameMaster.setSelectedPlayer(gameMaster.getState().getPitch().getAwayDogout().getReserves().get(reserve));
		
	}

	private void selectHomeReserve(int reserve) {
		gameMaster.setSelectedPlayer(gameMaster.getState().getPitch().getHomeDogout().getReserves().get(reserve));
		
	}

	private void selectPlayer(int x, int y) {
		
		if (gameMaster.getState().getPitch().getPlayerArr()[y][x] != null){
			
			gameMaster.setSelectedPlayer(gameMaster.getState().getPitch().getPlayerArr()[y][x]);
			
		} else {
			
			gameMaster.squareClicked(x,y);
			
		}
		
		
		
	}

	public Point2D mouseOverArray(){
		int pitchX = getMouseX()-pitchOrigin.getX();
		int pitchY = getMouseY()-pitchOrigin.getY();
		int fatLineY = (getMouseY()-pitchOrigin.getY())/(4*pitchSquareSize);
		int fatLineX = (getMouseY()-pitchOrigin.getY())/(13*pitchSquareSize);
		int squareX = ((pitchX/30)*30)+pitchOrigin.getX()+fatLineX;
		int squareY = ((pitchY/30)*30)+pitchOrigin.getY()+fatLineY; 
		return new Point2D(squareX, squareY);	 
	}
	
	public static int getScreenWidth() {
		return screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	public int getActionButtonWidth() {
		return actionButtonWidth;
	}

	public int getActionButtonHeight() {
		return actionButtonHeight;
	}

	public static Point2D getDiceButtonOrigin() {
		return diceButtonOrigin;
	}

}
