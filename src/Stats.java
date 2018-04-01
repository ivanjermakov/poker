import java.util.*;

public class Stats {
	public enum Ranking {
		HIGH_CARD(0),
		ONE_PAIR(1),
		TWO_PAIR(2),
		THREE_OF_A_KIND(3),
		STRAIGHT(4),
		FLUSH(5),
		FULL_HOUSE(6),
		FOUR_OF_A_KIND(7),
		STRAIGHT_FLUSH(8),
		ROYAL_FLUSH(9);

		public int value;

		Ranking(int value) {
			this.value = value;
		}
	}

	public ArrayList<Card> bestHand;
	public Player player;
	public Ranking ranking;
	public ArrayList<Card> rankingKickers = new ArrayList<>();
	public double winningRate = -1.0;

	private boolean isFlush(ArrayList<Card> deck) {
		//check whether all suits same as first card
		Card.Suit suit = deck.get(0).suit;
		for (Card card : deck) {
			if (card.suit != suit) {
				return false;
			}
		}

		return true;
	}

	private boolean isStraight(ArrayList<Card> deck) {
		int rank = deck.get(0).rank.value;
		for (int i = 0; i < 5; i++) {
			//Ace as 1
			if (deck.get(0).rank.value == 14 &&
					deck.get(1).rank.value == 5 &&
					deck.get(2).rank.value == 4 &&
					deck.get(3).rank.value == 3 &&
					deck.get(4).rank.value == 2) {
				return true;
			}
			if (deck.get(i).rank.value != rank) {
				return false;
			}
			rank--;
		}

		return true;
	}

	private boolean isFourOfAKind(ArrayList<Card> deck) {
		return deck.get(0).rank == deck.get(3).rank || deck.get(1).rank == deck.get(4).rank;
	}

	private boolean isFullHouse(ArrayList<Card> deck) {
		return (deck.get(0).rank == deck.get(1).rank && deck.get(2).rank == deck.get(4).rank) ||
				(deck.get(0).rank == deck.get(2).rank && deck.get(3).rank == deck.get(4).rank);
	}

	private boolean isThreeOfAKind(ArrayList<Card> deck) {
		return deck.get(0).rank == deck.get(2).rank ||
				deck.get(1).rank == deck.get(3).rank ||
				deck.get(2).rank == deck.get(4).rank;
	}

	private boolean isTwoPair(ArrayList<Card> deck) {
		return (deck.get(0).rank == deck.get(1).rank && deck.get(2).rank == deck.get(3).rank) ||
				(deck.get(0).rank == deck.get(1).rank && deck.get(3).rank == deck.get(4).rank) ||
				(deck.get(1).rank == deck.get(2).rank && deck.get(3).rank == deck.get(4).rank);
	}

	private boolean isOnePair(ArrayList<Card> deck) {
		return (deck.get(0).rank == deck.get(1).rank) ||
				(deck.get(1).rank == deck.get(2).rank) ||
				(deck.get(2).rank == deck.get(3).rank) ||
				(deck.get(3).rank == deck.get(4).rank);
	}

	private Ranking setRanking(ArrayList<Card> deck) {
		//sort
		Table.sortCards(deck);
		//STRAIGHT, FLUSH, STRAIGHT_FLUSH
		if (isFlush(deck) && isStraight(deck)) {
			//ROYAL_FLUSH
			if (deck.get(4).rank == Card.Rank.TEN) {
				return Ranking.ROYAL_FLUSH;
			} else {
				return Ranking.STRAIGHT_FLUSH;
			}
		}
		if (isFlush(deck)) {
			return Ranking.FLUSH;
		}
		if (isStraight(deck)) {
			return Ranking.STRAIGHT;
		}
		//FOUR_OF_A_KIND
		if (isFourOfAKind(deck)) {
			return Ranking.FOUR_OF_A_KIND;
		}
		//FULL_HOUSE
		if (isFullHouse(deck)) {
			return Ranking.FULL_HOUSE;
		}
		//THREE_OF_A_KIND
		if (isThreeOfAKind(deck)) {
			return Ranking.THREE_OF_A_KIND;
		}
		//TWO_PAIR
		if (isTwoPair(deck)) {
			return Ranking.TWO_PAIR;
		}
		//ONE_PAIR
		if (isOnePair(deck)) {
			return Ranking.ONE_PAIR;
		}

		//HIGH_CARD
		return Ranking.HIGH_CARD;
	}

	private boolean areSimilar(ArrayList<Pair<Ranking, ArrayList<Card>>> deck) {
		if (deck.size() <= 1) return true;

		for (int i = 1; i < deck.size(); i++) {
			if (deck.get(0) != deck.get(i)) {
				return true;
			}
		}

		return false;
	}

