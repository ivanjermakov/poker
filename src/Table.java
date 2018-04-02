import java.util.*;

public class Table {
	
	public enum State {
		PREFLOP,
		FLOP,
		TURN,
		RIVER
		
	}
	
	public List<Card> cardDeck;
	
	public List<Card> commonCards = new ArrayList<>();
	
	public List<Player> players = new ArrayList<>();
	
	public State state = State.PREFLOP;
	
	
	public static void sortCards(List<Card> cards) {
		boolean isSorted = false;
		
		while (!isSorted) {
			isSorted = true;
			
			for (int i = 0; i < cards.size() - 1; i++) {
				if (cards.get(i).rank.value < cards.get(i + 1).rank.value) {
					isSorted = false;
					Collections.swap(cards, i, i + 1);
				}
			}
			
			for (int i = 0; i < cards.size() - 1; i++) {
				if (cards.get(i).rank == cards.get(i + 1).rank) {
					if (cards.get(i).suit.value < cards.get(i + 1).suit.value) {
						isSorted = false;
						Collections.swap(cards, i, i + 1);
					}
				}
			}
			
		}
		
	}
	
	public Table(int playersAmount) {
		if (playersAmount > 23) return;
		for (int i = 0; i < playersAmount; i++) {
			addPlayer("Player " + Integer.toString(i + 1));
		}
	}
	
	public void addPlayer(String name) {
		Player player = new Player(name);
		players.add(player);
	}
	
	public void newGame() {
		System.out.println("---------------- New game ----------------");
		setCardDeck();
		setHands();
	}
	
	public void showPlayersHands() {
		if (players.size() > 23) return;
		for (int i = 0; i < players.size(); i++) {
			System.out.print("Player " + (i + 1) + ": ");
			players.get(i).printHand();
			System.out.println();
		}
	}
	
	public void showCommonCards() {
		setCommonCards();
		sortCards(commonCards);
		
		state = State.RIVER;
		System.out.print("Common cards: ");
		for (Card handCard : commonCards) {
			handCard.isTaken = true;
			System.out.print(Card.toShortString(handCard, true) + " ");
		}
		System.out.println();
		
		new Spy(this);
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
		
		new Spy(this);
	}
	
	public void showTurn() {
		setTurn();
		sortCards(commonCards);
		if (players.isEmpty()) return;
		state = State.TURN;
		System.out.print("Turn: " + Card.toShortString(cardDeck.get(3), true) + " ");
		System.out.println();
		
		new Spy(this);
	}
	
	public void showRiver() {
		setRiver();
		sortCards(commonCards);
		if (players.isEmpty()) return;
		state = State.RIVER;
		System.out.print("River: " + Card.toShortString(cardDeck.get(4), true) + " ");
		System.out.println();
		
		new Spy(this);
	}
	
	private void add(Card currentCard) {
		for (Card card : cardDeck) {
			if (card.rank.value == currentCard.rank.value &&
					card.suit.value == currentCard.suit.value) {
				return;
			}
		}
		cardDeck.add(currentCard);
	}
	
	private void setCardDeck() {
		cardDeck = new ArrayList<>();
		
		while (cardDeck.size() != 52) {
			Card card = new Card();
			add(card);
		}
	}
	
	private void setHands() {
		for (Player player : players) {
			player.hand = getHand();
			sortCards(player.hand);
		}
	}
	
	private void setCommonCards() {
		List<Card> newCommonCards = new ArrayList<>();
		
		int i = 0;
		while (newCommonCards.size() != 5) {
			if (i >= cardDeck.size()) {
				commonCards = newCommonCards;
			}
			if (!cardDeck.get(i).isTaken) {
				cardDeck.get(i).isTaken = true;
				newCommonCards.add(cardDeck.get(i));
			}
			++i;
		}
		
		this.commonCards = newCommonCards;
	}
	
	private void setFlop() {
		if (!commonCards.isEmpty()) return;
		List<Card> flop = new ArrayList<>();
		
		int i = 0;
		while (flop.size() != 3) {
			if (i >= cardDeck.size()) {
				commonCards = flop;
			}
			if (!cardDeck.get(i).isTaken) {
				cardDeck.get(i).isTaken = true;
				flop.add(cardDeck.get(i));
			}
			++i;
		}
		
		commonCards = flop;
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
		
		commonCards.add(card);
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
		
		commonCards.add(card);
	}
	
	private List<Card> getHand() {
		List<Card> hand = new ArrayList<>();
		
		int i = 0;
		while (hand.size() != 2) {
			if (i >= cardDeck.size()) {
				return hand;
			}
			if (!cardDeck.get(i).isTaken) {
				hand.add(cardDeck.get(i));
				cardDeck.get(i).isTaken = true;
			}
			++i;
		}
		
		return hand;
	}
	
}