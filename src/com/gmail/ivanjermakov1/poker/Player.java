package com.gmail.ivanjermakov1.poker;

import java.util.List;

class Player {
	
	public String name;
	
	public List<Card> hand;
	
	public Stats stats;
	
	public double winRate;
	
	
	public Player(String name) {
		this.name = name;
	}
	
	public void setStats(Table table) {
		this.stats = new Stats(table, this).getStats();
	}
	
	public void setStats(Table table, List commonCards) {
		Stats stats = new Stats(table, this, commonCards);
		this.stats = stats.getStats();
	}
	
}