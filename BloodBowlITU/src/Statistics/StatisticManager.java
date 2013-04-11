package Statistics;

public class StatisticManager {

	public static long timeSpendByGameMaster = 0;
	public static long timeSpendByAIAgent = 0;
	public static long timeSpendByRandomAI = 0;
	public static int actions = 0;
	public static boolean stopped = false;
	
	public static void print(){
		if (!stopped){
			double gameMaster = (double) timeSpendByGameMaster / 1000000d;
			double agent = (double) timeSpendByAIAgent / 1000000d;
			double ai = (double) timeSpendByRandomAI / 1000000d;
			
			agent -= ai;
			
			System.out.println("timeSpendByGameMaster: " + gameMaster + " ms.");
			System.out.println("timeSpendByAIAgent: " + agent + " ms.");
			System.out.println("timeSpendByRandomAI: " + ai + " ms.");
			System.out.println("actions: " + actions);
		}
	}
	
}
