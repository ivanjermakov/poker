import java.util.*;

public class Calculator {

	private Table.State state = Table.State.NONE;

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

	private void calculateFlopRates() {
		System.out.println("Calculating flop rates...");
	}

	private void calculateTurnRates() {
		System.out.println("Calculating turn rates...");

	}

	private static boolean isSameKickersRank(ArrayList<Card> hand1, ArrayList<Card> hand2) {
		if (hand1.isEmpty() && hand2.isEmpty()) return true;
		if (hand1.size() == hand2.size()) {
			for (int i = 0; i < hand1.size(); i++) {
				if (hand1.get(i).rank.value != hand2.get(i).rank.value) {
					return false;
				}
			}
		}
		return true;
	}

	private void calculateRiverRates() {
		System.out.println("Calculating river rates...");
		//all the winners should have hand as player at [0]
		Stats winningStats = playersStats.get(0);

		//all the winners would divide rate
		ArrayList<Stats> winnersStats = new ArrayList<>();
		for (Stats playerStats : playersStats) {
			if (playerStats.ranking == winningStats.ranking &&
					isSameKickersRank(playerStats.rankingKickers, winningStats.rankingKickers)) {
				winnersStats.add(playerStats);
			}
		}

		//divide rates (to one decimal)
		double winnersRate = Math.round(1.0 / winnersStats.size() * 10.0) / 10.0;
		for (Stats winnerStats : winnersStats) {
			winnerStats.winningRate = winnersRate;
		}

		//all losers rates = 0
		for (Stats playerStats : playersStats) {
			//he's not winner
			if (playerStats.winningRate == -1.0) {
				playerStats.winningRate = 0.0;
			}
		}
	}

	public ArrayList<Stats> playersStats = new ArrayList<>();

	public Calculator(Table table) {
		state = table.state;
		if (state == Table.State.RIVER) {
			for (Player player : table.getPlayers()) {
				Stats stats = new Stats(table.getDeck(), player);
				playersStats.add(stats);
			}
		}
	}

	public void calculateWinningRates() {
		switch (state) {
			case FLOP:
				calculateFlopRates();
				break;
			case TURN:
				calculateTurnRates();
				break;
			case RIVER:
				calculateRiverRates();
				break;
			default:
		}
	}

	public void calculateStats() {
		sortPlayersStats();
	}

	public void getStats() {
//		try {
//			PrintWriter out = new PrintWriter("output.txt", "utf-8");

		for (Stats playerStats : playersStats) {
			System.out.println(playerStats.player.name + " has " +
					playerStats.ranking + " " +
					Card.toShortStrings(playerStats.rankingKickers, true) +
					"with " + Card.toShortStrings(playerStats.bestHand, true) +
			"(" + playerStats.winningRate * 100 + "%)");
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
