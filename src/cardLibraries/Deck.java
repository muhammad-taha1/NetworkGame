package cardLibraries;

import java.util.Stack;

/*
 * This class forms the Deck object - a stack of 52 Card objects
 */
public class Deck {
	
	// attributes
	private Stack<Card> deck;
	
	// constructor for deck of cards. This will initialize and create a stack 
	// of 52 cards, called deck
	public Deck() {
		deck = new Stack<Card>();
		
		// TODO: Sabit bhai: fill the deck stack with 52 cards. Use the Card class
		// to fill the deck with. The deck has to be randomly filled and should 
		// not contain any duplicate cards
	}
	
	// getter for the deck
	public Stack<Card> getDeck() {
		return deck;
	}
	
	// dealing a card is synonymous to taking the top card (pop) from stack
	public Card deal() {
		return deck.pop();
	}

}
