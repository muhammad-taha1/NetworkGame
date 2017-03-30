package cardLibraries;

/*
 * This class forms the Card object - currently the implementation only contains
 * forming a card just by specifying a suit and a value to it.
 */
public class Card implements Comparable <Card> {

	// object attributes
	private final  CardValue value;
	private final CardSuit suit;
	private int comparator;

	// constructor for Card object
	public Card(CardValue aValue, CardSuit aSuit) {
		this.value = aValue;
		this.suit = aSuit;

		comparator = value.ordinal();
	}

	public Card(String aValue, String aSuit) {
		this.value = CardValue.valueOf(aValue);
		this.suit = CardSuit.valueOf(aSuit);

		comparator = value.ordinal();
	}

	// getter for Value
	public CardValue getValue() {
		return value;
	}

	// getter for Value
	public CardSuit getSuit() {
		return suit;
	}

	@Override
	public String toString() {
		String ans = "";

		if (this.value.getCardValue() < 10) {
			ans += this.value.getCardValue();
		}
		else {
			ans += this.value.toString().toLowerCase().charAt(0);
		}

		ans += this.suit.toString().toLowerCase().charAt(0);

		return ans;
	}

	@Override
	public int compareTo(Card o) {
		return (value.getCardValue() - o.getValue().getCardValue());
	}

	// TODO: functions to display image of the card would need to be added here
}
