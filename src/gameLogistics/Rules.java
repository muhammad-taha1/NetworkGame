package gameLogistics;

import cardLibraries.Card;
import cardLibraries.CardSuit;

public class Rules {
	
	public enum handType {
		pair,
		// TODO: add all
		flush;
	}
	public static handType winHierarchy(Card [] hand) {
		// TODO: logic to determine winning hand
		if (hand[0].getSuit() == CardSuit.Clubs) {
			return handType.pair;
		}
		return handType.flush;
	}

}
