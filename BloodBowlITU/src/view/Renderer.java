package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;

import models.GameStage;
import models.Player;
import models.Race;
import models.Square;
import models.Weather;
import models.dice.DiceFace;
import models.dice.DiceRoll;
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
	private static Point2D rerollButtonOrigin;
	
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
	private BBImage endTurnOn = new BBImage("endturnon.png");
	private BBImage endTurnOff = new BBImage("endturnoff.png");
	private BBImage endSetupOn = new BBImage("endsetupon.png");
	private BBImage endSetupOff = new BBImage("endsetupoff.png");
	private BBImage placeBallOn = new BBImage("placeballon.png");
	private BBImage placeBallOff = new BBImage("placeballoff.png");
	private BBImage startGameOn = new BBImage("startgameon.png");
	private BBImage startGameOff = new BBImage("startgameoff.png");
	//ball
	private BBImage ball = new BBImage("ball.png");
	//coin
	private BBImage heads = new BBImage("heads.png");
	private BBImage tails = new BBImage("tails.png");
	private BBImage kick = new BBImage("kick.png");
	private BBImage receive = new BBImage("receive.png");
	private BBImage kickGlow = new BBImage("kickGlow.png");
	private BBImage receiveGlow = new BBImage("receiveGlow.png");
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
	
	private BBImage weather = new BBImage();
	
	InputStream is;
	Font f;
	private GameMaster gameMaster;
	
	public Renderer(int fps, GameMaster gm, InputManager im){

		this.fps = fps;
		inputManager = im;
		screenWidth = InputManager.getScreenWidth();
		screenHeight = InputManager.getScreenHeight();
		pitchSquareSize = InputManager.getPitchSquareSize();
		actionButtonWidth = im.getActionButtonWidth();
		actionButtonHeight = im.getActionButtonHeight();
		pitchOrigin = InputManager.getPitchOrigin();
		rerollButtonOrigin = InputManager.getDiceButtonOrigin();
		gameMaster = gm;
		
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
		setWeather();
	}
	
	public void setWeather(){
		Weather w = gameMaster.getState().getWeather();
		
		switch(w){
			case SWELTERING_HEAT: weather.setImage("heat.png"); break;
			case VERY_SUNNY: weather.setImage("sun.png"); break;
			case NICE: weather.setImage("nice.png"); break;
			case POURING_RAIN: weather.setImage("rain.png"); break;
			case BLIZZARD: weather.setImage("blizzard.png"); break;
			default: System.out.println("WEATHER?");
		}
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
				case 0: g.drawImage(run.getBufferedImage(), i*actionButtonWidth-2, inputManager.getActionButtonOrigin().getY()+3, null); break;
				case 1: g.drawImage(block.getBufferedImage(), i*actionButtonWidth-2, inputManager.getActionButtonOrigin().getY()+3, null); break;
				case 2: g.drawImage(blitz.getBufferedImage(), i*actionButtonWidth-2, inputManager.getActionButtonOrigin().getY()+3, null); break;
				case 3: g.drawImage(foul.getBufferedImage(), i*actionButtonWidth-2, inputManager.getActionButtonOrigin().getY()+3, null); break;
				case 4: g.drawImage(pass.getBufferedImage(), i*actionButtonWidth-2, inputManager.getActionButtonOrigin().getY()+3, null); break;
				case 5: g.drawImage(handoff.getBufferedImage(), i*actionButtonWidth-2, inputManager.getActionButtonOrigin().getY()+3, null); break;
				default: System.out.println("dont have that image");
				}
			}else{
				g.drawImage(actionOff.getImage(), i*actionButtonWidth, 517, null);
				switch(i){
				case 0: g.drawImage(run.getBufferedImage(),inputManager.getActionButtonOrigin().getX() + i*actionButtonWidth-4, inputManager.getActionButtonOrigin().getY(), null); break;
				case 1: g.drawImage(block.getBufferedImage(),inputManager.getActionButtonOrigin().getX() +  i*actionButtonWidth-4, inputManager.getActionButtonOrigin().getY(), null); break;
				case 2: g.drawImage(blitz.getBufferedImage(),inputManager.getActionButtonOrigin().getX() +  i*actionButtonWidth-4, inputManager.getActionButtonOrigin().getY(), null); break;
				case 3: g.drawImage(foul.getBufferedImage(),inputManager.getActionButtonOrigin().getX() +  i*actionButtonWidth-4, inputManager.getActionButtonOrigin().getY(), null); break;
				case 4: g.drawImage(pass.getBufferedImage(),inputManager.getActionButtonOrigin().getX() +  i*actionButtonWidth-4, inputManager.getActionButtonOrigin().getY(), null); break;
				case 5: g.drawImage(handoff.getBufferedImage(),inputManager.getActionButtonOrigin().getX() +  i*actionButtonWidth-4, 517, null); break;
				default: System.out.println("dont have that image");
				}
			}
			if(i == 2){ //(i == gamestate.getSelectedAction())
				g.drawImage(greenGlow.getBufferedImage(), i*actionButtonWidth-12, 501, null);
			}
		}			
	}
	
	public void drawEndTurnButton(Graphics g){

			if(inputManager.getMouseX() > inputManager.getEndTurnButtonOrigin().getX() && inputManager.getMouseX() < inputManager.getEndTurnButtonOrigin().getX()+actionButtonWidth &&
					inputManager.getMouseY() > inputManager.getEndTurnButtonOrigin().getY() && inputManager.getMouseY() < inputManager.getEndTurnButtonOrigin().getX()+actionButtonHeight){
			
				switch(gameMaster.getState().getGameStage()){
					case START_UP: g.drawImage(startGameOff.getImage(), inputManager.getEndTurnButtonOrigin().getX(), inputManager.getEndTurnButtonOrigin().getY(), null); break;
					case KICKING_SETUP: g.drawImage(endSetupOff.getImage(), inputManager.getEndTurnButtonOrigin().getX(), inputManager.getEndTurnButtonOrigin().getY(), null); break;
					case RECEIVING_SETUP:  g.drawImage(endSetupOff.getImage(), inputManager.getEndTurnButtonOrigin().getX(), inputManager.getEndTurnButtonOrigin().getY(), null); break;
					case KICK_PLACEMENT: g.drawImage(placeBallOff.getImage(), inputManager.getEndTurnButtonOrigin().getX(), inputManager.getEndTurnButtonOrigin().getY(), null); break;
					default: g.drawImage(startGameOff.getImage(), inputManager.getEndTurnButtonOrigin().getX(), inputManager.getEndTurnButtonOrigin().getY(), null); break;
				}
				
				}else{
					switch(gameMaster.getState().getGameStage()){
					case START_UP: g.drawImage(startGameOn.getImage(), inputManager.getEndTurnButtonOrigin().getX(), inputManager.getEndTurnButtonOrigin().getY(), null); break;
					case KICKING_SETUP: g.drawImage(endSetupOn.getImage(), inputManager.getEndTurnButtonOrigin().getX(), inputManager.getEndTurnButtonOrigin().getY(), null); break;
					case RECEIVING_SETUP:  g.drawImage(endSetupOn.getImage(), inputManager.getEndTurnButtonOrigin().getX(), inputManager.getEndTurnButtonOrigin().getY(), null); break;
					case KICK_PLACEMENT: g.drawImage(placeBallOn.getImage(), inputManager.getEndTurnButtonOrigin().getX(), inputManager.getEndTurnButtonOrigin().getY(), null); break;
					default: g.drawImage(startGameOn.getImage(), inputManager.getEndTurnButtonOrigin().getX(), inputManager.getEndTurnButtonOrigin().getY(), null); break;
					}
				}	
		}
		
	
	public void drawReroll(Graphics g){

			g.drawImage(roll.getBufferedImage(), rerollButtonOrigin.getX(), rerollButtonOrigin.getY(), null);
	}
	
	public void drawPlayer(Graphics g, Player p, int x, int y){
		int screenX = inputManager.arrayToScreen(x, y).getX();
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
		
		// Selected
		if(p == gameMaster.getSelectedPlayer()){
			g.drawImage(selectedPlayer.getBufferedImage(), screenX-1, screenY, null);
		}
	}
	
	

	public void drawDice(Graphics g, int diceOne, int diceTwo, String type){
		if(type == "D6"){
			switch(diceOne){
			case 1: g.drawImage(dice1.getBufferedImage(), rerollButtonOrigin.getX()-110, rerollButtonOrigin.getY()+10, null); break;
			case 2: g.drawImage(dice2.getBufferedImage(), rerollButtonOrigin.getX()-110, rerollButtonOrigin.getY()+10, null); break;
			case 3: g.drawImage(dice3.getBufferedImage(), rerollButtonOrigin.getX()-110, rerollButtonOrigin.getY()+10, null); break;
			case 4: g.drawImage(dice4.getBufferedImage(), rerollButtonOrigin.getX()-110, rerollButtonOrigin.getY()+10, null); break;
			case 5: g.drawImage(dice5.getBufferedImage(), rerollButtonOrigin.getX()-110, rerollButtonOrigin.getY()+10, null); break;
			case 6: g.drawImage(dice6.getBufferedImage(), rerollButtonOrigin.getX()-110, rerollButtonOrigin.getY()+10, null); break;
			default: System.out.println("invalid diceRoll");
		}
		switch(diceTwo){
			case 1: g.drawImage(dice1.getBufferedImage(), rerollButtonOrigin.getX()-60, rerollButtonOrigin.getY()+10, null); break;
			case 2: g.drawImage(dice2.getBufferedImage(), rerollButtonOrigin.getX()-60, rerollButtonOrigin.getY()+10, null); break;
			case 3: g.drawImage(dice3.getBufferedImage(), rerollButtonOrigin.getX()-60, rerollButtonOrigin.getY()+10, null); break;
			case 4: g.drawImage(dice4.getBufferedImage(), rerollButtonOrigin.getX()-60, rerollButtonOrigin.getY()+10, null); break;
			case 5: g.drawImage(dice5.getBufferedImage(), rerollButtonOrigin.getX()-60, rerollButtonOrigin.getY()+10, null); break;
			case 6: g.drawImage(dice6.getBufferedImage(), rerollButtonOrigin.getX()-60, rerollButtonOrigin.getY()+10, null); break;
			default: System.out.println("invalid diceRoll");
		}
			
		}else if(type == "BB"){
			switch(diceOne){
				case 1: g.drawImage(bbDice1.getBufferedImage(), rerollButtonOrigin.getX()-110, rerollButtonOrigin.getY()+10, null); break;
				case 2: g.drawImage(bbDice2.getBufferedImage(), rerollButtonOrigin.getX()-110, rerollButtonOrigin.getY()+10, null); break;
				case 3: g.drawImage(bbDice3.getBufferedImage(), rerollButtonOrigin.getX()-110, rerollButtonOrigin.getY()+10, null); break;
				case 4: g.drawImage(bbDice4.getBufferedImage(), rerollButtonOrigin.getX()-110, rerollButtonOrigin.getY()+10, null); break;
				case 5: g.drawImage(bbDice5.getBufferedImage(), rerollButtonOrigin.getX()-110, rerollButtonOrigin.getY()+10, null); break;
				default: System.out.println("invalid diceRoll");
			}
			switch(diceTwo){
				case 1: g.drawImage(bbDice1.getBufferedImage(), rerollButtonOrigin.getX()-60, rerollButtonOrigin.getY()+10, null); break;
				case 2: g.drawImage(bbDice2.getBufferedImage(), rerollButtonOrigin.getX()-60, rerollButtonOrigin.getY()+10, null); break;
				case 3: g.drawImage(bbDice3.getBufferedImage(), rerollButtonOrigin.getX()-60, rerollButtonOrigin.getY()+10, null); break;
				case 4: g.drawImage(bbDice4.getBufferedImage(), rerollButtonOrigin.getX()-60, rerollButtonOrigin.getY()+10, null); break;
				case 5: g.drawImage(bbDice5.getBufferedImage(), rerollButtonOrigin.getX()-60, rerollButtonOrigin.getY()+10, null); break;
				default: System.out.println("invalid diceRoll");
			}
		}
	}


	private void drawDiceRoll(Graphics g, DiceRoll currentDiceRoll) {

		if (gameMaster.getState().getCurrentDiceRoll() != null){
			
			int i = 0;
			for(DiceFace face : gameMaster.getState().getCurrentDiceRoll().getFaces()){
				BufferedImage img = getDiceImage(face);
				g.drawImage(img, rerollButtonOrigin.getX()-55-i*55, rerollButtonOrigin.getY()+10, null);
				i++;
			}
			g.drawRect(inputManager.getDiceButtonOrigin().getX()-(i*56)-3, inputManager.getDiceButtonOrigin().getY()+2, i*56, 68);
			
			
		}	
	}

	private BufferedImage getDiceImage(DiceFace face) {
		switch(face){
			case ONE : return dice1.getBufferedImage();
			case TWO : return dice2.getBufferedImage();
			case THREE : return dice3.getBufferedImage();
			case FOUR : return dice4.getBufferedImage();
			case FIVE : return dice5.getBufferedImage();
			case SIX : return dice6.getBufferedImage();
			case SKULL : return bbDice1.getBufferedImage();
			case BOTH_DOWN : return bbDice2.getBufferedImage();
			case PUSH : return bbDice3.getBufferedImage();
			case DEFENDER_STUMBLES : return bbDice4.getBufferedImage();
			case DEFENDER_KNOCKED_DOWN: return bbDice5.getBufferedImage();
			default:
				return null;
		}

	}
	
	public void drawDice(Graphics g, int dice, String type){
		if(type == "D6"){
			switch(dice){
				case 1: g.drawImage(dice1.getBufferedImage(), rerollButtonOrigin.getX()-85, rerollButtonOrigin.getY()+10, null); break;
				case 2: g.drawImage(dice2.getBufferedImage(), rerollButtonOrigin.getX()-85, rerollButtonOrigin.getY()+10, null); break;
				case 3: g.drawImage(dice3.getBufferedImage(), rerollButtonOrigin.getX()-85, rerollButtonOrigin.getY()+10, null); break;
				case 4: g.drawImage(dice4.getBufferedImage(), rerollButtonOrigin.getX()-85, rerollButtonOrigin.getY()+10, null); break;
				case 5: g.drawImage(dice5.getBufferedImage(), rerollButtonOrigin.getX()-85, rerollButtonOrigin.getY()+10, null); break;
				case 6: g.drawImage(dice6.getBufferedImage(), rerollButtonOrigin.getX()-85, rerollButtonOrigin.getY()+10, null); break;
				default: System.out.println("invalid diceRoll");
			}
		}else if(type == "BB"){
			switch(dice){
				case 1: g.drawImage(bbDice1.getBufferedImage(), rerollButtonOrigin.getX()-85, rerollButtonOrigin.getY()+10, null); break;
				case 2: g.drawImage(bbDice2.getBufferedImage(), rerollButtonOrigin.getX()-85, rerollButtonOrigin.getY()+10, null); break;
				case 3: g.drawImage(bbDice3.getBufferedImage(), rerollButtonOrigin.getX()-85, rerollButtonOrigin.getY()+10, null); break;
				case 4: g.drawImage(bbDice4.getBufferedImage(), rerollButtonOrigin.getX()-85, rerollButtonOrigin.getY()+10, null); break;
				case 5: g.drawImage(bbDice5.getBufferedImage(), rerollButtonOrigin.getX()-85, rerollButtonOrigin.getY()+10, null); break;
				default: System.out.println("invalid diceRoll");
			}
		}
	}

	private void drawPlayers(Graphics g) {
		
		// Draw pitch
		Player[][] playerArr = gameMaster.getState().getPitch().getPlayerArr();
		
		for(int y = 0; y < playerArr.length; y++){
			for(int x = 0; x < playerArr[0].length; x++){
				
				if (playerArr[y][x] != null){
					drawPlayer(g, playerArr[y][x], x, y);
				}
				
			}
		}
		
		// Draw home dugout
		for(Player p : gameMaster.getState().getPitch().getHomeDogout().getReserves()){
			
			int index = gameMaster.getState().getPitch().getHomeDogout().getReserves().indexOf(p);
			
			drawPlayer(g, p, index%2 - 1, index/2 + 1);
			
		}
		
		// Draw away dugout
		for(Player p : gameMaster.getState().getPitch().getAwayDogout().getReserves()){
			
			int index = gameMaster.getState().getPitch().getAwayDogout().getReserves().indexOf(p);
			
			drawPlayer(g, p, index%2 + 1 + 26, index/2 + 1);
			
		}
		
	}
	
	public void drawCoinToss(Graphics g){
		if(gameMaster.getState().getGameStage() == GameStage.COIN_TOSS){
			g.drawImage(heads.getBufferedImage(), inputManager.getHeadsCenter().getX()-heads.getWidth()/2, inputManager.getHeadsCenter().getY()-heads.getHeight()/2, null);
			g.drawImage(tails.getBufferedImage(), inputManager.getTailsCenter().getX()-tails.getWidth()/2, inputManager.getTailsCenter().getY()-tails.getHeight()/2, null);
		}else if(gameMaster.getState().getGameStage() == GameStage.PICK_COIN_TOSS_EFFECT){
			if(inputManager.getMouseX() < inputManager.getHeadsCenter().getX()+heads.getWidth()/2 && inputManager.getMouseX() > inputManager.getHeadsCenter().getX()-heads.getWidth()/2 &&
					inputManager.getMouseY() < inputManager.getHeadsCenter().getY()+heads.getHeight()/2 && inputManager.getMouseY() > inputManager.getHeadsCenter().getY()-heads.getHeight()/2){
				g.drawImage(kickGlow.getBufferedImage(), inputManager.getHeadsCenter().getX()-heads.getWidth()/2, inputManager.getHeadsCenter().getY()-heads.getHeight()/2, null);
			}else{g.drawImage(kick.getBufferedImage(), inputManager.getHeadsCenter().getX()-heads.getWidth()/2, inputManager.getHeadsCenter().getY()-heads.getHeight()/2, null);}
			
			if(inputManager.getMouseX() < inputManager.getTailsCenter().getX()+tails.getWidth()/2 && inputManager.getMouseX() > inputManager.getTailsCenter().getX()-tails.getWidth()/2 &&
					inputManager.getMouseY() < inputManager.getTailsCenter().getY()+tails.getHeight()/2 && inputManager.getMouseY() > inputManager.getTailsCenter().getY()-tails.getHeight()/2){
				g.drawImage(receiveGlow.getBufferedImage(), inputManager.getTailsCenter().getX()-tails.getWidth()/2, inputManager.getTailsCenter().getY()-tails.getHeight()/2, null);
			}else{g.drawImage(receive.getBufferedImage(), inputManager.getTailsCenter().getX()-tails.getWidth()/2, inputManager.getTailsCenter().getY()-tails.getHeight()/2, null);}
		}
	}
	
	public void drawStats(Graphics g){
		Player p = gameMaster.getSelectedPlayer();
		if(p != null){
			int x = InputManager.getActionButtonOrigin().getX()+6*inputManager.getActionButtonWidth()+5;
			int y = InputManager.getActionButtonOrigin().getY()+2;
			g.drawRect(x, y, 140, 68);
			int movesLeft = p.getMA() - gameMaster.getSelectedPlayer().getPlayerStatus().getMovementUsed();
			
			Font font = new Font("Arial", Font.PLAIN, 16);	    
		    g.setFont(font); //<--
			g.drawString(p.getTitle(), x+15, y+17);
			
			font = new Font("Arial", Font.PLAIN, 12);	    
		    g.setFont(font); //<--
			g.drawString("MA = "+movesLeft+"/"+p.getMA(), x+15, y+37);
			g.drawString("AG = "+p.getAG(), x+75, y+37);
			g.drawString("AV = "+p.getAV(), x+15, y+57);
			g.drawString("ST = "+p.getST(), x+75, y+57);
		}
	}
	
	public void drawBall(Graphics g){
		if(gameMaster.getState().getPitch().getBall().getSquare() != null){
			int ballX = gameMaster.getState().getPitch().getBall().getSquare().getX();
			int ballY = gameMaster.getState().getPitch().getBall().getSquare().getY();
			int screenX = inputManager.arrayToScreen(ballX, ballY).getX();
			int screenY = inputManager.arrayToScreen(ballX, ballY).getY();
			g.drawImage(ball.getBufferedImage(), screenX, screenY, null);
		}
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(background.getImage(), 0, 0, null);
		g.setColor(Color.WHITE);
		hoverSquare(g, inputManager.getMouseX(), inputManager.getMouseY());
		
		drawPlayers(g);
		drawBall(g);
		drawActionButtons(g);
		drawStats(g);
		drawEndTurnButton(g);
		drawReroll(g);
		drawDiceRoll(g, gameMaster.getState().getCurrentDiceRoll());
		drawCoinToss(g);
		g.drawImage(weather.getBufferedImage(), 845, 535, null);
		
		Font font = new Font("Arial", Font.PLAIN, 25);	    
	    g.setFont(font); //<--
		g.drawString(team1Score.toString(), 23, 47);
		g.drawString(team2Score.toString(), screenWidth-38, 47);
		
		font = new Font("Arial", Font.PLAIN, 15);	    
	    g.setFont(font); //<--
		g.drawString("rerolls: " + gameMaster.getState().getHomeTeam().getRerolls(), 125, 37);
		g.drawString("rerolls: " + gameMaster.getState().getAwayTeam().getRerolls(),  screenWidth-178, 37);
		
		font = new Font("Arial", Font.PLAIN, 32);	    
	    g.setFont(font);
		g.drawString(gameMaster.getState().getHomeTeam().getTeamName(), 245, 37);
		g.drawString(gameMaster.getState().getAwayTeam().getTeamName(), screenWidth-378, 37);
		
	}
}
