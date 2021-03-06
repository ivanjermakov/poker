package com.gmail.ivanjermakov1.poker;

import java.util.List;
import java.util.Random;

class Card {
	
	public enum Rank {
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
	
	public Card() {
		rank = Rank.values()[new Random().nextInt(Rank.values().length)];
		suit = Suit.values()[new Random().nextInt(Suit.values().length)];
	}
	
	public Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}
	
	public static String toShortString(Card card, boolean colored) {
		String rank = "";
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
		}
		
		String suit = "";
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
		}
		
		return rank + suit;
	}
	
	public static String toShortStrings(List<Card> cards, boolean colored) {
		String string = "";
		
		for (Card card : cards) {
			string += Card.toShortString(card, colored) + " ";
		}
		
		return string;
	}
	
	public static String toShortStrings(Player player, boolean colored) {
		//swap for draw and then sort to fix
		if (player.stats.ranking == Stats.Ranking.STRAIGHT_TO_ACE) {
			player.stats.bestHand.add(player.stats.bestHand.remove(0));
		}
		String string = "";
		for (Card card : player.stats.bestHand) {
			String rank = "";
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
			}
			
			if (colored && (card == player.hand.get(0) ||
					card == player.hand.get(1)))  {
				rank = "\u001B[32m" + rank + "\u001B[0m";
			}
			
			String suit = "";
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
			}
			
			string += rank + suit + " ";
		}
		
		//fix STRAIGHT_TO_ACE case
		Table.sortCards(player.stats.bestHand);
		return string;
	}
	
}
