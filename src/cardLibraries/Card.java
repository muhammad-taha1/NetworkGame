package cardLibraries;

/*
 * This class forms the Card object - currently the implementation only contains
 * forming a card just by specifying a suit and a value to it.
 */
public class Card {
	
	// object attributes
	private final  CardValue value;
	private final CardSuit suit;
	
	// constructor for Card object
	public Card(CardValue aValue, CardSuit aSuit) {
		this.value = aValue;
		this.suit = aSuit;
	}

	// getter for Value
	public CardValue getValue() {
		return value;
	}

	// getter for Value
	public CardSuit getSuit() {
		return suit;
	}
	
	// TODO: functions to display image of the card would need to be added here
}
