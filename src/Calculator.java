import java.util.Vector;

public class Calculator {

	Vector<Stats> playersStats = new Vector<>();

	public Calculator(Table table) {
		for (Player player : table.players) {
			Stats stats = new Stats(table.deck, player);
			playersStats.add(stats);
		}
	}

	void getProbabilities() {
	}

	void getWinningRates() {
	}

	void getRankings() {
		for (Stats playerStats : playersStats) {
			System.out.println(playerStats.player.name + " has " + playerStats.ranking + " with " + Card.toShortStrings(playerStats.bestHand));
		}
	}
}
