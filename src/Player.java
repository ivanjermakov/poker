import java.util.*;

class Player {
	Vector<Card> cards = new Vector<>();

	Player() {
	}

	void hand() {
		for (Card card : cards) {
			System.out.print(Card.toShortString(card) + " ");
		}
	}
}
