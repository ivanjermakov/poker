class Spy {
	
	public Spy(Table table) {
		Calculator calculator = new Calculator(table);
		
		if (table.players.isEmpty()) return;
		
		switch (calculator.state) {
			case RIVER:
				calculator.sortPlayersStats();
				calculator.calculateWinningRates();
				calculator.sortPlayersStats();
				calculator.showStats();
				break;
			case FLOP:
			case TURN:
				calculator.calculateRates(table);
				calculator.showRates();
				break;
		}
		
	}
	
}
