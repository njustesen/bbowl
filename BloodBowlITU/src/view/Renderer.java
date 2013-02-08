package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;

import models.Player;
import models.Race;
import models.humans.HumanBlitzer;
import models.humans.HumanCatcher;
import models.humans.HumanLineman;
import models.humans.HumanThrower;
import models.orcs.OrcBlackOrc;
import models.orcs.OrcBlitzer;
import models.orcs.OrcLineman;
import models.orcs.OrcThrower;
import game.GameMaster;

public class Renderer extends JPanel{

	private static int fps;
	private static JFrame screen;
	private static Integer team1Score = 0;
	private static Integer team2Score = 0;
	private static String team1Name = "ORCSES";
	private static String team2Name = "HUMANS";
	//layout
	private static int screenWidth;
	private static int screenHeight;
	private static int pitchSquareSize;
	private static int actionButtonWidth;
	private static int actionButtonHeight;
	private static InputManager inputManager;
	private static Point2D pitchOrigin;
	private static Point2D diceButtonOrigin;
	
	/////////////////////////
	/////// graphics ////////
	////////////////////////
	
	//action-buttons
	private BBImage actionOn = new BBImage("actionButtonOn2.jpg");
	private BBImage actionOff = new BBImage("actionButtonOff.jpg");
	private BBImage run = new BBImage("footprints.png");
	private BBImage block = new BBImage("block.png");
	private BBImage blitz = new BBImage("blitz.png");
	private BBImage foul = new BBImage("foul.png");
	private BBImage pass = new BBImage("pass.png");
	private BBImage handoff = new BBImage("handoff.png");
	private BBImage greenGlow = new BBImage("greenGlow.png");
	//dice-button
	private BBImage diceOn = new BBImage("diceButtonOn2.jpg");
	private BBImage diceOff = new BBImage("diceButtonOff.jpg");
	//dice-images
	private BBImage bbDice1 = new BBImage("bbdiceSkull.png");
	private BBImage bbDice2 = new BBImage("bbdiceBothDown.png");
	private BBImage bbDice3 = new BBImage("bbdiceArrow.png");
	private BBImage bbDice4 = new BBImage("bbdiceEx.png");
	private BBImage bbDice5 = new BBImage("bbdiceBoom.png");
	private BBImage dice1 = new BBImage("dice1.png");
	private BBImage dice2 = new BBImage("dice2.png");
	private BBImage dice3 = new BBImage("dice3.png");
	private BBImage dice4 = new BBImage("dice4.png");
	private BBImage dice5 = new BBImage("dice5.png");
	private BBImage dice6 = new BBImage("dice6.png");
	//background
	private BBImage background = new BBImage("interface.jpg");	
	//animations
	private BBAnimation greenTile = new BBAnimation("greenTile", true, 10);
	private BBAnimation selectedPlayer = new BBAnimation("selectedTile", true, 20);
	private BBAnimation roll = new BBAnimation("roll", true, 30);	
	//orcs
	private BBImage olineman = new BBImage("olineman.png");
	private BBImage oblackorc = new BBImage("oblackorc.png");
	private BBImage othrower = new BBImage("othrower.png");
	private BBImage oblitzer = new BBImage("oblitzer.png");
	//humans
	private BBImage hlineman = new BBImage("hlineman.png");
	private BBImage hthrower = new BBImage("hthrower.png");
	private BBImage hcatcher = new BBImage("hcatcher1b.png");
	private BBImage hblitzer = new BBImage("hblitzer.png");
	
	InputStream is;
	Font f;
	
	public Renderer(int fps, GameMaster gm, InputManager im){
		this.fps = fps;
		inputManager = im;
		screenWidth = InputManager.getScreenWidth();
		screenHeight = InputManager.getScreenHeight();
		pitchSquareSize = InputManager.getPitchSquareSize();
		actionButtonWidth = im.getActionButtonWidth();
		actionButtonHeight = im.getActionButtonHeight();
		pitchOrigin = InputManager.getPitchOrigin();
		diceButtonOrigin = InputManager.getDiceButtonOrigin();
		
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		screen = new JFrame();
		//screen.setSize(screenWidth, screenHeight);
		
		screen.setTitle("BLOOD BOWL");
		screen.setVisible(true);
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen.add(this);
		screen.pack();
		this.addKeyListener(inputManager);
		this.addMouseListener(inputManager);
		this.addMouseMotionListener(inputManager);
			
		pitchOrigin = InputManager.getPitchOrigin();
		pitchSquareSize = InputManager.getPitchSquareSize();
		greenTile.loopAnimation();
		roll.loopAnimation();
		selectedPlayer.loopAnimation();
		
	}
	
