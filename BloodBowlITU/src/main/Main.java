package main;

import java.util.Date;

import ai.AIAgent;
import ai.BaseLineAI;
import ai.RandomAI;
import ai.RandomTouchdownAI;
import ai.monteCarlo.FlatMonteCarloAI;
import ai.monteCarlo.MctsDetermAi;

import models.GameState;
import models.Pitch;
import models.Team;
import models.TeamFactory;

import game.GameMaster;
import sound.FakeSoundManager;
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


		AIAgent montiCarlos = new MctsDetermAi(true, true);
		AIAgent montiCarlosB = new MctsDetermAi(false, true);

		Pitch pitch = new Pitch(home, away);
		gameMaster = new GameMaster(new GameState(home, away, pitch), montiCarlos, montiCarlosB, true, true);
		gameMaster.enableLogging();
		gameMaster.setSoundManager(new SoundManager());
		//gameMaster.setSoundManager(new FakeSoundManager());
		inputManager = new InputManager(gameMaster);
		renderer = new Renderer(600, gameMaster, inputManager);
	}
	
	public static void startGame(){

		long startTime = new Date().getTime();
		
		loop(startTime);
	
	}

	private static void loop(long startTime) {

		while(true){
			
			//startTime = new Date().getTime();
			
			renderer.renderFrame();
			
			gameMaster.update();
			
			/*
			long delta = new Date().getTime() - startTime;
			try {
				
				Thread.sleep(Math.max(250,(1000/renderer.getFps() - delta)));
				
			} catch (InterruptedException e) {e.printStackTrace();}
			*/
		}
	}
}
