class Spy {
	
	public Spy(Table table) {
		Calculator calculator = new Calculator(table);
		switch (calculator.state) {
			case RIVER:
				calculator.calculateStats();
				calculator.calculateWinningRates(table);
				calculator.getStats();
				break;
			case FLOP:
			case TURN:
				calculator.calculateWinningRates(table);
				break;
		}
	}
	
}
