package cardLibraries;

import java.util.ArrayList;
import java.util.Random;
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

		ArrayList<Card> tempDeck = new ArrayList<Card>();
		
		for (CardValue val : CardValue.values()) {
			for (CardSuit suit : CardSuit.values()) {
				tempDeck.add(new Card(val, suit));
			}
		}
		
		Random rand = new Random();
		while (!tempDeck.isEmpty()) {
			int cardToPushIdx = rand.nextInt(tempDeck.size());
			Card cardToPush = tempDeck.remove(cardToPushIdx);
			
			deck.push(cardToPush);
		}
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
