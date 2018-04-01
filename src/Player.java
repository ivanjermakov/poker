import java.util.*;

class Player {

	public String name;

	public ArrayList<Card> cards;

	public void sortHand() {
		if (cards.get(0).rank.value < cards.get(1).rank.value) {
			Collections.swap(cards, 0, 1);
		}

		if (cards.get(0).rank == cards.get(1).rank) {
			if (cards.get(0).suit.value < cards.get(1).suit.value) {
				Collections.swap(cards, 0, 1);
			}
		}
	}

	public Player(String name) {
		this.name = name;
	}

	public void printHand() {
		for (Card card : cards) {
			System.out.print(Card.toShortString(card, true) + " ");
		}
	}
}
