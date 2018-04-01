import java.util.*;

class Card {
	public enum Rank {
		ONE(1),
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

	public enum Suit {
		SPADES(0),
		HEARTS(1),
		DIAMONDS(2),
		CLUBS(3);

		public int value;

		Suit(int value) {
			this.value = value;
		}
	}

	public Rank rank;
	public Suit suit;
	public boolean isTaken = false;

	public Card() {
		rank = Rank.values()[new Random().nextInt(Rank.values().length)];
		suit = Suit.values()[new Random().nextInt(Suit.values().length)];

//		System.out.println("New card is " + rank + " of " + suit);
	}

	public Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;

//		System.out.println("New card is " + rank + " of " + suit);
	}

	public Card(Card card) {
		this.rank = card.rank;
		this.suit = card.suit;
		this.isTaken = card.isTaken;
	}

	public static String toShortString(Card card, boolean colored) {
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
			case ONE:
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
				if (colored) {
					suit = "\u001B[31m" + "♥" + "\u001B[0m";
				} else {
					suit = "♥";
				}
				break;
			case SPADES:
				suit = "♠";
				break;
			case DIAMONDS:
				if (colored) {
					suit = "\u001B[31m" + "♦" + "\u001B[0m";
				} else {
					suit = "♦";
				}
				break;
			default:
				suit = "und";
		}

		return rank + suit;
	}

	public static String toShortStrings(List<Card> cards, boolean colored) {
		StringBuilder string = new StringBuilder();

		for (Card card : cards) {
			string.append(Card.toShortString(card, colored)).append(" ");
		}
		return string.toString();
	}

}
