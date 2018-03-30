import java.util.*;

class Card {
	private enum Rank {
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE,
		TEN,
		JACK,
		QUEEN,
		KING,
		ACE
	}

	private enum Suit {
		HEARTS,
		DIAMONDS,
		CLUBS,
		SPACES
	}

	Card() {
		Rank rank = Rank.values()[new Random().nextInt(Rank.values().length)];
		Suit suit = Suit.values()[new Random().nextInt(Suit.values().length)];

		System.out.println("New card is " + rank + " of " + suit);
	}
}
