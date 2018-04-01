public class Main {
	
	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			Table table = new Table(8);
			table.newGame();
			
			table.showPlayersHands();
			table.showCommonCards();
//			table.showFlop();
//			table.showTurn();
//			table.showRiver();
		}
	}
}
