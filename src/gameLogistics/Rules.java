package gameLogistics;

import java.util.Arrays;

import cardLibraries.Card;
import cardLibraries.CardSuit;

public class Rules {

	// http://i.imgur.com/Xg3IVfy.gif
	public enum handType {
		royalFlush,
		straightFlush,
		fourOfKind,
		fullHouse,
		flush,
		straight,
		threeOfKind,
		twoPair,
		pair,
		highCard;
	}
	public static handType winHierarchy(Card[] playerHand, Card[] pot) {

		// this function should only be called when the whole pot is revealed.
		// Hence total number of cards we should check with is 7
		Card[] allCards = new Card[playerHand.length + pot.length];

		for (int i = 0; i < playerHand.length; i++) {
			allCards[i] = playerHand[i];
		}
		
		for (int i = 0; i < pot.length; i++) {
			allCards[i+playerHand.length] = pot[i];
		}

		if (isRoyalFlush(allCards)) return handType.royalFlush;

		else if (isStraightFlush(allCards)) return handType.straightFlush;

		else if (ofKind(allCards) == 4) return handType.fourOfKind;

		else if (isFullHouse(allCards)) return handType.fullHouse;

		else if (hasFlush(allCards)) return handType.flush;

		else if (hasStraight(allCards)) return handType.straight;

		else if (ofKind(allCards) == 3) return handType.threeOfKind;

		else if (numberOfPairs(allCards) >= 2) return handType.twoPair;

		else if (numberOfPairs(allCards) == 1) return handType.pair;

		else return handType.highCard;

	}

	public static int numberOfPairs(Card[] allCards) {
		int pairs = 0;

		for (int i = 0; i < allCards.length; i++) {
			Card initialCard = allCards[i];
			for (int j = i; j < allCards.length; j++) {
				// don't compare initialCard with itself
				if (j != i && initialCard.getValue() == allCards[j].getValue()) {
					pairs++;
					// found the pair we were looking for, break out of here
					break;
				}
			}
		}
		return pairs;
	}

	public static int ofKind(Card[] allCards) {
		int ans = 1;

		for (int i = 0; i < allCards.length; i++) {
			// ans should be reset for every new initial card
			// the 1 is used to count the initial card as well
			ans = 1;
			Card initialCard = allCards[i];
			for (int j = i; j < allCards.length; j++) {
				// don't compare initialCard with itself
				if (j != i && initialCard.getValue() == allCards[j].getValue()) {
					ans++;
				}
			}

			// check if we have a 3 of a kind or 4 of a kind
			if (ans >= 3) return ans;
		}

		// ans should be 1 if its not 3 or 4 of a kind
		return ans;
	}

	public static boolean hasFlush(Card[] allCards) {

		for (int i = 0; i < allCards.length; i++) {
			CardSuit initialSuit = allCards[i].getSuit();
			int straightCount = 1;
			for (int j = i; j < allCards.length; j++) {
				if (allCards[j].getSuit() == initialSuit && j != i) {
					straightCount++;
				}
				if (straightCount == 5) return true;
			}
		}
		return false;
	}

	public static boolean hasStraight(Card[] allCards) {
		Arrays.sort(allCards);
		int count = 1;
		for (int i = 0; i < allCards.length - 1; i++) {
			if ((allCards[i].getValue().getCardValue()+1) == allCards[i+1].getValue().getCardValue()) {
				count++;
				if (count == 5) return true;
			}
			else {
				count = 1;
			}
		}
		if (count == 5)
			return true;
		else return false;
	}

	public static boolean isFullHouse(Card[] allCards) {
		Arrays.sort(allCards);
		boolean threeOfKind = false;

		int threeOfKindCnt = 1;
		Card threeOfKindCard = null;
		for (int i = 0; i < allCards.length; i++) {
			// ans should be reset for every new initial card
			// the 1 is used to count the initial card as well
			threeOfKindCnt = 1;
			Card initialCard = allCards[i];
			for (int j = i; j < allCards.length; j++) {
				// don't compare initialCard with itself
				if (j != i && initialCard.getValue() == allCards[j].getValue()) {
					threeOfKindCnt++;
					threeOfKindCard = initialCard;
					if (threeOfKindCnt == 3) {
						threeOfKind = true;
						break;
					}
				}
			}				
		}
		// check if we have a 3 of a kind or 4 of a kind
		if (threeOfKindCnt == 3) {
			threeOfKind = true;
		}

		// check for pairs
		boolean hasAPair = false;
		Card pairCard = null;
		int pairs = 0;
		for (int i = 0; i < allCards.length; i++) {
			Card initialCard = allCards[i];
			for (int j = i; j < allCards.length; j++) {
				// don't compare initialCard with itself
				if (j != i && initialCard.getValue() == allCards[j].getValue()) {
					pairs++;
					pairCard = initialCard;
					// found the pair we were looking for, break out of here
					break;
				}
			}
			if (pairs == 2 &&
					pairCard.getValue().getCardValue() != threeOfKindCard.getValue().getCardValue()) {
				hasAPair = true;
			}
		}
		return (hasAPair && threeOfKind);
	}

	public static boolean isStraightFlush(Card[] allCards) {
		Arrays.sort(allCards);
		return (hasFlush(allCards) && hasStraight(allCards));
	}

	public static boolean isRoyalFlush(Card[] allCards) {
		Arrays.sort(allCards);

		boolean royalFlush = false;
		int royalCount = 10;
		CardSuit royalSuit = null;
		for (Card c : allCards) {
			if (c.getValue().getCardValue() == royalCount) {
				royalCount++;

				if (royalSuit == null) {
					royalSuit = c.getSuit();
				}
				else if (royalSuit.equals(c.getSuit())) {
					royalFlush = true;
				}
				else {
					royalFlush = false;
					break;
				}
			} else {
				royalFlush = false;
			}
		}

		return (royalFlush && hasFlush(allCards) && hasStraight(allCards));
	}

}
