import java.util.ArrayList;
import java.util.Collections;

public class Table {
	private ArrayList<Card> cardDeck = new ArrayList<>();

	private ArrayList<Card> commonCards = new ArrayList<>();

	private int playersAmount;

	private ArrayList<Player> players = new ArrayList<>();

	private void setCards() {
		while (cardDeck.size() != 52) {
			Card card = new Card();
			add(card);
		}

	}

	private boolean isFilled() {
		for (Card card : cardDeck) {
			if (card == null) {
				return false;
			}
		}

		return true;
	}

	private boolean contains(Card currentCard) {
		for (Card card : cardDeck) {
			if (card == currentCard) {
				return true;
			}
		}

		return false;
	}

	private void add(Card card) {
		for (Card i : cardDeck) {
			if (i.rank.value == card.rank.value && i.suit.value == card.suit.value) {
				return;
			}
		}

		cardDeck.add(card);
	}

	private void setDeck() {
		ArrayList<Card> deck = new ArrayList<>();

		int i = 0;
		while (deck.size() != 5) {
			if (i >= cardDeck.size()) {
				this.commonCards = deck;
			}

			if (!cardDeck.get(i).isTaken) {
				cardDeck.get(i).isTaken = true;
				deck.add(cardDeck.get(i));
			}
			++i;
		}

		this.commonCards = deck;
	}

	private void setFlop() {
		if (!commonCards.isEmpty()) return;
		ArrayList<Card> flop = new ArrayList<>();

		int i = 0;
		while (flop.size() != 3) {
			if (i >= cardDeck.size()) {
				this.commonCards = flop;
			}

			if (!cardDeck.get(i).isTaken) {
				cardDeck.get(i).isTaken = true;
				flop.add(cardDeck.get(i));
			}
			++i;
		}

		this.commonCards = flop;
	}

	private void setTurn() {
		if (commonCards.size() != 3) return;
		Card card = null;

		int i = 0;
		while (i <= cardDeck.size()) {
			if (!cardDeck.get(i).isTaken) {
				cardDeck.get(i).isTaken = true;
				card = cardDeck.get(i);
				break;
			}
			++i;
		}

		this.commonCards.add(card);
	}

	private void setRiver() {
		if (commonCards.size() != 4) return;
		Card card = null;

		int i = 0;
		while (i <= cardDeck.size()) {
			if (!cardDeck.get(i).isTaken) {
				cardDeck.get(i).isTaken = true;
				card = cardDeck.get(i);
				break;
			}
			++i;
		}

		this.commonCards.add(card);
	}

	private ArrayList<Card> getHand() {
		ArrayList<Card> tempCards = new ArrayList<>();

		int i = 0;
		while (tempCards.size() != 2) {
			if (i >= cardDeck.size()) {
				return tempCards;
			}

			if (!cardDeck.get(i).isTaken) {
				tempCards.add(cardDeck.get(i));
				cardDeck.get(i).isTaken = true;
			}
			++i;
		}

		return tempCards;
	}

	public enum State {
		NONE,
		FLOP,
		TURN,
		RIVER
	}

	public State state = State.NONE;

	public static void sortCards(ArrayList<Card> cards) {
		boolean sorted = false;

		while (!sorted) {
			sorted = true;

			for (int i = 0; i < cards.size() - 1; i++) {
				if (cards.get(i).rank.value < cards.get(i + 1).rank.value) {
					sorted = false;
					Collections.swap(cards, i, i + 1);
				}
			}

			for (int i = 0; i < cards.size() - 1; i++) {
				if (cards.get(i).rank == cards.get(i + 1).rank) {
					if (cards.get(i).suit.value < cards.get(i + 1).suit.value) {
						sorted = false;
						Collections.swap(cards, i, i + 1);
					}
				}
			}

		}

	}

	public Table() {
		setCards();
		setDeck();
//		sortCards();
	}

	public ArrayList<Card> getCommonCards() {
		return commonCards;
	}

	public ArrayList<Card> getCardDeck() {
		return cardDeck;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Table(int playersAmount) {
		if (playersAmount > 23) return;
		this.playersAmount = playersAmount;


	}

	public void newHand() {
		setCards();

		for (int i = 0; i < playersAmount; i++) {
			addPlayer("Player " + Integer.toString(i + 1));
		}
	}

	public void addPlayer(String name) {
		Player player = new Player(name);
		player.hand = getHand();
		player.sortHand();
		players.add(player);
	}

	public void showPlayersHands() {
		if (players.size() > 23) return;
		for (int i = 0; i < players.size(); i++) {
			System.out.print("Player " + (i + 1) + ": ");
			players.get(i).printHand();
			System.out.println();
		}
	}

	public void showTableCards() {
		setDeck();
		sortCards(commonCards);

		state = State.RIVER;
		System.out.print("Table cardDeck: ");
		for (Card handCard : commonCards) {
			handCard.isTaken = true;
			System.out.print(Card.toShortString(handCard, true) + " ");
		}
		System.out.println();

		Spy spy = new Spy(this);
	}

	public void showFlop() {
		setFlop();
		sortCards(commonCards);
		if (players.isEmpty()) return;
		state = State.FLOP;
		System.out.print("Flop: ");
		for (int i = 0; i < 3; i++) {
			System.out.print(Card.toShortString(cardDeck.get(i), true) + " ");
		}
		System.out.println();

		Spy spy = new Spy(this);
	}

	public void showTurn() {
		setTurn();
		sortCards(commonCards);
		if (players.isEmpty()) return;
		state = State.TURN;
		System.out.print("Turn: " + Card.toShortString(cardDeck.get(3), true) + " ");
		System.out.println();

		Spy spy = new Spy(this);

	}

	public void showRiver() {
		setRiver();
		sortCards(commonCards);
		if (players.isEmpty()) return;
		state = State.RIVER;
		System.out.print("River: " + Card.toShortString(cardDeck.get(4), true) + " ");
		System.out.println();

		Spy spy = new Spy(this);
	}

}