	private Pair<Ranking, ArrayList<Card>> getBestKicker(ArrayList<Pair<Ranking, ArrayList<Card>>> deck) {

		while (!areSimilar(deck)) {
			for (int i = 0; i < deck.size() - 1; i++) {
				for (int j = 0; j < deck.size(); j++) {
					if (deck.get(i).second.get(j).rank.value < deck.get(i + 1).second.get(j).rank.value) {
						deck.remove(i);
						break;
					}
					if (deck.get(i).second.get(j).rank.value > deck.get(i).second.get(j + 1).rank.value) {
						deck.remove(i + 1);
						break;
					}
				}
			}
		}

		return deck.get(0);
	}

	private Pair<Ranking, ArrayList<Card>> getBestHand(ArrayList<Pair<Ranking, ArrayList<Card>>> rankings) {
		//get pairs with best ranking
		//sort by rankings
		boolean sorted = false;

		while (!sorted) {
			sorted = true;

			for (int i = 0; i < rankings.size() - 1; i++) {
				if (rankings.get(i).first.value < rankings.get(i + 1).first.value) {
					sorted = false;
					Collections.swap(rankings, i, i + 1);
				}
			}

		}

		//get with best ranking
		ArrayList<Pair<Ranking, ArrayList<Card>>> withBestRanking = new ArrayList<>();

		int i = 0;
		while (i < rankings.size() && rankings.get(0).first.value == rankings.get(i).first.value) {
			withBestRanking.add(rankings.get(i));
			i++;
		}

		return getBestKicker(withBestRanking);
	}

	private void setRanking(final ArrayList<Card> deck, ArrayList<Card> hand) {
		ArrayList<Pair<Ranking, ArrayList<Card>>> rankings = new ArrayList<>();

		//table hand only
		Pair pair = new Pair();
		pair.first = setRanking(deck);
		pair.second = deck;
		rankings.add(pair);

		ArrayList<Card> currentDeck = (ArrayList) deck.clone();

		//first printHand card
		for (int i = 0; i < 5; i++) {
			currentDeck = (ArrayList) deck.clone();
			currentDeck.set(i, hand.get(0));
			pair = new Pair();
			pair.first = setRanking(currentDeck);
			pair.second = currentDeck;
			rankings.add(pair);
		}

		//second printHand card
		for (int i = 0; i < 5; i++) {
			currentDeck = (ArrayList) deck.clone();
			currentDeck.set(i, hand.get(1));
			pair = new Pair();
			pair.first = setRanking(currentDeck);
			pair.second = currentDeck;
			rankings.add(pair);
		}

		//both printHand hand
		for (int i = 0; i < 4; i++) {
			for (int j = i + 1; j < 5; j++) {
				//first is first
				currentDeck = (ArrayList) deck.clone();
				currentDeck.set(i, hand.get(0));
				currentDeck.set(j, hand.get(1));
				pair = new Pair();
				pair.first = setRanking(currentDeck);
				pair.second = currentDeck;
				rankings.add(pair);
				//second is first
				currentDeck = (ArrayList) deck.clone();
				currentDeck.set(i, hand.get(1));
				currentDeck.set(j, hand.get(0));
				pair = new Pair();
				pair.first = setRanking(currentDeck);
				pair.second = currentDeck;
				rankings.add(pair);
			}
		}

		Pair<Ranking, ArrayList<Card>> bestHand = getBestHand(rankings);
		this.ranking = bestHand.first;
		this.bestHand = bestHand.second;

	}

	private void setRankingKicker() {
		switch (ranking) {
			case STRAIGHT_FLUSH:
			case STRAIGHT:
				//Ace as 1
				if (bestHand.get(0).rank.value == 14 && bestHand.get(4).rank.value == 2) {
					bestHand.get(0).rank.value = 1;
					//add Ace
					rankingKickers.add(bestHand.get(0));
				} else {
					rankingKickers.add(bestHand.get(4));
				}
				break;
			case FOUR_OF_A_KIND:
				rankingKickers.add(bestHand.get(1));
				break;
			case FULL_HOUSE:
				rankingKickers.add(bestHand.get(2));
				if (bestHand.get(0).rank == bestHand.get(2).rank) {
					rankingKickers.add(bestHand.get(4));
				} else {
					rankingKickers.add(bestHand.get(0));
				}
				break;
			case THREE_OF_A_KIND:
				rankingKickers.add(bestHand.get(2));
				break;
			case TWO_PAIR:
				rankingKickers.add(bestHand.get(1));
				rankingKickers.add(bestHand.get(4));
				break;
			case ONE_PAIR:
				for (int i = 0; i < 4; i++) {
					if (bestHand.get(i).rank.value == bestHand.get(i + 1).rank.value) {
						rankingKickers.add(bestHand.get(i));
						break;
					}
				}
				break;
			default:
				rankingKickers.add(bestHand.get(0));
		}
	}

	public Stats(ArrayList<Card> deck, Player player) {
		this.player = player;
		setRanking(deck, player.hand);

		setRankingKicker();
	}

}
