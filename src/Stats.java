import java.util.Vector;

public class Stats {

	private enum Ranking {
		HIGH_CARD,
		ONE_PAIR,
		TOW_PAIR,
		THREE_OF_A_KIND,
		STRAIGHT,
		FLUSH,
		FULL_HOUSE,
		FOUR_OF_A_KIND,
		STRAIGHT_FLUSH,
		ROYAL_FLUSH;
	}

	private Player player;

	private Vector<Card> bestHand;

	private Ranking ranking;

	private double winningRate;

	private void getRanking(Vector<Card> deck, Vector<Card> hand) {

	}

	public Stats(Vector<Card> deck, Player player) {
		this.player = player;
		getRanking(deck, player.cards);
	}

	Stats getStats() {
		return this;
	}

}
