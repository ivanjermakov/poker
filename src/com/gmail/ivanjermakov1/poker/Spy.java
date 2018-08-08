package com.gmail.ivanjermakov1.poker;

class Spy {
	
	public Spy(Table table) {
		Calculator calculator = new Calculator(table);
		
		if (table.players.isEmpty()) return;
		
		switch (calculator.state) {
			case FLOP:
			case TURN:
				calculator.calculateRates(table);
				calculator.showRates();
				break;
			case RIVER:
				calculator.sortPlayersStats();
				calculator.calculateWinningRates();
				System.out.println(calculator.getStats(true));
//				calculator.printStats("output.txt");
				break;
		}
		
	}
	
}
