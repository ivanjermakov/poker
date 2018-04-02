import java.util.*;

public class Stats {
	
	public enum Ranking {
		HIGH_CARD(0),
		ONE_PAIR(1),
		TWO_PAIR(2),
		THREE_OF_A_KIND(3),
		STRAIGHT_TO_ACE(4),
		STRAIGHT(5),
		FLUSH(6),
		FULL_HOUSE(7),
		FOUR_OF_A_KIND(8),
		STRAIGHT_FLUSH(9),
		ROYAL_FLUSH(10);
		
		public int value;
		
		Ranking(int value) {
			this.value = value;
		}
	}
	
	public double winningRate = -1.0;
	public Ranking ranking;
	public List<Card> bestHand;
	public List<Card> rankingKickers = new ArrayList<>();
	public List<Card> kickers = new ArrayList<>();
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
	
	private boolean isStraightToAce(List<Card> hand) {
		return hand.get(0).rank == Card.Rank.ACE &&
				hand.get(1).rank == Card.Rank.FIVE &&
				hand.get(2).rank == Card.Rank.FOUR &&
				hand.get(3).rank == Card.Rank.THREE &&
				hand.get(4).rank == Card.Rank.TWO;
	}
	
	private boolean isStraight(List<Card> hand) {
		int rank = hand.get(0).rank.value;
		for (int i = 0; i < 5; i++) {
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
		//STRAIGHT_FLUSH
		if (isFlush(hand) && isStraight(hand)) {
			//ROYAL_FLUSH
			if (hand.get(4).rank == Card.Rank.TEN) {
				return Ranking.ROYAL_FLUSH;
			} else {
				return Ranking.STRAIGHT_FLUSH;
			}
		}
		if (isFlush(hand)) return Ranking.FLUSH;
		if (isStraightToAce(hand)) return Ranking.STRAIGHT_TO_ACE;
		if (isStraight(hand)) return Ranking.STRAIGHT;
		if (isFourOfAKind(hand)) return Ranking.FOUR_OF_A_KIND;
		if (isFullHouse(hand)) return Ranking.FULL_HOUSE;
		if (isThreeOfAKind(hand)) return Ranking.THREE_OF_A_KIND;
		if (isTwoPair(hand)) return Ranking.TWO_PAIR;
		if (isOnePair(hand)) return Ranking.ONE_PAIR;
		
		return Ranking.HIGH_CARD;
	}
	
	private List<Card> getRankingKickers(Stats candidate) {
		List<Card> rankingKickers = new ArrayList<>();
		switch (candidate.ranking) {
			case STRAIGHT_FLUSH:
			case STRAIGHT:
				rankingKickers.add(candidate.bestHand.get(4));
				break;
			case FOUR_OF_A_KIND:
				rankingKickers.add(candidate.bestHand.get(1));
				break;
			case FULL_HOUSE:
				rankingKickers.add(candidate.bestHand.get(0));
				rankingKickers.add(candidate.bestHand.get(4));
				break;
			case THREE_OF_A_KIND:
				rankingKickers.add(candidate.bestHand.get(2));
				break;
			case TWO_PAIR:
				rankingKickers.add(candidate.bestHand.get(1));
				rankingKickers.add(candidate.bestHand.get(3));
				break;
			case ONE_PAIR:
				for (int i = 0; i < 4; i++) {
					if (candidate.bestHand.get(i).rank.value == candidate.bestHand.get(i + 1).rank.value) {
						rankingKickers.add(candidate.bestHand.get(i));
						break;
					}
				}
				break;
		}
		
		return rankingKickers;
	}
	
	private List<Card> getKickers(Stats candidate) {
		List<Card> kickers = new ArrayList<>();
		//rankings without kickers
		if (candidate.ranking == Ranking.ROYAL_FLUSH ||
				candidate.ranking == Ranking.STRAIGHT_FLUSH ||
				candidate.ranking == Ranking.STRAIGHT ||
				candidate.ranking == Ranking.STRAIGHT_TO_ACE ||
				candidate.ranking == Ranking.FLUSH ||
				candidate.ranking == Ranking.HIGH_CARD ||
				candidate.ranking == Ranking.FULL_HOUSE) return kickers;
		
		for (Card card : candidate.bestHand) {
			//first kicker
			if (!candidate.rankingKickers.isEmpty() &&
					card.rank != candidate.rankingKickers.get(0).rank) {
				//second kicker
				if (candidate.rankingKickers.size() == 2 &&
						card.rank != candidate.rankingKickers.get(1).rank) {
					kickers.add(card);
				} else {
					kickers.add(card);
				}
			}
		}
		
		return kickers;
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

//		//sorting all another
//		for (Stats stats : withBestRanking) {
//			Table.sortCards(stats.bestHand);
//		}
		
		possibleBestHands = withBestRanking;
	}
	
	private void sortByKickers() {
		if (possibleBestHands.size() == 1) return;
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
		
	}
	
	private void setBestHand() {
		setWithBestRanking();
		sortByKickers();
		
		bestHand = possibleBestHands.get(0).bestHand;
	}
	
	private void setPossibleHands(ArrayList<Card> commonCards, List<Card> hand) {
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
	
	public Stats(Table table, Player player) {
		setPossibleHands((ArrayList) table.commonCards, player.hand);
		setBestHand();
		ranking = possibleBestHands.get(0).ranking;
		rankingKickers = possibleBestHands.get(0).rankingKickers;
	}
	
	public Stats getStats() {
		return this;
	}
	
	public Stats() {
	}
	
}
