public class Main {

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			Table table = new Table(23);

			table.showPlayersHands();
			table.hand();
		}
	}
}
