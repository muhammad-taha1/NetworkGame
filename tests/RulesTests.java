import gameLogistics.Rules;

import org.junit.Assert;
import org.junit.Test;

import cardLibraries.Card;
import cardLibraries.CardSuit;
import cardLibraries.CardValue;


// following TDD
public class RulesTests {


	@Test
	public void numberOfPairsTest() {

		Card[] cards = new Card[4];
		cards[0] = new Card(CardValue.Ace, CardSuit.Clubs);
		cards[1] = new Card(CardValue.Two, CardSuit.Clubs);
		cards[2] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Five, CardSuit.Clubs);

		Assert.assertSame(1, Rules.numberOfPairs(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Ace, CardSuit.Clubs);
		cards[1] = new Card(CardValue.Two, CardSuit.Clubs);
		cards[2] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Five, CardSuit.Clubs);
		cards[4] = new Card(CardValue.Ace, CardSuit.Spades);
		cards[5] = new Card(CardValue.Ace, CardSuit.Diamonds);
		cards[6] = new Card(CardValue.Queen, CardSuit.Clubs);

		Assert.assertSame(3, Rules.numberOfPairs(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Ace, CardSuit.Clubs);
		cards[1] = new Card(CardValue.Two, CardSuit.Clubs);
		cards[2] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Five, CardSuit.Clubs);
		cards[4] = new Card(CardValue.Two, CardSuit.Spades);
		cards[5] = new Card(CardValue.King, CardSuit.Diamonds);
		cards[6] = new Card(CardValue.Queen, CardSuit.Clubs);

		Assert.assertSame(2, Rules.numberOfPairs(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Jack, CardSuit.Clubs);
		cards[1] = new Card(CardValue.Ace, CardSuit.Clubs);
		cards[2] = new Card(CardValue.Three, CardSuit.Hearts);
		cards[3] = new Card(CardValue.King, CardSuit.Clubs);
		cards[4] = new Card(CardValue.Three, CardSuit.Spades);
		cards[5] = new Card(CardValue.King, CardSuit.Diamonds);
		cards[6] = new Card(CardValue.Queen, CardSuit.Clubs);

		Assert.assertSame(2, Rules.numberOfPairs(cards));
	}

	@Test
	public void threeOfAKindTest() {

		Card[] cards = new Card[7];
		cards[0] = new Card(CardValue.Ace, CardSuit.Clubs);
		cards[1] = new Card(CardValue.Two, CardSuit.Clubs);
		cards[2] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Five, CardSuit.Clubs);
		cards[4] = new Card(CardValue.Two, CardSuit.Spades);
		cards[5] = new Card(CardValue.King, CardSuit.Diamonds);
		cards[6] = new Card(CardValue.Queen, CardSuit.Clubs);

		Assert.assertSame(1, Rules.ofKind(cards));


