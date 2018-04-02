public class Main {
	
	public static void main(String[] args) {
		Table table = new Table(20);
		for (int i = 0; i < 1000; i++) {
			table.newGame();
			
			table.showCommonCards();
//			table.showFlop();
//			table.showTurn();
//			table.showRiver();
		}
	}
}
