package View;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputManager implements KeyListener, MouseListener, MouseMotionListener{
	
//	boolean leftDown;
//	boolean rightDown;
//	boolean upDown;
//	boolean downDown;
//	boolean spaceDown;
//	boolean shiftDown;
//	boolean wDown;
//	boolean sDown;
//	boolean aDown;
//	boolean dDown;
//	boolean Down1;
//	boolean Down2;
//	boolean Down3;
//	boolean controlDown;
//	Point2D mousePosition;
//	long mouseDownTimeStart;
//	long mouseDownTime;
	int mouseX;
	int mouseY;
	boolean mouse1Down;
	
	boolean [] keysDown = new boolean [256];
	boolean [] keysToggled = new boolean [256];
	
	public InputManager(){
		
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
			mouse1Down = true;
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

}
