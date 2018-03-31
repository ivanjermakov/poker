class Spy {
	private Calculator calculator;

	public Spy(Table table) {
		calculator = new Calculator(table);
		calculator.getWinningRates();
	}
}
