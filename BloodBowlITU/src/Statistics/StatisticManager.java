package Statistics;

import java.util.Date;

public class StatisticManager {

	public static long timeSpendByGameMaster = 0;
	public static long timeSpendByAIAgent = 0;
	public static long timeSpendByRandomAI = 0;
	public static int actions = 0;
	public static int games = 0;
	public static boolean stopped = false;
	public static int draws = 0;
	public static int homeWon = 0;
	public static int awayWon = 0;
	
	public static void print(Date time){
		if (!stopped){
			double gameMaster = (double) timeSpendByGameMaster / 1000000d;
			double agent = (double) timeSpendByAIAgent / 1000000d;
			double ai = (double) timeSpendByRandomAI / 1000000d;
			
			agent -= ai;

			System.out.println("Game took: " + (new Date().getTime() - time.getTime()) + " ms.");
			/*
			System.out.println("timeSpendByGameMaster: " + gameMaster + " ms.");
			System.out.println("timeSpendByAIAgent: " + agent + " ms.");
			System.out.println("timeSpendByRandomAI: " + ai + " ms.");
			System.out.println("actions: " + actions);
			*/
			System.out.println("games: " + games);
			System.out.println("draws: " + draws);
			System.out.println("homeWon: " + homeWon);
			System.out.println("awayWon: " + awayWon);
			
			
		}
	}

	public static void stop() {
		stopped = true;
		timeSpendByGameMaster = 0;
		timeSpendByAIAgent = 0;
		timeSpendByRandomAI = 0;
		actions = 0;
	}
	
}
