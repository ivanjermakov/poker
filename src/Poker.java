import java.util.Vector;

class Poker {
	private Vector<Card> cards = new Vector<>();

	private Vector<Card> hand = new Vector<>();

	private Vector<Player> players = new Vector<>();

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

	private void getHand() {
		Vector<Card> hand = new Vector<>();

		int i = 0;
		while (hand.size() != 5) {
			if (i >= cards.size()) {
				this.hand = hand;
			}

			if (!cards.get(i).isTaken) {
				cards.get(i).isTaken = true;
				hand.add(cards.get(i));
			}
			++i;
		}

		this.hand = hand;
	}

	private Vector<Card> getCards(int amount) {
		Vector<Card> tempCards = new Vector<>();

		int i = 0;
		while (tempCards.size() != amount) {
			if (i >= cards.size()) {
				return tempCards;
			}

			if (!cards.get(i).isTaken) {
				tempCards.add(cards.get(i));
				cards.get(i).isTaken = true;
			}
			++i;
		}

		return tempCards;
	}

	Poker() {
		fill();
	}

	void addPlayer() {
		Player player = new Player();
		player.cards = this.getCards(2);
		players.add(player);
	}

	void playersHands() {
		for (int i = 0; i < players.size(); i++) {
			System.out.print("Player " + (i + 1) + ": ");
			players.get(i).hand();
			System.out.println();
		}
	}

	void hand() {
		getHand();
		System.out.print("Hand: ");
		for (Card handCard : hand) {
			handCard.isTaken = true;
			System.out.print(Card.toShortString(handCard) + " ");
		}
		System.out.println();
	}

	void flop() {
		System.out.print("Flop: ");
		for (int i = 0; i < 3; i++) {
			System.out.print(Card.toShortString(cards.get(i)) + " ");
		}
		System.out.println();
	}

	void turn() {
		System.out.print("Turn: " + Card.toShortString(cards.get(3)) + " ");
		System.out.println();
	}

	void river() {
		System.out.print("River: " + Card.toShortString(cards.get(4)) + " ");
		System.out.println();
	}
}
