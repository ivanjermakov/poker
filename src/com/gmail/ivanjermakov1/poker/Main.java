package com.gmail.ivanjermakov1.poker;

public class Main {
	
	public static void main(String[] args) {
		Table table = new Table(4);
		for (int i = 0; i < 1; i++) {
			table.newGame();
			
//			table.setCommonCards();
//			table.setFlop();
//			table.showResults();
			table.showPlayersHands();
			table.showFlop();
			table.showTurn();
			table.showRiver();
		}
		
	}
}
