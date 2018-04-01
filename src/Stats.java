import java.util.*;

public class Stats {
	public enum Ranking {
		HIGH_CARD(0),
		ONE_PAIR(1),
		TWO_PAIR(2),
		THREE_OF_A_KIND(3),
		STRAIGHT(4),
		FLUSH(5),
		FULL_HOUSE(6),
		FOUR_OF_A_KIND(7),
		STRAIGHT_FLUSH(8),
		ROYAL_FLUSH(9);
		
		public int value;
		
		Ranking(int value) {
			this.value = value;
		}
	}
	
	public Player player;
	
	public List<Card> bestHand;
	public Ranking ranking;
	public List<Card> rankingKickers = new ArrayList<>();
	public double winningRate = -1.0;
	
	public List<Stats> possibleBestHands = new ArrayList<>();
	
	private boolean isFlush(List<Card> hand) {
		//check whether all suits same as first card
		Card.Suit suit = hand.get(0).suit;
		for (Card card : hand) {
			if (card.suit != suit) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean isStraight(List<Card> hand) {
		int rank = hand.get(0).rank.value;
		for (int i = 0; i < 5; i++) {
			//Ace as 1
			if (hand.get(0).rank.value == 14 &&
					hand.get(1).rank.value == 5 &&
					hand.get(2).rank.value == 4 &&
					hand.get(3).rank.value == 3 &&
					hand.get(4).rank.value == 2) {
				return true;
			}
			if (hand.get(i).rank.value != rank) {
				return false;
			}
			rank--;
		}
		
		return true;
	}
	
	private boolean isFourOfAKind(List<Card> hand) {
		return hand.get(0).rank == hand.get(3).rank || hand.get(1).rank == hand.get(4).rank;
	}
	
	private boolean isFullHouse(List<Card> hand) {
		return (hand.get(0).rank == hand.get(1).rank && hand.get(2).rank == hand.get(4).rank) ||
				(hand.get(0).rank == hand.get(2).rank && hand.get(3).rank == hand.get(4).rank);
	}
	
	private boolean isThreeOfAKind(List<Card> hand) {
		return hand.get(0).rank == hand.get(2).rank ||
				hand.get(1).rank == hand.get(3).rank ||
				hand.get(2).rank == hand.get(4).rank;
	}
	
	private boolean isTwoPair(List<Card> hand) {
		return (hand.get(0).rank == hand.get(1).rank && hand.get(2).rank == hand.get(3).rank) ||
				(hand.get(0).rank == hand.get(1).rank && hand.get(3).rank == hand.get(4).rank) ||
				(hand.get(1).rank == hand.get(2).rank && hand.get(3).rank == hand.get(4).rank);
	}
	
	private boolean isOnePair(List<Card> hand) {
		return (hand.get(0).rank == hand.get(1).rank) ||
				(hand.get(1).rank == hand.get(2).rank) ||
				(hand.get(2).rank == hand.get(3).rank) ||
				(hand.get(3).rank == hand.get(4).rank);
	}
	
	private Ranking getRanking(List<Card> hand) {
		//sort
		Table.sortCards(hand);
		//STRAIGHT, FLUSH, STRAIGHT_FLUSH
		if (isFlush(hand) && isStraight(hand)) {
			//ROYAL_FLUSH
			if (hand.get(4).rank == Card.Rank.TEN) {
				return Ranking.ROYAL_FLUSH;
			} else {
				return Ranking.STRAIGHT_FLUSH;
			}
		}
		if (isFlush(hand)) {
			return Ranking.FLUSH;
		}
		if (isStraight(hand)) {
			return Ranking.STRAIGHT;
		}
		//FOUR_OF_A_KIND
		if (isFourOfAKind(hand)) {
			return Ranking.FOUR_OF_A_KIND;
		}
		//FULL_HOUSE
		if (isFullHouse(hand)) {
			return Ranking.FULL_HOUSE;
		}
		//THREE_OF_A_KIND
		if (isThreeOfAKind(hand)) {
			return Ranking.THREE_OF_A_KIND;
		}
		//TWO_PAIR
		if (isTwoPair(hand)) {
			return Ranking.TWO_PAIR;
		}
		//ONE_PAIR
		if (isOnePair(hand)) {
			return Ranking.ONE_PAIR;
		}
		
		//HIGH_CARD
		return Ranking.HIGH_CARD;
	}
	
	private List<Card> getRankingKickers(Stats candidate) {
		switch (candidate.ranking) {
			case STRAIGHT_FLUSH:
			case STRAIGHT:
				//Ace as 1
				if (candidate.bestHand.get(0).rank.value == 14 && candidate.bestHand.get(4).rank.value == 2) {
					candidate.bestHand.get(0).rank.value = 1;
					//add Ace
					candidate.rankingKickers.add(candidate.bestHand.get(0));
				} else {
					candidate.rankingKickers.add(candidate.bestHand.get(4));
				}
				break;
			case FOUR_OF_A_KIND:
				candidate.rankingKickers.add(candidate.bestHand.get(1));
				break;
			case FULL_HOUSE:
				candidate.rankingKickers.add(candidate.bestHand.get(2));
				if (candidate.bestHand.get(0).rank == candidate.bestHand.get(2).rank) {
					candidate.rankingKickers.add(candidate.bestHand.get(4));
				} else {
					candidate.rankingKickers.add(candidate.bestHand.get(0));
				}
				break;
			case THREE_OF_A_KIND:
				candidate.rankingKickers.add(candidate.bestHand.get(2));
				break;
			case TWO_PAIR:
				candidate.rankingKickers.add(candidate.bestHand.get(1));
				candidate.rankingKickers.add(candidate.bestHand.get(4));
				break;
			case ONE_PAIR:
				for (int i = 0; i < 4; i++) {
					if (candidate.bestHand.get(i).rank.value == candidate.bestHand.get(i + 1).rank.value) {
						candidate.rankingKickers.add(candidate.bestHand.get(i));
						break;
					}
				}
				break;
			default:
				candidate.rankingKickers.add(candidate.bestHand.get(0));
		}
		
		return candidate.rankingKickers;
	}
	
	private void setWithBestRanking() {
		//get candidates with best ranking
		//sort by rankings
		boolean sorted = false;
		
		while (!sorted) {
			sorted = true;
			
			for (int i = 0; i < possibleBestHands.size() - 1; i++) {
				if (possibleBestHands.get(i).ranking.value < possibleBestHands.get(i + 1).ranking.value) {
					sorted = false;
					Collections.swap(possibleBestHands, i, i + 1);
				}
			}
			
		}
		
		//set with best ranking
		List<Stats> withBestRanking = new ArrayList<>();
		
		int i = 0;
		while (i < possibleBestHands.size() &&
				possibleBestHands.get(0).ranking.value == possibleBestHands.get(i).ranking.value) {
			withBestRanking.add(possibleBestHands.get(i));
			i++;
		}
		
		//sorting all another
		for (Stats stats : withBestRanking) {
			Table.sortCards(stats.bestHand);
		}
		
		possibleBestHands = withBestRanking;
	}
	
	private void sortByKickers() {
		//sort by biggest rank
		boolean isSorted = false;
		
		while (!isSorted) {
			for (int i = 0; i < possibleBestHands.size() - 1; i++) {
				isSorted = true;
				if (possibleBestHands.get(i).bestHand.get(0).rank.value <
						possibleBestHands.get(i + 1).bestHand.get(0).rank.value) {
					isSorted = false;
					Collections.swap(possibleBestHands, i, i + 1);
					break;
				}
			}
		}
		
		//for every col of ranks
		for (int j = 1; j < 5; j++) {
			isSorted = false;
			
			while (!isSorted) {
				isSorted = true;
				
				for (int i = 0; i < possibleBestHands.size() - 1; i++) {
					
					//only if previous the same
					if (possibleBestHands.get(i).bestHand.get(j - 1).rank ==
							possibleBestHands.get(i + 1).bestHand.get(j - 1).rank &&
							possibleBestHands.get(i).bestHand.get(j).rank.value <
									possibleBestHands.get(i + 1).bestHand.get(j).rank.value) {
						isSorted = false;
						Collections.swap(possibleBestHands, i, i + 1);
						break;
					}
					
				}
				
			}
			
		}
	}
	
	private void setBestHand() {
		setWithBestRanking();
		sortByKickers();
		
		bestHand = possibleBestHands.get(0).bestHand;
	}
	
	private void setPossibleBestHands(ArrayList<Card> commonCards, List<Card> hand) {
		//table hand only
		Stats stats = new Stats();
		stats.bestHand = commonCards;
		stats.ranking = getRanking(commonCards);
		stats.rankingKickers = getRankingKickers(stats);
		possibleBestHands.add(stats);
		
		List<Card> currentCommonCards = (ArrayList) commonCards.clone();
		
		//first printHand card
		for (int i = 0; i < 5; i++) {
			currentCommonCards = (ArrayList) commonCards.clone();
			currentCommonCards.set(i, hand.get(0));
			stats = new Stats();
			stats.bestHand = currentCommonCards;
			stats.ranking = getRanking(currentCommonCards);
			stats.rankingKickers = getRankingKickers(stats);
			possibleBestHands.add(stats);
		}
		
		//second printHand card
		for (int i = 0; i < 5; i++) {
			currentCommonCards = (ArrayList) commonCards.clone();
			currentCommonCards.set(i, hand.get(1));
			stats = new Stats();
			stats.bestHand = currentCommonCards;
			stats.ranking = getRanking(currentCommonCards);
			stats.rankingKickers = getRankingKickers(stats);
			possibleBestHands.add(stats);
		}
		
		//both printHand hand
		for (int i = 0; i < 4; i++) {
			for (int j = i + 1; j < 5; j++) {
				//first is first
				currentCommonCards = (ArrayList) commonCards.clone();
				currentCommonCards.set(i, hand.get(0));
				currentCommonCards.set(j, hand.get(1));
				stats = new Stats();
				stats.bestHand = currentCommonCards;
				stats.ranking = getRanking(currentCommonCards);
				stats.rankingKickers = getRankingKickers(stats);
				possibleBestHands.add(stats);
				//second is first
				currentCommonCards = (ArrayList) commonCards.clone();
				currentCommonCards.set(i, hand.get(1));
				currentCommonCards.set(j, hand.get(0));
				stats = new Stats();
				stats.bestHand = currentCommonCards;
				stats.ranking = getRanking(currentCommonCards);
				stats.rankingKickers = getRankingKickers(stats);
				possibleBestHands.add(stats);
			}
		}
	}
	
	private void setStats(List<Card> commonCards, List<Card> hand) {
		setPossibleBestHands((ArrayList) commonCards, hand);
		setBestHand();
		ranking = possibleBestHands.get(0).ranking;
		rankingKickers = possibleBestHands.get(0).rankingKickers;
	}
	
	public Stats(List<Card> commonCards, Player player) {
		this.player = player;
		setStats(commonCards, player.hand);
	}
	
	public Stats() {
	}
	
}
