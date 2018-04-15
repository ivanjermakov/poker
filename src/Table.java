import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Table {
	
	public enum State {
		PREFLOP,
		FLOP,
		TURN,
		RIVER
	}
	
	public List<Card> cardDeck = new ArrayList<>();
	
	public List<Card> commonCards;
	
	public List<Player> players = new ArrayList<>();
	
	public State state = State.PREFLOP;
	
	
	public static void sortCards(List<Card> cards) {
		cards.sort((o1, o2) -> {
			if (o1.rank == o2.rank) {
				return o2.suit.value - o1.suit.value;
			}
			
			return o2.rank.value - o1.rank.value;
		});
	}
	
	public Table(int playersAmount) {
		if (playersAmount > 23) {
			throw new IllegalArgumentException("Players amount cannot be larger than 23");
		}
		
		IntStream
				.range(0, playersAmount)
				.forEach(e -> addPlayer("Player " + Integer.toString(e + 1)));
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
	
	public void showResults() {
		new Spy(this);
	}
	
	public void showPlayersHands() {
		if (players.size() > 23) return;
		for (Player player : players) {
			System.out.print(player.name + " : " + Card.toShortStrings(player.hand, true) + '\n');
		}
	}
	
	public void showCommonCards() {
		setCommonCards();
		System.out.print("Common cards: " + Card.toShortStrings(commonCards, true) + "\n");
		
		new Spy(this);
	}
	
	public void setCommonCards() {
		if (commonCards != null) {
			throw new IllegalStateException("Unable to set common cards");
		}
		
		List newCommonCards = new ArrayList<>();
		
		for (int i = 0; i < 5; i++) {
			newCommonCards.add(cardDeck.get(0));
			cardDeck.remove(cardDeck.get(0));
		}
		
		this.commonCards = newCommonCards;
		
		sortCards(commonCards);
		state = State.RIVER;
	}
	
	public void showFlop() {
		setFlop();
		System.out.print("Flop: " + Card.toShortStrings(commonCards, true) + " \n");
		
		new Spy(this);
	}
	
	public void showTurn() {
		setTurn();
		System.out.print("Turn: " + Card.toShortString(commonCards.get(3), true) + " " + '\n');
		
		new Spy(this);
	}
	
	public void showRiver() {
		setRiver();
		
		System.out.print("River: " + Card.toShortString(commonCards.get(4), true) + " " + '\n');
		
		new Spy(this);
	}
	
	public void setFlop() {
		if (commonCards != null) {
			throw new IllegalStateException("Unable to set flop");
		}
		
		if (commonCards != null) return;
		List flop = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			flop.add(cardDeck.get(0));
			cardDeck.remove(cardDeck.get(0));
		}
		
		commonCards = flop;
		
		sortCards(commonCards);
		if (players.isEmpty()) return;
		state = State.FLOP;
	}
	
	public void setTurn() {
		if (commonCards == null || commonCards.size() != 3) {
			throw new IllegalStateException("Unable to set turn");
		}
		
		Card card;
		
		card = cardDeck.get(0);
		cardDeck.remove(cardDeck.get(0));
		
		commonCards.add(card);
		
		sortCards(commonCards);
		if (players.isEmpty()) return;
		state = State.TURN;
	}
	
	public void setRiver() {
		if (commonCards == null || commonCards.size() != 4) {
			throw new IllegalStateException("Unable to set river");
		}
		
		Card card;
		
		card = cardDeck.get(0);
		cardDeck.remove(cardDeck.get(0));
		
		commonCards.add(card);
		
		sortCards(commonCards);
		if (players.isEmpty()) return;
		state = State.RIVER;
	}
	
	private void setCardDeck() {
		for (int i = 0; i < Card.Rank.values().length; i++) {
			for (int j = 0; j < Card.Suit.values().length; j++) {
				cardDeck.add(new Card(Card.Rank.values()[i], Card.Suit.values()[j]));
			}
		}
		
		Collections.shuffle(cardDeck);
	}
	
	private void setHands() {
		for (Player player : players) {
			player.hand = getHand();
			sortCards(player.hand);
		}
	}
	
	private List getHand() {
		List hand = new ArrayList<>();
		
		while (hand.size() != 2) {
			hand.add(cardDeck.get(0));
			cardDeck.remove(cardDeck.get(0));
		}
		
		return hand;
	}
	
}