import java.util.*;

class Card {
	enum Rank {
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

	private Rank rank;

	enum Suit {
		HEARTS,
		DIAMONDS,
		CLUBS,
		SPADES
	}

	private Suit suit;

	boolean isTaken = false;

	Card() {
		rank = Rank.values()[new Random().nextInt(Rank.values().length)];
		suit = Suit.values()[new Random().nextInt(Suit.values().length)];

//		System.out.println("New card is " + rank + " of " + suit);
	}

	static String toShortString(Card card) {
		String rank;
		switch (card.rank) {
			case TWO:
				rank = "2";
				break;
			case THREE:
				rank = "3";
				break;
			case FOUR:
				rank = "4";
				break;
			case FIVE:
				rank = "5";
				break;
			case SIX:
				rank = "6";
				break;
			case SEVEN:
				rank = "7";
				break;
			case EIGHT:
				rank = "8";
				break;
			case NINE:
				rank = "9";
				break;
			case TEN:
				rank = "10";
				break;
			case JACK:
				rank = "J";
				break;
			case QUEEN:
				rank = "Q";
				break;
			case KING:
				rank = "K";
				break;
			case ACE:
				rank = "A";
				break;
			default:
				rank = "und";
		}

		String suit;
		switch (card.suit) {
			case CLUBS:
				suit = "♣";
				break;
			case HEARTS:
				suit = "\u001B[31m" + "♥" + "\u001B[0m";
				break;
			case SPADES:
				suit = "♠";
				break;
			case DIAMONDS:
				suit = "\u001B[31m" + "♦" + "\u001B[0m";
				break;
			default:
				suit = "und";
		}

		return rank + suit;
	}
}