	public int getFps(){
		return fps;
	}
	
	public void renderFrame(){
		this.repaint();
	}
	
	public void hoverSquare(Graphics g, int x, int y){
		if(inputManager.getMouseX() > pitchOrigin.getX() && inputManager.getMouseX() < 26*pitchSquareSize + pitchOrigin.getX() &&
				inputManager.getMouseY() > pitchOrigin.getY() && inputManager.getMouseY() < 15*pitchSquareSize + pitchOrigin.getY()){
			 
		//	 System.out.println("playerArray ["+inputManager.mouseOverPlayerArrIndex().getX()+"]["+inputManager.mouseOverPlayerArrIndex().getY()+"]");
			 g.drawImage(greenTile.getBufferedImage(),inputManager.mouseOverArray().getX(),inputManager.mouseOverArray().getY(), null);
		}
	}
	
	public void drawActionButtons(Graphics g){
		
		for(int i = 0; i < 6; i++){
			if(inputManager.getMouseX() > i*actionButtonWidth && inputManager.getMouseX() < (actionButtonWidth * i)+actionButtonWidth &&
					inputManager.getMouseY() > 517 && inputManager.getMouseY() < 517+actionButtonHeight){
				if(inputManager.mouse1Down){
					int n = i+1;
					System.out.println("ActionButton "+n+" pressed");
				}
				g.drawImage(actionOn.getImage(), i*actionButtonWidth, 517, null);
				switch(i){
				case 0: g.drawImage(run.getBufferedImage(), i*actionButtonWidth-2, 517+3, null); break;
				case 1: g.drawImage(block.getBufferedImage(), i*actionButtonWidth-2, 517+3, null); break;
				case 2: g.drawImage(blitz.getBufferedImage(), i*actionButtonWidth-2, 517+3, null); break;
				case 3: g.drawImage(foul.getBufferedImage(), i*actionButtonWidth-2, 517+3, null); break;
				case 4: g.drawImage(pass.getBufferedImage(), i*actionButtonWidth-2, 517+3, null); break;
				case 5: g.drawImage(handoff.getBufferedImage(), i*actionButtonWidth-2, 517+3, null); break;
				default: System.out.println("dont have that image");
				}
			}else{
				g.drawImage(actionOff.getImage(), i*actionButtonWidth, 517, null);
				switch(i){
				case 0: g.drawImage(run.getBufferedImage(), i*actionButtonWidth-4, 517, null); break;
				case 1: g.drawImage(block.getBufferedImage(), i*actionButtonWidth-4, 517, null); break;
				case 2: g.drawImage(blitz.getBufferedImage(), i*actionButtonWidth-4, 517, null); break;
				case 3: g.drawImage(foul.getBufferedImage(), i*actionButtonWidth-4, 517, null); break;
				case 4: g.drawImage(pass.getBufferedImage(), i*actionButtonWidth-4, 517, null); break;
				case 5: g.drawImage(handoff.getBufferedImage(), i*actionButtonWidth-4, 517, null); break;
				default: System.out.println("dont have that image");
				}
			}
			if(i == 2){ //(i == gamestate.getSelectedAction())
				g.drawImage(greenGlow.getBufferedImage(), i*actionButtonWidth-12, 501, null);
			}
		}			
	}
	
	public void drawDiceButton(Graphics g){
			
			if(inputManager.getMouseX() > diceButtonOrigin.getX() && inputManager.getMouseX() < diceButtonOrigin.getX()+actionButtonWidth &&
					inputManager.getMouseY() > diceButtonOrigin.getY() && inputManager.getMouseY() < diceButtonOrigin.getX()+actionButtonHeight){				
				g.drawImage(diceOn.getImage(), diceButtonOrigin.getX(), diceButtonOrigin.getY(), null);
				}else{
					g.drawImage(diceOff.getImage(), diceButtonOrigin.getX(), diceButtonOrigin.getY(), null);
				}
						
		}
	
	public void diceAlert(Graphics g, boolean alert){
		if(alert){
			g.drawImage(roll.getBufferedImage(), diceButtonOrigin.getX()+actionButtonWidth+1, diceButtonOrigin.getY(), null);
		}
	}
	
