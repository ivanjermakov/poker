public class Main {
	
	public static void main(String[] args) {
		Table table = new Table(10);
		for (int i = 0; i < 1; i++) {
			table.newGame();
			
			table.showCommonCards();
//			table.showFlop();
//			table.showTurn();
//			table.showRiver();
		}
		
	}
}
