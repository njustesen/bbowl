package main;

import java.util.Date;

import game.GameMaster;
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
		
		DiceTester.testDices();
		initialize();
		startGame();
	}

	public static void initialize(){
		inputManager = new InputManager();
		gameMaster = new GameMaster();
		renderer = new Renderer(60, gameMaster, inputManager);
	}
	
	public static void startGame(){


		
		long startTime = new Date().getTime();
		
		while(true){
			startTime = new Date().getTime();
			
			gameMaster.update();
			renderer.renderFrame();
			
			long delta = new Date().getTime() - startTime;
			try {
				if(1000/renderer.getFps() - delta > 0)
					Thread.sleep(1000/renderer.getFps() - delta);
			} catch (InterruptedException e) {e.printStackTrace();}
			
		}
	
		
		}
}
