public class Main {
	
	public static void main(String[] args) {
		Table table = new Table(23);
		for (int i = 0; i < 10000; i++) {
			table.newGame();
			
			table.showCommonCards();
//			table.showFlop();
//			table.showTurn();
//			table.showRiver();
		}
		
	}
}
