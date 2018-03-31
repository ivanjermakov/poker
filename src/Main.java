public class Main {

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			Table table = new Table(10);

			table.showPlayersHands();
			table.hand();
		}
	}
}
