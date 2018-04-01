import java.util.*;

class Player {
	
	public String name;
	
	public List<Card> hand;
	
	public void sortHand() {
		if (hand.get(0).rank.value < hand.get(1).rank.value) {
			Collections.swap(hand, 0, 1);
		}
		
		if (hand.get(0).rank == hand.get(1).rank) {
			if (hand.get(0).suit.value < hand.get(1).suit.value) {
				Collections.swap(hand, 0, 1);
			}
		}
	}
	
	public Player(String name) {
		this.name = name;
	}
	
	public void printHand() {
		for (Card card : hand) {
			System.out.print(Card.toShortString(card, true) + " ");
		}
	}
}