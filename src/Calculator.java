import java.util.*;

public class Calculator {
	
	private List<List<Card>> possibleDecks = new ArrayList<>();
	public Table.State state = Table.State.PREFLOP;
	public List<Stats> playersStats = new ArrayList<>();
	
	private void sortByKickers() {
		if (playersStats.size() == 1) return;
		
		boolean isSorted = false;
		
		while (!isSorted) {
			for (int i = 0; i < playersStats.size() - 1; i++) {
				isSorted = true;
				//same ranking kickers
				//first kicker is the same
				//first col
				if (playersStats.get(i).ranking == playersStats.get(i + 1).ranking &&
						playersStats.get(i).bestHand.get(0).rank.value < playersStats.get(i + 1).bestHand.get(0).rank.value) {
					//has ranking kickers
					if (playersStats.get(i).rankingKickers.size() >= 1 && playersStats.get(i + 1).rankingKickers.size() >= 1) {
						if (playersStats.get(i).rankingKickers.get(0).rank == playersStats.get(i + 1).rankingKickers.get(0).rank) {
							//second ranking kicker is present and the same
							if (playersStats.get(i).rankingKickers.size() == 2 && playersStats.get(i + 1).rankingKickers.size() == 2) {
								if (playersStats.get(i).rankingKickers.get(0).rank == playersStats.get(i + 1).rankingKickers.get(0).rank &&
										playersStats.get(i).ranking == playersStats.get(i + 1).ranking &&
										playersStats.get(i).bestHand.get(0).rank.value < playersStats.get(i + 1).bestHand.get(0).rank.value) {
									isSorted = false;
									Collections.swap(playersStats, i, i + 1);
									break;
								}
							} else {
								isSorted = false;
								Collections.swap(playersStats, i, i + 1);
								break;
							}
						}
						//no ranking kickers
					} else {
						isSorted = false;
						Collections.swap(playersStats, i, i + 1);
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
				
				if (playersStats.get(i).ranking == playersStats.get(i + 1).ranking &&
						!playersStats.get(i).rankingKickers.isEmpty()) {
					
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
		Stats winningStats = playersStats.get(0);
		
		//all the winners would divide rate
		List<Stats> winnersStats = new ArrayList<>();
		for (Stats playerStats : playersStats) {
			if (playerStats.ranking == winningStats.ranking &&
					isSameHandsRanks(playerStats.rankingKickers, winningStats.rankingKickers)) {
				winnersStats.add(playerStats);
			}
		}
		
		//divide rates (to three decimal)
		double winnersRate = 1.0 / winnersStats.size();
		winnersRate = Math.round(winnersRate * 1000) / 1000d;
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
					Stats stats = new Stats(table.commonCards, player);
					playersStats.add(stats);
				}
				break;
			case TURN:
				setPossibleTurnDecks(table.commonCards, (ArrayList) getContainedCards(table.cardDeck));
				break;
			case FLOP:
				setPossibleFlopDecks(table.commonCards, (ArrayList) getContainedCards(table.cardDeck));
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
	
	public void getStats() {
		for (Stats playerStats : playersStats) {
			System.out.println(
					Card.toShortStrings(playerStats, true) + " " +
							playerStats.player.name + " has " +
							playerStats.ranking + " " +
							Card.toShortStrings(playerStats.rankingKickers, true) +
							"(" + playerStats.winningRate + ")"
			);
		}
	}
}