	public void drawPlayer(Graphics g, Player p, int x, int y){
		int screenX = inputManager.arrayToScreen(x, y).getX()+1;
		int screenY = inputManager.arrayToScreen(x, y).getY();
		if(p.getRace() == Race.HUMANS){
			if(p.getTitle() == "Blitzer"){
				g.drawImage(hblitzer.getBufferedImage(), screenX, screenY, null);
			}else if(p.getTitle() == "Lineman"){
				g.drawImage(hlineman.getBufferedImage(), screenX, screenY, null);
			}else if(p.getTitle() == "Thrower"){
				g.drawImage(hthrower.getBufferedImage(), screenX, screenY, null);
			}else if(p.getTitle() == "Catcher"){
				g.drawImage(hcatcher.getBufferedImage(), screenX, screenY, null);
			}
		}else if(p.getRace() == Race.ORCS){
			if(p.getTitle() == "Blitzer"){
				g.drawImage(oblitzer.getBufferedImage(), screenX, screenY, null);
			}else if(p.getTitle() == "Lineman"){
				g.drawImage(olineman.getBufferedImage(), screenX, screenY, null);
			}else if(p.getTitle() == "Thrower"){
				g.drawImage(othrower.getBufferedImage(), screenX, screenY, null);
			}else if(p.getTitle() == "black orc"){
				g.drawImage(oblackorc.getBufferedImage(), screenX, screenY, null);
			}
		}
		if(p.getTitle() == "Blitzer"){//p == gameMaster.getSelectedPlayer())
			g.drawImage(selectedPlayer.getBufferedImage(), screenX-1, screenY, null);
		}
	}
	
	public void drawDice(Graphics g, int dice, String type){
		if(type == "D6"){
			switch(dice){
				case 1: g.drawImage(dice1.getBufferedImage(), diceButtonOrigin.getX()-85, diceButtonOrigin.getY()+10, null); break;
				case 2: g.drawImage(dice2.getBufferedImage(), diceButtonOrigin.getX()-85, diceButtonOrigin.getY()+10, null); break;
				case 3: g.drawImage(dice3.getBufferedImage(), diceButtonOrigin.getX()-85, diceButtonOrigin.getY()+10, null); break;
				case 4: g.drawImage(dice4.getBufferedImage(), diceButtonOrigin.getX()-85, diceButtonOrigin.getY()+10, null); break;
				case 5: g.drawImage(dice5.getBufferedImage(), diceButtonOrigin.getX()-85, diceButtonOrigin.getY()+10, null); break;
				case 6: g.drawImage(dice6.getBufferedImage(), diceButtonOrigin.getX()-85, diceButtonOrigin.getY()+10, null); break;
				default: System.out.println("invalid diceRoll");
			}
		}else if(type == "BB"){
			switch(dice){
				case 1: g.drawImage(bbDice1.getBufferedImage(), diceButtonOrigin.getX()-85, diceButtonOrigin.getY()+10, null); break;
				case 2: g.drawImage(bbDice2.getBufferedImage(), diceButtonOrigin.getX()-85, diceButtonOrigin.getY()+10, null); break;
				case 3: g.drawImage(bbDice3.getBufferedImage(), diceButtonOrigin.getX()-85, diceButtonOrigin.getY()+10, null); break;
				case 4: g.drawImage(bbDice4.getBufferedImage(), diceButtonOrigin.getX()-85, diceButtonOrigin.getY()+10, null); break;
				case 5: g.drawImage(bbDice5.getBufferedImage(), diceButtonOrigin.getX()-85, diceButtonOrigin.getY()+10, null); break;
				default: System.out.println("invalid diceRoll");
			}
		}
	}

