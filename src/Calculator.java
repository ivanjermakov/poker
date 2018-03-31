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

		//TODO: sort by ranking kickers. EG: pair of 2's vs pair of A's
		//sort by kickers
		isSorted = false;
		while (!isSorted) {
			isSorted = true;

			for (int i = 0; i < playersStats.size() - 1; i++) {
				//case with same ranking kicker
				if (!playersStats.get(i).rankingKickers.isEmpty() &&
						playersStats.get(i).ranking.value == playersStats.get(i + 1).ranking.value &&
						playersStats.get(i).rankingKickers.get(0).rank.value == playersStats.get(i).rankingKickers.get(0).rank.value) {

					//case with 2 same ranking kickers
					if (playersStats.get(i).rankingKickers.size() == 2 &&
							playersStats.get(i).rankingKickers.get(1).rank.value == playersStats.get(i).rankingKickers.get(1).rank.value) {

						for (int j = 0; j < 5; j++) {
							if (playersStats.get(i).bestHand.get(j).rank.value <
									playersStats.get(i + 1).bestHand.get(j).rank.value) {
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
		for (Player player : table.players) {
			Stats stats = new Stats(table.deck, player);
			playersStats.add(stats);
		}
	}

	public void getProbabilities() {
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
