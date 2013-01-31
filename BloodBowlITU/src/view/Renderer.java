package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import game.GameMaster;

public class Renderer extends JPanel{

	private int fps;
	private GameMaster gameMaster;
	
	private static JFrame screen;
	private static int screenWidth = 844;
	private static int screenHeight = 598;
	private static int pitchSquareSize = 30;
	private static InputManager inputManager;
	private static Point2D pitchOrigin;
	private static int pitchSqureSize;
	
	public Renderer(int fps, GameMaster gm, InputManager im){
		
		this.fps = fps;
		this.gameMaster = gm;
		this.inputManager = im;
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		screen = new JFrame();
		//screen.setSize(screenWidth, screenHeight);
		
		screen.setTitle("no coincidance");
		screen.setVisible(true);
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen.add(this);
		screen.pack();
		this.addKeyListener(inputManager);
		this.addMouseListener(inputManager);
		this.addMouseMotionListener(inputManager);
		
		this.pitchOrigin = InputManager.getPitchOrigin();
		this.pitchSquareSize = InputManager.getPitchSquareSize();
		
	}
	
	public int getFps(){
		return fps;
	}
	
	public void renderFrame(){
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.fillRect(2, 2, 840, 40);
		g.setColor(Color.green);
		g.fillRect((int)pitchOrigin.getX(), (int)pitchOrigin.getY(), 26*pitchSquareSize, 15*pitchSquareSize);
		g.setColor(Color.blue);
		
		if(inputManager.getMouseX() > pitchOrigin.getX() && inputManager.getMouseX() < 26*pitchSquareSize + pitchOrigin.getX() &&
				inputManager.getMouseY() > pitchOrigin.getY() && inputManager.getMouseY() < 15*pitchSquareSize + pitchOrigin.getY()){
			 
			int pitchX = inputManager.getMouseX()-pitchOrigin.getX();
			 int pitchY = inputManager.getMouseY()-pitchOrigin.getY();
			 int squareX = ((pitchX/30)*30)+pitchOrigin.getX();
			 int squareY = ((pitchY/30)*30)+pitchOrigin.getY();
		/*	 
			System.out.println("pitchX = "+pitchX+"  pitchY = "+pitchY);
			System.out.println("squareX = "+squareX+"  squareY = "+squareY);	
		*/	
			 System.out.println("playerArray ["+inputManager.mouseOverPlayerArrIndex().getX()+"]["+inputManager.mouseOverPlayerArrIndex().getY()+"]");
			 g.drawRect(squareX, squareY, pitchSquareSize, pitchSquareSize);
		}
		
		g.setColor(Color.RED);
		g.fillRect(2, 556, 300, 40);
		g.setColor(Color.BLUE);
		g.fillRect(304, 556, 400, 40);
	}
}
