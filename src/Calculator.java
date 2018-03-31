import java.io.*;
import java.util.*;

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
				//	NOT THE CASE WITH STRAIGHT TO ACE
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

		}

		//straight to Ace
		for (int i = 0; i < playersStats.size() - 1; i++) {
			if (playersStats.get(i).ranking == playersStats.get(i + 1).ranking) {
				if (playersStats.get(i).ranking == Stats.Ranking.STRAIGHT ||
						playersStats.get(i).ranking == Stats.Ranking.STRAIGHT_FLUSH) {
					//any straight better than straight to Ace
					if (playersStats.get(i).rankingKickers.get(0).rank.value == 14 &&
							playersStats.get(i + 1).rankingKickers.get(0).rank.value != 14) {
						isSorted = false;
						Collections.swap(playersStats, i, i + 1);
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

//		try {
//			PrintWriter out = new PrintWriter("output.txt", "utf-8");


			for (Stats playerStats : playersStats) {
				System.out.println(playerStats.player.name + " has " +
						playerStats.ranking + " " +
						Card.toShortStrings(playerStats.rankingKickers, true) +
						"with " + Card.toShortStrings(playerStats.bestHand, true));
//				out.append(playerStats.player.name)
//						.append(" has ")
//						.append(String.valueOf(playerStats.ranking))
//						.append(" ")
//						.append(Card.toShortStrings(playerStats.rankingKickers, false))
//						.append("with ").append(Card.toShortStrings(playerStats.bestHand, false))
//						.append(String.valueOf('\n'));
			}
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
