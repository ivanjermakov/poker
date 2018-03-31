class Spy {
	public Spy(Table table) {
		Calculator calculator = new Calculator(table);
		calculator.getRankings();
//		calculator.getWinningRates();
	}
}
