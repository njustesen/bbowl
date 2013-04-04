package main;

import java.util.Date;

import ai.AIAgent;
import ai.MontiCarlos;

import models.Coach;
import models.GameState;
import models.Pitch;
import models.Team;
import models.TeamFactory;

import game.GameMaster;
import sound.SoundManager;
import test.DiceTester;
import view.InputManager;
import view.Renderer;

public class Main {

	private static InputManager inputManager;
	private static Renderer renderer;
	private static GameMaster gameMaster;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//test();
		
		initialize();
		startGame();
	}
	
	public static void test(){
		
		DiceTester.testDices();
		
	}

	public static void initialize(){
		
		Team home = TeamFactory.getHumanTeam();
		Team away = TeamFactory.getHumanOrc();
		AIAgent montiCarlos = new MontiCarlos(true);
		Pitch pitch = new Pitch(home, away);
		gameMaster = new GameMaster(new GameState(home, away, pitch), montiCarlos, null);
		gameMaster.setSoundManager(new SoundManager());
		inputManager = new InputManager(gameMaster);
		renderer = new Renderer(60, gameMaster, inputManager);
	}
	
	public static void startGame(){

		long startTime = new Date().getTime();
		
		loop(startTime);
	
	}

	private static void loop(long startTime) {

		while(true){
			startTime = new Date().getTime();
			
			gameMaster.update();
			
			renderer.renderFrame();
			
			long delta = new Date().getTime() - startTime;
			try {
				
				Thread.sleep(Math.max(1,(1000/renderer.getFps() - delta)));
				
			} catch (InterruptedException e) {e.printStackTrace();}
			
		}
	}
}
