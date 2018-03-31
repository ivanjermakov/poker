public class Main {

    public static void main(String[] args) {
        Poker poker = new Poker();

		poker.addPlayer();
		poker.addPlayer();
		poker.addPlayer();
		poker.addPlayer();

		poker.playersHands();
		poker.hand();

    }
}
