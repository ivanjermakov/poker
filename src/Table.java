import java.util.Collections;
import java.util.Vector;

public class Table {
	private Vector<Card> cards = new Vector<>();

	private Vector<Card> deck = new Vector<>();

	private Vector<Player> players = new Vector<>();

	private void getCards() {
		while (cards.size() != 52) {
			Card card = new Card();
			add(card);
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

	private void add(Card card) {
		for (Card i : cards) {
			if (i.rank.value == card.rank.value && i.suit.value == card.suit.value) {
				return;
			}
		}

		cards.add(card);
	}

	private void setDeck() {
		Vector<Card> hand = new Vector<>();

		int i = 0;
		while (hand.size() != 5) {
			if (i >= cards.size()) {
				this.deck = hand;
			}

			if (!cards.get(i).isTaken) {
				cards.get(i).isTaken = true;
				hand.add(cards.get(i));
			}
			++i;
		}

		this.deck = hand;
	}

	private Vector<Card> getHand() {
		Vector<Card> tempCards = new Vector<>();

		int i = 0;
		while (tempCards.size() != 2) {
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

	public static void sortDeck(Vector<Card> deck) {
		boolean sorted = false;

		while (!sorted) {
			sorted = true;

			for (int i = 0; i < 4; i++) {
				if (deck.get(i).rank.value < deck.get(i + 1).rank.value) {
					sorted = false;
					Collections.swap(deck, i, i + 1);
				}
			}

			for (int i = 0; i < 4; i++) {
				if (deck.get(i).rank == deck.get(i + 1).rank) {
					if (deck.get(i).suit.value < deck.get(i + 1).suit.value) {
						sorted = false;
						Collections.swap(deck, i, i + 1);
					}
				}
			}

		}

	}

	public Table() {
		getCards();
		setDeck();
//		sortDeck();
	}

	public Vector<Card> getDeck() {
		return deck;
	}

	public Vector<Player> getPlayers() {
		return players;
	}

	public Table(int playersAmount) {
		if (playersAmount > 23) return;
		getCards();
		setDeck();
		sortDeck(deck);

		for (int i = 0; i < playersAmount; i++) {
			addPlayer("Player " + Integer.toString(i + 1));
		}
	}

	public void addPlayer(String name) {
		Player player = new Player(name);
		player.cards = getHand();
		player.sortHand();
		players.add(player);
	}

	public void showPlayersHands() {
		for (int i = 0; i < players.size(); i++) {
			System.out.print("Player " + (i + 1) + ": ");
			players.get(i).printHand();
			System.out.println();
		}
	}

	public void hand() {
		System.out.print("Table cards: ");
		for (Card handCard : deck) {
			handCard.isTaken = true;
			System.out.print(Card.toShortString(handCard) + " ");
		}
		System.out.println();

		Spy spy = new Spy(this);
	}

	public void flop() {
		System.out.print("Flop: ");
		for (int i = 0; i < 3; i++) {
			System.out.print(Card.toShortString(cards.get(i)) + " ");
		}
		System.out.println();
	}

	public void turn() {
		System.out.print("Turn: " + Card.toShortString(cards.get(3)) + " ");
		System.out.println();
	}

	public void river() {
		System.out.print("River: " + Card.toShortString(cards.get(4)) + " ");
		System.out.println();
	}
}
