import java.util.List;

class Player {
	
	public String name;
	
	public List<Card> hand;
	
	public Stats stats;
	
	
	public Player(String name) {
		this.name = name;
	}
	
	public void setStats(Table table) {
		Stats stats = new Stats(table, this);
		this.stats = stats.getStats();
	}
	
	public void printHand() {
		for (Card card : hand) {
			System.out.print(Card.toShortString(card, true) + " ");
		}
	}
	
}