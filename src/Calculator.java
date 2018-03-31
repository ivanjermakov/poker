import java.util.Vector;

public class Calculator {

	Vector<Stats> playersStats = new Vector<>();

	public Calculator(Table table) {
		for (Player player : table.players) {
			Stats stats = new Stats(table.deck, player);
			playersStats.add(stats.getStats());
		}
	}

	void getProbabilities() {
	}

	void getWinningRates() {
	}
}
