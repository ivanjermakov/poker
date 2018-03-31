import java.util.*;

class Card {
	enum Rank {
		TWO(2),
		THREE(3),
		FOUR(4),
		FIVE(5),
		SIX(6),
		SEVEN(7),
		EIGHT(8),
		NINE(9),
		TEN(10),
		JACK(11),
		QUEEN(12),
		KING(13),
		ACE(14);

		public int value;
		Rank(int value) {
			this.value = value;
		}
	}

	Rank rank;

	enum Suit {
		SPADES(0),
		HEARTS(1),
		DIAMONDS(2),
		CLUBS(3);

		public int value;
		Suit(int value) {
			this.value = value;
		}
	}

	Suit suit;

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