		cards = new Card[7];
		cards[0] = new Card(CardValue.Ace, CardSuit.Clubs);
		cards[1] = new Card(CardValue.Ace, CardSuit.Spades);
		cards[2] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Five, CardSuit.Clubs);
		cards[4] = new Card(CardValue.Two, CardSuit.Spades);
		cards[5] = new Card(CardValue.King, CardSuit.Diamonds);
		cards[6] = new Card(CardValue.Queen, CardSuit.Clubs);

		Assert.assertSame(3, Rules.ofKind(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Ace, CardSuit.Clubs);
		cards[1] = new Card(CardValue.Two, CardSuit.Spades);
		cards[2] = new Card(CardValue.Five, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Two, CardSuit.Spades);
		cards[5] = new Card(CardValue.Ace, CardSuit.Diamonds);
		cards[6] = new Card(CardValue.Queen, CardSuit.Clubs);

		Assert.assertSame(3, Rules.ofKind(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Ace, CardSuit.Clubs);
		cards[1] = new Card(CardValue.Two, CardSuit.Spades);
		cards[2] = new Card(CardValue.Five, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Ace, CardSuit.Spades);
		cards[5] = new Card(CardValue.Ace, CardSuit.Diamonds);
		cards[6] = new Card(CardValue.Ace, CardSuit.Hearts);

		Assert.assertSame(4, Rules.ofKind(cards));
	}

	@Test
	public void hasFlushTest() {

		Card[] cards = new Card[7];
		cards[0] = new Card(CardValue.Ace, CardSuit.Clubs);
		cards[1] = new Card(CardValue.Two, CardSuit.Spades);
		cards[2] = new Card(CardValue.Five, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Ace, CardSuit.Spades);
		cards[5] = new Card(CardValue.Ace, CardSuit.Diamonds);
		cards[6] = new Card(CardValue.Ace, CardSuit.Hearts);

		Assert.assertFalse(Rules.hasFlush(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[1] = new Card(CardValue.Two, CardSuit.Spades);
		cards[2] = new Card(CardValue.Five, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Ace, CardSuit.Spades);
		cards[5] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Ace, CardSuit.Hearts);

		Assert.assertTrue(Rules.hasFlush(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Ace, CardSuit.Diamonds);
		cards[1] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[2] = new Card(CardValue.Five, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Ace, CardSuit.Spades);
		cards[5] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Ace, CardSuit.Hearts);

		Assert.assertTrue(Rules.hasFlush(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Ace, CardSuit.Diamonds);
		cards[1] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[2] = new Card(CardValue.Five, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[5] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Ace, CardSuit.Hearts);

		Assert.assertTrue(Rules.hasFlush(cards));

	}

	@Test
	public void hasStraightTest() {

		Card[] cards = new Card[7];
		cards[0] = new Card(CardValue.Ace, CardSuit.Diamonds);
		cards[1] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[2] = new Card(CardValue.Five, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[5] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Ace, CardSuit.Hearts);

		Assert.assertFalse(Rules.hasStraight(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Ace, CardSuit.Diamonds);
		cards[1] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[2] = new Card(CardValue.Queen, CardSuit.Hearts);
		cards[3] = new Card(CardValue.King, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Jack, CardSuit.Hearts);
		cards[5] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Ten, CardSuit.Hearts);

		Assert.assertTrue(Rules.hasStraight(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Three, CardSuit.Diamonds);
		cards[1] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[2] = new Card(CardValue.Queen, CardSuit.Hearts);
		cards[3] = new Card(CardValue.King, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Jack, CardSuit.Hearts);
		cards[5] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Ten, CardSuit.Hearts);

		Assert.assertTrue(Rules.hasStraight(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Three, CardSuit.Diamonds);
		cards[1] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[2] = new Card(CardValue.Four, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Nine, CardSuit.Hearts);
		cards[5] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Ten, CardSuit.Hearts);

		Assert.assertFalse(Rules.hasStraight(cards));
	}

	@Test
	public void isFullHouseTest() {

		Card[] cards = new Card[7];
		cards[0] = new Card(CardValue.Three, CardSuit.Diamonds);
		cards[1] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[2] = new Card(CardValue.Four, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Nine, CardSuit.Hearts);
		cards[5] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Ten, CardSuit.Hearts);

		Assert.assertFalse(Rules.isFullHouse(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Four, CardSuit.Diamonds);
		cards[1] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[2] = new Card(CardValue.Four, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Nine, CardSuit.Hearts);
		cards[5] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Two, CardSuit.Hearts);

		Assert.assertTrue(Rules.isFullHouse(cards));
	}

	@Test
	public void isStraightFlush() {

		Card[] cards = new Card[7];
		cards[0] = new Card(CardValue.Four, CardSuit.Diamonds);
		cards[1] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[2] = new Card(CardValue.Four, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Nine, CardSuit.Hearts);
		cards[5] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Two, CardSuit.Hearts);

		Assert.assertFalse(Rules.isStraightFlush(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Four, CardSuit.Diamonds);
		cards[1] = new Card(CardValue.Five, CardSuit.Hearts);
		cards[2] = new Card(CardValue.Four, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Three, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Two, CardSuit.Clubs);
		cards[5] = new Card(CardValue.Six, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Queen, CardSuit.Hearts);

		Assert.assertFalse(Rules.isStraightFlush(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Five, CardSuit.Hearts);
		cards[1] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[2] = new Card(CardValue.Four, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Three, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Nine, CardSuit.Clubs);
		cards[5] = new Card(CardValue.Six, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Two, CardSuit.Clubs);

		Assert.assertTrue(Rules.isStraightFlush(cards));
	}

	@Test
	public void isRoyalFlush() {

		Card[] cards = new Card[7];
		cards[0] = new Card(CardValue.Five, CardSuit.Hearts);
		cards[1] = new Card(CardValue.Two, CardSuit.Hearts);
		cards[2] = new Card(CardValue.Four, CardSuit.Hearts);
		cards[3] = new Card(CardValue.Three, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Nine, CardSuit.Clubs);
		cards[5] = new Card(CardValue.Six, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Two, CardSuit.Clubs);

		Assert.assertFalse(Rules.isRoyalFlush(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[1] = new Card(CardValue.Ten, CardSuit.Hearts);
		cards[2] = new Card(CardValue.Four, CardSuit.Hearts);
		cards[3] = new Card(CardValue.King, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Nine, CardSuit.Clubs);
		cards[5] = new Card(CardValue.Jack, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Queen, CardSuit.Clubs);

		Assert.assertFalse(Rules.isRoyalFlush(cards));

		cards = new Card[7];
		cards[0] = new Card(CardValue.Ace, CardSuit.Hearts);
		cards[1] = new Card(CardValue.Ten, CardSuit.Hearts);
		cards[2] = new Card(CardValue.Four, CardSuit.Hearts);
		cards[3] = new Card(CardValue.King, CardSuit.Hearts);
		cards[4] = new Card(CardValue.Nine, CardSuit.Clubs);
		cards[5] = new Card(CardValue.Jack, CardSuit.Hearts);
		cards[6] = new Card(CardValue.Queen, CardSuit.Hearts);

		Assert.assertTrue(Rules.isRoyalFlush(cards));
	}

	@Test
	public void winHierarchyTests() {

		Card[] potCards = new Card[5];
		potCards[0] = new Card(CardValue.Ace, CardSuit.Hearts);
		potCards[1] = new Card(CardValue.Ten, CardSuit.Hearts);
		potCards[2] = new Card(CardValue.Four, CardSuit.Hearts);
		potCards[3] = new Card(CardValue.King, CardSuit.Hearts);
		potCards[4] = new Card(CardValue.Nine, CardSuit.Clubs);

		Card[] handCards = new Card[2];
		handCards[0] = new Card(CardValue.Jack, CardSuit.Hearts);
		handCards[1] = new Card(CardValue.Queen, CardSuit.Hearts);

		Assert.assertEquals(Rules.winHierarchy(handCards, potCards), Rules.handType.royalFlush);

		potCards = new Card[5];
		potCards[0] = new Card(CardValue.Ace, CardSuit.Hearts);
		potCards[1] = new Card(CardValue.Four, CardSuit.Diamonds);
		potCards[2] = new Card(CardValue.Four, CardSuit.Hearts);
		potCards[3] = new Card(CardValue.King, CardSuit.Hearts);
		potCards[4] = new Card(CardValue.Nine, CardSuit.Clubs);

		handCards = new Card[2];
		handCards[0] = new Card(CardValue.Jack, CardSuit.Hearts);
		handCards[1] = new Card(CardValue.Jack, CardSuit.Clubs);

		Assert.assertEquals(Rules.winHierarchy(handCards, potCards), Rules.handType.twoPair);

		potCards = new Card[5];
		potCards[0] = new Card(CardValue.Ten, CardSuit.Hearts);
		potCards[1] = new Card(CardValue.Four, CardSuit.Diamonds);
		potCards[2] = new Card(CardValue.Three, CardSuit.Hearts);
		potCards[3] = new Card(CardValue.King, CardSuit.Hearts);
		potCards[4] = new Card(CardValue.Nine, CardSuit.Clubs);

		handCards = new Card[2];
		handCards[0] = new Card(CardValue.Ace, CardSuit.Hearts);
		handCards[1] = new Card(CardValue.Jack, CardSuit.Clubs);

		Assert.assertEquals(Rules.winHierarchy(handCards, potCards), Rules.handType.highCard);

	}


}
