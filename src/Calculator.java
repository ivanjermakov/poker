import java.util.Collections;
import java.util.Vector;

public class Calculator {

	private void sortPlayersStats() {
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

//		sort by ranking kickers
		isSorted = false;
		while (!isSorted) {
			isSorted = true;

			for (int i = 0; i < playersStats.size() - 1; i++) {

				if (playersStats.get(i).ranking != Stats.Ranking.STRAIGHT &&
						playersStats.get(i).ranking != Stats.Ranking.STRAIGHT_FLUSH &&
						playersStats.get(i).rankingKickers.get(0).rank.value != 14) {
					if (playersStats.get(i).ranking == playersStats.get(i + 1).ranking) {
						if (playersStats.get(i).rankingKickers.get(0).rank.value <
								playersStats.get(i + 1).rankingKickers.get(0).rank.value) {
							isSorted = false;
							Collections.swap(playersStats, i, i + 1);
						} else if (playersStats.get(i).rankingKickers.get(0).rank ==
								playersStats.get(i + 1).rankingKickers.get(0).rank) {
							if (playersStats.get(i).rankingKickers.size() == 2 &&
									playersStats.get(i + 1).rankingKickers.size() == 2) {
								if (playersStats.get(i).rankingKickers.get(1).rank.value <
										playersStats.get(i + 1).rankingKickers.get(1).rank.value) {
									isSorted = false;
									Collections.swap(playersStats, i, i + 1);
								}
							}
						}
					}
				}

			}

			for (int i = 0; i < playersStats.size() - 1; i++) {
				if (playersStats.get(i).ranking == playersStats.get(i + 1).ranking) {
					//straight to Ace
					if (playersStats.get(i).ranking == Stats.Ranking.STRAIGHT ||
							playersStats.get(i).ranking == Stats.Ranking.STRAIGHT_FLUSH) {
						//any straight better than straight to Ace
						if (playersStats.get(i).rankingKickers.get(0).rank.value == 14 &&
								playersStats.get(i + 1).rankingKickers.get(0).rank.value != 14) {

							System.out.println("swap " +
									Card.toShortString(playersStats.get(i).rankingKickers.get(0)) +
									" " + Card.toShortString(playersStats.get(i + 1).rankingKickers.get(0)));

							isSorted = false;
							Collections.swap(playersStats, i, i + 1);
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
		sortPlayersStats();

		for (Stats playerStats : playersStats) {
			System.out.println(playerStats.player.name + " has " +
					playerStats.ranking + " " +
					Card.toShortStrings(playerStats.rankingKickers) +
					"with " + Card.toShortStrings(playerStats.bestHand));

		}
	}
}
