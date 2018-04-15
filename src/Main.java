public class Main {
	
	public static void main(String[] args) {
		Table table = new Table(8);
		for (int i = 0; i < 1; i++) {
			table.newGame();
			
//			table.setCommonCards();
//			table.setFlop();
//			table.showResults();
			table.showPlayersHands();
			table.showFlop();
			table.showTurn();
			table.showRiver();
		}
		
	}
}