	public void drawDice(Graphics g, int diceOne, int diceTwo, String type){
		if(type == "D6"){
			switch(diceOne){
			case 1: g.drawImage(dice1.getBufferedImage(), diceButtonOrigin.getX()-110, diceButtonOrigin.getY()+10, null); break;
			case 2: g.drawImage(dice2.getBufferedImage(), diceButtonOrigin.getX()-110, diceButtonOrigin.getY()+10, null); break;
			case 3: g.drawImage(dice3.getBufferedImage(), diceButtonOrigin.getX()-110, diceButtonOrigin.getY()+10, null); break;
			case 4: g.drawImage(dice4.getBufferedImage(), diceButtonOrigin.getX()-110, diceButtonOrigin.getY()+10, null); break;
			case 5: g.drawImage(dice5.getBufferedImage(), diceButtonOrigin.getX()-110, diceButtonOrigin.getY()+10, null); break;
			case 6: g.drawImage(dice6.getBufferedImage(), diceButtonOrigin.getX()-110, diceButtonOrigin.getY()+10, null); break;
			default: System.out.println("invalid diceRoll");
		}
		switch(diceTwo){
			case 1: g.drawImage(dice1.getBufferedImage(), diceButtonOrigin.getX()-60, diceButtonOrigin.getY()+10, null); break;
			case 2: g.drawImage(dice2.getBufferedImage(), diceButtonOrigin.getX()-60, diceButtonOrigin.getY()+10, null); break;
			case 3: g.drawImage(dice3.getBufferedImage(), diceButtonOrigin.getX()-60, diceButtonOrigin.getY()+10, null); break;
			case 4: g.drawImage(dice4.getBufferedImage(), diceButtonOrigin.getX()-60, diceButtonOrigin.getY()+10, null); break;
			case 5: g.drawImage(dice5.getBufferedImage(), diceButtonOrigin.getX()-60, diceButtonOrigin.getY()+10, null); break;
			case 6: g.drawImage(dice6.getBufferedImage(), diceButtonOrigin.getX()-60, diceButtonOrigin.getY()+10, null); break;
			default: System.out.println("invalid diceRoll");
		}
			
		}else if(type == "BB"){
			switch(diceOne){
				case 1: g.drawImage(bbDice1.getBufferedImage(), diceButtonOrigin.getX()-110, diceButtonOrigin.getY()+10, null); break;
				case 2: g.drawImage(bbDice2.getBufferedImage(), diceButtonOrigin.getX()-110, diceButtonOrigin.getY()+10, null); break;
				case 3: g.drawImage(bbDice3.getBufferedImage(), diceButtonOrigin.getX()-110, diceButtonOrigin.getY()+10, null); break;
				case 4: g.drawImage(bbDice4.getBufferedImage(), diceButtonOrigin.getX()-110, diceButtonOrigin.getY()+10, null); break;
				case 5: g.drawImage(bbDice5.getBufferedImage(), diceButtonOrigin.getX()-110, diceButtonOrigin.getY()+10, null); break;
				default: System.out.println("invalid diceRoll");
			}
			switch(diceTwo){
				case 1: g.drawImage(bbDice1.getBufferedImage(), diceButtonOrigin.getX()-60, diceButtonOrigin.getY()+10, null); break;
				case 2: g.drawImage(bbDice2.getBufferedImage(), diceButtonOrigin.getX()-60, diceButtonOrigin.getY()+10, null); break;
				case 3: g.drawImage(bbDice3.getBufferedImage(), diceButtonOrigin.getX()-60, diceButtonOrigin.getY()+10, null); break;
				case 4: g.drawImage(bbDice4.getBufferedImage(), diceButtonOrigin.getX()-60, diceButtonOrigin.getY()+10, null); break;
				case 5: g.drawImage(bbDice5.getBufferedImage(), diceButtonOrigin.getX()-60, diceButtonOrigin.getY()+10, null); break;
				default: System.out.println("invalid diceRoll");
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(background.getImage(), 0, 0, null);
		g.setColor(Color.WHITE);
		hoverSquare(g, inputManager.getMouseX(), inputManager.getMouseY());
		//draw orcs
		drawPlayer(g, new OrcLineman(1),1,1);
		drawPlayer(g, new OrcBlackOrc(2),2,2);
		drawPlayer(g, new OrcBlitzer(3),3,3);
		drawPlayer(g, new OrcThrower(4),4,4);
		//draw humans
		drawPlayer(g, new HumanLineman(5),23,12);
		drawPlayer(g, new HumanCatcher(6),24,13);
		drawPlayer(g, new HumanBlitzer(7),25,14);
		drawPlayer(g, new HumanThrower(8),26,15);
				
		drawActionButtons(g);
		drawDiceButton(g);
		diceAlert(g,true);
		drawDice(g, 2, 3, "BB");
		
		Font font = new Font("Arial", Font.PLAIN, 25);	    
	    g.setFont(font); //<--
		g.drawString(team1Score.toString(), 45, 47);
		g.drawString(team2Score.toString(), screenWidth-58, 47);
		
		Font font2 = new Font("Arial", Font.PLAIN, 32);	    
	    g.setFont(font2);
		g.drawString(team1Name.toString(), 145, 37);
		g.drawString(team2Name.toString(), screenWidth-278, 37);
		
	}
}
