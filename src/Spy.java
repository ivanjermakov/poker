class Spy {
	
	public Spy(Table table) {
		Calculator calculator = new Calculator(table);
		if (table.players.isEmpty()) return;
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
