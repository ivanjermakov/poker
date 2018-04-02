import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Calculator {
	
	private List<List<Card>> possibleDecks = new ArrayList<>();
	public Table.State state = Table.State.PREFLOP;
	public List<Player> players = new ArrayList<>();
	
	private void sortByKickers() {
		if (players.size() <= 1) return;
		
		boolean isSorted = false;
		
		while (!isSorted) {
			for (int i = 0; i < players.size() - 1; i++) {
				isSorted = true;
				//same ranking kickers
				//first kicker is the same
				//first col
				if (players.get(i).stats.ranking == players.get(i + 1).stats.ranking &&
						players.get(i).stats.bestHand.get(0).rank.value < players.get(i + 1).stats.bestHand.get(0).rank.value) {
					//has ranking kickers
					if (players.get(i).stats.rankingKickers.size() >= 1 && players.get(i + 1).stats.rankingKickers.size() >= 1) {
						if (players.get(i).stats.rankingKickers.get(0).rank == players.get(i + 1).stats.rankingKickers.get(0).rank) {
							//second ranking kicker is present and the same
							if (players.get(i).stats.rankingKickers.size() == 2 && players.get(i + 1).stats.rankingKickers.size() == 2) {
								if (players.get(i).stats.rankingKickers.get(0).rank == players.get(i + 1).stats.rankingKickers.get(0).rank &&
										players.get(i).stats.ranking == players.get(i + 1).stats.ranking &&
										players.get(i).stats.bestHand.get(0).rank.value < players.get(i + 1).stats.bestHand.get(0).rank.value) {
									isSorted = false;
									Collections.swap(players, i, i + 1);
									break;
								}
							} else {
								isSorted = false;
								Collections.swap(players, i, i + 1);
								break;
							}
						}
						//no ranking kickers
					} else {
						isSorted = false;
						Collections.swap(players, i, i + 1);
						break;
					}
					
				}
			}
		}
		
	}
	
	private void sortPlayersStats() {
		//sort by ranking
		boolean isSorted = false;
		while (!isSorted) {
			isSorted = true;
			
			for (int i = 0; i < players.size() - 1; i++) {
				if (players.get(i).stats.ranking.value < players.get(i + 1).stats.ranking.value) {
					isSorted = false;
					Collections.swap(players, i, i + 1);
				}
			}
			
		}
		
		//sort by ranking kickers
		isSorted = false;
		while (!isSorted) {
			isSorted = true;
			
			for (int i = 0; i < players.size() - 1; i++) {
				
				if (players.get(i).stats.ranking == players.get(i + 1).stats.ranking &&
						!players.get(i).stats.rankingKickers.isEmpty()) {
					
					if (players.get(i).stats.rankingKickers.get(0).rank.value <
							players.get(i + 1).stats.rankingKickers.get(0).rank.value) {
						isSorted = false;
						Collections.swap(players, i, i + 1);
					} else if (players.get(i).stats.rankingKickers.get(0).rank ==
							players.get(i + 1).stats.rankingKickers.get(0).rank) {
						if (players.get(i).stats.rankingKickers.size() == 2 &&
								players.get(i + 1).stats.rankingKickers.size() == 2) {
							if (players.get(i).stats.rankingKickers.get(1).rank.value <
									players.get(i + 1).stats.rankingKickers.get(1).rank.value) {
								isSorted = false;
								Collections.swap(players, i, i + 1);
							}
						}
					}
				}
				
			}
			
		}
		
		//sort by kickers (with the same ranking kickers)
		sortByKickers();
	}
	
	private List<Card> getContainedCards(List<Card> cards) {
		List<Card> containedCards = new ArrayList<>();
		
		for (Card card : cards) {
			if (!card.isTaken) {
				Card newCard = new Card(card);
				containedCards.add(newCard);
			}
		}
		
		return containedCards;
	}
	
	private void setPossibleFlopDecks(List<Card> flop, List<Card> cards) {
		for (int i = 0; i < cards.size() - 1; i++) {
			for (int j = i + 1; j < cards.size(); j++) {
				List<Card> possibleDeck = new ArrayList<>(flop);
				possibleDeck.add(cards.get(i));
				possibleDeck.add(cards.get(j));
				possibleDecks.add(possibleDeck);
			}
		}
	}
	
	private void setPossibleTurnDecks(List<Card> turn, List<Card> cards) {
		for (int i = 0; i < cards.size() - 1; i++) {
			List<Card> possibleDeck = new ArrayList<>(turn);
			possibleDeck.add(cards.get(i));
			possibleDecks.add(possibleDeck);
		}
	}
	
	private void calculateFlopRates() {
	}
	
	private void calculateTurnRates() {
	}
	
	private void calculateRiverRates() {
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
		
		//divide rates (to three decimal)
		double winnersRate = 1.0 / winnersStats.size();
		winnersRate = Math.round(winnersRate * 1000) / 1000d;
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
	
	public Calculator(Table table) {
		state = table.state;
		switch (state) {
			case RIVER:
				for (Player player : table.players) {
					player.setStats(table);
					players.add(player);
				}
				break;
			case TURN:
				setPossibleTurnDecks(table.commonCards, getContainedCards(table.cardDeck));
				break;
			case FLOP:
				setPossibleFlopDecks(table.commonCards, getContainedCards(table.cardDeck));
				break;
		}
	}
	
	public void calculateWinningRates(Table table) {
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
	
}
