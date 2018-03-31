import java.util.*;

public class Stats {

	enum Ranking {
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

	Player player;

	Vector<Card> bestHand;

	Ranking ranking;

	double winningRate;

	private boolean isFlush(Vector<Card> deck) {
		//check whether all suits same as first card
		Card.Suit suit = deck.get(0).suit;
		for (Card card : deck) {
			if (card.suit != suit) {
				return false;
			}
		}

		return true;
	}

	//TODO: fix D: J 9 8 8 7 H: 6 5
	private boolean isStraight(Vector<Card> deck) {
		int rank = deck.get(0).rank.value;
		for (int i = 0; i < 5; i++) {
			//Ace as 1
			if (i == 4 && deck.get(i - 1).rank.value == 2 && deck.get(i + 1).rank.value == 14) {
				return true;
			}
			if (deck.get(i).rank.value != rank) {
				return false;
			}
			rank--;
		}

		return true;
	}

	private boolean isFourOfAKind(Vector<Card> deck) {
		return deck.get(0).rank == deck.get(3).rank || deck.get(1).rank == deck.get(4).rank;
	}

	private boolean isFullHouse(Vector<Card> deck) {
		return (deck.get(0).rank == deck.get(1).rank && deck.get(2).rank == deck.get(4).rank) ||
				(deck.get(0).rank == deck.get(2).rank && deck.get(3).rank == deck.get(4).rank);
	}

	private boolean isThreeOfAKind(Vector<Card> deck) {
		return deck.get(0).rank == deck.get(2).rank ||
				deck.get(1).rank == deck.get(3).rank ||
				deck.get(2).rank == deck.get(4).rank;
	}

	private boolean isTwoPair(Vector<Card> deck) {
		return (deck.get(0).rank == deck.get(1).rank && deck.get(2).rank == deck.get(3).rank) ||
				(deck.get(0).rank == deck.get(1).rank && deck.get(3).rank == deck.get(4).rank) ||
				(deck.get(1).rank == deck.get(2).rank && deck.get(3).rank == deck.get(4).rank);
	}

	private boolean isOnePair(Vector<Card> deck) {
		return (deck.get(0).rank == deck.get(1).rank) ||
				(deck.get(1).rank == deck.get(2).rank) ||
				(deck.get(2).rank == deck.get(3).rank) ||
				(deck.get(3).rank == deck.get(4).rank);
	}

	private Ranking getRanking(Vector<Card> deck) {
		//sort
		Table.sortDeck(deck);
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

	private Pair<Ranking, Vector<Card>> getBestKicker(Vector<Pair<Ranking, Vector<Card>>> hand) {

		while (hand.size() != 1) {
			for (int i = 0; i < hand.size() - 1; i++) {
				for (int j = 0; j < hand.size(); j++) {
					if (hand.get(i).second.get(j).rank.value < hand.get(i + 1).second.get(j).rank.value) {
						hand.remove(i);
						break;
					}
					if (hand.get(i).second.get(j).rank.value > hand.get(i).second.get(j + 1).rank.value) {
						hand.remove(i + 1);
						break;
					}
				}
			}
		}

		return hand.get(0);
	}

	private Pair<Ranking, Vector<Card>> getBestHand(Vector<Pair<Ranking, Vector<Card>>> rankings) {
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
		Vector<Pair<Ranking, Vector<Card>>> withBestRanking = new Vector<>();

		int i = 0;
		while (i < rankings.size() && rankings.get(0).first.value == rankings.get(i).first.value) {
			withBestRanking.add(rankings.get(i));
			i++;
		}

		return getBestKicker(withBestRanking);
	}

	private void getRanking(final Vector<Card> deck, Vector<Card> hand) {
		Vector<Pair<Ranking, Vector<Card>>> rankings = new Vector<>();

		//table cards only
		Pair pair = new Pair();
		pair.first = getRanking(deck);
		pair.second = deck;
		rankings.add(pair);

		Vector<Card> currentDeck = (Vector) deck.clone();

		//first hand card
		for (int i = 0; i < 5; i++) {
			currentDeck = (Vector) deck.clone();
			currentDeck.set(i, hand.get(0));
			pair = new Pair();
			pair.first = getRanking(currentDeck);
			pair.second = currentDeck;
			rankings.add(pair);
		}

		//second hand card
		for (int i = 0; i < 5; i++) {
			currentDeck = (Vector) deck.clone();
			currentDeck.set(i, hand.get(1));
			pair = new Pair();
			pair.first = getRanking(currentDeck);
			pair.second = currentDeck;
			rankings.add(pair);
		}

		//both hand cards
		for (int i = 0; i < 4; i++) {
			for (int j = i + 1; j < 5; j++) {
				//first is first
				currentDeck = (Vector) deck.clone();
				currentDeck.set(i, hand.get(0));
				currentDeck.set(j, hand.get(1));
				pair = new Pair();
				pair.first = getRanking(currentDeck);
				pair.second = currentDeck;
				rankings.add(pair);
				//second is first
				currentDeck = (Vector) deck.clone();
				currentDeck.set(i, hand.get(1));
				currentDeck.set(j, hand.get(0));
				pair = new Pair();
				pair.first = getRanking(currentDeck);
				pair.second = currentDeck;
				rankings.add(pair);
			}
		}

		ranking = getBestHand(rankings).first;
		bestHand = getBestHand(rankings).second;
	}

	public Stats(Vector<Card> deck, Player player) {
		this.player = player;
		getRanking(deck, player.cards);
	}

}
