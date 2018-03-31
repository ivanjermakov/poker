import java.util.*;

class Player {
	Vector<Card> cards = new Vector<>();

	void sortHand() {
		if (cards.get(0).rank.value < cards.get(1).rank.value) {
			Collections.swap(cards, 0, 1);
		}

		if (cards.get(0).rank == cards.get(1).rank) {
			if (cards.get(0).suit.value < cards.get(1).suit.value) {
				Collections.swap(cards, 0, 1);
			}
		}
	}

	Player() {
	}

	void hand() {
		for (Card card : cards) {
			System.out.print(Card.toShortString(card) + " ");
		}
	}
}
