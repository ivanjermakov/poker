import java.util.Vector;

class Poker {
	private Vector<Card> cards = new Vector<>();

	private void fill() {
		cards.setSize(52);

		while (!isFilled()) {
			Card card = new Card();

			if (!contains(card)) {
				add(card);
			}
		}

	}

	private boolean isFilled() {
		for (Card card : cards) {
			if (card == null) {
				return false;
			}
		}

		return true;
	}

	private boolean contains(Card currentCard) {
		for (Card card : cards) {
			if (card == currentCard) {
				return true;
			}
		}

		return false;
	}

	private void add(Card currentCard) {
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i) == null) {
				cards.set(i, currentCard);
				break;
			}
		}
	}

	Poker() {
		fill();
	}

	void hand() {
		System.out.print("Hand: ");
		for (int i = 0; i < 5; i++) {
			System.out.print(Card.toShortString(cards.get(i)) + " ");
		}
	}
}
