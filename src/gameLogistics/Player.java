package gameLogistics;

import java.util.ArrayList;

import cardLibraries.Card;

/*
 * This class forms the Player object. This should contain everything a player 
 * would see on his screen when the game is played
 */
public class Player {
	
	// attributes
	private String name;
	private int id;
	private int money;
	private ArrayList<Card> hand;
	private boolean hasFolded;
	
	// constructor
	public Player(String name, int id, int money) {
		// initialize the hand and assign all arguments
		this.hand = new ArrayList<Card>(2);
		this.name = name;
		this.id = id;
		this.money = money;
		// player has not folded initially
		hasFolded = false;
	}
	
	// functionality allowing a player to bet
	public int bet(int value) {
		
		if (value > money) {
			throw new RuntimeException("Insufficient funds for the bet");
		}
		money -= value;
		// the return just makes sure to validate the bet has been successful. 
		return value;
	}
	
	// player folds his hand
	public void fold() {
		hasFolded = true;
	}
	
	// add the card to a player's hand
	public void addCardToHand(Card card) {
		hand.add(card);
	}

	
	// getters for all attributes
	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getMoney() {
		return money;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public boolean isHasFolded() {
		return hasFolded;
	}
}
