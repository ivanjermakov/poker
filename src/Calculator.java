import java.util.Collections;
import java.util.Vector;

public class Calculator {

	private void sortPlayers() {
		//sort by ranking
		boolean isSorted = false;
		while (!isSorted) {
			isSorted = true;

			for (int i = 0; i < playersStats.size() - 1; i++) {
				if (playersStats.get(i).ranking.value < playersStats.get(i + 1).ranking.value) {
					isSorted = false;
					Collections.swap(playersStats, i, i + 1);
				}
			}
		}

		//sort by ranking kickers
		isSorted = false;
		while (!isSorted) {
			isSorted = true;

			for (int i = 0; i < playersStats.size() - 1; i++) {
				if (playersStats.get(i).ranking == playersStats.get(i + 1).ranking) {
					if (playersStats.get(i).rankingKickers.get(0).rank.value < playersStats.get(i + 1).rankingKickers.get(0).rank.value) {
						isSorted = false;
						Collections.swap(playersStats, i, i + 1);
					} else if (playersStats.get(i).rankingKickers.get(0).rank == playersStats.get(i + 1).rankingKickers.get(0).rank) {
						if (playersStats.get(i).rankingKickers.size() == 2) {
							if (playersStats.get(i).rankingKickers.get(1).rank.value < playersStats.get(i + 1).rankingKickers.get(1).rank.value) {
								isSorted = false;
								Collections.swap(playersStats, i, i + 1);
							}
						}
					}
				}
			}

		}

	}

	public Vector<Stats> playersStats = new Vector<>();

	public Calculator(Table table) {
		for (Player player : table.getPlayers()) {
			Stats stats = new Stats(table.getDeck(), player);
			playersStats.add(stats);
		}
	}

	public void getWinningRates() {
	}

	public void getStats() {
		sortPlayers();

		for (Stats playerStats : playersStats) {
			System.out.println(playerStats.player.name + " has " +
					playerStats.ranking + " " +
					Card.toShortStrings(playerStats.rankingKickers) +
					"with " + Card.toShortStrings(playerStats.bestHand));

		}
	}
}
