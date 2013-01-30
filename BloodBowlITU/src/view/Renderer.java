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
	private static InputManager inputManager;
	
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
		
	}
	
	public int getFps(){
		return fps;
	}
	
	public void renderFrame(){
		System.out.println("RENDERFRAME CALLED");
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.fillRect(2, 2, 840, 40);
		g.setColor(Color.GREEN);
		g.fillRect(2, 44, 840, 510);
		g.setColor(Color.RED);
		g.fillRect(2, 556, 300, 40);
		g.setColor(Color.BLUE);
		g.fillRect(304, 556, 400, 40);
		System.out.println("FRAMES RENDERED");
	}
}
