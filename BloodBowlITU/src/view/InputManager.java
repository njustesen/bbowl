package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputManager implements KeyListener, MouseListener, MouseMotionListener{
	
	int mouseX;
	int mouseY;
	boolean mouse1Down;
	private static Point2D pitchOrigin = new Point2D(2,44);
	private static int pitchSquareSize = 30;
	boolean [] keysDown = new boolean [256];
	boolean [] keysToggled = new boolean [256];
	
	public InputManager(){
		
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
		//	System.out.println(key+" is tested");
			char c = key.charAt(0);
		//	System.out.println((int)c+" is c");

				 
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
			System.out.println(getMousePosition().getX()+"  "+getMousePosition().getX());
			mouse1Down = true;
		}
	}

	public Point2D mouseOverPlayerArrIndex(){
		 
		int pitchX = getMouseX()-pitchOrigin.getX();
		 int pitchY = getMouseY()-pitchOrigin.getY();
		 int squareX = ((pitchX/pitchSquareSize)*pitchSquareSize)+pitchOrigin.getX();
		 int squareY = ((pitchY/pitchSquareSize)*pitchSquareSize)+pitchOrigin.getY();
		 int xIndex = ((squareX-pitchOrigin.getX())/pitchSquareSize)+1;
		 int yIndex = ((squareY-pitchOrigin.getY())/pitchSquareSize)+1;
		
		return new Point2D(xIndex, yIndex);
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

}
