package game;

import java.util.LinkedList;

public class GameLog {
	
	public static LinkedList<String> messeges = new LinkedList<String>();

	public static void push(String msg){
		
		messeges.add(msg);
		
	}
	
	public static String poll(){
		
		if (messeges.size() > 0){
			return messeges.removeFirst();
		}
		
		return "";
	}
	
	public static String peek(){
		if (messeges.size() > 0){
			return messeges.peek();
		}
		
		return "";
	}
	
}
