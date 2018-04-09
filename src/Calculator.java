import java.util.ArrayList;
import java.util.List;

public class Calculator {
	
	public Table.State state;
	
	public List<Player> players = new ArrayList<>();
	
	private List<List<Card>> possibleCommonCards = new ArrayList<>();
	
	
	public Calculator(Table table) {
		state = table.state;
		
		switch (state) {
			case FLOP:
				setPossibleFlopDecks(table.commonCards, table.cardDeck);
				calculateRates(table);
				break;
			case TURN:
				setPossibleTurnDecks(table.commonCards, table.cardDeck);
				calculateRates(table);
				break;
			case RIVER:
				for (Player player : table.players) {
					player.setStats(table);
					players.add(player);
				}
				break;
		}
	}
	
	public void showStats() {
		for (Player player : players) {
			System.out.println(
					Card.toShortStrings(player, true) + " " +
							player.name + " has " +
							player.stats.ranking + " " +
							Card.toShortStrings(player.stats.rankingKickers, true) +
							//format rate as ##.#%
							"(" + Math.round((player.stats.winningRate * 100) * 1000) / 1000d + "%)"
			);
		}
	}
	
	//TODO: show outs if percentage is lower than 20%
	public void showRates() {
		for (Player player : players) {
			System.out.println(
					Card.toShortStrings(player.hand, true) + " " +
							player.name +
							//format rate as ##.#%
							" (" + Math.round((player.stats.winningRate * 100) * 1000) / 1000d + "%)"
			);
		}
	}
	
	public void sortPlayersStats() {
		//sort by ranking
		players.sort((e, e2) -> (e2.stats.ranking.value - e.stats.ranking.value));
		
		//sort by ranking kickers
		players.sort((o1, o2) -> {
			//o1 and o2 has the same amount of ranking kickers
			if (o1.stats.rankingKickers == null || o2.stats.rankingKickers == null ||
					o1.stats.rankingKickers.size() != o2.stats.rankingKickers.size()) return 0;
			
			if (o1.stats.ranking == o2.stats.ranking &&
					o1.stats.rankingKickers.size() == o2.stats.rankingKickers.size() &&
					o1.stats.rankingKickers.size() >= 1) {
				if (o1.stats.rankingKickers.size() == 2) {
					if (o1.stats.rankingKickers.get(0).rank.value == o2.stats.rankingKickers.get(0).rank.value) {
						return o2.stats.rankingKickers.get(1).rank.value - o1.stats.rankingKickers.get(1).rank.value;
					}
				}
				return o2.stats.rankingKickers.get(0).rank.value - o1.stats.rankingKickers.get(0).rank.value;
			}
			
			return 0;
		});
		
		//sort by kickers (with the same ranking kickers)
		sortByKickers();
	}
	
	public void calculateRates(Table table) {
		players = new ArrayList<>(table.players);
		int summaryRate = 0;
		
		for (List commonCards : possibleCommonCards) {
			//each point is 100% of game on current common cards
			summaryRate++;
			
			for (Player player : players) {
				//set current possible common cards as table common cards
				player.setStats(table, commonCards);
			}
			
			sortPlayersStats();
			calculateWinningRates();
			
			for (Player player : players) {
				player.winRate += player.stats.winningRate;
			}
			
		}
		
		//convert winRate into rates
		for (Player player : players) {
			player.stats.winningRate = player.winRate / summaryRate;
			//reset winRate
			player.winRate = 0;
		}
		
		sortByWinningRate();
	}
	
	public void calculateWinningRates() {
		//all the winners should have hand as player at [0]
		Player winningStats = players.get(0);
		
		//all the winners would divide rate
		List<Player> winnersStats = new ArrayList<>();
		for (Player playerStats : players) {
			if (playerStats.stats.ranking == winningStats.stats.ranking &&
					isSameHandsRanks(playerStats.stats.rankingKickers, winningStats.stats.rankingKickers)) {
				winnersStats.add(playerStats);
			}
		}
		
		//divide rates between winners
		double winnersRate = 1.0 / winnersStats.size();
		for (Player winnerStats : winnersStats) {
			winnerStats.stats.winningRate = winnersRate;
		}
		
		//all losers rates = 0
		for (Player playerStats : players) {
			//he's not winner
			if (playerStats.stats.winningRate == -1.0) {
				playerStats.stats.winningRate = 0.0;
			}
		}
	}
	
	private static boolean isSameHandsRanks(List<Card> hand1, List<Card> hand2) {
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
	
	private void sortByKickers() {
		if (players.size() <= 1) return;

//		same ranking kickers
		players.sort((o1, o2) -> {
			if (o1.stats.ranking == o2.stats.ranking && isSameHandsRanks(o1.stats.rankingKickers, o2.stats.rankingKickers)) {
				if (o1.stats.bestHand.get(0).rank.value != o2.stats.bestHand.get(0).rank.value) {
					return o2.stats.bestHand.get(0).rank.value - o1.stats.bestHand.get(0).rank.value;
				} else {
					
					//if previous card is the same
					for (int i = 1; i < 5; i++) {
						if (o1.stats.bestHand.get(i - 1).rank.value == o2.stats.bestHand.get(i - 1).rank.value) {
							if (o1.stats.bestHand.get(i).rank.value != o2.stats.bestHand.get(i).rank.value) {
								return o2.stats.bestHand.get(i).rank.value - o1.stats.bestHand.get(i).rank.value;
							}
						}
					}
					
				}
			}
			return 0;
		});
		
	}
	
	private void sortByWinningRate() {
		//TODO: investigate why
		players.sort((e, e2) -> (int) (e2.stats.winningRate * 1000 - e.stats.winningRate * 1000));
	}
	
	private void setPossibleFlopDecks(List<Card> flop, List<Card> cards) {
		for (int i = 0; i < cards.size() - 1; i++) {
			for (int j = i + 1; j < cards.size(); j++) {
				List<Card> possibleDeck = new ArrayList<>(flop);
				possibleDeck.add(cards.get(i));
				possibleDeck.add(cards.get(j));
				possibleCommonCards.add(possibleDeck);
			}
		}
	}
	
	private void setPossibleTurnDecks(List<Card> turn, List<Card> cards) {
		for (int i = 0; i < cards.size() - 1; i++) {
			List<Card> possibleDeck = new ArrayList<>(turn);
			possibleDeck.add(cards.get(i));
			possibleCommonCards.add(possibleDeck);
		}
	}
	
}
